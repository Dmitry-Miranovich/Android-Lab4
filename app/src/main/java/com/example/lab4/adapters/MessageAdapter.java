package com.example.lab4.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4.R;
import com.example.lab4.db.Message;
import com.example.lab4.db.MessageList;
import com.example.lab4.db.User;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Message> list;

    public MessageAdapter(Context context, ArrayList<Message> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_item_layout, parent, false);
        return new ViewHolder(view);
    }
/*
    public View getView(int position, View convertView, ViewGroup parent){
        final MessageHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            holder = new MessageHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (MessageHolder) convertView.getTag();
        }
        final Message pd = list.get(position);
        holder.message_user.setText(pd.getUser_name());
        holder.message_text.setText(pd.getText());
        holder.message_time.setText(pd.getDate());
        return convertView;
    }

 */
    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        Message pd = list.get(position);
        holder.message_user.setText(pd.getUser_name());
        holder.message_text.setText(pd.getText());
        holder.message_time.setText(pd.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView message_user;
        final TextView message_text;
        final TextView message_time;
        ViewHolder(View view){
            super(view);
            message_user = view.findViewById(R.id.message_user);
            message_text = view.findViewById(R.id.message_text);
            message_time = view.findViewById(R.id.message_time);
        }
    }
}
