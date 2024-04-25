package com.example.facebook_iso.entities;

// User.java

import com.google.gson.Gson;

public class User {
    private UserClass user;
    private String token;

    public User(UserClass user, String token)
    {
        this.user = user;
        this.token = token;
    }

    public UserClass getUser() { return user; }
    public void setUser(UserClass value) { this.user = value; }

    public String getToken() { return token; }
    public void setToken(String value) { this.token = value; }

    // Method to serialize User object to JSON string
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Method to deserialize JSON string to User object
    public static User fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }
}

