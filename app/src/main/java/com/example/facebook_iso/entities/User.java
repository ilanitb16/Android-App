package com.example.facebook_iso.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.util.ArrayList;
import java.util.List;
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private String displayName;
    private String profilePic;
    @Ignore
    private List<User> friends;
    @Ignore
    private List<User> friends_request;
    @Ignore
    private String token;
    @Ignore
    private List<Post> userPosts;
    public User() {
        this.friends = new ArrayList<>();
        this.friends_request = new ArrayList<>();
        this.userPosts = new ArrayList<>();
    }

    public User(String userName, String profileImage, String fullName) {
        this.username = userName;
        this.profilePic = profileImage;
        this.displayName = fullName;
        this.friends = new ArrayList<>();
        this.friends_request = new ArrayList<>();
        this.userPosts = new ArrayList<>();

    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public List<User> getFriends() {
        if (friends==null){
            friends = new ArrayList<>();
        }
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User user) {
        friends.add(user);
    }

    public List<Post> getUserPosts() {
        return userPosts;
    }

    public void sendFriendRequest(User user) {
            friends_request.add(user);

    }

    public void acceptFriendRequest(User user) {
        if (friends_request.contains(user) && !friends.contains(user)) {
            friends_request.remove(user);
            friends.add(user);
        }
    }

    public void rejectFriendRequest(User user) {
        if (friends_request.contains(user)) {
            friends_request.remove(user);
        }
    }

    public void addPost(Post post) {
        post.setUser(this);
    }
    public void setUserPosts(List<Post> userPosts) {
        this.userPosts = userPosts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
      
    public boolean findFriend(User user) {
        for (User friend : friends) {
            if (friend.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }
    public User findRequest(String username) {
        if (friends_request != null) {
            for (User request : friends_request) {
                if (request.getUsername().equals(username)) {
                    return request;
                }
            }
        }
        return null;
    }

    public List<User> getFriends_request() {
        return friends_request;
    }

    public void setFriends_request(List<User> friends_request) {
        this.friends_request = friends_request;
    }
}
