package com.example.facebook_iso.entities;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.editHandler.ThreeDots;

import android.view.LayoutInflater;

@Entity
public class Comment {
    @PrimaryKey(autoGenerate = true)

    private int id;
    private User user;
    private String context;

    public Comment() {

    }

    public Comment(User user, String context) {
        this.user = user;
        this.context = context;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthor() {
        return user.getUsername();
    }

    public String getAuthor_photo() {
        return user.getProfilePic();
    }
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
