package com.haircut.frontend;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Connect to the Firebase emulators
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.useEmulator("127.0.0.1", 9099);  // Authentication Emulator

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("127.0.0.1", 8080);  // Firestore Emulator

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.useEmulator("127.0.0.1", 9000);  // Realtime Database Emulator

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.useEmulator("127.0.0.1", 9199);  // Storage Emulator
    }
}
