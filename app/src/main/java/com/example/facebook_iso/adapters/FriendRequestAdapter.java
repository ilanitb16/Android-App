package com.example.facebook_iso.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.RequestsViewHolder> {
    private final LayoutInflater mInflater;
    private List<User> friendRequests;

    public FriendRequestAdapter(Context context) {
        this.friendRequests = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    static class RequestsViewHolder extends RecyclerView.ViewHolder {
        private final TextView usernameTextView;
        private final TextView displayNameTextView;
        private final ImageView profilePicImageView;
        private final Button acceptButton;
        private final Button declineButton;

        private RequestsViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            displayNameTextView = itemView.findViewById(R.id.displayNameTextView);
            profilePicImageView = itemView.findViewById(R.id.profilePicImageView);
            acceptButton = itemView.findViewById(R.id.button);
            declineButton = itemView.findViewById(R.id.button2);
        }
    }

    @Override
    public RequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.friends_request, parent, false);
        return new RequestsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestsViewHolder holder, int position) {
        FeedPage.owner.getFriends_request();
        User current = friendRequests.get(position);
        holder.usernameTextView.setText(current.getUsername());
        holder.displayNameTextView.setText(current.getDisplayName());
        holder.profilePicImageView.setImageURI(Converters.fromString(current.getProfilePic()));

        holder.acceptButton.setOnClickListener(v -> {
            FeedPage.owner.addFriend(current);
            FeedPage.userViewModel.approveFriendsRequest(current);
            removeRequest(current);
        });

        holder.declineButton.setOnClickListener(v -> {
            FeedPage.userViewModel.deleteFriendsRequest(current);
            removeRequest(current);
        });
    }

    public void removeRequest(User user) {
        this.friendRequests.remove(user);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFriendRequests(List<User> friendRequests) {
        this.friendRequests = friendRequests;
        notifyDataSetChanged();
    }
}