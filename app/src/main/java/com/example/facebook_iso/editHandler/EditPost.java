package com.example.facebook_iso.editHandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.login.Login_Page;
import com.example.facebook_iso.picture.picture;

import java.util.List;

public class EditPost extends Activity {

    private Post post;
    private Uri imagePost;

    private DataSaver helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpost);
        DataSaver.getInstance().setEdit_T();
        this.imagePost = null;
        this.helper= DataSaver.getInstance();
        this.post=helper.getPost();

        editOperation();
    }

    private void editOperation() {

        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDescription = findViewById(R.id.etDescription);

        etTitle.setHint(post.getTitle());
        etDescription.setHint(post.getDescription());

        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnCancel = findViewById(R.id.btnCancel);


        btnChooseImage.setOnClickListener(v -> {

            Intent i =new Intent(this, picture.class);
            startActivity(i);
        });


        btnUpdate.setOnClickListener(v -> {
            imagePost = helper.getImagePost();
            updatePost(etTitle.getText().toString(), etDescription.getText().toString());
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });
    }

    private void updatePost(String newTitle, String newDescription) {
        List<Post> posts = FeedPage.adapter.getPosts();
        int indexOfPost = posts.indexOf(post);
        Post current = posts.get(indexOfPost);
        if (!newTitle.isEmpty()){
            current.setTitle(newTitle);
        }
        if (!newDescription.isEmpty()){
            current.setDescription(newDescription);
        }
        if (imagePost != null){
            current.setImg(Converters.uriToString(imagePost));
        }
        FeedPage.postsViewModel.edit(current);
        FeedPage.adapter.setPosts(posts);
    }
}
