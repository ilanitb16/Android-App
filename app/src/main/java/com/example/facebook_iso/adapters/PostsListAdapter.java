package com.example.facebook_iso.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.R;
import com.example.facebook_iso.entities.Post;

import java.util.List;
import java.util.concurrent.Semaphore;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {
    private CommentAdapter commentAdapter;
    private String user_name;
    private Uri user_photo;
    private String commentInsert;

    public PostsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        commentInsert = "";
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvDate;
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvLikes;
        private final ImageView ivImg;
        private final ImageView ivAuthorPhoto;
        private final ImageView likeButton;
        private final ImageView shareButton;
        private final ImageView commentButton;
        private final ImageView threeDots;
        private final Button postCommentButton;

        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            ivImg = itemView.findViewById(R.id.ivImg);
            ivAuthorPhoto = itemView.findViewById(R.id.ivAuthorPhoto);
            likeButton = itemView.findViewById(R.id.likeButton);
            shareButton = itemView.findViewById(R.id.shareButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            threeDots = itemView.findViewById(R.id.threeDots);
            postCommentButton = itemView.findViewById(R.id.postCommentButton);
        }
    }

    private final LayoutInflater mInflater;
    private List<Post> posts;

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.post_layout, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        if (posts != null) {
            final Post current = posts.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvDate.setText(current.getDate());
            holder.tvTitle.setText(current.getTitle());
            holder.tvDescription.setText(current.getDescription());
            holder.tvLikes.setText(String.valueOf(current.getLikes()));
            holder.ivImg.setImageURI(current.getImg());
            holder.ivAuthorPhoto.setImageURI(current.getAuthorPhoto());
            this.commentAdapter = new CommentAdapter(current.getCommentPost().getComments(), user_name, user_photo, current, this);
            current.setPostLayout(holder.itemView);

            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current.toggleLike();
                }
            });
            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current.toggleShare(v);
                }
            });
            holder.commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current.toggleComment(v);
                }
            });
            holder.threeDots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current.toggleThreeDots(v);
                }
            });
            holder.postCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText commentInput = holder.itemView.findViewById(R.id.commentInput);
                    commentInsert = commentInput.getText().toString();
                    current.getCommentPost().addComment(commentInsert);
                    commentInput.setText("");
                }
            });

            RecyclerView commentsRecyclerView = holder.itemView.findViewById(R.id.commentsRecyclerView);
            commentsRecyclerView.setAdapter(this.commentAdapter);
            commentsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

        }
    }


    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void addPost(Post post) {
        this.posts.add(0, post);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        else return 0;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }
    public void setUserPhoto(Uri user_photo) {
        this.user_photo = user_photo;
    }
}