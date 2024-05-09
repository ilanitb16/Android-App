package com.iso.facebook.Fragments;

import static com.iso.facebook.FeedScreen.currentUser;
import static com.iso.facebook.FeedScreen.drawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.iso.facebook.FeedScreen;
import com.iso.facebook.R;
import com.iso.facebook.adapters.PostAdapter;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.SharedPreferencesManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.common.keys;
import com.iso.facebook.entities.Post;
import com.iso.facebook.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment
{
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private ImageView imageView;

    void fetchPosts() {
        ProgressDialogManager.showProgressDialog(getActivity(), "Getting Posts", "Please wait...");
        new ApiService(getActivity()).get(endPoints.getPosts, currentUser.getToken(), new ApiService.ApiCallback()
        {
            @Override
            public void onSuccess(JSONObject response)
            {
                ProgressDialogManager.dismissProgressDialog();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(JSONArray response)
            {
                getFriends(response);
            }

            @Override
            public void onError(String errorMessage)
            {
                UIToast.showToast(getActivity(), errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.feed_recycler_view);
        imageView = view.findViewById(R.id.menu_button);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(postAdapter);
        fetchPosts();

        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(() -> {
            fetchPosts();
            refreshLayout.setRefreshing(false);
        });

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    void getFriends(JSONArray responseArray)
    {
        new ApiService(getActivity()).get(endPoints.getUser +currentUser.getUsername(), currentUser.getToken(), new ApiService.ApiCallback()
        {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject response)
            {
                ProgressDialogManager.dismissProgressDialog();
                User user = User.fromJson(String.valueOf(response));
                if(user != null)
                {
                    FeedScreen.currentUserFriends = user.getFriends();
                }
                SharedPreferencesManager.setObject(getActivity(), keys.user, user);
                postList.clear();
                for(int i = 0; i < responseArray.length(); i++)
                {
                    try
                    {
                        postList.add(Post.fromJson(String.valueOf(responseArray.getJSONObject(i))));
                    }
                    catch (JSONException e)
                    {
                        UIToast.showToast(getActivity(), e.getMessage());
                        ProgressDialogManager.dismissProgressDialog();
                    }
                }
                postAdapter.notifyDataSetChanged();
                ProgressDialogManager.dismissProgressDialog();
            }

            @Override
            public void onSuccess(JSONArray response)
            {
                ProgressDialogManager.dismissProgressDialog();
            }

            @Override
            public void onError(String errorMessage)
            {
                UIToast.showToast(getActivity(), errorMessage);
                ProgressDialogManager.dismissProgressDialog();
            }
        });
    }
}