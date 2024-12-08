package com.haircut.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UtilityTest {
    private static final Logger log = LoggerFactory.getLogger(UtilityTest.class);

    //validates an email syntactically via regex.
    @ParameterizedTest
    @ValueSource(strings = {
            "trent39@hotmail.com",
            "marques46@yahoo.com",
            "brennon.grady7@yahoo.com",
            "allan.johnson92@yahoo.com",
            "melissa45@hotmail.com",
            "bria83@gmail.com",
            "torey_schultz79@yahoo.com",
            "hermann_wiza@hotmail.com",
            "ludie_feest@yahoo.com",
            "adrain.ziemann@yahoo.com",
            "keagan_barrows41@yahoo.com",
            "vinnie.goldner@gmail.com",
            "marielle_welch@hotmail.com",
            "evalyn.cronin@hotmail.com",
            "newell_okon@gmail.com",
            "shana_toy86@yahoo.com",
            "margie.vandervort@yahoo.com",
            "waino11@hotmail.com",
            "marcelle73@yahoo.com",
            "test.user@example.com",
            "invalid-email@.com",
    })
    void isValidEmail(String email) {
        assertEquals(true, Utility.isValidEmail(email));
//        System.out.println(email + " is valid: "+ com.haircut.backend.Utility.isValidEmail(email)); // true
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Mike", "Joseph", "Ahmad", "Nasser", "K5alid", "f", "A", ""
    })
    void isValidName(String name) {
        boolean isValid = Utility.isValidName(name);

        switch (name) {
            case "Mike":
                assertEquals(true, isValid); // Assert true for valid names
                break;
            case "Joseph":
                assertEquals(true, isValid); // Assert true for valid names
                break;
            case "Ahmad":
                assertEquals(true, isValid); // Assert true for valid names
                break;
            case "Nasser":
                assertEquals(true, isValid); // Assert true for valid names
                break;
            case "K5alid":
                assertEquals(true, isValid); // Assert true for valid names
                break;
            case "f":
                assertEquals(true, isValid); // Assert true for valid names
                break;
            case "A":
                assertEquals(true, isValid); // Assert true for valid names
                break;
            case "":
                assertEquals(true, isValid); // Assert false for empty string
                break;
        }
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "Mike",
            "GSGSOK",
            "#%@#%@#%",
            "29837589",
            "dlsfjsdkfj",
            "g9384y9g()*Y98g2498y9*Y9f82908fy2",
            "1tk@aoeY",
            "Tw1%2k0",
            "Tw12k0",
            "Valid21891",
            "Valid2qr1"
    })
    void isValidPassword(String password) {
        assertEquals(true, Utility.isValidPassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "CUSTOMER",
            "BARBER",
            "ADMIN",
            "GUEST",
            "UNKNOWN"
    })
    void isValidUser(String userType) {
        User.UserType userType1 = User.UserType.valueOf(userType);
        assertEquals(true, Utility.isValidUser(userType1));
    }
}