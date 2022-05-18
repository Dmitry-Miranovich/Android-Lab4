package com.example.lab4.db;

import java.util.Date;

public class Message {
    private String text;
    private String date;
    private String user_name;

    public Message(String text, String date, String user_name){
        this.text = text;
        this.date = date;
        this.user_name = user_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", date='" + date + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
