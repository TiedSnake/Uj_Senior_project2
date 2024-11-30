package com.haircut.frontend;

import static com.google.common.base.Throwables.getRootCause;

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

import com.haircut.R;
import com.haircut.backend.Exceptions.UnregisteredUserException;
import com.haircut.backend.Exceptions.DataFetchException;
import com.haircut.backend.FLAGS;
import com.haircut.backend.Service;
import com.haircut.backend.User.UserType;
import com.haircut.backend.Utility;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class LoginPage extends AppCompatActivity {
    Button loginBtn;
    EditText email;
    EditText pwd;
    TextView loginToSignup;
    ProgressBar progressBar;



    //Onclick method to send user from login to password reset page
    public void passwordReset(View view) {
        loginToSignup = findViewById(R.id.login_to_forgot_password_label);
        startActivity(new Intent(getApplicationContext(), PasswordResetPage.class));
        finish();
    }

    //Onclick method to send user from login to signup page
    public void loginToSignup(View view) {
        loginToSignup = findViewById(R.id.login_to_signup_label);
        startActivity(new Intent(getApplicationContext(), SignupPage.class));
        finish();
    }

    public static CompletableFuture<FLAGS> login(String email, String password, UserType userType) {
        if (!Utility.isValidEmail(email)) {
            return CompletableFuture.completedFuture(FLAGS.INVALID_EMAIL);
        }
        if (!Utility.isValidPassword(password)) {
            return CompletableFuture.completedFuture(FLAGS.INVALID_PASSWORD);
        }
        if (!Utility.isValidUser(userType)) {
            return CompletableFuture.completedFuture(FLAGS.INVALID_USER);
        }
        return Service.login(email, password, userType).handleAsync((user, ex) -> {
            if (user != null && ex == null)
                return FLAGS.SUCCESS;
            else
                throw new CompletionException(ex);
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
            //User type may help in user lookup in the system
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            UserType userType = UserType.valueOf(prefs.getString("USER_TYPE", UserType.GUEST.name()));
            String _email = email.getText().toString();
            String _pwd = pwd.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            login(_email, _pwd, userType).thenAccept(flags -> {
                runOnUiThread(() ->{
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
                    case SUCCESS:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginPage.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        Intent forward = new Intent(LoginPage.this, CustomerPage.class);
                        //FLAG_ACTIVITY_CLEAR_TASK: This flag clears any existing task that would be associated with the new activity, effectively clearing the back stack.
                        //FLAG_ACTIVITY_NEW_TASK: This flag starts the activity in a new task.
                        forward.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(forward); finish(); //This ensures the LoginActivity itself is closed, so it is not left in the back stack.
                        break;
//                    default:
//                        progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(LoginPage.this, "This is unexpected error", Toast.LENGTH_SHORT).show();
                }
                });
            }).exceptionally(ex -> {
                Throwable rootCause = getRootCause(ex);
                runOnUiThread(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                if (rootCause instanceof UnregisteredUserException)
                    Toast.makeText(LoginPage.this, "The entered email is not registered in the system", Toast.LENGTH_SHORT).show();
                if (rootCause instanceof NullPointerException)
                    Toast.makeText(LoginPage.this, "Error: Firebase user object is null", Toast.LENGTH_SHORT).show();
                if (rootCause instanceof DataFetchException)
                    Toast.makeText(LoginPage.this, "Error: Failed to fetch user's data from the database", Toast.LENGTH_SHORT).show();
                });
                return null;
            });
        });
    }
}
