package com.example.facebook_iso.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.repositories.PostsRepository;

import java.util.ArrayList;
import java.util.List;

public class PostsViewModel extends ViewModel {

    private final PostsRepository mRepository;

    private final LiveData<List<Post>> posts;

    public PostsViewModel() {
        mRepository =new PostsRepository();
        posts =mRepository.getAll();

    }

    public LiveData<List<Post>> get() {
        return posts;
    }
    public void getUserPosts(User user) {
        mRepository.getUserPosts(user);
    }
    public void add(Post post) {
        mRepository.add(post);
    }
    public void edit(Post postToEdit) {
        mRepository.edit(postToEdit);
    }
    public void delete(Post post) {
        mRepository.delete(post);
    }
    public void reload() {
        mRepository.reload();
    }
    public void friendsRequest(User friend){mRepository.friendRequest(friend);}
}