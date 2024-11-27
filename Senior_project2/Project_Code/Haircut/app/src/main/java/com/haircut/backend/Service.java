package com.haircut.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.haircut.backend.Exceptions.DataFetchException;
import com.haircut.backend.Exceptions.EmailVerificationException;
import com.haircut.backend.Exceptions.UnregisteredUserException;
import com.haircut.backend.Exceptions.UserAlreadyExistsException;
import com.haircut.backend.Exceptions.UserCreationException;
import com.haircut.backend.Exceptions.UserExistenceCheckException;
import com.haircut.backend.Exceptions.UserPersistenceException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


public abstract class Service {
    static final int DATABASE_PORT = 9000, AUTH_PORT = 9099, FUNCTIONS_PORT = 5001;
    static final String IP_ADDRESS = "192.168.8.101", PROJECT_ID = "haircut-93a44";
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
    private static final FirebaseDatabase database;
    private final static FirebaseAuth auth;
    private final static FirebaseFunctions functions;
    private final static DatabaseReference databaseRef;
    private final static HashMap<String, User> user_records = new HashMap<>();
    private final static HashMap<String, String> users_emails = new HashMap<>();
    private static final String CUSTOM_TOKEN_CLOUD_FUNCTION_URL = String.format(Locale.ENGLISH, "http://%s:%d/%s/us-central1/verify", IP_ADDRESS, FUNCTIONS_PORT, PROJECT_ID);
    private static FirebaseUser fUser;
    //Initialize a reference to realtime database. {old project}
//    private final static DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://haircut-508fa-default-rtdb.europe-west1.firebasedatabase.app/").getReference("schema");
    private static User user;

    static {
//        final FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
//        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
        /**
         * Computer must be connected to the router using wifi.
         * The ip address below should be the ip address of the computer's wifi interface.
         * The phone must be connected to the same wifi network.
         */
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(false);
//        database.useEmulator("10.0.2.2", 9002);
        database.useEmulator(IP_ADDRESS, DATABASE_PORT);
        auth = FirebaseAuth.getInstance();
        auth.useEmulator(IP_ADDRESS, AUTH_PORT);
        functions = FirebaseFunctions.getInstance();
        functions.useEmulator(IP_ADDRESS, FUNCTIONS_PORT);
        databaseRef = database.getReference("schema");
    }

    public static boolean isLoggedIn() {
        if (user != null) return user.isLoggedIn();
        else return false;
    }

