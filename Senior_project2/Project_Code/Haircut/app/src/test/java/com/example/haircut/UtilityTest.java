package com.example.haircut;

import com.example.haircut.backend.Utility;

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
        System.out.println(email + " is valid: "+ Utility.isValidEmailFormat(email)); // true
    }

    //validates an email by checking if its domain has "mail-exchange"(MX) record.
//    @ParameterizedTest
//    @ValueSource(strings ={
//            "gowirhgoirhv.com",
//            "gmail.com",
//            "outlook.com",
//            "uj.edu.sa",
//            "yahoo.com"
//
//    })
//    void validateMX(String domain) {
////        System.out.println("MX/A validation for " + domain + ": " + Utility.validateMX(domain));
//    }
}