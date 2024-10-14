package com.example.haircut.backend;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.security.SecureRandom;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

public class Utility {
    //Needs more work
    private static String NAME_REGEX = "[A-Za-z]+";

    /**
     * ^                 # start-of-string
     * (?=.*[0-9])       # a digit must occur at least once
     * (?=.*[a-z])       # a lower case letter must occur at least once
     * (?=.*[A-Z])       # an upper case letter must occur at least once
     * (?=.*[@#$%^&+=])  # a special character must occur at least once
     * (?=\S+$)          # no whitespace allowed in the entire string
     * .{8,}             # anything, at least eight places though
     * $                 # end-of-string
     */
    private static String PASSWORD_REGEX =
            "^(?=.*[0-9])"
                    + "(?=.*[a-z])"
                    + "(?=.*[A-Z])"
//                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,}$";

    static boolean isValidName(@NonNull String fName) {
        return fName.length() < 20 && fName.matches(NAME_REGEX);
    }

    static boolean isValidPassword(@NonNull String pass) {
        return pass.length() <= 20 && pass.matches(PASSWORD_REGEX);
    }
    // RFC 5322 Official Standard Regex
    /**
     * `^` starts with
     * `?i` enables case-Insensitivity (lower+upper cases)
     * `()` capturing group
     * `*(?:...)` non-capturing-group i.e. match regex inside the group but not included
     * `+` matches one or more of the previous
     * `*` matches zero or more from the previous
     * `a-z` a lower-case letter in the range
     * `0-9` a digit can in the range
     * `!#$%&'\*+/=?^\_\{|}~-` special symbols that can be used in first part of email i.e. username
     * `|` Logical or between capturing group
     * `\"` matches double-quotation mark
     * `\\u0001-\\u0008` matches unicode characters in the range
     * `\\` matches a single backslash by escaping it \
     * `@` matches 'at' symbol.
     * `\\.` matches a dot by escaping it \
     */
    private static final String EMAIL_REGEX =
            "^(?i)(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
                    + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\u0001-\u0008\u000B\u000C\u000E-"
                    + "\u001F!#-]-\\u007F|\\\\[\u0001-\t\u000B\f"
                    + "\u000E-\u007F])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]"
                    + "(?:[a-z0-9-]*[a-z0-9])?|\\[(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?\\.){3}"
                    + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:"
                    + "[\u0001-\b\u000B\f\u000E-\u001F!-ZS-\\u007F]\u0001-\t\u000E-\u007F"
                    + "])$)";


    //Compiles the regex to have it ready to matched against a value.
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Validates the email format using regex.
     *
     * @param email The email address to validate.
     * @return True if the email format is valid, false otherwise.
     */
    public static boolean isValidEmailFormat(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches(); //returns true/false
    }


    public static String signup(String _fname, String _lname, String _email, String _pwd) {
        if (_fname != null && _lname != null && _email != null && _pwd != null)
            if (Utility.isValidName(_fname) && Utility.isValidName(_lname) && Utility.isValidEmailFormat(_email) && Utility.isValidPassword(_pwd)) {//if entered values are valid fname & lname
                return String.valueOf(Service.signup(new User(_fname, _fname, _email, _pwd)));
            } else {
                if (!Utility.isValidName(_fname))
                    return "Please enter a valid first name";
                //                Toast.makeText(signup.this, "Please enter a valid first name", Toast.LENGTH_SHORT).show();
                if (!Utility.isValidName(_lname))
                    return "Please enter a valid last name";
                //                Toast.makeText(signup.this, "Please enter a valid last name", Toast.LENGTH_SHORT).show();
                if (!Utility.isValidEmailFormat(_email))
                    return "Please enter a valid email";
                //                Toast.makeText(signup.this, "Please enter a valid last name", Toast.LENGTH_SHORT).show();
                if (!Utility.isValidPassword(_pwd))
                    return "Please enter a valid password";
                //                Toast.makeText(signup.this, "Please enter a valid last name", Toast.LENGTH_SHORT).show();
            }
        return null;
    }
}

