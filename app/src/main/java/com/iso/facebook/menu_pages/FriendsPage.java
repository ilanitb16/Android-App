package com.iso.facebook.menu_pages;

import static com.iso.facebook.FeedScreen.currentUser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.iso.facebook.R;
import com.iso.facebook.adapters.FriendAdapter;
import com.iso.facebook.adapters.FriendRequestAdapter;
import com.iso.facebook.adapters.MyPostAdapter;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.entities.Post;
import com.iso.facebook.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsPage extends AppCompatActivity {

    private RecyclerView recyclerView, requestRecyclerView;
    private FriendAdapter friendAdapter;
    private FriendRequestAdapter requestAdapter;

    private List<User.Friend> friendList;
    private List<User.FriendRequest> requestList;
    private ImageButton backButton;
    private TextView requestsButton;
    private TextView friendsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);
        recyclerView = findViewById(R.id.friends_feed_recycler_view);
        friendList = new ArrayList<>();
        requestList = new ArrayList<>();
        friendAdapter = new FriendAdapter(FriendsPage.this, friendList);
        requestAdapter = new FriendRequestAdapter(FriendsPage.this, requestList);

        backButton = findViewById(R.id.back_button);
        requestsButton = findViewById(R.id.requests_button);
        friendsButton = findViewById(R.id.friends_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(FriendsPage.this));
        recyclerView.setAdapter(friendAdapter);

        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        fetchData();

        refreshLayout.setOnRefreshListener(() -> {
            fetchData();
            refreshLayout.setRefreshing(false);
        });

        requestsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recyclerView.setAdapter(requestAdapter);
                handleButtonClick(requestsButton, friendsButton);
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recyclerView.setAdapter(friendAdapter);
                handleButtonClick(friendsButton, requestsButton);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void handleButtonClick(TextView clickedButton, TextView otherButton) {
        clickedButton.setBackgroundResource(R.drawable.rounded_button_background);
        clickedButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        otherButton.setBackground(null);
        otherButton.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    void fetchData() {
        ProgressDialogManager.showProgressDialog(FriendsPage.this, "Getting Data", "Please wait...");
        new ApiService(FriendsPage.this).get(endPoints.getUser +currentUser.getUsername(), currentUser.getToken(), new ApiService.ApiCallback()
        {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(JSONObject response)
            {
                friendList.clear();
                requestList.clear();
                User user = User.fromJson(String.valueOf(response));
                friendList = user.getFriends();
                requestList = user.getFriendsRequest();
                Log.d("Api call", String.valueOf(friendList.size()));
                Log.d("Api call", String.valueOf(requestList.size()));
                requestAdapter.notifyDataSetChanged();
                friendAdapter.notifyDataSetChanged();
                UIToast.showToast(FriendsPage.this, "Getting Data");
                ProgressDialogManager.dismissProgressDialog();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(JSONArray response)
            {
                ProgressDialogManager.dismissProgressDialog();
            }

            @Override
            public void onError(String errorMessage)
            {
                UIToast.showToast(FriendsPage.this, errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
}
