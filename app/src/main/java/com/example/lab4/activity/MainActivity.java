package com.example.lab4.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.example.lab4.R;
import com.example.lab4.client.Client;
import com.example.lab4.databinding.ActivityMainBinding;
import com.example.lab4.db.ChatDBHelper;
import com.example.lab4.db.User;
import com.example.lab4.fragment.AllMessagesFragment;
import com.example.lab4.fragment.ReadMessagesFragment;
import com.example.lab4.fragment.UnreadMessagesFragment;
import com.example.lab4.server.Server;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public static User user;
    ChatDBHelper helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        replaceFragment(new AllMessagesFragment());
        setContentView(binding.getRoot());
        binding.upperNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.all_item:{
                    replaceFragment(new AllMessagesFragment());
                    break;
                }
                case R.id.unread_item:{
                    replaceFragment(new UnreadMessagesFragment());
                    break;
                }
                case R.id.read_item:{
                    replaceFragment(new ReadMessagesFragment());
                    break;
                }
            }
            return true;
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        helper = new ChatDBHelper(this);
        db = helper.getWritableDatabase();
        ImageButton button = findViewById(R.id.add_chat_button);
        button.setOnClickListener((view)->{
            Intent intent = new Intent(this, ChatCreateActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.user_profile:{
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.developer_info:{
                break;
            }
            case R.id.exit:{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }
}