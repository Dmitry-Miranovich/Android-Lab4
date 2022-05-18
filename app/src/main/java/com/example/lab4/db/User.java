package com.example.lab4.db;

import java.util.Objects;

public class User {
    private String name;
    private String nickname;
    private String email;
    private int chat_count;
    private String hex;

    public User(String name, String nickname, String email, int chat_count, String hex){
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.chat_count = chat_count;
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getChat_count() {
        return chat_count;
    }

    public void setChat_count(int friend_count) {
        this.chat_count = friend_count;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", chat_count=" + chat_count +
                ", hex='" + hex + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return chat_count == user.chat_count && Objects.equals(name, user.name) && Objects.equals(nickname, user.nickname) && Objects.equals(email, user.email) && Objects.equals(hex, user.hex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nickname, email, chat_count, hex);
    }
}
