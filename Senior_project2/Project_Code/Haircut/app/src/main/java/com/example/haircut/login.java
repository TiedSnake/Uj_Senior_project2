package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.haircut.backend.Service;

public class login extends AppCompatActivity {
    Button login_btn;
    EditText email;
    EditText pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_page);
//        login_btn = findViewById(R.id.signup_button);
//        email = findViewById(R.id.email_field);
//        pwd = findViewById(R.id.pwd_field);
//        login_btn.setOnClickListener(view -> {
//            String _email = email.getText().toString();
//            String _pwd = pwd.getText().toString();
//            Service.ResponseFlag flag = Service.login(_email, _pwd);
//            switch (flag.name()) {
//                case "EMAIL_NOT_ENTERED":
//                    Toast.makeText(login.this, "Please enter the email", Toast.LENGTH_SHORT).show();
//                    break;
//                case "PASSWORD_NOT_ENTERED":
//                    Toast.makeText(login.this, "Please enter the password", Toast.LENGTH_SHORT).show();
//                    break;
//                case "EMAIL_NOT_REGISTERED":
//                    Toast.makeText(login.this, "This email isn't registered", Toast.LENGTH_SHORT).show();
//                    break;
//                case "INCORRECT_CREDENTIALS":
//                    Toast.makeText(login.this, "email or password is invalid", Toast.LENGTH_SHORT).show();
//                    break;
//                case "SUCCESS":
//                    Toast.makeText(login.this, "Access granted", Toast.LENGTH_SHORT).show();
//                    String user = getIntent().getStringExtra("user_type");
//                    Intent intent = null;
//                    if (user != null) {
//                        if (user.equals("customer"))
//                            intent = new Intent(login.this, customer.class);
//                        if (user.equals("barber"))
//                            intent = new Intent(login.this, barber.class);
//                        if (user.equals("admin"))
//                            intent = new Intent(login.this, admin.class);
//                        if (intent != null)
//                            startActivity(intent);
//                    }
//                    break;
//            }
//        });
    }
}
