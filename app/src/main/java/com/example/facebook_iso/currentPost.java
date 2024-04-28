package com.example.facebook_iso;

import android.net.Uri;

import com.example.facebook_iso.entities.Post;

public class currentPost {
    private static currentPost instance;

    private Uri imagePost;

    private currentPost() {

    }

    public static synchronized currentPost getInstance() {
        if (instance == null) {
            instance = new currentPost();
        }
        return instance;
    }

    public void setImagePost(Uri imagePost) {
        this.imagePost = imagePost;
    }

    public Uri getImagePost() {
        return this.imagePost;
    }


}