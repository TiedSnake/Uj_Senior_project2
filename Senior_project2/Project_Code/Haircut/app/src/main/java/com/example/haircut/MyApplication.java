package com.example.haircut;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
    }
}
    