    //Saves|persist a user in the database
    private static CompletableFuture<Boolean> persistUser(User user) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        databaseRef.child("users").child(user.getUuid()).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, String.format("User %s saved successfully in the database", user.getEmail()));
                future.complete(true);
            } else {
                Log.e(TAG, "DatabaseError: Error saving user to database", task.getException());
                future.completeExceptionally(new UserPersistenceException("Error: failed to persist user in the database due to: ", task.getException()));
            }
        });
        return future;
    }

    //Fetches the user object from database using usere's ID.
    public static CompletableFuture<User> fetchUserById(String userId) {
        CompletableFuture<User> future = new CompletableFuture<>();
        databaseRef.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) future.complete(task.getResult().getValue(User.class));
            else {
                Log.e(TAG, String.format("Login Error: failed to fetch user's data firebase database for the email \n%s", user.getEmail()));
                future.completeExceptionally(new DataFetchException("Error: Failed to fetch user's data from the database", task.getException()));
            }
        });
        return future;
    }

    //Fetches the user object from database using usere's ID.
    public static CompletableFuture<User> fetchUserByEmail(String email) {
        CompletableFuture<User> future = new CompletableFuture<>();

        // Query the "users" node, ordering by "email" and filtering for the given email
        databaseRef.child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Since email is unique, we expect only one child
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class); // Assuming User is a POJO
                        future.complete(user);
                        return;
                    }
                }
                // If no user is found, complete exceptionally
                future.completeExceptionally(new RuntimeException("User not found for email: " + email));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(new RuntimeException("DatabaseError: " + databaseError.getMessage()));
            }
        });

        return future;
    }

    //Check whether user exist in DB
    protected static CompletableFuture<Boolean> userExist(String email) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Listen for single value event to check if the user exists
        databaseRef.child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If the snapshot exists and contains data, return true (user exists)
                if (snapshot.exists()) {
                    future.complete(true);
                } else {
                    // If the snapshot doesn't exist, the user is not found
                    future.complete(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors (e.g., permissions issue or network failure)
                Log.e(TAG, "Error checking if user exists: " + error.getMessage(), error.toException());
                future.completeExceptionally(new UserExistenceCheckException("Error when checking existence of the user in the system", error.toException()));
            }
        });

        return future;
    }

    private static CompletableFuture<User> createUserInFirebase(User user, String password) {
        CompletableFuture<User> future = new CompletableFuture<>();
        auth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //After firebase creates a user using the method above `createUserWithEmailAndPassword` only then we can fetch the user instance below!!!.
                fUser = auth.getCurrentUser();
                if (fUser == null)
                    future.completeExceptionally(new NullPointerException("Error: Firebase user object is null"));
                else {
                    String userId = fUser.getUid();
                    user.setUuid(userId);
                    future.complete(user);
                }
            } else//if user object creation failed propagate the exception.
            {
                Log.e(TAG, String.format("Error: creating user with email & password in Firebase failed due to:\n%s", task.getException()));
                future.completeExceptionally(new UserCreationException("Error: failed to create a user in firebase authentication service due to:\n", task.getException()));
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

    private static CompletableFuture<Boolean> sendVerificationEmail(String token) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        if (fUser != null) {
//            String url = CUSTOM_TOKEN_CLOUD_FUNCTION_URL+ String.format("?token=%s", token);
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder().setUrl(CUSTOM_TOKEN_CLOUD_FUNCTION_URL).setHandleCodeInApp(false).build();
            fUser.sendEmailVerification(actionCodeSettings).addOnCompleteListener(task -> {
                if (task.isSuccessful()) //sending succeed
                {
                    Log.i(TAG, "verification email has been sent successfully!");
                    future.complete(true);
                } else //sending failed{
                {
                    Log.e(TAG, String.format("Error: failed to send verification\n%s", task.getException()));
                    future.completeExceptionally(new EmailVerificationException("Error: failed to send verification email to user due to:\n", task.getException())); //propagate exception
                }
            });
        } else {
            Log.e(TAG, "Error: Sending email verification process failed due to firebaseUser object being null");
            future.completeExceptionally(new NullPointerException("Error: failed to send verification email due to Firebase user object is null"));
        }
        return future;
    }

    public static CompletableFuture<String> fetchUserToken() {
        CompletableFuture<String> future = new CompletableFuture<>();
        if (fUser != null) {
            fUser.getIdToken(true).addOnCompleteListener(fetchUserTask -> {
                if (fetchUserTask.isSuccessful()) {
                    future.complete(fetchUserTask.getResult().getToken());
                } else {
                    future.completeExceptionally(new RuntimeException("Failed to fetch the Firebase user token due to:\n", fetchUserTask.getException()));
                    Log.e(TAG, "Failed to get ID token", fetchUserTask.getException());
                }
            });
        } else {
            Log.e(TAG, "Failed to fetch the Firebase user token due to the user object being null");
            future.completeExceptionally(new NullPointerException("Failed to fetch the Firebase user token due to the user object being null"));
        }
        return future;
    }

    public static CompletableFuture<User> signup(String firstName, String lastName, String email, String password, User.UserType userType) {
        return userExist(email).thenCompose(userExists -> {
            if (userExists) {
                Log.e(TAG, String.format("This email %s is already registered in the system with a user", email));
                throw new UserAlreadyExistsException("This user is already registered in the system");
            } else {
                user = switch (userType) {
                    case CUSTOMER -> new Customer(firstName, lastName, email);
                    case BARBER -> new Barber(firstName, lastName, email);
                    case ADMIN -> new Admin(firstName, lastName, email);
                    default -> new User(firstName, lastName, email, User.UserType.GUEST);
                };
                return createUserInFirebase(user, password);
            }
        }).thenCompose(createdUser -> fetchUserToken().thenCompose(Service::sendVerificationEmail)).thenCompose(isSent -> {
            user.setIsLoggedIn(true);
            return persistUser(user);
        }).thenApply(isPersisted -> user);
    }


    /**
     * @param uuid       user's uid
     * @param properties the property(ies) to be updated in user's table in the database.
     *                   {@link #updateProperty(String, Map)}
     */
    private static CompletableFuture<Boolean> updateProperty(String uuid, Map<String, Object> properties) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        databaseRef.child("users").child(uuid).updateChildren(properties).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                properties.keySet().forEach(propertyName -> Log.i(TAG, String.format("The property %s has been updated", propertyName)));
                future.complete(true);
            } else {
                properties.keySet().forEach(propertyName -> Log.i(TAG, String.format("DatabaseError: Error updating the property %s", propertyName), task.getException()));
                future.completeExceptionally(new RuntimeException("Error: Failed to sign out user in the database due to: ", task.getException()));
            }
        });
        return future;
    }

    /**
     * @return returns {@code true} if the process finished successfully else it will return {@code NullPointerException} if the user object is null or {@code RuntimeException} which is thrown by {@link #updateProperty(String, Map)}
     * @apiNote Once the update property method finishes successfully {@link #signout()} finalizes the process by assigning user objects to null
     */
    public static CompletableFuture<Boolean> signout() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        if (user == null) {
            Log.e(TAG, "Error: Signing out failed, the user object is already null");
            future.completeExceptionally(new NullPointerException("Error: Failed to sign-out the user because the user object is null"));
            return future;
        } else
            return updateProperty(user.getUuid(), Collections.singletonMap("isLoggedIn", false)).thenApply(isUpdated -> {
                auth.signOut(); // Signing out/invalidating the Firebase authentication user instance
                fUser = null; // Updating the firebase user reference to reflect the latest changes.
                user = null; // Updating the POJO reference of the user to reflect the latest changes.
                Log.i(TAG, "User has been signed out successfully in the database");
                return true;
            });
    }

    public static CompletableFuture<FirebaseUser> authenticateUser(String email, String password) {
        CompletableFuture<FirebaseUser> future = new CompletableFuture<>();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                fUser = auth.getCurrentUser();
                future.complete(fUser);
            } else {
                Log.e(TAG, "The task failed to authenticate the user in firebase during sign-in process with the provided credentials");
                future.completeExceptionally(new RuntimeException("Error: Failed to authenticate user due to\n" + task.getException()));
            }
        });
        return future;
    }

    public static CompletableFuture<User> login(String email, String password, User.UserType userType) {
        return userExist(email).thenCompose(userExists -> {
            if (!userExists) {
                Log.e(TAG, String.format("This email %s is not registered in the system", email));
                throw new UnregisteredUserException(String.format("The entered email %s is not registered in the system", email));
            } else return authenticateUser(email, password).thenCompose(firebaseUser -> {
                if (firebaseUser == null) {
                    Log.e(TAG, String.format("Login Error: failed to authenticate firebase user with the provided credentials for the email:\n%s", email));
                    throw new NullPointerException("Error: Firebase user object is null");
                } else return fetchUserById(firebaseUser.getUid());

            }).thenApply(user -> {
                //Verify whether the user type which was clicked in the frontend matches the userType in the database
                if (!user.getUserType().equals(userType))
                    Log.w(TAG, String.format("Usertype from frontend %s doesn't match UserType from the database %s for this user %s", userType, user.getUserType(), user.getUuid()));
                Log.i(TAG, String.format("User data with email %s has been fetched successfully", email));

                //Updating the user status in the database.
                user.setIsLoggedIn(true);
                updateProperty(user.getUuid(), Collections.singletonMap("isLoggedIn", true));
                Service.user = user;
                return user;
            });
        });
    }

    /**
     * @param uuid user can't logout if he/she isn't logged in from the first place, that explains why the parameter is of type User instead of email.
     *             the point is to avoid the overhead of finding in the records for the user object again when the user is already logged in.
     * @return
     */
    protected static CompletableFuture<FLAGS> logout(UUID uuid) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        User user = user_records.getOrDefault(uuid, null);
        if (user == null)
            future.completeExceptionally(new RuntimeException("Unexpected error during logout, the user associated with this uuid " + uuid.toString() + " is null"));
        else if (user.isLoggedIn()) {
            auth.signOut();
            user.setIsLoggedIn(false);
            future.complete(FLAGS.SUCCESS);
        } else
            future.completeExceptionally(new RuntimeException("Unexpected error during logout, the user associated with this uuid " + uuid.toString() + " is not signed in the system"));
        return future;
    }

    static CompletableFuture<FLAGS> codeVerification(String code) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        auth.verifyPasswordResetCode(code).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(FLAGS.SUCCESS);
            } else
                future.completeExceptionally(new RuntimeException("Invalid verification code or it has expired " + task.getException()));
        });
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

    /**
     * <ul>
     * <li>{@code Service.user = user} Initializing the user object so the method {@link #buildToken(String)} ()} can proceed.
     * <li>Once the result comes from {@link} it will be substituted in {@link #buildToken(String)} directly & the value will returned true if successful or exception if not</li>
     * <li>{@code exceptionally} block will handle exceptions coming from {@link #userExist(String)}, {@link #fetchUserById(String)}, {@link } and {@link #buildToken(String)}
     * </ul>
     */
    public static CompletableFuture<Boolean> resetPassword(String email) {
        return userExist(email).thenCompose(userExists -> {
            if (userExists) {
                Log.i(TAG, String.format("This email %s is registered in the system with a user", email));
                // Step 1: Send password reset email
                return fetchUserByEmail(email).thenCompose(user -> {
                    if (user != null) {
                        Service.user = user;

//                        return generateCustomToken(user.getUuid()).thenCompose(Service::buildToken);
                    }
                    return null;
                });
            } else {
                Log.e(TAG, String.format("This email %s is not registered in the system", email));
                throw new UnregisteredUserException(String.format("The entered email %s is not registered in the system", email));
            }
        });
//                .exceptionally(ex -> {
//            Log.e(TAG, "Error occurred while resetting password: ", ex);
//            throw new CompletionException(ex);
//        });
    }

    /**
     * @param token Takes the token string which will be embedded in the password reset link.
     * @return returns true if the operation completed successfully or an exception if not.
     */
    public static CompletableFuture<Boolean> buildToken(String token) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        if (auth != null) {
            // String url = "https://appname.firebaseapp.com/verify?token=" + token + "&context=passwordReset";
            String url = String.format(Locale.getDefault(), "http://%s:%d?token=%s&context=passwordReset", IP_ADDRESS, FUNCTIONS_PORT, token);
            ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder().setUrl(url).setHandleCodeInApp(true).build();
            auth.sendPasswordResetEmail(user.getEmail(), actionCodeSettings).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i(TAG, "password reset email has been sent successfully!");
                    future.complete(true);
                } else {
                    Log.e(TAG, String.format("Error: failed to send verification\n%s", task.getException()));
                    future.completeExceptionally(new RuntimeException("Error: failed to send verification email to user due to:\n", task.getException()));
                }
            });
        } else {
            Log.e(TAG, "Error: Sending password reset email failed due to authentication object being null");
            future.completeExceptionally(new NullPointerException("Error: Sending password reset email failed due to authentication object being null"));
        }
        return future;
    }

    /**
     * After the function from typescript returns a custom token.
     * <ol>
     * <li> Store the token in the database in relation to the user who requested it.</li>
     * <li> After successfully storing it, append the token to the verification link(cloud function) sent to the user in the email.</li>
     * <li> There should be a cloud function which takes this token & verifies it against the token present in the database.</li>
     * <li> If the token matches another cloud function should return the `successful verification` static html file.</li>
     * <li> If not, then the cloud Typescript function would return the `verification failed` static html file.</li>
     * </ol>
     */
    @NonNull
    public static CompletableFuture<String> sendVerification(String userToken, String context) {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {
            URL url = new URL(CUSTOM_TOKEN_CLOUD_FUNCTION_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json"); //Sets the request header.

            long createdAt = System.currentTimeMillis(), expiresAt = System.currentTimeMillis() + (1000 * 60); //expires after minute

            urlConnection.setDoOutput(true);
            JSONObject jsonParam = new JSONObject();

            urlConnection.setRequestProperty("authorization token:", userToken); //Sets the request header.
            urlConnection.setRequestProperty("context:", context); //Sets the request header.

//            jsonParam.put("userToken", userToken);

            jsonParam.put("createdAt", createdAt);
            jsonParam.put("expiresAt", expiresAt);


            try (OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int code = urlConnection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream responseStream = urlConnection.getInputStream();
                String result = new BufferedReader(new InputStreamReader(responseStream)).lines().collect(Collectors.joining("\n"));
                JSONObject jsonResponse = new JSONObject(result);
                String token = jsonResponse.getString("token");
                future.complete(token);
            } else {
                future.completeExceptionally(new RuntimeException("Error: " + code));
            }
        } catch (JSONException | IOException e) {
            future.completeExceptionally(new RuntimeException(e));
        }
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