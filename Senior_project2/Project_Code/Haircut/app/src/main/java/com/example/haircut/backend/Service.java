package com.example.haircut.backend;

import android.util.Base64;

import androidx.annotation.Nullable;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.haircut.backend.User;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public abstract class Service {
    /**
     * Simulating a database Key-->email_username, value--> (User{fname, lname, email, password})
     * [username]@[domain_name].tld
     */
    private final static HashMap<UUID, User> user_records = new HashMap<>();
    private final static HashMap<String, UUID> users_emails = new HashMap<>();

    static {
        //Users Objects, UUID generated inside the User constructor.
        User u1 = new User("Mike", "Jake", "bria83@gmail.com", "bria83");
        User u2 = new User("Vince", "Morgan", "torey_schultz79@yahoo.com", "torey_schultz79");
        User u3 = new User("John", "Alex", "ermann_wiza@hotmail.com", "hermann_wiza");
        User u4 = new User("Shaun", "Luis", "udie_feest@yahoo.com", "ludie_feest");
        User u5 = new User("Ryan", "Jason", "drain.ziemann@yahoo.com", "adrain.ziemann");
        User u6 = new User("Peter", "Grey", "eagan_barrows41@yahoo.com", "keagan_barrows41");

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
     */
    protected static ResponseFlag signup(User user) {
        if (users_emails.containsKey(user.getEmail()))
            return ResponseFlag.EMAIL_ALREADY_REGISTERED;
        else {
            //if insertion is successful the `put` method would return `null` i.e. no value is present yet in user_records with the entered ID & null if there's a value.
            if (user_records.put(user.getUuid(), user) != null) {
                String token = generateToken(user.getUuid().toString(), generateSecretKey());
                user.setToken(token);
                /*
                * Need to send a verification email to the user in here. then prompt user to enter code that's sent into the email.
                * if user entered the correct verification code then they will be directed to the jappropriate page & the flag `isLoggedIn` is set to true.
                 */
                return ResponseFlag.SUCCESS;
            } else
                return null;
        }
    }


    protected static ResponseFlag login(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {//if both email & password fields are NOT empty
            //if the database contains the email then fetch its value inside record otherwise pass null to the user
            UUID uuid = users_emails.containsKey(email) ? users_emails.get(email) : null;
            if (uuid == null)//if user is null then email isn't present in the system
                return ResponseFlag.EMAIL_NOT_REGISTERED;
            else {//email is present.
                User user = user_records.get(uuid);
                assert user != null;
                if (!password.equals(user.getPassword()))//entered password i.e. `password` doesn't equal user's password which is the stored in user_records
                    return ResponseFlag.INCORRECT_CREDENTIALS;
                else {
                    return ResponseFlag.SUCCESS;
                }
            }
        } else {//else one of the fields is empty or both of them are empty
            if (email.isEmpty())
                return ResponseFlag.EMAIL_NOT_ENTERED;
            if (password.isEmpty())
                return ResponseFlag.PASSWORD_NOT_ENTERED;
        }
        return null;
    }

    /**
     * @param user user can't logout if he/she isn't logged in from the first place, that explains why the parameter is of type User instead of email.
     *             the point is to avoid the overhead of finding in the records for the user object again when the user is already logged in.
     * @return
     */
    protected static boolean logout(User user) {
        if (user != null && user.isLoggedIn()) {
            user.setIsLoggedIn(false);
            //redirect user to main page.
            return true;
        }
        return false;
    }
    static ResponseFlag reset_password(UUID uuid, String password)
    {
        User user = user_records.get(uuid);
        if (user!=null)
        {
            if(!password.isEmpty())
            {

            }
        }else
        {
            return ResponseFlag.ERROR;
        }
        return null;
    }
    static ResponseFlag forgotPassword(String entered_email) {
        if (!entered_email.isEmpty()) {
            UUID uuid = users_emails.containsKey(entered_email) ? users_emails.get(entered_email) : null;
            if (uuid != null) {
                /*
                * Send email contains code verification to the user email.
                * forward the user to the code verification page along with the UUID of the user.
                 */
                return ResponseFlag.SUCCESS;
            } else
                return ResponseFlag.ERROR;// There's no account associated with this email.
        } else
            return ResponseFlag.EMAIL_NOT_ENTERED;
    }

    static ResponseFlag deleteAccount(User user, String entered_Pass) {
        if (!entered_Pass.isEmpty()) {//if entered password value is NOT empty
            if (!entered_Pass.equals(user.getPassword()))//entered password i.e. `password` doesn't equal user's password which is the stored in user_records
                return ResponseFlag.INCORRECT_CREDENTIALS;
            else {
                int records_length = user_records.size();
                user_records.remove(user);
                if (records_length < user_records.size())// Validating that the records size decreased after user account deletion.
                    return ResponseFlag.SUCCESS;
                else
                    return ResponseFlag.ERROR; //Otherwise send a signal to the frontend that the deletion process failed.
            }
        } else
            return ResponseFlag.PASSWORD_NOT_ENTERED;
    }

    static String generateToken(String userId, String secretKey) {
        /**
         * Token to identify the user consist of:
         * Issuer: The one who issue the token which is our app in this case.
         * IssuedAt: The date when the token was issued.
         * WithExpiresAt: when does the token expire.
         * withClaim: header & payload of the token which contains data specific to the user.
         * secretKey: encryption key used to encrypted the previous data.
         * sign: The algorithm used to sign the token with a secret key.
         */
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Date now = new Date();
        //1 hour expiration in milliseconds
        Date expiryDate = new Date(now.getTime() + 3600 * 1000);
        //returns the token
        return JWT.create()
                .withIssuer("myApp")
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withClaim("userId", userId)
                .sign(algorithm);
    }

    private static String generateSecretKey() {
        byte[] key = new byte[32];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);

        return Base64.encodeToString(key, Base64.NO_WRAP);
    }
}