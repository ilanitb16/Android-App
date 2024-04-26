package com.example.facebook_iso.login;

import com.example.facebook_iso.entities.User;

public class current_user {
    private static current_user instance;
    private loginInfo current;
    private User currentUser;

    private current_user() {
    }

    public static synchronized current_user getInstance() {
        if (instance == null) {
            instance = new current_user();
        }
        return instance;
    }

    public void set_current(loginInfo current) {
        this.current=current;
    }

    public void set_CurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}