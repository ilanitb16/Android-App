package com.example.facebook_iso.editHandler;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.facebook_iso.Feed_Page;
import com.example.facebook_iso.R;
import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.EditPost;
import com.example.facebook_iso.api_manager.api_service;
import com.example.facebook_iso.api_manager.constants;
import com.example.facebook_iso.common.CurrentUserManager;
import com.example.facebook_iso.common.ProgressDialogManager;
import com.example.facebook_iso.common.UIToast;
import com.example.facebook_iso.entities.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ThreeDots {

    private static final int REQUEST_SELECT_PHOTO = 1;
    private final View menuView;
    private final PostsListAdapter adapter;
    private final RecyclerView lstPosts;
    private Post post;

    private final PopupWindow popupWindow;

    public ThreeDots(View menuView, PostsListAdapter adapter, RecyclerView lstPosts, Post post, PopupWindow popupWindow) {
        this.menuView = menuView;
        this.adapter = adapter;
        this.lstPosts = lstPosts;
        this.post = post;
        this.popupWindow =popupWindow;
        setupMenu();
    }

    private void setupMenu() {
        int indexOfPost = adapter.getPosts().indexOf(post);
        if (indexOfPost != -1) {
            lstPosts.scrollToPosition(indexOfPost);
        }
        ImageButton editButton = menuView.findViewById(R.id.EditButton);
        TextView editText = menuView.findViewById(R.id.EditText);
        ImageButton deleteButton = menuView.findViewById(R.id.DeleteButton);
        TextView deleteText = menuView.findViewById(R.id.DeleteText);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOperation(v);
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editOperation(v);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOperation(v);
            }
        });

        deleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOperation(v);
            }
        });
    }

    private void editOperation(View view) {
        popupWindow.dismiss();
        EditPost editPost = new EditPost(adapter, lstPosts, post, view);
    }

    private void deleteOperation(View v) {
        ProgressDialogManager.showProgressDialog(v.getContext(), "Deleting Post", "Please wait...");
        popupWindow.dismiss();
        String username = CurrentUserManager.getInstance(v.getContext()).getCurrentUser().getUser().getUsername();
        String userToken = CurrentUserManager.getInstance(v.getContext()).getCurrentUser().getToken();
        String postId = post.getId();
        new api_service(v.getContext()).delete(constants.deletePost + "/" + username + "/posts/" + postId, userToken, new api_service.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                List<Post> posts = adapter.getPosts();
                posts.remove(post);
                adapter.setPosts(posts);
                UIToast.showToast(v.getContext(), "Post deleted successfully");
                ProgressDialogManager.dismissProgressDialog();
            }

            @Override
            public void onSuccess(JSONArray response) {}

            @Override
            public void onError(String errorMessage) {
                UIToast.showToast(v.getContext(), errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
}