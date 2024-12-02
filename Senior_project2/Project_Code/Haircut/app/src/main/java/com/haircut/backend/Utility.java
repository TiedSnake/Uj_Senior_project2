package com.haircut.backend;

import static com.haircut.backend.User.validUserTypes;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Utility {
    private static final String NAME_REGEX = "[A-Za-z]+";

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

    public static boolean isValidName(String fName) {
        if (fName == null)
            return false;
        else
            return fName.length() < 20 && fName.matches(NAME_REGEX);
    }

    //Checks whether user type is part of the enum set in WelcomePage {CUSTOMER, BARBER, ADMIN}
    public static boolean isValidUser(User.UserType userType) {
        try {
            return validUserTypes.contains(userType);
        } catch (IllegalArgumentException e) {
            return false; // Not a valid enum constant
        }
    }

    public static boolean isValidPassword(String pass) {
        if (pass == null)
            return false;
        else
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
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            Matcher matcher = EMAIL_PATTERN.matcher(email);
            return matcher.matches(); //returns true/false
        }
    }

    // TODO: 12/2/24 Needs to be verified
    private static final String VERIFICATION_CODE_REGEX = "[0-9]{6}";
    private static final Pattern VERIFICATION_CODE_PATTERN = Pattern.compile(VERIFICATION_CODE_REGEX);
    // FIXME: 11/30/24 pattern needs to match 6 digits number
    public static boolean isValidVerificationCode(String code) {
        if (code == null) {
            return false;
        } else {
            Matcher matcher = VERIFICATION_CODE_PATTERN.matcher(code);
            return matcher.matches(); //returns true/false
        }
    }
}

