package com.iso.facebook.entities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

class Friend
{
    String username;
    String displayName;
    String profilePic;

    public Friend(String username, String displayName, String profilePic)
    {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public static Friend fromJson(String json)
    {
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        String username = jsonObject.get("username").getAsString();
        String displayName = jsonObject.get("displayName").getAsString();
        String profilePic = jsonObject.get("profilePic").getAsString();

        return new Friend(username, displayName, profilePic);
    }


    String toJson()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
