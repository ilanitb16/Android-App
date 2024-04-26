package com.example.facebook_iso.adapters;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook_iso.R;
import com.example.facebook_iso.entities.Post;

import java.util.List;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<String> comments;
    private String user_name;
    private Uri user_photo;
    private Post post;
    private PostsListAdapter adapter;
    public CommentAdapter(List<String> comments, String user_name, Uri user_photo, Post post, PostsListAdapter adapter) {
        this.comments = comments;
        this.user_name = user_name;
        this.user_photo =user_photo;
        this.post = post;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();
        String comment = comments.get(currentPosition);
        holder.commentText.setText(comment);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.updateComment.setVisibility(View.VISIBLE);
                holder.commentEdit.setVisibility(View.VISIBLE);
                holder.commentText.setVisibility(View.GONE);
                holder.editButton.setVisibility(View.GONE);
                holder.deleteButton.setVisibility(View.GONE);
                String currentComment = comments.get(currentPosition);
                holder.commentEdit.setText(currentComment);
                holder.updateComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText commentEdit = holder.itemView.findViewById(R.id.commentEdit);
                        String newComment = commentEdit.getText().toString();
                        holder.commentText.setText(newComment);
                        holder.updateComment.setVisibility(View.GONE);
                        holder.commentEdit.setVisibility(View.GONE);
                        holder.commentText.setVisibility(View.VISIBLE);
                        holder.editButton.setVisibility(View.VISIBLE);
                        holder.deleteButton.setVisibility(View.VISIBLE);
                        post.getCommentPost().onEditClick(currentComment, newComment);

                    }
                });
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.getCommentPost().onDeleteClick(comments.get(currentPosition));
            }
        });

        holder.userNameComment.setText(user_name);
        holder.userPhotoComment.setImageURI(user_photo);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        ImageButton editButton;
        ImageButton deleteButton;
        TextView userNameComment;
        ImageView userPhotoComment;
        EditText commentEdit;
        Button updateComment;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            userNameComment = itemView.findViewById(R.id.userNameComment);
            userPhotoComment = itemView.findViewById(R.id.userPhotoComment);
            commentEdit = itemView.findViewById(R.id.commentEdit);
            updateComment = itemView.findViewById(R.id.updateComment);
        }
    }
}