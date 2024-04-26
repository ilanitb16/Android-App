package com.example.facebook_iso.repositories;

import com.example.facebook_iso.UserDao;
import com.example.facebook_iso.api.UserAPI;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;

public class UserRepository {
    private final UserAPI userAPI;

    public UserRepository() {
        userAPI = new UserAPI();
    }

    public void signUp(User user) {
        userAPI.signUp(user);
    }

    public void signIn(String username, String password) {
        userAPI.signIn(username, password);
    }
    public void updateUser(User user) {
        userAPI.updateUser(user);
    }

    public void getUser(String username, Post post) {
        userAPI.getUser(username, post);
    }

    public void deleteUser(User user) {
        userAPI.deleteUser(user);
    }

    public void getUserFriends(User user) {
        userAPI.getUserFriends(user);
    }

    public void deleteFriendsRequest(User friend) {
        userAPI.deleteFriendsRequest(friend);
    }

    public void approveFriendsRequest(User friend) {
        userAPI.approveFriendsRequest(friend);
    }
}