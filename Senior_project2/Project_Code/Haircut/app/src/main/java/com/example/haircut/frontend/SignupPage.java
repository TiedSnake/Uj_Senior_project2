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
import com.example.haircut.backend.Service;
import com.example.haircut.backend.User;
import com.example.haircut.backend.Utility;
import com.example.haircut.backend.User.UserType;
import com.google.firebase.BuildConfig;

import java.util.concurrent.CompletableFuture;

public class SignupPage extends AppCompatActivity {
    EditText fName;
    EditText lName;
    EditText email;
    EditText pwd;
    Button signup_btn;
    ProgressBar progressBar;
    TextView signupToLogin;

    public enum FLAGS {
        SUCCESS, INVALID_FNAME, INVALID_LNAME, INVALID_EMAIL, INVALID_PASSWORD, INVALID_USER, ERROR
    }

    public static CompletableFuture<FLAGS> signup(String fName, String lName, String email, String pwd, User.UserType userType) {
        if (!Utility.isValidName(fName))
            return CompletableFuture.completedFuture(FLAGS.INVALID_FNAME);
        if (!Utility.isValidName(lName))
            return CompletableFuture.completedFuture(FLAGS.INVALID_LNAME);
        if (!Utility.isValidEmailFormat(email))
            return CompletableFuture.completedFuture(FLAGS.INVALID_EMAIL);
        if (!Utility.isValidPassword(pwd))
            return CompletableFuture.completedFuture(FLAGS.INVALID_PASSWORD);
        if (!Utility.isValidUser(userType))
            return CompletableFuture.completedFuture(FLAGS.INVALID_USER);
        return Service.signup(fName, lName, email, pwd, userType).thenApply(responseFlag -> {
            if (responseFlag == Service.ResponseFlag.SUCCESS) return FLAGS.SUCCESS;
            else return FLAGS.ERROR;
        }).exceptionally(ex -> {
            System.out.println("Exception during signup: " + ex.getMessage());
            return FLAGS.ERROR; //may needs logging
        });
    }

    //Onclick method to send user from signup to login page
    public void SignupToLogin(View view) {
        signupToLogin = findViewById(R.id.signup_to_login);
        startActivity(new Intent(getApplicationContext(), LoginPage.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        fName = findViewById(R.id.f_name);
        lName = findViewById(R.id.l_name);
        email = findViewById(R.id.email_field);
        signup_btn = findViewById(R.id.signup_button);
        pwd = findViewById(R.id.password_field);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        fName.setText(getString(R.string.default_first_name));
        lName.setText(getString(R.string.default_last_name));
        email.setText(getString(R.string.default_email));
        pwd.setText(getString(R.string.default_password));

        signup_btn.setOnClickListener(view -> {
            String _fName = fName.getText().toString();
            String _lName = lName.getText().toString();
            String _email = email.getText().toString();
            String _pwd = pwd.getText().toString();
            //used to get the userType from the previous page `WelcomePage`
//            Intent intent = getIntent();
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            UserType userType = UserType.valueOf(prefs.getString("USER_TYPE", UserType.GUEST.name()));
            progressBar.setVisibility(View.VISIBLE);
//            UserType userType = (User.UserType) intent.getSerializableExtra("userType");
//            if (userType == null) throw new NullPointerException("User type is null");
            signup(_fName, _lName, _email, _pwd, userType).thenAccept(flags -> {
                switch (flags) {
                    case INVALID_FNAME:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignupPage.this, "Please enter a valid first name", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_LNAME:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignupPage.this, "Please enter a valid last name", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_EMAIL:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignupPage.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_PASSWORD:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignupPage.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_USER:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignupPage.this, "Unexpected error: the user type is unidentified!", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignupPage.this, "Signup succeeded!", Toast.LENGTH_SHORT).show();
                        Intent forward = new Intent(SignupPage.this, CustomerPage.class);
                        startActivity(forward);
                        finish();
                        break;
                    default:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignupPage.this, "This is unexpected error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
