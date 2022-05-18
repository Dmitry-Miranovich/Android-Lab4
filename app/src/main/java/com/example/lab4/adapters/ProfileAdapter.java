package com.example.lab4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lab4.R;

import java.util.LinkedList;

public class ProfileAdapter extends ArrayAdapter<ProfileDescription> {

    private final LayoutInflater inflater;
    private Context context;
    private final LinkedList<ProfileDescription> list;
    private final int layout;

    public ProfileAdapter(Context context, int position , LinkedList<ProfileDescription> list){
        super(context, position, list);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.layout = position;
        this.list = list;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        final ProfileHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            holder = new ProfileHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ProfileHolder) convertView.getTag();
        }
        final ProfileDescription pd = list.get(position);
        holder.name_label.setText(pd.getName());
        holder.description_label.setText(pd.getDescription());
        return convertView;
    }


    private static class ProfileHolder{
        final TextView name_label;
        final TextView description_label;
        ProfileHolder(View view){
            this.name_label = view.findViewById(R.id.profile_name_label);
            this.description_label = view.findViewById(R.id.profile_description_label);
        }
    }
}
