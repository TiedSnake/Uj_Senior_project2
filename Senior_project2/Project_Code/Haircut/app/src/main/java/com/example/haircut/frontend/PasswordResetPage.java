package com.example.haircut.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.haircut.R;
import com.example.haircut.backend.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class PasswordResetPage extends AppCompatActivity {
    boolean sendCode(String email) {
        File file = new File("resources", "verification_code.txt");
        int code = new Double(Math.random() * 100000).intValue();
        file.getParentFile().mkdirs();
        // Write content to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.format("Your verification code is: %s for the email: %s", code, email));
            System.out.println("File written successfully to " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            Toast.makeText(PasswordResetPage.this, "Error ", Toast.LENGTH_SHORT).show();
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        EditText email_field = findViewById(R.id.email_field);
        Button reset_btn = findViewById(R.id.recover_password_btn);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_field.getText().toString();
                try {
                    CompletableFuture<Service.ResponseFlag> response = Service.sendPasswordResetEmail(email);
                    String flag = response.get().name();
                    switch (flag) {
                        case "EMAIL_NOT_ENTERED":
                            Toast.makeText(PasswordResetPage.this, "Please enter the email", Toast.LENGTH_SHORT).show();
                            break;
                        case "ERROR":
                            Toast.makeText(PasswordResetPage.this, "There's no account associated with email.", Toast.LENGTH_SHORT).show();
                            break;
                        case "SUCCESS":
                            if (sendCode(email)) {
                                Intent intent = new Intent(PasswordResetPage.this, CodeVerification.class);
                                startActivity(intent);
                            }
                            break;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                Intent intent = new Intent(PasswordResetPage.this, PasswordResetPage.class);
                intent.putExtra("user_type", "customer");
            }
        });
    }
}
