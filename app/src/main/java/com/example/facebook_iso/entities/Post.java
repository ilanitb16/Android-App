package com.example.facebook_iso.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.facebook_iso.editHandler.ThreeDots;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    @SerializedName("_id")
    private String postId;

    @SerializedName("username")
    private String username;

    @SerializedName("create_date")
    private String createDate;

    @SerializedName("description")
    private String description;

    @SerializedName("img")
    private String img;

    @SerializedName("title")
    private String title;

    @SerializedName("profilePic")
    private String profilePic;
    @Ignore
    private int likes;
    @Ignore
    private boolean isLiked;
    @Ignore
    private boolean openThreeDots;
    @Ignore
    private ThreeDots threeDots;
    @Ignore
    private Boolean isFriend;
    @Ignore
    private List<Comment> comments;
    @Ignore
    private User user;

    public Post() {
        this.isLiked = false;
        this.openThreeDots = false;
        this.comments = new ArrayList<>();
    }

    public Post(String postId, String title, String description, String date, String img, User user) {
        this.postId = postId;
        this.createDate = date;
        this.title = title;
        this.description = description;
        this.likes = 0;
        this.img = img;
        this.isLiked = false;
        this.openThreeDots = false;
        this.comments = new ArrayList<>();
        this.user = user;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostId()
    {
        return postId;
    }

    public void setPostId(String postId)
    {
        this.postId = postId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setOpenThreeDots(boolean openThreeDots) {
        this.openThreeDots = openThreeDots;
    }

    public boolean isOpenThreeDots() {
        return openThreeDots;
    }

    public ThreeDots getThreeDots() {
        return threeDots;
    }

    public void setThreeDots(ThreeDots threeDots) {
        this.threeDots = threeDots;
    }

    public Boolean getFriend() {
        return isFriend;
    }

    public void setFriend(Boolean friend) {
        isFriend = friend;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}