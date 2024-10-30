package com.example.haircut.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.haircut.R;

public class SignupAndLoginPage extends AppCompatActivity {
    Button login_btn;
    Button signup_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup);
        login_btn = findViewById(R.id.signup_button);
        signup_btn = findViewById(R.id.Signup_button);
        login_btn.setOnClickListener(view -> {
            Intent intent = new Intent(SignupAndLoginPage.this, LoginPage.class);
            intent.putExtra("action", "login");
            startActivity(intent);
        });
        signup_btn.setOnClickListener(view -> {
            Intent intent = new Intent(SignupAndLoginPage.this, SignupPage.class);
            intent.putExtra("action", "signup");
            startActivity(intent);
        });
    }
}
