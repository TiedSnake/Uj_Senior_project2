package com.example.haircut.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.haircut.R;
import com.example.haircut.backend.Service;

import java.util.concurrent.CompletableFuture;

public class LoginPage extends AppCompatActivity {
    Button login_btn;
    EditText email;
    EditText pwd;
    TextView loginToSignup;
    //Onclick method to send user from login to signup page
    public void LoginToSignup(View view) {
        loginToSignup = findViewById(R.id.login_to_signup);
        loginToSignup.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignupPage.class));
        });
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        login_btn = findViewById(R.id.login_button);
        email = findViewById(R.id.email_field);
        pwd = findViewById(R.id.pwd_field);
        login_btn.setOnClickListener(view -> {
            String _email = email.getText().toString();
            String _pwd = pwd.getText().toString();
            try {
                CompletableFuture<Service.ResponseFlag> response = Service.login(_email, _pwd);
                String flag = response.get().name();
                switch (flag) {
                    case "EMAIL_NOT_ENTERED":
                        Toast.makeText(LoginPage.this, "Please enter the email", Toast.LENGTH_SHORT).show();
                        break;
                    case "PASSWORD_NOT_ENTERED":
                        Toast.makeText(LoginPage.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                        break;
                    case "EMAIL_NOT_REGISTERED":
                        Toast.makeText(LoginPage.this, "This email isn't registered", Toast.LENGTH_SHORT).show();
                        break;
                    case "INCORRECT_CREDENTIALS":
                        Toast.makeText(LoginPage.this, "email or password is invalid", Toast.LENGTH_SHORT).show();
                        break;
                    case "SUCCESS":
                        Toast.makeText(LoginPage.this, "Access granted", Toast.LENGTH_SHORT).show();
                        String user = getIntent().getStringExtra("user_type");
                        Intent intent = null;
                        if (user != null) {
                            if (user.equals("customer"))
                                intent = new Intent(LoginPage.this, CustomerPage.class);
                            if (user.equals("barber"))
                                intent = new Intent(LoginPage.this, BarberPage.class);
                            if (user.equals("admin"))
                                intent = new Intent(LoginPage.this, AdminPage.class);
                            if (intent != null)
                                startActivity(intent);
                        }
                        break;
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
