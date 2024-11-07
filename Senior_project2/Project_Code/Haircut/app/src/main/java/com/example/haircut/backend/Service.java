package com.example.haircut.backend;

import androidx.annotation.NonNull;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import android.util.Log;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


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

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance("http://127.0.0.1:9002/?ns=haircut-508fa");
    private final static FirebaseAuth auth = FirebaseAuth.getInstance();
    private final static DatabaseReference databaseRef;
    private final static FirebaseFunctions functions = FirebaseFunctions.getInstance();
    private static User user;
    //Initialize a reference to realtime database.
//    private final static DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://haircut-508fa-default-rtdb.europe-west1.firebasedatabase.app/").getReference("schema");

     static {
        database.useEmulator("10.0.2.2", 9002);
        auth.useEmulator("10.0.2.2", 9099);
        functions.useEmulator("10.0.2.2", 5001);
        databaseRef = database.getReference("schema");
    }

    //Saves|persist a user in the database
    private static CompletableFuture<FLAGS> persistUser(User user) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        //Passing the user object where the user ID is present in Firebase realtime DB.
        databaseRef.child("users").child(user.getUuid()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, String.format("User %s saved successfully in the database", user.getEmail()));
                future.complete(FLAGS.SUCCESS);
            } else {
                Log.e(TAG, String.format("DatabaseError: Error saving user to database\n%s", task.getException()));
                future.complete(FLAGS.ERROR);
            }
        });
        return future;
    }

    //Saves|persist a customer in the database
    private static CompletableFuture<FLAGS> persistCustomer(Customer customer) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        //Passing the customer object where the customer ID is present in Firebase realtime DB.
        databaseRef.child("users").child("customers").child(customer.getUuid()).setValue(customer).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, String.format("Customer %s saved successfully in the database", customer.getEmail()));
                future.complete(FLAGS.SUCCESS);
            } else {
                Log.e(TAG, String.format("DatabaseError: Error saving customer to database\n%s", task.getException()));
                future.complete(FLAGS.ERROR);
            }
        });
        return future;
    }

    //Saves|persist a barber in the database
    private static CompletableFuture<FLAGS> persistBarber(Barber barber) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        //Passing the barber object where the customer ID is present in Firebase realtime DB.
        databaseRef.child("users").child("barbers").child(barber.getUuid()).setValue(barber).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, String.format("Barber %s saved successfully in the database", barber.getEmail()));
                future.complete(FLAGS.SUCCESS);
            } else {
                Log.e(TAG, String.format("DatabaseError: Error saving barber to database\n%s", task.getException()));
                future.complete(FLAGS.ERROR);
            }
        });
        return future;
    }

    //Saves|persist an administrator in the database
    private static CompletableFuture<FLAGS> persistAdmin(Admin admin) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        //Passing the admin object where the admin ID is present in Firebase realtime DB.
        databaseRef.child("users").child("administrators").child(admin.getUuid()).setValue(admin).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, String.format("Barber %s saved successfully in the database", admin.getEmail()));
                future.complete(FLAGS.SUCCESS);
            } else {
                Log.e(TAG, String.format("DatabaseError: Error saving barber to database\n%s", task.getException()));
                future.complete(FLAGS.ERROR);
            }
        });
        return future;
    }


    //Fetches the user object from database using usere's ID.
    public static CompletableFuture<User> fetchUserById(String userId) {
        CompletableFuture<User> future = new CompletableFuture<>();
        databaseRef.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) future.complete(task.getResult().getValue(User.class));
            else future.completeExceptionally(task.getException());
        });
        return future;
    }

    /**
     * Auxiliary function which takes a user type & classify which column a user belong to in the database.
     *
     * @param userType takes one of the three user types {CUSTOMER, BARBER, ADMIN}.
     * @return Returns a string of the column name.
     */
    static String classifyUser(User.UserType userType) {
        return switch (userType.name()) {
            case "CUSTOMER" -> "customers";
            case "BARBER" -> "barbers";
            case "ADMIN" -> "administrators";
            default -> null;
        };
    }

    //Fetches the user object from database using usere's ID.
    public static CompletableFuture<User> fetchUserByEmail(String email, User.UserType userType) {
        CompletableFuture<User> future = new CompletableFuture<>();
        String table = classifyUser(userType);
        if (table == null)
            failedFuture(new NullPointerException("Fetch error: Unexpected usertype"));
        else databaseRef.child(table).child(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) future.complete(task.getResult().getValue(User.class));
            else future.completeExceptionally(task.getException());
        });
        return future;
    }

    /**
     * @formatter:off
     * Check whether user exist in DB
     * Could use more improvement which is taking the user type (CUSTOMER, BARBER, ADMIN) as a parameter.
         * Pro: Helps in finding the user object faster in the database with guided search.
         * Con: needs to restructure the database to be from Schema->users->current_user.  to Schema->users->usertype{CUSTOMER, BARBER, ADMIN}->current_user
     * @param email
     * @return
     * @formatter:on
     */
    protected static CompletableFuture<Boolean> userExist(String email) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        databaseRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                future.complete(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.completeExceptionally(new RuntimeException("Error when checking existence of the user in the system" +error.getMessage()));
            }
        });
        return future;
    }

    private static CompletableFuture<FLAGS> createUserInFirebase(User user, String password) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        auth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    user.setUuid(userId);
                    future.complete(FLAGS.SUCCESS);
                }
            } else//if user object creation failed propagate the exception.
            {
                Log.e(TAG, String.format("Error: creating user with email & password in Firebase failed due to:\n%s", task.getException()));
                future.complete(FLAGS.ERROR);
            }
        });
        return future;
    }

    /**
     * adding to the hashmap will return the user object we compare against Non-null
     * to force the function into return true if user added successfully & false if not.
     * This Method returns multiple types of object depending on the program flow(User, FLAGS)
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

    private static CompletableFuture<FLAGS> sendVerificationEmail() {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            Log.e(TAG, "Error: Sending email verification process failed due to firebaseUser object being null");
            future.completeExceptionally(new NullPointerException("User error: the Firebase user object is null"));
            return future;
        } else firebaseUser.sendEmailVerification().addOnCompleteListener(verificationTask -> {
            if (verificationTask.isSuccessful()) //sending succeed
            {
                Log.i(TAG, "verification email has been sent successfully!");
                future.complete(FLAGS.SUCCESS);
            } else //sending failed{
            {
                Log.e(TAG, String.format("Error: failed to send verification\n%s", verificationTask.getException()));
                future.completeExceptionally(verificationTask.getException()); //propagate exception
            }
        });
        return future;
    }

    public static CompletableFuture<FLAGS> signup(String firstName, String lastName, String email, String password, User.UserType userType) {
        return userExist(email).thenCompose(exists -> {
            if (exists) {
                Log.e(TAG, String.format("This email %s is already registered in the system with a user", email));
                return CompletableFuture.completedFuture(FLAGS.EMAIL_ALREADY_REGISTERED);
            } else {
                user = createUser(firstName, lastName, email, userType);
                if (user == null)
                    return failedFuture(new IllegalArgumentException("Invalid user type: " + userType));

                return createUserInFirebase(user, password).thenCompose(isCreated -> {
                    if (isCreated.equals(FLAGS.ERROR))
                        return CompletableFuture.completedFuture(FLAGS.ERROR);
                    else {
                        user.setIsLoggedIn(true);
                        return persistUser(user);
                    }
                }).thenCompose(isPersisted -> {
                    if (isPersisted.equals(FLAGS.ERROR)) {
                        Log.e(TAG, "Unexpected error: the user has not been persisted");
                        return CompletableFuture.completedFuture(FLAGS.ERROR);
                    } else return sendVerificationEmail();
                }).thenCompose(isVerified -> {
                    if (isVerified.equals(FLAGS.SUCCESS)) {
                        return CompletableFuture.completedFuture(FLAGS.SUCCESS);
                    } else {
                        Log.e(TAG, "Unexpected error: email verification failed");
                        return CompletableFuture.completedFuture(FLAGS.ERROR);
                    }
                });
            }
        });
    }

    public static CompletableFuture<FLAGS> signout() {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        if (user == null) {
            Log.e(TAG, "Error: Signing out failed, the user object is already null");
            future.completeExceptionally(new NullPointerException());
        } else {
            user.setIsLoggedIn(false);
            auth.signOut();
            future.complete(FLAGS.SUCCESS);
        }
        return future;
    }
//return failedFuture(new NullPointerException("Error: When signing-in, failed to fetch the user object from Firebase by the id"));

    public static CompletableFuture<FirebaseUser> authenticateUser(String email, String password) {
        CompletableFuture<FirebaseUser> future = new CompletableFuture<>();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) future.complete(user);
            } else {
                Log.e(TAG, "The task failed to authenticate the user in firebase during sign-in process with the provided credentials");
                future.completeExceptionally(new RuntimeException("Error: Failed to authenticate user due to\n" + task.getException()));
            }
        });
        return future;
    }

    //This method could use more improvements i.e. custom exceptions
    public static CompletableFuture<User> login(String email, String password, User.UserType userType) {
        return userExist(email).thenCompose(exists -> {
            if (exists.equals(FLAGS.SUCCESS)) {
                Log.i(TAG, String.format("This email %s is registered in the system with a user", email));
                return authenticateUser(email, password).thenCompose(firebaseUser -> {
                    if (firebaseUser != null) {
                        return fetchUserById(firebaseUser.getUid());
                    } else {
                        Log.e(TAG, String.format("Login Error: failed to authenticate firebase user with the provided credentials for the email:\n%s", email));
                        return failedFuture(new Exception("Failed to authenticate user to Firebase"));
                    }
                }).thenCompose(user -> {
                    if (user != null) {
                        //Verify whether the user type which was clicked in the frontend matches the userType in the database
                        if (!user.getUserType().equals(userType))
                            Log.w(TAG, String.format("Usertype from frontend %s doesn't match UserType from the database %s for this user %s", userType, user.getUserType(), user.getUuid()));
                        Log.i(TAG, String.format("User data with email %s has been fetched successfully", email));
                        return CompletableFuture.completedFuture(user);
                    } else {
                        Log.e(TAG, String.format("Login Error: failed to fetch user's data firebase database for the email \n%s", email));
                        return failedFuture(new Exception("Fetching user data failed"));
                    }
                });
            } else {
                Log.e(TAG, String.format("This email %s is not registered in the system", email));
                return failedFuture(new Exception("The entered email is not registered in the system"));
            }
        });
    }

    public static CompletableFuture<FLAGS> verifyCode(String code, User.UserType userType) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        if (user.getUserType().equals(userType)) {
            auth.verifyPasswordResetCode(code).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    future.complete(FLAGS.SUCCESS);
                } else
                    future.completeExceptionally(new RuntimeException("Invalid verification code or it has expired " + task.getException()));
            });
        } else
            future.completeExceptionally(new Exceptions.UserMismatchException(String.format("Usertype from frontend %s doesn't match UserType from the database %s for this user ", userType, user.getUserType())));
        return future;
    }

    public static CompletableFuture<FLAGS> verifyPassword(String resetCode, String newPassword, User.UserType userType) {
        if (true/*flag.equals(FLAGS.SUCCESS)*/) {
            if (!user.getUserType().equals(userType))
                Log.w(TAG, String.format("Usertype from frontend %s doesn't match UserType from the database %s for this user %s", userType, user.getUserType(), user.getUuid()));
            Log.i(TAG, "Password reset code verified successfully.");
            // Step 3: Confirm the password reset with new password
            return confirmPasswordReset(resetCode, newPassword).thenApply(flag2 -> {
                if (flag2.equals(FLAGS.SUCCESS)) {
                    Log.i(TAG, "Password has been reset successfully.");
                    return FLAGS.SUCCESS;
                } else {
                    Log.e(TAG, "Error: failed to confirm the password reset.");
                    return FLAGS.ERROR;
                }
            });
        } else {
            Log.e(TAG, "Error: invalid password reset code.");
            return failedFuture(new Exception("Invalid password reset code"));
        }
    }

    public static CompletableFuture<FLAGS> resetPassword(String email) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        return userExist(email).thenCompose(exists -> {
            if (!exists) {
                future.completeExceptionally(new Exception("This user does not exist in the database"));
                return future;
            } else return sendPasswordResetEmail(email);
        }).thenApply(flag -> {
            if (flag.equals(FLAGS.SUCCESS)) {
                Log.i(TAG, String.format("Password reset email sent successfully to %s", email));
                return FLAGS.SUCCESS;
            } else throw new RuntimeException("Failed to send password reset email to user");
        }).exceptionally(ex -> {
            throw new CompletionException(ex);
        });
    }


    /**
     * Takes an email & Sends a password reset to any email!!!.
     *
     * @param email
     * @return
     */
    private static CompletableFuture<FLAGS> sendPasswordResetEmail(String email) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            future.completeExceptionally(new NullPointerException("User error: the Firebase user object is null"));
            return future;
        } else auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) future.complete(FLAGS.SUCCESS);
            else
                future.completeExceptionally(new RuntimeException("Password Reset Error: Failed to send password reset email due to\n" + task.getException()));
        });
        return future;
    }

    /**
     * Takes a code & verifies whether the reset code matches the one generated by Firebase authentication for the specific user linked with the email.
     *
     * @param resetCode
     * @return
     */
    private static CompletableFuture<FLAGS> verifyPasswordResetCode(String resetCode) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        auth.verifyPasswordResetCode(resetCode).addOnCompleteListener(task -> {
            if (task.isSuccessful()) future.complete(FLAGS.SUCCESS);
            else
                future.completeExceptionally(new RuntimeException("Password Verification Error: Failed to verify password  due to\n" + task.getException()));
        });
        return future;
    }

    /**
     * @param resetCode
     * @param newPassword
     * @return
     */
    private static CompletableFuture<FLAGS> confirmPasswordReset(String resetCode, String newPassword) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        auth.confirmPasswordReset(resetCode, newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) future.complete(FLAGS.SUCCESS);
            else
                future.completeExceptionally(new RuntimeException("Password Confirmation Error: Failed to confirm password  due to\n" + task.getException()));
        });
        return future;
    }

    /**
     * After the function from typescript returns a custom token.
     * 1. Store the token in the database in relation to the user who requested it.
     * 2. After successfully storing it, append the token to the verification link(cloud function) sent to the user in the email.
     * 3. There should be a cloud function which takes this token & verifies it against the token present in the database.
     * 4. If the token matches another cloud funciton should return the `successful verification` static html file.
     * 5. If not, then the cloud Typescript function would return the `verification failed` static html file.
     */
    public static CompletableFuture<String[]> generateCustomToken(String uid) {
        CompletableFuture<String[]> future = new CompletableFuture<>();
        //Calling Typescript function from remote cloud functions.
        functions.getHttpsCallable("generateCustomToken").call(Collections.singletonMap("uid", uid)).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String[] result = (String[]) task.getResult().getData();
                if (result != null && result.length > 1) //result is not empty & contains more than 1 element i.e. result[0]=customToken, result[1]=code
                    future.complete(result);
            } else {
                // Handle error
                Exception e = task.getException();
                Log.e("Firebase", "Error generating custom token", e);
                future.completeExceptionally(new RuntimeException("Firebase error: Error generating custom token\n" + e));
            }
        });
        return future;
    }


//    static FLAGS deleteAccount(String email, String password) {
//        CompletableFuture<FLAGS> future = new CompletableFuture<>();
//        if (users_emails.containsKey(email)) {
//            auth.
//                    //..uncomplete
//        }else
//            future.completeExceptionally(new RuntimeException("This email " + email + "is not registered in the system"));
//        if (!password.isEmpty()) {//if entered password value is NOT empty
//            if (!password.equals(user.getPassword()))//entered password i.e. `password` doesn't equal user's password which is the stored in user_records
//                return FLAGS.INCORRECT_CREDENTIALS;
//            else {
//                int records_length = user_records.size();
//                user_records.remove(user);
//                if (records_length < user_records.size())// Validating that the records size decreased after user account deletion.
//                    return FLAGS.SUCCESS;
//                else
//                    return FLAGS.ERROR; //Otherwise send a signal to the frontend that the deletion process failed.
//            }
//        } else
//            return FLAGS.PASSWORD_NOT_ENTERED;
//    }
}