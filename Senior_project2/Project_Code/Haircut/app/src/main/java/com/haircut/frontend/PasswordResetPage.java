package com.haircut.frontend;

import static com.google.common.base.Throwables.getRootCause;

import android.content.Intent;
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
import com.haircut.backend.Exceptions;
import com.haircut.backend.Exceptions.PasswordResetEmailException;
import com.haircut.backend.FLAGS;
import com.haircut.backend.Service;
import com.haircut.backend.Utility;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class PasswordResetPage extends AppCompatActivity {
    Button passwordResetBtn;
    EditText emailField;
    TextView loginLabel;
    TextView signupLabel;
    ProgressBar progressBar;

    public static CompletableFuture<FLAGS> passwordReset(String email) {
        if (!Utility.isValidEmail(email)) {
            return CompletableFuture.completedFuture(FLAGS.INVALID_EMAIL);
        }
        return Service.resetPassword(email).handle((isSent, ex) -> {
            if (isSent && ex == null)
                return FLAGS.SUCCESS;
            else throw new CompletionException(ex);

        }).exceptionally(ex -> {
            throw new CompletionException(ex);
//            System.out.println("Exception during login: " + ex.getMessage());
//            return FLAGS.ERROR;
        });
    }


    //Onclick method to send user from  password reset login page
    public void passwordResetToLogin(View view) {
        loginLabel = findViewById(R.id.forgot_password_to_login_label);
        startActivity(new Intent(getApplicationContext(), LoginPage.class));
        finish();
    }

    //Onclick method to send user from password reset to signup page
    public void passwordResetToSignup(View view) {
        signupLabel = findViewById(R.id.forgot_password_to_signup_label);
        startActivity(new Intent(getApplicationContext(), PasswordResetPage.class));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        emailField = findViewById(R.id.email_field);
        emailField.setText(getString(R.string.default_email));
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        passwordResetBtn = findViewById((R.id.recover_password_btn));
        passwordResetBtn.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = emailField.getText().toString();
            passwordReset(email).thenAccept(flag -> {
                switch (flag) {
                    case INVALID_EMAIL:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PasswordResetPage.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PasswordResetPage.this, "Reset password email has been sent!", Toast.LENGTH_SHORT).show();
                        Intent forward = new Intent(PasswordResetPage.this, CodeVerification.class);
                        startActivity(forward);
                        finish();
                        break;
                    default:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PasswordResetPage.this, "This is unexpected error", Toast.LENGTH_SHORT).show();
                }
            }).exceptionally(ex -> {
                Throwable rootCause = getRootCause(ex);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (rootCause instanceof Exceptions.UserExistenceCheckException)
                        Toast.makeText(PasswordResetPage.this, "Bad connection with the database", Toast.LENGTH_SHORT).show();
                    if (rootCause instanceof Exceptions.UnregisteredUserException)
                        Toast.makeText(PasswordResetPage.this, "There's no user registered in the system with this email", Toast.LENGTH_SHORT).show();
                    if (rootCause instanceof RuntimeException)
                        Toast.makeText(PasswordResetPage.this, "Bad connection with the cloud function", Toast.LENGTH_SHORT).show();
                    if (rootCause instanceof PasswordResetEmailException)
                        Toast.makeText(PasswordResetPage.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    if (rootCause instanceof NullPointerException)
                        Toast.makeText(PasswordResetPage.this, "Firebase error authentication object is null", Toast.LENGTH_SHORT).show();
                });
                return null;
            });
        });
    }
}
