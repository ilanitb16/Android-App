package com.iso.facebook.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.iso.facebook.R;
import com.iso.facebook.entities.Post;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // Set data to views
        holder.feedPersonProfile.setImageURI(post.getProfilePic(context));
        holder.feedPersonName.setText(post.getUsername());
        holder.feedTitle.setText(post.getTitle());
        holder.feedDescription.setText(post.getDescription());
        holder.feedDate.setText(post.getDate());
        holder.feedTime.setText(post.getTime());
        holder.feedImage.setImageURI(post.getImg(context));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        CircleImageView feedPersonProfile;
        TextView feedPersonName;
        ImageView feedMenuButton;
        ImageView feedImage;
        TextView feedTitle;
        TextView feedDescription;
        TextView feedDate;
        TextView feedTime;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            feedPersonProfile = itemView.findViewById(R.id.feed_person_profile);
            feedPersonName = itemView.findViewById(R.id.feed_person_name);
            feedMenuButton = itemView.findViewById(R.id.feed_menu_button);
            feedImage = itemView.findViewById(R.id.feed_image);
            feedTitle = itemView.findViewById(R.id.feed_title);
            feedDescription = itemView.findViewById(R.id.feed_description);
            feedDate = itemView.findViewById(R.id.feed_date);
            feedTime = itemView.findViewById(R.id.feed_time);
        }
    }
}
