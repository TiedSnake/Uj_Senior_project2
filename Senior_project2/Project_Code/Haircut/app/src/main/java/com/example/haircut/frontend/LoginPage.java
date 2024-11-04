package com.example.haircut.frontend;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.haircut.R;
import com.example.haircut.backend.FLAGS;
import com.example.haircut.backend.Service;
import com.example.haircut.backend.User;
import com.example.haircut.backend.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class LoginPage extends AppCompatActivity {
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    Button loginBtn;
    EditText email;
    EditText pwd;
    TextView loginToSignup;
    ProgressBar progressBar;

    //User type may help in user lookup in the system
    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
    User.UserType userType = User.UserType.valueOf(prefs.getString("USER_TYPE", User.UserType.GUEST.name()));

    //Onclick method to send user from login to password reset page
    public void passwordReset(View view) {
        loginToSignup = findViewById(R.id.reset_password);
        startActivity(new Intent(getApplicationContext(), PasswordResetPage.class));
    }
    //Onclick method to send user from login to signup page
    public void LoginToSignup(View view) {
        loginToSignup = findViewById(R.id.login_to_signup);
        startActivity(new Intent(getApplicationContext(), SignupPage.class));
    }

    public static CompletableFuture<FLAGS> login(String email, String password, User.UserType userType) {
        if (!Utility.isValidEmailFormat(email)) {
            return CompletableFuture.completedFuture(FLAGS.INVALID_EMAIL);
        }
        if (!Utility.isValidPassword(password)) {
            return CompletableFuture.completedFuture(FLAGS.INVALID_PASSWORD);
        }
        if (!Utility.isValidUser(userType)) {
            return CompletableFuture.completedFuture(FLAGS.INVALID_USER);
        }
        return Service.login(email, password, userType).thenApply(user -> {
            if (user == null)
                return FLAGS.ERROR;
            else {
                return FLAGS.SUCCESS;
            }
        }).exceptionally(ex -> {
            System.out.println("Exception during login: " + ex.getMessage());
            return FLAGS.ERROR;
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        loginBtn = findViewById(R.id.login_button);
        email = findViewById(R.id.email_field);
        pwd = findViewById(R.id.pwd_field);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        email.setText(getString(R.string.default_email));
        pwd.setText(getString(R.string.default_password));

        loginBtn.setOnClickListener(view -> {
            String _email = email.getText().toString();
            String _pwd = pwd.getText().toString();
            progressBar.setVisibility(View.VISIBLE);


            login(_email, _pwd, userType).thenAccept(flags -> {
                switch (flags) {
                    case INVALID_EMAIL:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginPage.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_PASSWORD:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginPage.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_USER:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginPage.this, "Unexpected error: the user type is unidentified!", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR: //Needs to be completed
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginPage.this, "Please aenter a valid email", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginPage.this, "Signup succeeded!", Toast.LENGTH_SHORT).show();
                        Intent forward = new Intent(LoginPage.this, CustomerPage.class);
                        startActivity(forward);
                        finish();
                        break;
                    default:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginPage.this, "This is unexpected error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
