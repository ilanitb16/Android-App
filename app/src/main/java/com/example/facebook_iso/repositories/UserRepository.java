package com.example.facebook_iso.repositories;

import android.content.Context;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.facebook_iso.UserDao;
import com.example.facebook_iso.api.UserAPI;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;

import java.util.concurrent.CompletableFuture;

public class UserRepository {
    private final UserAPI userAPI;

    public UserRepository() {
        userAPI = new UserAPI();
    }

    public void signUp(User user, Context context) {
        userAPI.signUp(user, context);
    }

    public void signIn(String username, String password) {
        userAPI.signIn(username, password);
    }
    public void updateUser(User user) {
        userAPI.updateUser(user);
    }

    public void getUser(String username, Post post, CompletableFuture<Void> completion, Boolean isLast) {
        userAPI.getUser(username, post, completion, isLast);
    }

    public void getUserRequests(String username, SwipeRefreshLayout swipeRefreshLayout) {
        userAPI.getUserRequests(username, swipeRefreshLayout);
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