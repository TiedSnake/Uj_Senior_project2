package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class welcome extends AppCompatActivity {
    Button customer_btn;
    Button barber_btn;
    Button admin_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        customer_btn = findViewById(R.id.Customer);
        barber_btn = findViewById(R.id.Barber);
        admin_btn = findViewById(R.id.Admin);
        customer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(welcome.this, signup_login.class);
                intent.putExtra("user_type", "customer");
                startActivity(intent);
            }
        });
        barber_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(welcome.this, signup_login.class);
                intent.putExtra("user_type", "barber");
                startActivity(intent);
            }
        });
        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(welcome.this, signup_login.class);
                intent.putExtra("user_type", "admin");
                startActivity(intent);
            }
        });
    }
}
