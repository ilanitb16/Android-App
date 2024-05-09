package com.iso.facebook.entities;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.iso.facebook.convertors.Convertors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Post {
    private String id;
    private String username;
    private String description;
    private String img;
    private String title;
    private String profilePic;
    private String create_date;

    public Post(Context context, String id, String username, String description, String img, String title, String profilePic, String offsetDateTime, String time, String date)
    {
        this.id = id;
        this.username = username;
        this.description = description;
        this.img = img;
        this.title = title;
        this.profilePic = profilePic;
        this.create_date = offsetDateTime;
    }

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getUsername() { return username; }
    public void setUsername(String value) { this.username = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public Uri getImg(Context context) {
        return Convertors.base64ToUri(img, context);
    }


    public void setImg(Uri profilePic, Context context) {
        this.img = Convertors.uriToBase64(profilePic, context);
    }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public Uri getProfilePic(Context context) {
        return Convertors.base64ToUri(profilePic, context);
    }

    public void setProfilePic(Uri profilePic, Context context) {
        this.profilePic = Convertors.uriToBase64(profilePic, context);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }


    public String getTime()
    {
        return Convertors.convertTimestamp(String.valueOf(create_date))[1];
    }

    public String getDate()
    {
        return Convertors.convertTimestamp(String.valueOf(create_date))[0];
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Post fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Post.class);
    }
}
