package com.haircut.frontend.shared;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        FirebaseApp.initializeApp(this);
//        FirebaseAppCheck appCheck = FirebaseAppCheck.getInstance();
//        appCheck.installAppCheckProviderFactory(
//                PlayIntegrityAppCheckProviderFactory.getInstance());
    }
}
