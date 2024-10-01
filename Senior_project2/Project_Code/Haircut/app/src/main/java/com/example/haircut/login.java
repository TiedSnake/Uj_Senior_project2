package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class login extends AppCompatActivity {
    Button login_btn;
    EditText email;
    EditText pwd;
    /**
     * Simulating a database Key-->email_username, value--> (email_domain, password)
     * [username]@[domain_name].tld
     */
    private static final Map<String, String[]> DB = new HashMap<String, String[]>() {
        {
            put("bria83", new String[]{"@gmail.com", "bria83"});
            put("torey_schultz79", new String[]{"@yahoo.com", "torey_schultz79"});
            put("hermann_wiza", new String[]{"@hotmail.c", "hermann_wiza"});
            put("ludie_feest", new String[]{"@yahoo.com", "ludie_feest"});
            put("adrain.ziemann", new String[]{"@yahoo.com", "adrain.ziemann"});
            put("keagan_barrows41", new String[]{"@yahoo.com", "keagan_barrows41"});
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        login_btn = findViewById(R.id.signup_button);
        email = findViewById(R.id.email_field);
        pwd = findViewById(R.id.pwd_field);
        login_btn.setOnClickListener(view -> {
            String _email = email.getText().toString();
            String _email_username = _email.split("@")[0];
            String _pwd = pwd.getText().toString();
            if (!_email.isEmpty() && !_pwd.isEmpty()) {//if both email & password fields are NOT empty
                String[] record;
                //if the database contains the email then fetch its value inside record otherwise pass null to record
                record = DB.containsKey(_email_username) ? DB.get(_email_username) : null;
                if (record == null)//if record is null then email isn't present in the system
                    Toast.makeText(login.this, "This email isn't registered", Toast.LENGTH_SHORT).show();
                else {//email is present.
                    if (!_pwd.equals(record[1]))//entered password i.e. `_pwd` doesn't equal record [1] which is the stored password in DB
                        Toast.makeText(login.this, "email or password is invalid", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(login.this, "Access granted", Toast.LENGTH_SHORT).show();
                        String user = getIntent().getStringExtra("user_type");
                        Intent intent = null;
                        if (user!=null)
                        {
                        if (user.equals("customer"))
                            intent = new Intent(login.this, customer.class);
                        if (user.equals("barber"))
                            intent = new Intent(login.this, barber.class);
                        if (user.equals("admin"))
                            intent = new Intent(login.this, admin.class);
                        if (intent != null)
                            startActivity(intent);
                        }
                    }
                }
            } else {//else one of the fields is empty or both of them are empty
                if (_email.isEmpty())
                    Toast.makeText(login.this, "Please enter the email", Toast.LENGTH_SHORT).show();
                if (_pwd.isEmpty())
                    Toast.makeText(login.this, "Please enter the password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
