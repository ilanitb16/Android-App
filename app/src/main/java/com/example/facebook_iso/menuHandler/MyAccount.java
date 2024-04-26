package com.example.facebook_iso.menuHandler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.UserInfoActivity;
import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.login.Login_Page;
import com.example.facebook_iso.update_user_activity;

import java.util.ArrayList;
import java.util.List;

public class MyAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.my_account);


        TextView userName0 = findViewById(R.id.userName);
        ImageView userPhoto0 = findViewById(R.id.userPhoto);
        TextView userFullName0 = findViewById(R.id.userFullName);
        Button btnDone = findViewById(R.id.btnDone);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnPosts = findViewById(R.id.ownPostBtn);

        userName0.setText(FeedPage.owner.getUsername());
        userPhoto0.setImageURI(Converters.fromString(FeedPage.owner.getProfilePic()));
        userFullName0.setText(FeedPage.owner.getDisplayName());

        btnDone.setOnClickListener(v -> {
            FeedPage.picbtn = false;
            Intent i =new Intent(this, FeedPage.class);
            startActivity(i);
        });

        btnEdit.setOnClickListener(v -> {
            Intent i =new Intent(this, update_user_activity.class);
            startActivity(i);
        });
        btnPosts.setOnClickListener(v -> {
            FeedPage.picbtn = true;
            PostsListAdapter.author = FeedPage.owner;
            Intent intent = new Intent(this, UserInfoActivity.class);
            startActivity(intent);
        });
    }
}