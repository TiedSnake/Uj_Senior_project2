package com.haircut.frontend;

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
import com.haircut.backend.FLAGS;
import com.haircut.backend.Service;
import com.haircut.backend.User;
import com.haircut.backend.Utility;

import java.util.concurrent.CompletableFuture;

public class CodeVerification extends AppCompatActivity {
    EditText verificationCodeField;
    TextView newPasswordLabel;
    TextView retypedPasswordLabel;
    EditText newPasswordField;
    EditText retypedPasswordField;
    Button verifyBtn;
    Button resetButton;
    ProgressBar progressBar;

    //User type may help in user lookup in the system
    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
    User.UserType userType = User.UserType.valueOf(prefs.getString("USER_TYPE", User.UserType.GUEST.name()));

    public static CompletableFuture<FLAGS> confirmVerificationCode(String resetCode, String newPassword, String retypedPassword, User.UserType userType) {
        if (!Utility.isValidUser(userType))
            return CompletableFuture.completedFuture(FLAGS.INVALID_USER);
        if (!Utility.isValidPassword(newPassword))
            return CompletableFuture.completedFuture(FLAGS.INVALID_NEW_PASSWORD);
        if (!newPassword.equals(retypedPassword))
            return CompletableFuture.completedFuture(FLAGS.PASSWORD_MISMATCH);
        return Service.verifyPassword(resetCode, newPassword, userType).thenApply(user -> {
            if (user == null) return FLAGS.ERROR;
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
        setContentView(R.layout.verification_page);

        verificationCodeField = findViewById(R.id.verification_code_field);
        newPasswordLabel = findViewById(R.id.new_password_label);
        newPasswordField = findViewById(R.id.new_password_field);
        retypedPasswordField = findViewById(R.id.verify_new_password_field);
        retypedPasswordLabel = findViewById(R.id.verify_new_password_label);
        resetButton = findViewById(R.id.reset_btn);
        verifyBtn = findViewById(R.id.verify_btn);
        progressBar = findViewById(R.id.progressBar);

        verifyBtn.setVisibility(View.INVISIBLE);
        newPasswordLabel.setVisibility(View.INVISIBLE);
        newPasswordField.setVisibility(View.INVISIBLE);
        retypedPasswordLabel.setVisibility(View.INVISIBLE);
        retypedPasswordField.setVisibility(View.INVISIBLE);
        verificationCodeField.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        //Can't assign default values in here. must verify manually.


        String code = verificationCodeField.toString();

        //Must see what kind of verification code Firebase sends in here so I could compose a Regex to verify whether entered verification code matches it.
        resetButton.setOnClickListener(view -> {
            if (Utility.isValidVerificationCode(code)) {
                verificationCodeField.setVisibility(View.INVISIBLE);
                verifyBtn.setVisibility(View.VISIBLE);
                newPasswordLabel.setVisibility(View.VISIBLE);
                newPasswordField.setVisibility(View.VISIBLE);
                retypedPasswordLabel.setVisibility(View.VISIBLE);
                retypedPasswordField.setVisibility(View.VISIBLE);
            } else
                Toast.makeText(CodeVerification.this, "Please enter a valid verification code", Toast.LENGTH_SHORT).show();
        });
        verifyBtn.setOnClickListener(view -> {
            String newPassword = newPasswordField.toString();
            String retypedPassword = retypedPasswordField.toString();
            progressBar.setVisibility(View.VISIBLE);
            confirmVerificationCode(code, newPassword, retypedPassword, userType).thenAccept(flags -> {
                switch (flags) {
                    case INVALID_USER:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CodeVerification.this, "Unexpected error: the user type is unidentified!", Toast.LENGTH_SHORT).show();
                        break;
                    case INVALID_NEW_PASSWORD:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CodeVerification.this, "New password error: please enter a valid new password!", Toast.LENGTH_SHORT).show();
                        break;
                    case PASSWORD_MISMATCH:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CodeVerification.this, "Password mismatch error: The passwords do not match", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR: //Needs to be completed
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CodeVerification.this, "Please aenter a valid email", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CodeVerification.this, "Signup succeeded!", Toast.LENGTH_SHORT).show();
                        Intent forward = new Intent(CodeVerification.this, CustomerPage.class);
                        startActivity(forward);
                        finish();
                        break;
                    default:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CodeVerification.this, "This is unexpected error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
