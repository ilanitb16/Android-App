package com.example.facebook_iso.menuHandler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.editHandler.DataSaver;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.picture.picture;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;


public class New_post extends AppCompatActivity {
    private Uri finalImage;
    private DataSaver helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post);
        this.helper = DataSaver.getInstance();
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etDescription = findViewById(R.id.etDescription);
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        Button btnPost = findViewById(R.id.btnPost);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnChooseImage.setOnClickListener(v -> {
            DataSaver.getInstance().setEdit_T();
            Intent intent = new Intent(this, picture.class);
            startActivity(intent);
        });

        btnPost.setOnClickListener(v -> {
            String setTitle = etTitle.getText().toString();
            String setDescription = etDescription.getText().toString();
            etTitle.setText("");
            etDescription.setText("");
            finalImage = helper.getImagePost();
            createPost(setTitle, setDescription);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void createPost(String setTitle, String setDescription) {
        String date = getDate();
        Post newPost = new Post(setTitle, setDescription, date, Converters.uriToString(finalImage), FeedPage.owner);
        FeedPage.postsViewModel.add(newPost);
        FeedPage.adapter.addPost(newPost);
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }
}