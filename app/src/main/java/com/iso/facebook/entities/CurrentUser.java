package com.iso.facebook.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iso.facebook.convertors.Convertors;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class CurrentUser {
    public Context context;
    private String _id;
    private String username;
    private String password;
    private String displayName;
    private String profilePic; // Base64 encoded string
    private String token;

    // Constructor
    public CurrentUser(Context context, String _id, String username, String password, String displayName, String profilePic, String token) {
        this.context = context;
        this._id = _id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.token = token;
    }

    // Getter and Setter methods
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Uri getProfilePic(Context context) {
        return Convertors.base64ToUri(profilePic, context);
    }

    public String getProfilePicBase64() {
        return profilePic;
    }

    public void setProfilePic(Uri profilePic, Context context) {
        this.profilePic = Convertors.uriToBase64(profilePic, context);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // To JSON method
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // From JSON method
    // From JSON method
    public static CurrentUser fromJson(String json) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject userObject = jsonObject.getAsJsonObject("user");

        String _id = userObject.get("_id").getAsString();
        String username = userObject.get("username").getAsString();
        String password = userObject.get("password").getAsString();
        String displayName = userObject.get("displayName").getAsString();
        String profilePic = userObject.get("profilePic").getAsString();
        String token = jsonObject.get("token").getAsString();

        return new CurrentUser(null, _id, username, password, displayName, profilePic, token); // Assuming context can be null
    }

}
