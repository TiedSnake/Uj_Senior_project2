package com.haircut.frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.haircut.R;
import com.haircut.backend.Service;
import com.haircut.backend.User.UserType;

public class WelcomePage extends AppCompatActivity {
    Button customer_btn;
    Button barber_btn;
    Button admin_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        customer_btn = findViewById(R.id.Customer);
        barber_btn = findViewById(R.id.Barber);
        admin_btn = findViewById(R.id.Admin);
//        if (Service.isLoggedIn()) { //forward the user to the page if user is logged in
//            startActivity(new Intent(WelcomePage.this, CustomerSidebar.class));
//            finish();
//        }
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        customer_btn.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, SignupAndLoginPage.class);
            prefs.edit().putString("USER_TYPE", UserType.CUSTOMER.toString()).apply();
//            intent.putExtra("userType", UserType.CUSTOMER);
            startActivity(intent);
        });
        barber_btn.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, SignupAndLoginPage.class);
            prefs.edit().putString("USER_TYPE", UserType.BARBER.toString()).apply();
//            intent.putExtra("userType", UserType.BARBER);
            startActivity(intent);
        });
        admin_btn.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomePage.this, SignupAndLoginPage.class);
            prefs.edit().putString("USER_TYPE", UserType.ADMIN.toString()).apply();
//            intent.putExtra("userType", UserType.ADMIN);
            startActivity(intent);
        });
    }
}
