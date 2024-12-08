package com.haircut.frontend.shared;

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

import com.google.firebase.auth.FirebaseAuth;
import com.haircut.R;
import com.haircut.backend.Exceptions;
import com.haircut.backend.Exceptions.PasswordResetEmailException;
import com.haircut.backend.FLAGS;
import com.haircut.backend.Service;
import com.haircut.backend.Utility;

import java.util.concurrent.CompletableFuture;

public class PasswordResetPage extends AppCompatActivity {
    Button passwordResetBtn;
    EditText emailField;
    TextView loginLabel;
    TextView signupLabel;
    ProgressBar progressBar;


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
            passwordReset(Service.getCurrentAuth(), email).thenAccept(flag -> {
                switch (flag) {
                    case INVALID_EMAIL:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PasswordResetPage.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PasswordResetPage.this, "Reset password email has been sent!", Toast.LENGTH_SHORT).show();
                        Intent forward = new Intent(PasswordResetPage.this, SignupAndLoginPage.class);
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

    private CompletableFuture<FLAGS> passwordReset(FirebaseAuth auth, String email) {
        CompletableFuture<FLAGS> future = new CompletableFuture<>();
        if (auth != null) {
            progressBar.setVisibility(View.VISIBLE);
            passwordResetBtn.setVisibility(View.INVISIBLE);
            if (!Utility.isValidEmail(email)) {
                future.complete(FLAGS.INVALID_EMAIL);
                return future;
            }
            auth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
                future.complete(FLAGS.SUCCESS);
                Toast.makeText(PasswordResetPage.this, "Password reset link has been sent to the registered email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PasswordResetPage.this, SignupAndLoginPage.class);
                startActivity(intent);
                finish();

            }).addOnFailureListener(e -> {
//            Toast.makeText(PasswordResetPage.this, "Error : " + e.getCause(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                passwordResetBtn.setVisibility(View.VISIBLE);
                future.completeExceptionally(new Exception(e.getCause()));
            });
        } else
            future.completeExceptionally(new RuntimeException("null Firebase authentication object"));
        return future;
    }
}
