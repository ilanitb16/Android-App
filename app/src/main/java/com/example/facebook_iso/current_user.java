package com.example.facebook_iso;

public class current_user {
    private static current_user instance;
    private loginInfo current;

    private current_user() {
    }

    public static synchronized current_user getInstance() {
        if (instance == null) {
            instance = new current_user();
        }
        return instance;
    }

    // Getters and setters for fields...

    // Example method to set user details
    public void set_current(loginInfo current) {
        this.current=current;
    }
    public loginInfo get_Current(){
        return this.current;
    }
}