package com.haircut.backend;

public class Exceptions extends Exception {
//    public static class UserMismatchException extends RuntimeException {
//        /*public UserMismatchException(String errMessage, Throwable err) {
//            super(errMessage, err);
//        }*/
//
//        /**
//         * Exception trigger: User type in the database does not match the user type chosen in the application
//         * @param errMessage
//         */
//        public UserMismatchException(String errMessage) {
//            super(errMessage);
//        }
//    }

    /**
     * Exception trigger: User registering with email already present in the system.
     */
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String errMessage) {
            super(errMessage);
        }
    }

    /**
     * Exception trigger: Database schema initialization failure.
     */
    public static class SchemaInitializationException extends RuntimeException {
        public SchemaInitializationException(String errMessage, Throwable err) {
            super(errMessage, err);
        }
        public SchemaInitializationException(String errMessage) {
            super(errMessage);
        }
    }
    /**
     * Exception trigger: Task exception when fetching any column from Firebase real-time database
     */
    public static class SchemaColumnException extends RuntimeException {
        public SchemaColumnException(String errMessage, Throwable err) {
            super(errMessage, err);
        }
    }
    /**
     * Exception trigger: Failure when checking user existence in Firebase real-time database
     */
    public static class UserExistenceCheckException extends RuntimeException {
        public UserExistenceCheckException(String errMessage, Throwable err) {
            super(errMessage, err);
        }
    }
    /**
     * Exception trigger: Failure when creating a user in Firebase authentication
     */
    public static class UserCreationException extends RuntimeException {
        public UserCreationException(String errMessage, Throwable err) {
            super(errMessage, err);
        }
    }
    /**
     * Exception trigger: Failure when persisting a user in Firebase real-time database
     */
    public static class UserPersistenceException extends RuntimeException {
        public UserPersistenceException(String errMessage, Throwable err) {
            super(errMessage, err);
        }
    }
    /**
     * Exception trigger: Failure when persisting a user in Firebase real-time database
     */
    public static class EmailVerificationException extends RuntimeException {
        public EmailVerificationException(String errMessage, Throwable err) {
            super(errMessage, err);
        }
    }
    /**
     * Exception trigger: User tries to login with unregistered credentials in the system.
     */
    public static class UnregisteredUserException extends RuntimeException {
        public UnregisteredUserException(String errMessage) {
            super(errMessage);
        }
    }

    /**
     * Exception trigger: exception when fetching data from Firebase real-time database
     */
    public static class DataFetchException extends RuntimeException {
        public DataFetchException(String errMessage, Throwable err) {
            super(errMessage, err);
        }
        public DataFetchException(String errMessage) {
            super(errMessage);
        }
    }
    public static class WrongCredentialsException extends RuntimeException {
        public WrongCredentialsException(String errMessage, Throwable err) {
            super(errMessage, err);
        }
        public WrongCredentialsException(String errMessage) {
            super(errMessage);
        }
    }

}
