package com.example.lab4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.lab4.R;
import com.example.lab4.adapters.ProfileAdapter;
import com.example.lab4.adapters.ProfileDescription;
import com.example.lab4.db.User;

import java.util.LinkedList;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    public static User user;
    public static User guest;
    public static boolean isAuthorized = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onResume(){
        super.onResume();
        LinkedList<ProfileDescription> descriptions;
        descriptions = (isAuthorized)? addDescriptions(user):addDescriptions(guest);
        ListView view = findViewById(R.id.profile_list_view);
        ProfileAdapter adapter = new ProfileAdapter(this, R.layout.profile_list_layout, descriptions);
        view.setAdapter(adapter);
        isAuthorized = true;
    }

    private LinkedList<ProfileDescription> addDescriptions(User user){
        LinkedList<ProfileDescription> descriptions = new LinkedList<>();
        descriptions.add(new ProfileDescription("Имя", user.getName()));
        descriptions.add(new ProfileDescription("Никнейм", user.getNickname()));
        descriptions.add(new ProfileDescription("Email", user.getEmail()));
        descriptions.add(new ProfileDescription("Количество чатов", Integer.toString(user.getChat_count())));
        return descriptions;
    }
}