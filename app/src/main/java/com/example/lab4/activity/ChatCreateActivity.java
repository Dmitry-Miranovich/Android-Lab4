package com.example.lab4.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.lab4.R;
import com.example.lab4.adapters.UserAdapter;
import com.example.lab4.adapters.UserDescription;
import com.example.lab4.db.ChatDBHelper;
import com.example.lab4.db.MessageList;
import com.example.lab4.db.User;
import com.example.lab4.fragment.AllMessagesFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class ChatCreateActivity extends AppCompatActivity {

    public static User user;

    private ListView view;
    public static LinkedList<User> users;
    private UserAdapter adapter;
    private ChatDBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_create);
    }

    private boolean isChatAlreadyCreated(User curr_user){
        ArrayList<MessageList> messageLists = helper.getMessageLists(db,curr_user);
        return messageLists.size()==0;
    }

    @Override
    public void onResume(){
        super.onResume();
        helper = new ChatDBHelper(this);
        db = helper.getWritableDatabase();
        ArrayList<UserDescription> list = new ArrayList<>();
        ArrayList<User> users = helper.getAllUsers(db);
        for(User curr_user: users){
            if(!curr_user.equals(user) && isChatAlreadyCreated(curr_user)){
                list.add(new UserDescription(curr_user, true));
            }
        }
        view = findViewById(R.id.users_list);
        adapter = new UserAdapter(this, R.layout.user_list_layout, list);
        view.setAdapter(adapter);
        Button button = findViewById(R.id.add_chat);
        //todo Запрос с бд по добавлению чатов
        button.setOnClickListener((view)->{
            for(UserDescription d : list){
                if(d.isChecked()){
                    helper.addMessageList(db,user ,d.getUser());
                }
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
        Button button1 = findViewById(R.id.cancel_chat);
        button1.setOnClickListener((view)->{
            //user.setChat_count(user.getChat_count()+1);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}