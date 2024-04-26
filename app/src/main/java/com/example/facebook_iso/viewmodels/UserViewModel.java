package com.example.facebook_iso.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.repositories.UserRepository;

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

    public void signUp(User user) {
        userRepository.signUp(user);
    }

    public void signIn(String username, String password) {
        userRepository.signIn(username, password);
    }
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void getUser(String username, Post post) {
        userRepository.getUser(username, post);
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
