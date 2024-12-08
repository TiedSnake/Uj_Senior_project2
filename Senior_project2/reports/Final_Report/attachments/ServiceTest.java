package com.haircut.backend;


import static com.google.common.base.Throwables.getRootCause;
import static com.haircut.backend.User.UserType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseUser;
import com.haircut.frontend.shared.SignupPage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RunWith(Parameterized.class)
public class ServiceTest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String userType;


    public ServiceTest(String firstName, String lastName, String email, String password, String userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }


    @Parameterized.Parameters(name = "{index}: Test with firstName={0}, lastName={1}, email={2}, password={3}, userType={4}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{{"Mike", "Smith", "mike@something.com", "ValidPassword1", "CUSTOMER"}, // successful test case
                {"Mi3ke", "Smith", "bria83@gmail.com", "ValidPassword1", "CUSTOMER"}, // Invalid first name
                {"Mike", "Sm!th", "keagan_barrows41@yahoo.com", "ValidPassword1", "CUSTOMER"}, // Invalid last name
                {"Mike", "Smith", "mike@something", "ValidPassword1", "CUSTOMER"},  // Invalid email
                {"Mike", "Smith", "newell_okon@gmail.com", "pass", "CUSTOMER"}, // Invalid password
                {"Mike", "Smith", "margie.vandervort@yahoo.com", "ValidPassword1", "UNKNOWN"} // Invalid usertype
        });
    }

    @Test
    public void testSignup() throws Exception {
        UserType userType1 = UserType.valueOf(userType);
        CompletableFuture<FLAGS> future = SignupPage.signup(firstName, lastName, email, password, userType1);
        User user = Service.getCurrentUser();
        FirebaseUser fUser = Service.getCurrentAuth().getCurrentUser();
        FLAGS result;
        Throwable ex;

        try {
            result = future.join();
            ex = null; // No exception
        } catch (CompletionException e) {
            result = null; // No result
            ex = e.getCause(); // Gets the actual cause of the exception
        }
        if (ex == null && result != null) switch (result) {
            case INVALID_FNAME -> assertEquals(true, Utility.isValidName(firstName));
            case INVALID_LNAME -> assertEquals(true, Utility.isValidName(lastName));
            case INVALID_EMAIL -> assertEquals(true, Utility.isValidEmail(email));
            case INVALID_PASSWORD -> assertEquals(true, Utility.isValidPassword(password));
            case INVALID_USER -> assertEquals(true, Utility.isValidUser(userType1));
            case SUCCESS -> assertNotNull(Service.getCurrentUser());
        }
        else {
            assertNotNull(ex);
            Throwable rootCause = getRootCause(ex);
            String exceptionMessage = rootCause.getMessage();
            assertNotNull(exceptionMessage);
            if (rootCause instanceof Exceptions.UserAlreadyExistsException)
                assertTrue(Service.userExist(email).join());
            else if (rootCause instanceof Exceptions.InvalidUserTypeException)
                assertFalse(Utility.isValidUser(userType1));
            else if (rootCause instanceof Exceptions.UserExistenceCheckException) {
                assertTrue(exceptionMessage.contains("Error when checking existence of the user in the system"));
            } else if (rootCause instanceof Exceptions.UserCreationException) {
                assertTrue(exceptionMessage.contains("Error: failed to create a user in firebase authentication service due to:\n"));
            } else if (rootCause instanceof Exceptions.UserPersistenceException) {
                assertTrue(exceptionMessage.contains("Error: failed to persist user in the database due to: "));
            } else if (rootCause instanceof Exceptions.VerificationEmailException)
                assertTrue(exceptionMessage.contains("Error: failed to send verification email to user due to:\n"));
            else if (rootCause instanceof NullPointerException)
                assertTrue(exceptionMessage.toLowerCase().matches("null"));
        }
    }
}