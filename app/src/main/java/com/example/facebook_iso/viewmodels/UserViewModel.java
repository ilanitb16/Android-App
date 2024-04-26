package com.example.facebook_iso.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.repositories.UserRepository;

import java.util.concurrent.CompletableFuture;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;

    private MutableLiveData<User> currentUser;

    public UserViewModel() {
        userRepository = new UserRepository();
        currentUser = new MutableLiveData<>();
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void signUp(User user, Context context) {
        userRepository.signUp(user, context);
    }

    public void signIn(String username, String password) {
        userRepository.signIn(username, password);
    }
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void getUser(String username, Post post, CompletableFuture<Void> completion, Boolean isLast) {
        userRepository.getUser(username, post, completion, isLast);
    }

    public void getUserRequests(String username, SwipeRefreshLayout swipeRefreshLayout) {
        userRepository.getUserRequests(username, swipeRefreshLayout);
    }

    public void deleteUser(User user) {
        userRepository.deleteUser(user);
    }

    public void getUserFriends(User user) {
        userRepository.getUserFriends(user);
    }

    public void deleteFriendsRequest(User friend) {
        userRepository.deleteFriendsRequest(friend);
    }

    public void approveFriendsRequest(User friend) {
        userRepository.approveFriendsRequest(friend);
    }
}
