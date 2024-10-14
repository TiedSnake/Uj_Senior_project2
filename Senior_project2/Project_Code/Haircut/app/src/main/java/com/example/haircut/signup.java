package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {
    EditText fname;
    EditText lname;
    EditText email;
    EditText pwd;
    Button signup_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.signup);
//        fname = findViewById(R.id.f_name);
//        lname = findViewById(R.id.l_name);
//        email = findViewById(R.id.email_field);
//        signup_btn = findViewById(R.id.signup_button);
//        pwd = findViewById(R.id.pwd_field);
        signup_btn.setOnClickListener(view -> {
            String _fname = fname.getText().toString();
            String _lname = lname.getText().toString();
            String _email = email.getText().toString();
            String _pwd = pwd.getText().toString();
            //Needs more writing
            Intent intent = new Intent(signup.this, login.class);
            intent.putExtra("action", "login");
            startActivity(intent);
        });
    }
}
