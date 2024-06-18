package com.iso.facebook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iso.facebook.R;
import com.iso.facebook.entities.User.Friend;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private final List<Friend> friendList;
    private final Context context;

    public FriendAdapter(Context context, List<Friend> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.bind(context, friend);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePicImageView;
        TextView displayNameTextView;
        TextView displayUsernameTextView;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePicImageView = itemView.findViewById(R.id.profile_image);
            displayNameTextView = itemView.findViewById(R.id.friend_name);
            displayUsernameTextView = itemView.findViewById(R.id.friend_username);
        }

        public void bind(Context context, Friend friend) {
            displayNameTextView.setText(friend.getDisplayName());
            profilePicImageView.setImageURI(friend.getProfilePic(context));
            displayUsernameTextView.setText(friend.getUsername());
        }
    }
}
