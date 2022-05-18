package com.example.lab4.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab4.R;
import com.example.lab4.adapters.MessageAdapter;
import com.example.lab4.client.Client;
import com.example.lab4.db.ChatDBHelper;
import com.example.lab4.db.Message;
import com.example.lab4.db.User;
import com.example.lab4.server.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    public static User current_user;
    public static User foreign_user;
    public ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
    ChatDBHelper helper;
    SQLiteDatabase database;
    Client client = new Client(current_user);
    MessageAdapter adapter;
    RecyclerView chat;
    @Override
    public void onResume(){
        super.onResume();
        helper = new ChatDBHelper(this);
        database = helper.getWritableDatabase();
        chat = findViewById(R.id.chat_message_list_view);
        ArrayList<Message> m1 = helper.getAllMessages(database, current_user);
        ArrayList<Message> m2 = helper.getAllMessages(database, foreign_user);
        if(m1.size()> m2.size()){
            messages.addAll(m1);
        }else{
            messages.addAll(m2);
        }
        for(Message m : messages){
            System.out.println(m);
        }
        chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter.getItemCount() > 0){
                            recyclerView.smoothScrollToPosition(adapter.getItemCount()- 1);
                        }
                    }
                });
            }
        });
        adapter = new MessageAdapter(this, messages);
        chat.setAdapter(adapter);
        Thread thread = new Thread(()->{
            client.connect("10.0.2.2", 8000);
        });
        thread.start();
    }

    public void sendMessage(View view){
        EditText message_edit = findViewById(R.id.message_edit);
        String message = message_edit.getText().toString();
        if(message.equals("")){
            Toast.makeText(this, "Empty message", Toast.LENGTH_SHORT).show();
        }else{
            Thread thread = new Thread(()->{
                client.send(message);
            });
            thread.start();
            Date date = new Date();
            String builder = date.getHours() +
                    ":" +
                    date.getMinutes();
            Message mess = new Message(message, builder, current_user.getName());
            messages.add(mess);
            helper.addMessage(database,mess, current_user, foreign_user);
            adapter = new MessageAdapter(this, messages);
            chat.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.disconnect();
    }
}