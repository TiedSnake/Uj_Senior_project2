package com.example.haircut.backend;

import androidx.annotation.NonNull;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public abstract class Service {
    /**
     * Simulating a database Key-->email_username, value--> (User{fname, lname, email, password})
     * [username]@[domain_name].tld
     */
    private final static String TAG = "Service.java";
    /**
     * Log.d: debug
     * Log.i: info
     * Log.w: warning
     * Log.e: error
     * Log.v: verbose
     */

    private final static HashMap<String, User> user_records = new HashMap<>();
    private final static HashMap<String, String> users_emails = new HashMap<>();
    private final static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static User user;
    //Initialize a reference to realtime database.
    private final static DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://haircut-508fa-default-rtdb.europe-west1.firebasedatabase.app/").getReference("schema");

    public enum ResponseFlag {
        SUCCESS, EMAIL_NOT_REGISTERED, INCORRECT_CREDENTIALS, EMAIL_ALREADY_REGISTERED, ERROR
    }

    //Saves|persist a user in the database
    private static CompletableFuture<ResponseFlag> persistUser(User user) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        //Passing the user object where the user ID is present in Firebase realtime DB.
        databaseRef.child("users").child(user.getUuid()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, String.format("User %s saved successfully in the database", user.getEmail()));
                future.complete(ResponseFlag.SUCCESS);
            } else {
                Log.e(TAG, String.format("DatabaseError: Error saving user to database\n%s", task.getException()));
                future.complete(ResponseFlag.ERROR);
            }
        });
        return future;
    }

    //Fetches the user object from database using usere's ID.
    public CompletableFuture<User> fetchUserById(String userId) {
        CompletableFuture<User> future = new CompletableFuture<>();
        databaseRef.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) future.complete(task.getResult().getValue(User.class));
            else future.completeExceptionally(task.getException());
        });
        return future;
    }

    //Check whether user exist in DB
    protected static CompletableFuture<ResponseFlag> userExist(String email) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        databaseRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) future.complete(ResponseFlag.SUCCESS);
                else future.complete(ResponseFlag.ERROR);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.completeExceptionally(new RuntimeException("Error when checking existence of the user in the system"));
            }
        });
        return future;
    }

    private static CompletableFuture<ResponseFlag> createUserInFirebase(User user, String password) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        auth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    user.setUuid(userId);
                    future.complete(ResponseFlag.SUCCESS);
                }
            } else//if user object creation failed propagate the exception.
            {
                Log.e(TAG, String.format("Error: creating user with email & password in Firebase failed due to:\n%s", task.getException()));
                future.complete(ResponseFlag.ERROR);
            }
        });
        return future;
    }

    /**
     * adding to the hashmap will return the user object we compare against Non-null
     * to force the function into return true if user added successfully & false if not.
     * This Method returns multiple types of object depending on the program flow(User, ResponseFlag)
     */
    public static <T> CompletableFuture<T> failedFuture(Throwable exception) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(exception);
        return future;
    }

    static User createUser(String firstName, String lastName, String email, User.UserType userType) {
        return switch (userType.name().toLowerCase()) {
            case "customer" -> new Customer(firstName, lastName, email);
            case "barber" -> new Barber(firstName, lastName, email);
            case "admin" -> new Admin(firstName, lastName, email);
            default -> null;
        };
    }

    private static CompletableFuture<ResponseFlag> sendVerificationEmail() {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(verificationTask -> {
                if (verificationTask.isSuccessful()) //sending succeed
                {
                    Log.i(TAG, "verification email has been sent successfully!");
                    future.complete(ResponseFlag.SUCCESS);
                } else //sending failed{
                {
                    Log.e(TAG, String.format("Error: failed to send verification\n%s", verificationTask.getException()));
                    future.completeExceptionally(verificationTask.getException()); //propagate exception
                }

            });
        } else {
            Log.e(TAG, "Error: Sending email verification process failed due to firebaseUser object being null");
            future.completeExceptionally(new NullPointerException());
        }
        return future;
    }

    public static CompletableFuture<ResponseFlag> signup(String firstName, String lastName, String email, String password, User.UserType userType) {
        return userExist(email).thenCompose(exists -> {
            if (exists.equals(ResponseFlag.SUCCESS)) {
                Log.e(TAG, String.format("This email %s is already registered in the system with a user", email));
                return CompletableFuture.completedFuture(ResponseFlag.EMAIL_ALREADY_REGISTERED);
            } else {
                user = createUser(firstName, lastName, email, userType);
                if (user == null)
                    return failedFuture(new IllegalArgumentException("Invalid user type: " + userType));

                return createUserInFirebase(user, password)
                        .thenCompose(isCreated -> {
                            if (isCreated.equals(ResponseFlag.ERROR))
                                return CompletableFuture.completedFuture(ResponseFlag.ERROR);
                            else {
                                user.setIsLoggedIn(true);
                                return persistUser(user);
                            }
                        }).thenCompose(isPersisted -> {
                            if (isPersisted.equals(ResponseFlag.ERROR)) {
                                Log.e(TAG, "Unexpected error: the user has not been persisted");
                                return CompletableFuture.completedFuture(ResponseFlag.ERROR);
                            } else
                                return sendVerificationEmail();
                        }).thenCompose(isVerified -> {
                            if (isVerified.equals(ResponseFlag.SUCCESS)) {
                                return CompletableFuture.completedFuture(ResponseFlag.SUCCESS);
                            } else {
                                Log.e(TAG, "Unexpected error: email verification failed");
                                return CompletableFuture.completedFuture(ResponseFlag.ERROR);
                            }
                        });
            }
        });
    }

    public static CompletableFuture<ResponseFlag> signout() {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        if (user == null) {
            Log.e(TAG, "Error: Signing out failed, the user object is already null");
            future.completeExceptionally(new NullPointerException());
        } else {
            user.setIsLoggedIn(false);
            auth.signOut();
            future.complete(ResponseFlag.SUCCESS);
        }
        return future;
    }

    public static CompletableFuture<ResponseFlag> login(String email, String password) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();

        if (users_emails.containsKey(email)) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String uuid = users_emails.getOrDefault(email, null);
                    if (uuid == null)
                        future.completeExceptionally(new RuntimeException("Unexpected error this email " + email + " already exist but,\nthe fetched uuid associated with it is null"));
                    else {
                        User user = user_records.get(uuid);
                        if (user == null)
                            future.completeExceptionally(new RuntimeException("Unexpected error this email " + email + " already exist but,\nthe fetched user object with the uuid " + uuid + " is null"));
                        else {
                            user.setIsLoggedIn(true); //Marking the user as logged in
                            future.complete(ResponseFlag.SUCCESS);
                        }
                    }
                } else
                    //if signing the user in the system fails for Firebase, propagate the exception.
                    future.completeExceptionally(task.getException());
            });
        } else
            future.completeExceptionally(new RuntimeException("This email " + email + "is not registered in the system"));
        return future;
    }

    /**
     * @param uuid user can't logout if he/she isn't logged in from the first place, that explains why the parameter is of type User instead of email.
     *             the point is to avoid the overhead of finding in the records for the user object again when the user is already logged in.
     * @return
     */
    protected static CompletableFuture<ResponseFlag> logout(UUID uuid) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        User user = user_records.getOrDefault(uuid, null);
        if (user == null)
            future.completeExceptionally(new RuntimeException("Unexpected error during logout, the user associated with this uuid " + uuid.toString() + " is null"));
        else if (user.isLoggedIn()) {
            auth.signOut();
            user.setIsLoggedIn(false);
            future.complete(ResponseFlag.SUCCESS);
        } else
            future.completeExceptionally(new RuntimeException("Unexpected error during logout, the user associated with this uuid " + uuid.toString() + " is not signed in the system"));
        return future;
    }

    static CompletableFuture<ResponseFlag> codeVerification(String code) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        auth.verifyPasswordResetCode(code).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(ResponseFlag.SUCCESS);
            } else
                future.completeExceptionally(new RuntimeException("Invalid verification code or it has expired " + task.getException()));
        });
        return future;
    }

    //This Method returns multiple types of object depending on the program flow(User, ResponseFlag)
    public static CompletableFuture<ResponseFlag> sendPasswordResetEmail(String email) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        if (users_emails.containsKey(email)) {
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder().setUrl("https://Haircut.com/passwordReset").setHandleCodeInApp(true).build();

            auth.sendPasswordResetEmail(email, actionCodeSettings).addOnCompleteListener(task -> {
                if (task.isSuccessful()) future.complete(ResponseFlag.SUCCESS);
                else
                    future.completeExceptionally(new RuntimeException("Sending password reset email failed " + task.getException()));
            });

        } else
            future.completeExceptionally(new RuntimeException("This email " + email + "is not registered in the system"));
        return future;
    }

    static CompletableFuture<ResponseFlag> confirmVerification(String resetCode, String newPassword) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        // Get the reset code from the Intent (or deep link)
//        resetCode = getIntent().getStringExtra("resetCode");
        auth.confirmPasswordReset(resetCode, newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) future.complete(ResponseFlag.SUCCESS);
            else {
                future.completeExceptionally(new RuntimeException("Password reset Confirmation failed: " + task.getException()));
            }
        });
        return future;
    }

//    static ResponseFlag deleteAccount(String email, String password) {
//        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
//        if (users_emails.containsKey(email)) {
//            auth.
//                    //..uncomplete
//        }else
//            future.completeExceptionally(new RuntimeException("This email " + email + "is not registered in the system"));
//        if (!password.isEmpty()) {//if entered password value is NOT empty
//            if (!password.equals(user.getPassword()))//entered password i.e. `password` doesn't equal user's password which is the stored in user_records
//                return ResponseFlag.INCORRECT_CREDENTIALS;
//            else {
//                int records_length = user_records.size();
//                user_records.remove(user);
//                if (records_length < user_records.size())// Validating that the records size decreased after user account deletion.
//                    return ResponseFlag.SUCCESS;
//                else
//                    return ResponseFlag.ERROR; //Otherwise send a signal to the frontend that the deletion process failed.
//            }
//        } else
//            return ResponseFlag.PASSWORD_NOT_ENTERED;
//    }
}