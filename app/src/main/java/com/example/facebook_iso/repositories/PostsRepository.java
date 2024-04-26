package com.example.facebook_iso.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.facebook_iso.AppDB;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.MyApplication;
import com.example.facebook_iso.PostDao;
import com.example.facebook_iso.api.PostAPI;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;

import java.util.ArrayList;
import java.util.List;

public class PostsRepository {
    public static PostDao postDao;
    private final PostListData postListData;
    private final PostAPI postAPI;
    private AppDB db;

    public PostsRepository() {
        new Thread(() -> {
            db = Room.databaseBuilder(MyApplication.context, AppDB.class, "FacebookDB").
                    fallbackToDestructiveMigration().build();
            this.postDao = db.postDao();
        }).start();
        postAPI = new PostAPI();
        postListData = new PostListData();
    }


    class PostListData extends MutableLiveData<List<Post>> {
        public PostListData() {
            super();
            new Thread(() -> {
                if (postDao!=null) {
                    List<Post> posts = postDao.index();
                    postListData.postValue(posts);
                }
            }).start();

        }

        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                if (postDao != null) {
                    postListData.postValue(postDao.index());
                }
            }).start();
        }
    }

    public LiveData<List<Post>> getAll() {
        return postListData;
    }

    public void getUserPosts(User user) {
        postAPI.indexUser(user);
    }

    public void add(final Post post) {
        postAPI.add(post, postListData);
    }

    public void edit(final Post post) {
        postAPI.edit(post, postListData);
    }

    public void delete(final Post post) {
        postAPI.delete(post, postListData);
    }

    public void reload() {
        postAPI.index(FeedPage.owner, postListData);
    }

    public void friendRequest(final User friend) {
        postAPI.friendsRequest(friend);
    }
}