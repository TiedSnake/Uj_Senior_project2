package com.example.haircut.backend;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public abstract class Service {
    /**
     * Simulating a database Key-->email_username, value--> (User{fname, lname, email, password})
     * [username]@[domain_name].tld
     */
    private final static HashMap<UUID, User> user_records = new HashMap<>();
    private final static HashMap<String, UUID> users_emails = new HashMap<>();
    private final static FirebaseAuth auth;

    static {
        //Initializing firebase authentication object
        auth = FirebaseAuth.getInstance();
        //Users Objects, UUID generated inside the User constructor.
        User u1 = new Customer("Mike", "Jake", "bria83@gmail.com", "bria83");
        User u2 = new Barber("Vince", "Morgan", "torey_schultz79@yahoo.com", "torey_schultz79");
        User u3 = new Admin("John", "Alex", "ermann_wiza@hotmail.com", "hermann_wiza");
        User u4 = new Customer("Shaun", "Luis", "udie_feest@yahoo.com", "ludie_feest");
        User u5 = new Customer("Ryan", "Jason", "drain.ziemann@yahoo.com", "adrain.ziemann");
        User u6 = new Customer("Peter", "Grey", "eagan_barrows41@yahoo.com", "keagan_barrows41");

        /*
         * Users records
         * Key: Universally unique identifier
         * Value: user object.
         */
        user_records.put(u1.getUuid(), u1);
        user_records.put(u2.getUuid(), u2);
        user_records.put(u3.getUuid(), u3);
        user_records.put(u4.getUuid(), u4);
        user_records.put(u5.getUuid(), u5);
        user_records.put(u6.getUuid(), u6);

        /*
         * Users Emails map for fast lookups whether a specific user exist in System.
         * Key: User's email.
         * Value: Universally unique identifier
         */
        users_emails.put(u1.getEmail(), u1.getUuid());
        users_emails.put(u2.getEmail(), u2.getUuid());
        users_emails.put(u3.getEmail(), u3.getUuid());
        users_emails.put(u4.getEmail(), u4.getUuid());
        users_emails.put(u5.getEmail(), u5.getUuid());
        users_emails.put(u6.getEmail(), u6.getUuid());
    }

    public enum ResponseFlag {
        SUCCESS,
        EMAIL_NOT_ENTERED,
        PASSWORD_NOT_ENTERED,
        EMAIL_NOT_REGISTERED,
        INCORRECT_CREDENTIALS,
        EMAIL_ALREADY_REGISTERED,
        ERROR
    }

    /**
     * adding to the hashmap will return the user object we compare against Non-null
     * to force the function into return true if user added successfully & false if not.
     * This Method returns multiple types of object depending on the program flow(User, ResponseFlag)
     */
    protected static CompletableFuture<ResponseFlag> signup(String firstName, String lastName, String email, String password, String usertype) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        if (!users_emails.containsKey(email)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            User user;
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                switch (usertype) {
                                    case "customer" ->
                                            user = new Customer(firstName, lastName, email, usertype);
                                    case "barber" ->
                                            user = new Barber(firstName, lastName, email, usertype);
                                    case "admin" ->
                                            user = new Admin(firstName, lastName, email, usertype);
                                    default -> {
                                        future.completeExceptionally(new IllegalArgumentException("Invalid user type: " + usertype));
                                        return;
                                    }
                                }
                                user.setUuid(UUID.fromString(firebaseUser.getUid()));
                                /*
                                If insertion is successful the `put` method would return `null`
                                i.e. no value is present yet in user_records with the entered ID & null if there's a value.
                                 */
                                if (user_records.put(user.getUuid(), user) == null)
                                    //Sending email verification to the registered using Firebase.
                                    firebaseUser.sendEmailVerification()
                                            .addOnCompleteListener(verificationTask ->
                                            {
                                                if (verificationTask.isSuccessful()) //sending succeed
                                                    future.complete(ResponseFlag.SUCCESS);
                                                else //sending failed
                                                    future.completeExceptionally(verificationTask.getException()); //propagate exception
                                            });
                                else //Adding to Database failed.
                                    future.complete(ResponseFlag.ERROR);
                            }
                        } else
                            //if user object creation failed propagate the exception.
                            future.completeExceptionally(task.getException());
                    });
        } else {
            future.complete(ResponseFlag.EMAIL_ALREADY_REGISTERED);
        }
        return future;
    }


    protected static CompletableFuture<ResponseFlag> login(String email, String password) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        if (users_emails.containsKey(email)) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            UUID uuid = users_emails.getOrDefault(email, null);
                            if (uuid == null)
                                future.completeExceptionally(new RuntimeException("Unexpected error this email " + email + " already exist but,\nthe fetched uuid associated with it is null"));
                            else {
                                User user = user_records.get(uuid);
                                if (user == null)
                                    future.completeExceptionally(new RuntimeException("Unexpected error this email " + email + " already exist but,\nthe fetched user object with the uuid " + uuid + " is null"));
                                else {
                                    user.setLoggedIn(true); //Marking the user as logged in
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
            user.setLoggedIn(false);
            future.complete(ResponseFlag.SUCCESS);
        } else
            future.completeExceptionally(new RuntimeException("Unexpected error during logout, the user associated with this uuid " + uuid.toString() + " is not signed in the system"));
        return future;
    }

    static CompletableFuture<ResponseFlag> codeVerification(String code) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        auth.verifyPasswordResetCode(code)
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        future.complete(ResponseFlag.SUCCESS);
                    } else
                        future.completeExceptionally(new RuntimeException("Invalid verification code or it has expired " + task.getException()));
                });
        return future;
    }

    //This Method returns multiple types of object depending on the program flow(User, ResponseFlag)
    static CompletableFuture<ResponseFlag> sendPasswordResetEmail(String email) {
        CompletableFuture<ResponseFlag> future = new CompletableFuture<>();
        if (users_emails.containsKey(email)) {
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setUrl("https://Haircut.com/passwordReset")
                    .setHandleCodeInApp(true)
                    .build();

            auth.sendPasswordResetEmail(email, actionCodeSettings)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            future.complete(ResponseFlag.SUCCESS);
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
        auth.confirmPasswordReset(resetCode, newPassword)
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful())
                        future.complete(ResponseFlag.SUCCESS);
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