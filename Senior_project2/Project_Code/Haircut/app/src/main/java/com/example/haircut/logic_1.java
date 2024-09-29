package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class logic_1 extends AppCompatActivity {
    Button customer_button;
    Button barber_button;
    Button admin_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        customer_button = findViewById(R.id.Customer);
        barber_button = findViewById(R.id.Barber);
        admin_button = findViewById(R.id.Admin);
        customer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(logic_1.this, logic_2.class);
                intent.putExtra("user_type", "customer");
            }
        });
        barber_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(logic_1.this, logic_2.class);
                intent.putExtra("user_type", "barber");
            }
        });
        admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(logic_1.this, logic_2.class);
                intent.putExtra("user_type", "admin");
            }
        });
    }
}
