package com.example.lab4.adapters;

import com.example.lab4.db.User;

public class UserDescription {

    private User user;
    private boolean isChecked;

    public UserDescription(User user, boolean isChecked){
        this.user = user;
        this.isChecked = isChecked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "UserDescription{" +
                "user=" + user +
                ", isChecked=" + isChecked +
                '}';
    }
}
