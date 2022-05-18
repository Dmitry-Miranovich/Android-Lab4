package com.example.lab4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4.R;
import com.example.lab4.db.ChatDBHelper;
import com.example.lab4.db.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    ChatDBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    public void onResume(){
        super.onResume();
        Button button = findViewById(R.id.login_button);
        TextView email_view = findViewById(R.id.login);
        TextView password_view = findViewById(R.id.password);
        email_view.setOnClickListener(view -> {
            email_view.setText("");
        });
        password_view.setOnClickListener(view -> {
            password_view.setText("");
        });
        TextView registration_field = findViewById(R.id.registration);
        helper = new ChatDBHelper(this);
        db = helper.getWritableDatabase();

        button.setOnClickListener(view -> login());
        registration_field.setOnClickListener((view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        }));
    }
    private void login(){
        TextView email_view = findViewById(R.id.login);
        TextView password_view = findViewById(R.id.password);
        String email = email_view.getText().toString();
        String password = password_view.getText().toString();
        if(!email.equals("") && !password.equals("")){
            String hex = email + password;
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                final byte[] hash = md.digest(hex.getBytes());
                StringBuilder res = new StringBuilder();
                for(byte b : hash){
                    res.append(String.format("%02x", b));
                }
                User user = helper.getUserByHex(db,res.toString());
                if(user != null){
                    System.out.println(user);
                    MainActivity.user = user;
                    ProfileActivity.user = user;
                    ProfileActivity.isAuthorized = true;
                    ChatCreateActivity.user = user;
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }else{
                    System.out.println("ERROR : person is null");
                    Toast.makeText(getApplicationContext(), "Email or password are invalid", Toast.LENGTH_SHORT).show();
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Email or password are not written", Toast.LENGTH_SHORT).show();
        }
    }
}