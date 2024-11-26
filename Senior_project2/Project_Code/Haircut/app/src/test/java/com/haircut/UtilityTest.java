package com.haircut;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class UtilityTest {
    private static final Logger log = LoggerFactory.getLogger(UtilityTest.class);
    //validates an email syntactically via regex.
    @ParameterizedTest
    @ValueSource(strings ={
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
    void isValidEmailFormat(String email) {
        System.out.println(email + " is valid: "+ com.haircut.backend.Utility.isValidEmailFormat(email)); // true
    }

    @ParameterizedTest
    @ValueSource(strings ={
            "Mike",
            "Joseph",
            "Ahmad",
            "Nasser",
            "K5alid",
            "f",
            "A",
            "",
    })
    void isValidName() {
    }

    @ParameterizedTest
    @ValueSource(strings ={
            "Mike",
            "GSGSOK",
            "#%@#%@#%",
            "29837589",
            "dlsfjsdkfj",
            "g9384y9g()*Y98g2498y9*Y9f82908fy2",
            "1tk@aoeY",
            "Tw1%2k0",
            "Tw12k0",
    })
    void isValidPassword() {
    }
//    @ParameterizedTest
//    @CsvSource({
//            "Mike, ssga, something@something.com, t24oth2owT", "customer", //valid data
//            "Mi3ke, ssga, something@something.com, t24oth2owT", "customer",//invalid first name
//            "Mike, ss$ga, something@something.com, t24oth2owT", "customer",//invalid last name
//            "Mike, ssga, somethingsomething.com, t24oth2owT", "customer",//invalid email
//            "Mike, ssga, something@something.com, t24oth2owt", "customer",//invalid password
//    })
//    void signupTest(String fname, String lname, String email, String password, String userType) {
//        String result = com.example.haircut.backend.Utility.signup( fname, lname, email, password, userType);
//        Assertions.assertEquals("-1", result);
//    }
}