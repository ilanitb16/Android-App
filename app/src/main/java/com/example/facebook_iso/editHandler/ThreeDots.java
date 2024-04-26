package com.example.facebook_iso.editHandler;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.UserInfoActivity;
import com.example.facebook_iso.entities.Post;

import java.util.List;

public class ThreeDots {

    private final View menuView;
    private final Post post;
    private DataSaver helper;

    private final PopupWindow popupWindow;

    public ThreeDots(View menuView, Post post, PopupWindow popupWindow) {
        this.menuView = menuView;
        this.post = post;
        this.popupWindow =popupWindow;
        this.helper = DataSaver.getInstance();
        setupMenu();
    }

    private void setupMenu() {
        int indexOfPost = FeedPage.feedAdapter.getPosts().indexOf(post);
        if (indexOfPost != -1) {
            FeedPage.feedRecyclerView.scrollToPosition(indexOfPost);
        }
        ImageButton editButton = menuView.findViewById(R.id.EditButton);
        TextView editText = menuView.findViewById(R.id.EditText);
        ImageButton deleteButton = menuView.findViewById(R.id.DeleteButton);
        TextView deleteText = menuView.findViewById(R.id.DeleteText);

        editButton.setOnClickListener(v -> editOperation(v));
        editText.setOnClickListener(v -> editOperation(v));
        deleteButton.setOnClickListener(v -> deleteOperation());
        deleteText.setOnClickListener(v -> deleteOperation());
    }

    private void editOperation(View view) {
        DataSaver.getInstance().set_first(this.post);
        Intent i =new Intent(this.menuView.getContext(), EditPost.class);
        menuView.getContext().startActivity(i);
        popupWindow.dismiss();

    }


    private void deleteOperation() {
        popupWindow.dismiss();
        List<Post> posts = UserInfoActivity.adapter.getPosts();
        Log.d("API call", String.valueOf(posts.size()));
        int indexOfPost = posts.indexOf(post);
        Post post = posts.get(indexOfPost);
        posts.remove(indexOfPost);
        FeedPage.postsViewModel.delete(post);
        UserInfoActivity.adapter.setPosts(posts);
        UserInfoActivity.adapter.notifyItemRemoved(indexOfPost);
    }
}