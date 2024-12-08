package com.haircut.backend;

import static com.haircut.backend.User.validUserTypes;

import com.google.firebase.auth.FirebaseAuthException;

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


    private static final String VERIFICATION_CODE_REGEX = "[0-9]{6}";
    private static final Pattern VERIFICATION_CODE_PATTERN = Pattern.compile(VERIFICATION_CODE_REGEX);

    public static boolean isValidVerificationCode(String code) {
        if (code == null) {
            return false;
        } else {
            Matcher matcher = VERIFICATION_CODE_PATTERN.matcher(code);
            return matcher.matches(); //returns true/false
        }
    }

    public static String errorMessage(Throwable ex) {
        String errorCode = ((FirebaseAuthException) ex).getErrorCode();
        switch (errorCode) {
            case "ERROR_INVALID_CUSTOM_TOKEN":
                return "The custom token format is incorrect. Please check the documentation.";

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                return "The custom token corresponds to a different audience.";

            case "ERROR_INVALID_CREDENTIAL":
                return "The supplied auth credential is malformed or has expired.";

            case "ERROR_INVALID_EMAIL":
                return "The email address is badly formatted.";

            case "ERROR_WRONG_PASSWORD":
                return "The password is invalid or the user does not have a password.";

            case "ERROR_USER_MISMATCH":
                return "The supplied credentials do not correspond to the previously signed in user.";

            case "ERROR_REQUIRES_RECENT_LOGIN":
                return "This operation is sensitive and requires recent authentication. Log in again before retrying this request.";

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                return "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.";

            case "ERROR_EMAIL_ALREADY_IN_USE":
                return "The email address is already in use by another account.";

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                return "This credential is already associated with a different user account.";

            case "ERROR_USER_DISABLED":
                return "The user account has been disabled by an administrator.";

            case "ERROR_USER_TOKEN_EXPIRED":
                return "The user\\'s credential is no longer valid. The user must sign in again.";

            case "ERROR_USER_NOT_FOUND":
                return "There is no user record corresponding to this identifier. The user may have been deleted.";

            case "ERROR_INVALID_USER_TOKEN":
                return "The user\\'s credential is no longer valid. The user must sign in again.";

            case "ERROR_OPERATION_NOT_ALLOWED":
                return "This operation is not allowed. You must enable this service in the console.";

            case "ERROR_WEAK_PASSWORD":
                return "The given password is invalid.";
        }
        return null;
    }
}

