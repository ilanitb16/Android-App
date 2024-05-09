package com.iso.facebook.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;

import com.google.gson.Gson;
import com.iso.facebook.convertors.Convertors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class User {
    public Context context;
    private String _id;
    private String username;
    private String password;
    private String displayName;
    private String profilePic;
    private List<Friend> friends;
    private List<FriendRequest> friendsRequest;

    public User(Context context, String _id, String username, String password, String displayName, String profilePic, List<Friend> friends, List<FriendRequest> friendsRequest)
    {
        this.context = context;
        this._id = _id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.friends = friends;
        this.friendsRequest = friendsRequest;
    }

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

    public Uri getProfilePic() {
        return Convertors.base64ToUri(profilePic, context);
    }

    public void setProfilePic(Uri profilePic) {
        this.profilePic = Convertors.uriToBase64(profilePic, context);
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<FriendRequest> getFriendsRequest() {
        return friendsRequest;
    }

    public void setFriendsRequest(List<FriendRequest> friendsRequest) {
        this.friendsRequest = friendsRequest;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static User fromJson(String json) {
        try {
            // Create a JSONObject from the JSON string
            JSONObject jsonObject = new JSONObject(json);

            // Extract values from the JSONObject and create a User object
            String id = jsonObject.getString("_id");
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");
            String displayName = jsonObject.getString("displayName");
            String profilePic = jsonObject.getString("profilePic");

            // Extract friends array and convert it into a list of Friend objects
            JSONArray friendsArray = jsonObject.getJSONArray("friends");
            List<Friend> friends = new ArrayList<>();
            for (int i = 0; i < friendsArray.length(); i++) {
                JSONObject friendObject = friendsArray.getJSONObject(i);
                String friendUsername = friendObject.getString("username");
                String friendDisplayName = friendObject.getString("displayName");
                String friendProfilePic = friendObject.getString("profilePic");
                // Create a new Friend object and add it to the list
                friends.add(new Friend(null, friendUsername, friendDisplayName, friendProfilePic));
            }

            // Extract friend requests array and convert it into a list of FriendRequest objects
            JSONArray friendRequestsArray = jsonObject.getJSONArray("friendsRequest");
            List<FriendRequest> friendRequests = new ArrayList<>();
            for (int i = 0; i < friendRequestsArray.length(); i++) {
                JSONObject friendRequestObject = friendRequestsArray.getJSONObject(i);
                String friendRequestUsername = friendRequestObject.getString("username");
                String friendRequestDisplayName = friendRequestObject.getString("displayName");
                String friendRequestProfilePic = friendRequestObject.getString("profilePic");
                // Create a new FriendRequest object and add it to the list
                friendRequests.add(new FriendRequest(null, friendRequestUsername, friendRequestDisplayName, friendRequestProfilePic));
            }

            // Create and return a new User object
            return new User(null, id, username, password, displayName, profilePic, friends, friendRequests);

        } catch (JSONException e) {
            e.printStackTrace();
            // Return null if there's an error during parsing
            return null;
        }
    }


    public static class Friend {
        public Context context;
        private String username;
        private String displayName;
        private String profilePic;

        public Friend(Context context, String username, String displayName, String profilePic)
        {
            this.context = context;
            this.username = username;
            this.displayName = displayName;
            this.profilePic = profilePic;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public void setProfilePic(Uri profilePic) {
            this.profilePic = Convertors.uriToBase64(profilePic, context);
        }
    }

    public static class FriendRequest {
        public Context context;
        private String username;
        private String displayName;
        private String profilePic;

        public FriendRequest(Context context, String username, String displayName, String profilePic)
        {
            this.context = context;
            this.username = username;
            this.displayName = displayName;
            this.profilePic = profilePic;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public void setProfilePic(Uri profilePic) {
            this.profilePic = Convertors.uriToBase64(profilePic, context);
        }
    }
}
