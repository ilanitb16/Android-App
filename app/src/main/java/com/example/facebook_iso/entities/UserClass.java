package com.example.facebook_iso.entities;

import com.google.gson.Gson;

public class UserClass
{
    private String id;
    private String username;
    private String password;
    private String displayName;
    private String profilePic;

    public UserClass(String id, String username, String password, String displayName, String profilePic)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public String getID()
    {
        return id;
    }

    public void setID(String value)
    {
        this.id = value;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String value)
    {
        this.username = value;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String value)
    {
        this.password = value;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String value)
    {
        this.displayName = value;
    }

    public String getProfilePic()
    {
        return profilePic;
    }

    public void setProfilePic(String value)
    {
        this.profilePic = value;
    }

    // Method to serialize UserClass object to JSON string
    public String toJson()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Method to deserialize JSON string to UserClass object
    public static UserClass fromJson(String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json, UserClass.class);
    }
}
