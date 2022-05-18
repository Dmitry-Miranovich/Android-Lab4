package com.example.lab4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lab4.R;
import com.example.lab4.db.MessageList;

import java.util.ArrayList;
import java.util.LinkedList;

public class ChatListAdapter extends ArrayAdapter<MessageList> {
    private final LayoutInflater inflater;
    private Context context;
    private final ArrayList<MessageList> list;
    private final int layout;

    public ChatListAdapter(Context context, int position , ArrayList<MessageList> list){
        super(context, position, list);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.layout = position;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final ChatListHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            holder = new ChatListHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ChatListHolder) convertView.getTag();
        }
        final MessageList pd = list.get(position);
        holder.name_label.setText(pd.getName());
        holder.description_label.setText(pd.getDescription());
        return convertView;
    }
    private static class ChatListHolder{
        final TextView name_label;
        final TextView description_label;
        ChatListHolder(View view){
            this.name_label = view.findViewById(R.id.chat_name);
            this.description_label = view.findViewById(R.id.chat_date);
        }
    }
}
