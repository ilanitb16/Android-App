package com.example.facebook_iso;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPost {

    private final PostsListAdapter adapter;
    private final RecyclerView lstPosts;
    private final String user_name;
    private final Uri user_photo;
    private Uri imagePost;

    private static final int REQUEST_SELECT_PHOTO = 1;
    private Boolean is_camera;
    private final int CAMERA_REQ_CODE = 100;


    public NewPost(PostsListAdapter adapter, RecyclerView lstPosts, View view, String user_name, Uri user_photo) {
        this.adapter = adapter;
        this.lstPosts = lstPosts;
        this.user_name = user_name;
        this.user_photo = user_photo;
        this.is_camera = false;
        this.imagePost = null;
        createNewPost(view);
    }

    @SuppressLint("InflateParams")
    public void createNewPost(View view) {
        view = LayoutInflater.from(view.getContext()).inflate(R.layout.newpost, null);
        PopupWindow popupWindowNewPost = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0] + view.getWidth() / 2 - popupWindowNewPost.getWidth() / 2;
        int y = view.getHeight() * 5;
        popupWindowNewPost.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, x, y);

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etDescription = view.findViewById(R.id.etDescription);



        Button btnChooseImage = view.findViewById(R.id.btnChooseImage);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), picturePost.class);
                v.getContext().startActivity(intent);

            }
        });



        Button btnPost = view.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePost = currentPost.getInstance().getImagePost();
                String setTitle = etTitle.getText().toString();
                String setDescription = etDescription.getText().toString();
                if (imagePost != null){
                    createPost(v, setTitle, setDescription);
                }
                popupWindowNewPost.dismiss();
            }
        });

        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the popup window without making any changes
                popupWindowNewPost.dismiss();
            }
        });


    }


    private void createPost(View v, String setTitle, String setDescription) {
        ProgressDialogManager.showProgressDialog(v.getContext(), "Adding Post", "Please wait...");
        String username = CurrentUserManager.getInstance(v.getContext()).getCurrentUser().getUser().getUsername();
        String userToken = CurrentUserManager.getInstance(v.getContext()).getCurrentUser().getToken();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("description", setDescription);
            requestBody.put("img", "profile2");
            requestBody.put("title", setTitle);
            requestBody.put("profilePic", "laylaflower");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new api_service(v.getContext()).post(constants.createPost + "/" + username + "/posts", requestBody, userToken, new api_service.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                String id = response.optString("insertedId", "");
                Post newPost = new Post(id, setTitle, user_name, user_photo, setDescription, getDate(), imagePost, lstPosts, adapter);
                newPost.setId(id);
                adapter.addPost(newPost);
                UIToast.showToast(v.getContext(), "Post added successfully");
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



    private String getDate() {
        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(currentDate);

    }

}