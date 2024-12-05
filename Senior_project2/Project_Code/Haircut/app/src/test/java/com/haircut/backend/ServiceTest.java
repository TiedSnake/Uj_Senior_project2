//package com.haircut.backend;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.functions.FirebaseFunctions;
//
//import junit.framework.TestCase;
//
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//import java.util.Locale;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//public class ServiceTest extends TestCase {
//    public void setUp() throws Exception {
//        final int DATABASE_PORT = 9000, AUTH_PORT = 9099, FUNCTIONS_PORT = 5001;
//        final String IP_ADDRESS = "10.0.2.2", PROJECT_ID = "haircut-93a44";
//
//        final FirebaseDatabase database;
//        final FirebaseAuth auth;
//        final FirebaseFunctions functions;
//        final DatabaseReference databaseRef;
//        final String CUSTOM_TOKEN_CLOUD_FUNCTION_URL = String.format(Locale.ENGLISH, "http://%s:%d/%s/us-central1/generateLink", IP_ADDRESS, FUNCTIONS_PORT, PROJECT_ID);
//        //      final String CUSTOM_TOKEN_CLOUD_FUNCTION_URL = "https://us-central1-haircut-93a44.cloudfunctions.net/generateLink";
//        FirebaseUser fUser;
//        //Initialize a reference to realtime database. {old project}
////     final  DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://haircut-508fa-default-rtdb.europe-west1.firebasedatabase.app/").getReference("schema");
//        User user;
//
//
////        final FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
////        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
//        /**
//         * Computer must be connected to the router using wifi.
//         * The ip address below should be the ip address of the computer's wifi interface.
//         * The phone must be connected to the same wifi network.
//         */
//        database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(false);
////        database.useEmulator("10.0.2.2", 9002);
//        database.useEmulator(IP_ADDRESS, DATABASE_PORT);
//        auth = FirebaseAuth.getInstance();
//        auth.useEmulator(IP_ADDRESS, AUTH_PORT);
//        functions = FirebaseFunctions.getInstance();
//        functions.useEmulator(IP_ADDRESS, FUNCTIONS_PORT);
//        databaseRef = database.getReference("schema");
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "Mike, Smith, mike@something.com, ValidPassword1, CUSTOMER",
//            "Mi3ke, Smith, mike@something.com, ValidPassword1, CUSTOMER", // Invalid first name
//            "Mike, Sm!th, mike@something.com, ValidPassword1, CUSTOMER", // Invalid last name
//            "Mike, Smith, mike@something, ValidPassword1, CUSTOMER",     // Invalid email
//            "Mike, Smith, mike@something.com, pass, CUSTOMER"            // Invalid password
//    })
//    public void testSignup(String firstName, String lastName, String email, String password, UserType userType) throws Exception {
//        CompletableFuture<User> future = Service.signup(firstName, lastName, email, password, userType);
//
//        try {
//            User user = future.get();
//            if (userType != null && firstName.matches("[a-zA-Z]+") && lastName.matches("[a-zA-Z]+") && email.contains("@") && password.length() >= 8) {
//                assertNotNull(user);
//                assertEquals(email, user.getEmail());
//            } else {
//                fail("Invalid input should not create a user");
//            }
//        } catch (ExecutionException | InterruptedException ex) {
//            // Handle expected errors for invalid input
//            assertTrue(ex.getCause() instanceof IllegalArgumentException || ex.getCause() instanceof Exceptions.UserAlreadyExistsException);
//        }
//    }
//
//    enum UserType {
//        CUSTOMER,
//        BARBER,
//        ADMIN,
//        GUEST,
//        A,
//        B,
//        C,
//        D
//    }
//}