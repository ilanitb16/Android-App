package com.example.facebook_iso;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.PopupWindow;


import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.api_manager.api_service;
import com.example.facebook_iso.api_manager.constants;
import com.example.facebook_iso.common.CurrentUserManager;
import com.example.facebook_iso.common.ProgressDialogManager;
import com.example.facebook_iso.common.UIToast;
import com.example.facebook_iso.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EditPost {

    public static final int REQUEST_SELECT_PHOTO = 1;
    private final PostsListAdapter adapter;
    private final RecyclerView lstPosts;
    private Post post;
    private Uri imagePost;
    private View view;

    public EditPost(PostsListAdapter adapter, RecyclerView lstPosts, Post post, View view) {
        this.adapter = adapter;
        this.lstPosts = lstPosts;
        this.post = post;
        this.view = view;
        this.imagePost = null;
        editOperation(view);
    }

    public void editOperation(View view) {
        view = LayoutInflater.from(view.getContext()).inflate(R.layout.editpost, null);
        PopupWindow popupWindowEdit = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0] + view.getWidth() / 2 - popupWindowEdit.getWidth() / 2;
        int y = view.getHeight() * 5;
        popupWindowEdit.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, x, y);

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etDescription = view.findViewById(R.id.etDescription);

        etTitle.setHint(post.getTitle());
        etDescription.setHint(post.getDescription());




        Button btnChooseImage = view.findViewById(R.id.btnChooseImage);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), picturePost.class);
                v.getContext().startActivity(intent);
            }
        });




        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePost = currentPost.getInstance().getImagePost();
                updatePost(v, etTitle.getText().toString(), etDescription.getText().toString());
                popupWindowEdit.dismiss();
            }
        });

        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the popup window without making any changes
                popupWindowEdit.dismiss();
            }
        });


    }





    private void updatePost(View v, String newTitle, String newDescription) {
        ProgressDialogManager.showProgressDialog(v.getContext(), "Updating Post", "Please wait...");
        List<Post> posts = adapter.getPosts();
        Post current = posts.get(posts.indexOf(post));
        String username = CurrentUserManager.getInstance(v.getContext()).getCurrentUser().getUser().getUsername();
        String userToken = CurrentUserManager.getInstance(v.getContext()).getCurrentUser().getToken();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("description", newDescription);
            requestBody.put("img", "profile2");
            requestBody.put("title", newTitle);
            requestBody.put("profilePic", "laylaflower");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new api_service(v.getContext()).put(constants.deletePost + "/" + username + "/posts/" + current.getId(), requestBody, userToken, new api_service.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (!newTitle.equals("")){
                    current.setTitle(newTitle);
                }
                if (!newDescription.equals("")){
                    current.setDescription(newDescription);
                }
                if (imagePost != null){
                    current.setPhotoUri(imagePost);
                }

                adapter.setPosts(posts);
                UIToast.showToast(v.getContext(), "Post updated successfully");
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