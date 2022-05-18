package com.example.lab4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4.R;
import com.example.lab4.db.ChatDBHelper;
import com.example.lab4.db.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrationActivity extends AppCompatActivity {

    private ChatDBHelper helper;
    private SQLiteDatabase db;
    private TextView name_view;
    private TextView nickname_view;
    private TextView email_view;
    private TextView password_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Button register = findViewById(R.id.register_button);
        Button decline = findViewById(R.id.register_decline_button);
        register.setOnClickListener(view -> {
            registration();
        });
        decline.setOnClickListener(view -> {
            declination();
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        name_view = findViewById(R.id.register_name_input);
        nickname_view = findViewById(R.id.register_nickname_input);
        email_view = findViewById(R.id.register_email_input);
        password_view = findViewById(R.id.register_password_input);
        name_view.setOnClickListener(view -> {
            name_view.setText("");
        });
        nickname_view.setOnClickListener(view -> {
            nickname_view.setText("");
        });
        email_view.setOnClickListener(view -> {
            email_view.setText("");
        });
        password_view.setOnClickListener(view -> {
            password_view.setText("");
        });
        helper = new ChatDBHelper(getApplicationContext());
        db = helper.getWritableDatabase();
    }
    private void registration(){
        String email = email_view.getText().toString();
        String password = password_view.getText().toString();
        String name = name_view.getText().toString();
        String nickname = nickname_view.getText().toString();
        if(!email.equals("") && !password.equals("")){
            String hex = email + password;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                final byte[] hash = md.digest(hex.getBytes());
                StringBuilder res = new StringBuilder();
                for(byte b : hash){
                    res.append(String.format("%02x", b));
                }
                User user = new User(name, nickname, email, 0, res.toString());
                boolean result =  helper.addUser(user, db);
                if(!result){
                    Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Email or password are not written", Toast.LENGTH_SHORT).show();
        }
    }
    private void declination(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}