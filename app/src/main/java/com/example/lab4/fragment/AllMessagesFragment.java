package com.example.lab4.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lab4.R;
import com.example.lab4.activity.ChatActivity;
import com.example.lab4.activity.MainActivity;
import com.example.lab4.adapters.ChatListAdapter;
import com.example.lab4.db.ChatDBHelper;
import com.example.lab4.db.MessageList;
import com.example.lab4.db.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllMessagesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ChatDBHelper helper;
    SQLiteDatabase db;

    public static ArrayList<User> chatList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllMessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllMessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllMessagesFragment newInstance(String param1, String param2) {
        AllMessagesFragment fragment = new AllMessagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        helper = new ChatDBHelper(getContext());
        db = helper.getWritableDatabase();
        return inflater.inflate(R.layout.fragment_all_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView list_view = view.findViewById(R.id.all_chat_view);
        ArrayList<MessageList> list = helper.getMessageLists(db,MainActivity.user);
        //todo запрос на получение чатов
        ChatListAdapter adapter = new ChatListAdapter(getContext(), R.layout.chat_list_item, list);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener((adapterView, view2, i, l) -> {
            ChatActivity.current_user = MainActivity.user;
            TextView name = view2.findViewById(R.id.chat_name);
            System.out.println(name.getText());
            ChatActivity.foreign_user = helper.getForeignUser(db, name.getText().toString());
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        });
    }
}