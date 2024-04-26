package com.example.facebook_iso.editHandler;

import android.net.Uri;

import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.entities.Post;

public class DataSaver {
    private static DataSaver instance;

    private Post post;
    private Uri imagePost;

    private String imageString;
    private FeedPage feedPage;

    private Boolean edit;


    private DataSaver() {

    }

    public String getImageString()
    {
        return imageString;
    }

    public void setImageString(String imageString)
    {
        this.imageString = imageString;
    }

    public static synchronized DataSaver getInstance() {
        if (instance == null) {
            instance = new DataSaver();
        }
        return instance;
    }

    public void set_first(Post post) {
        this.post=post;
        this.imagePost=null;
        this.edit = false;
        this.feedPage = null;
    }

    public FeedPage getFeedPage() {
        return feedPage;
    }

    public void setFeedPage(FeedPage feedPage) {
        this.feedPage = feedPage;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit_T() {
        this.edit = true;
    }
    public void setEdit_F() {
        this.edit = false;
    }

    public void set_pic(Uri imagePost){
        this.imagePost = imagePost;
    }
    public void set_post(Post post) {
        this.post=post;
    }

    public Post getPost() {
        return post;
    }

    public Uri getImagePost() {
        return imagePost;
    }
}
