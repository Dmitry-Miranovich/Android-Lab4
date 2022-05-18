package com.example.lab4.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.lab4.R;
import com.example.lab4.activity.ProfileActivity;
import com.example.lab4.db.User;

import java.util.ArrayList;
import java.util.LinkedList;

public class UserAdapter extends ArrayAdapter<UserDescription> {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<UserDescription> list;
    private int layout;

    public UserAdapter(Context context, int resource, ArrayList<UserDescription> list){
        super(context, resource, list);
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.layout = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final UserHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            holder = new UserHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (UserHolder) convertView.getTag();
        }
        final UserDescription description = list.get(position);
        holder.description.setText(description.getUser().getNickname());
        holder.button.setChecked(description.isChecked());
        holder.button.setOnClickListener((view)->{
            if(!description.isChecked()){
                description.setChecked(true);
            }else{
                description.setChecked(false);
            }
            holder.button.setChecked(description.isChecked());
        });
        holder.description.setOnClickListener((view)->{
            ProfileActivity.isAuthorized = false;
            ProfileActivity.guest = description.getUser();
            Intent intent = new Intent(context, ProfileActivity.class);
            context.startActivity(intent);
        });
        return convertView;
    }

    private static class UserHolder{
        TextView description;
        RadioButton button;
        public UserHolder(View view){
            this.description = view.findViewById(R.id.user_list_name);
            this.button = view.findViewById(R.id.button_to_choose);
        }
    }

}
