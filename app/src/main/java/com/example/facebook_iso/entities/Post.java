package com.example.facebook_iso.entities;

import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.facebook_iso.R;
import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.commentPost.CommentPost;
import com.example.facebook_iso.editHandler.ThreeDots;

import android.view.LayoutInflater;

@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)

    private String id;
    private String author;
    private String date;
    private String title;
    private String description;
    private int likes;
    private Uri img;
    private Uri author_photo;
    private boolean isLiked;
    private boolean openComment;
    private boolean threeDots;
    private RecyclerView lstPosts;

    private View postLayout;

    private CommentPost commentPost;

    private  PostsListAdapter adapter;
    public Post(String id, String title, String author, Uri author_photo, String description, String date, Uri img, RecyclerView lstPosts,  PostsListAdapter adapter) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.title = title;
        this.description = description;
        this.likes = 0;
        this.img = img;
        this.author_photo = author_photo;
        this.isLiked = false;
        this.openComment = false;
        this.threeDots = false;
        this.adapter = adapter;
        this.lstPosts = lstPosts;
        this.commentPost = new CommentPost(this, adapter);
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }
    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public int getLikes() {
        return likes;
    }

    public String getDescription() {
        return description;
    }

    public Uri getImg() {
        return img;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Uri getAuthorPhoto() {
        return author_photo;
    }

    public PostsListAdapter getAdapter() {
        return adapter;
    }

    public void toggleLike() {
        isLiked = !isLiked;
        if (isLiked) {
            setLikes(getLikes() + 1);
        } else {
            setLikes(getLikes() - 1);
        }
        if (adapter != null) {
            int position = adapter.getPosts().indexOf(this);
            if (position != RecyclerView.NO_POSITION) {
                adapter.notifyItemChanged(position);
            }
        }
    }

    public void toggleShare(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        View menuView = LayoutInflater.from(view.getContext()).inflate(R.layout.sharebutton, null);
        PopupWindow popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y);
    }

    public void toggleComment(View v) {
        openComment = !openComment;
        LinearLayout commentsSection = postLayout.findViewById(R.id.commentsSection);
        if (openComment) {
            commentsSection.setVisibility(View.VISIBLE);
        } else {
            commentsSection.setVisibility(View.GONE);
        }
    }


    public void toggleThreeDots(View view) {
        View menuView = LayoutInflater.from(view.getContext()).inflate(R.layout.threedots, null);
        PopupWindow popupWindow = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAsDropDown(view);
        ThreeDots threeDots = new ThreeDots(menuView, adapter, lstPosts, this, popupWindow);
    }

    public void setPhotoUri(Uri img) {
        this.img = img;
    }

    public void setPostLayout(View layout) {
        this.postLayout = layout;
    }

    public View getPostLayout() {
        return postLayout;
    }

    public CommentPost getCommentPost() {
        return commentPost;
    }
}