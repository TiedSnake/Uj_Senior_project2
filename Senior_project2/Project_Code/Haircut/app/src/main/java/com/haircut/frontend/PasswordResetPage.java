package com.haircut.frontend;

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

//    boolean sendCode(String email) {
//        /**
//         * Incorrect File Path in sendCode Method:
//         *
//         *     The path "resources" is not valid on Android. The File constructor you’re using creates a file in the local filesystem where your app does not have permission.
//         *     On Android, files should be stored in app-specific directories such as getFilesDir() or getCacheDir().
//         */
//        File file = new File("resources", "verification_code.txt");
//        int code = new Double(Math.random() * 100000).intValue();
//        file.getParentFile().mkdirs();
//        // Write content to the file
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//            writer.write(String.format("Your verification code is: %s for the email: %s", code, email));
//            System.out.println("File written successfully to " + file.getAbsolutePath());
//            return true;
//        } catch (IOException e) {
//            Toast.makeText(PasswordResetPage.this, "Error ", Toast.LENGTH_SHORT).show();
//            System.out.println("Error: " + e.getMessage());
//            return false;
//        }
//    }

    public static CompletableFuture<FLAGS> passwordReset(String email) {
        if (!Utility.isValidEmail(email)) {
            return CompletableFuture.completedFuture(FLAGS.INVALID_EMAIL);
        }
        return Service.resetPassword(email).thenApply(flag -> {
            if (flag.equals(FLAGS.SUCCESS))
                return FLAGS.SUCCESS;
            else {
                return FLAGS.ERROR;
            }
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
        startActivity(new Intent(getApplicationContext(), SignupPage.class));
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
                    case ERROR: //Needs to be completed
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PasswordResetPage.this, "Please aenter a valid email", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PasswordResetPage.this, "Signup succeeded!", Toast.LENGTH_SHORT).show();
                        Intent forward = new Intent(PasswordResetPage.this, CodeVerification.class);
                        startActivity(forward);
                        finish();
                        break;
                    default:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PasswordResetPage.this, "This is unexpected error", Toast.LENGTH_SHORT).show();
                }
            }).exceptionally(ex -> {
                Toast.makeText(PasswordResetPage.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            });
        });
    }
}
