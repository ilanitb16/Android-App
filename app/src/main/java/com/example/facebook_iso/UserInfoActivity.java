package com.example.facebook_iso;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.adapters.OwnPostsListAdapter;
import com.example.facebook_iso.adapters.PostsListAdapter;
import com.example.facebook_iso.adapters.UsersAdapter;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;

import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity {
    private User user;
    private boolean isFriend;
    public static OwnPostsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        user = PostsListAdapter.author;
        ImageView profileImageView = findViewById(R.id.image_profile);
        TextView usernameTextView = findViewById(R.id.text_username);
        TextView nameTextView = findViewById(R.id.text_name);
        ImageButton backButton = findViewById(R.id.backbtn);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_posts);
        Button addFriendButton = findViewById(R.id.addFriendButton);


        usernameTextView.setText(user.getUsername());
        nameTextView.setText(user.getDisplayName());
        backButton.setOnClickListener(v -> {
            finish();
        });

        profileImageView.setImageURI(Converters.fromString(user.getProfilePic()));

        isFriend = checkFriend(user);
        if (isFriend) {
            nameTextView.setVisibility(View.VISIBLE);
            addFriendButton.setVisibility(View.GONE);
        } else {
            nameTextView.setVisibility(View.GONE);
            addFriendButton.setVisibility(View.VISIBLE);
            addFriendButton.setOnClickListener(v -> {
                User friend = user;
                if (friend != null) {
                    if (FeedPage.owner.findFriend(friend)) {
                        Toast.makeText(this, "Already a friend", Toast.LENGTH_SHORT).show();
                    } else if (friend.findRequest(FeedPage.owner.getUsername()) != null) {
                        Toast.makeText(this, "Friend request already sent", Toast.LENGTH_SHORT).show();
                    } else if (FeedPage.owner.getUsername().equals(friend.getUsername())) {
                        Toast.makeText(this, "You can't be your own friend", Toast.LENGTH_SHORT).show();
                    } else {
                        friend.sendFriendRequest(FeedPage.owner);
                        FeedPage.postsViewModel.friendsRequest(friend);
                        addFriendButton.setVisibility(View.GONE);
                    }
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OwnPostsListAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOwnPosts(user.getUserPosts());
    }


    private boolean checkFriend(User checking) {
        if (checking.getUsername().equals(FeedPage.owner.getUsername())){
            return true;
        }
        FeedPage.userViewModel.getUserFriends(FeedPage.owner);
        for (User friend : FeedPage.owner.getFriends()) {
            if (friend.getUsername().equals(checking.getUsername())) {
                return true;

            }
        }
        return false;
    }


}
