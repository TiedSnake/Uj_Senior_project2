package com.example.haircut.backend;

import java.util.HashMap;

public abstract class Service {
    /**
     * Simulating a database Key-->email_username, value--> (User{fname, lname, email, password})
     * [username]@[domain_name].tld
     */
    private final static HashMap<String, User> user_records = new HashMap<>()
    {
        {
            put("bria83", new User("Mike", "Jake", "bria83@gmail.com", "bria83"));
            put("torey_schultz79", new User("Vince", "Morgan", "torey_schultz79@yahoo.com", "torey_schultz79"));
            put("hermann_wiza", new User("John", "Alex", "ermann_wiza@hotmail.c", "hermann_wiza"));
            put("ludie_feest", new User("Shaun", "Luis", "udie_feest@yahoo.com", "ludie_feest"));
            put("adrain.ziemann", new User("Rayan", "Jason", "drain.ziemann@yahoo.com", "adrain.ziemann"));
            put("keagan_barrows41", new User("Peter", "Grey", "eagan_barrows41@yahoo.com", "keagan_barrows41"));
        }
    };

    public enum ResponseFlag {
        SUCCESS,
        EMAIL_NOT_ENTERED,
        PASSWORD_NOT_ENTERED,
        EMAIL_NOT_REGISTERED,
        INCORRECT_CREDENTIALS,
        ERROR
    }

    /**
     * adding to the hashmap will return the user object we compare against Non-null
     * to force the function into return true if user added successfully & false if not.
     */
    public static boolean signup(User user) {
        return user_records.put(user.getEmail(), user) != null;
    }


    protected static ResponseFlag login(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {//if both email & password fields are NOT empty
            //if the database contains the email then fetch its value inside record otherwise pass null to the user
            User user = user_records.containsKey(email) ? user_records.get(email) : null;
            if (user == null)//if user is null then email isn't present in the system
                return ResponseFlag.EMAIL_NOT_REGISTERED;
            else {//email is present.
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
            return true;
        }
        return false;
    }

    public static ResponseFlag forgotPassword(String entered_email) {
        if (!entered_email.isEmpty()) {
            User user = user_records.containsKey(entered_email) ? user_records.get(entered_email) : null;
            if (user != null) {

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
}