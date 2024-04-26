package com.example.facebook_iso.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Ignore;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.R;
import com.example.facebook_iso.entities.Comment;
import com.example.facebook_iso.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private final Post post;
    private int backgroundColor;
    private int TextColor;
    private final PostsListAdapter postsListAdapter;
    @Ignore
    private View postLayout;
    public CommentAdapter(Post post, PostsListAdapter postListAdapter) {
        this.comments = new ArrayList<>();
        this.post = post;
        this.postsListAdapter = postListAdapter;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment currentComment = comments.get(position);
        holder.userNameComment.setText(currentComment.getAuthor());
        holder.userPhotoComment.setImageURI(Converters.fromString(currentComment.getAuthor_photo()));
        holder.commentText.setText(currentComment.getContext());

        toggleTheme();
        holder.commentLayout.setBackgroundColor(backgroundColor);
        holder.commentText.setTextColor(TextColor);
        holder.userNameComment.setTextColor(TextColor);
        holder.commentEdit.setTextColor(TextColor);


        holder.editButton.setOnClickListener(v -> {
            holder.updateComment.setVisibility(View.VISIBLE);
            holder.commentEdit.setVisibility(View.VISIBLE);
            holder.commentText.setVisibility(View.GONE);
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);

            holder.updateComment.setOnClickListener(v1 -> {
                editComment(currentComment);
                holder.updateComment.setVisibility(View.GONE);
                holder.commentEdit.setVisibility(View.GONE);
                holder.commentText.setVisibility(View.VISIBLE);
                holder.editButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setVisibility(View.VISIBLE);
            });
        });

        holder.deleteButton.setOnClickListener(v -> deleteComment(currentComment));
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
        View commentLayout;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            userNameComment = itemView.findViewById(R.id.userNameComment);
            userPhotoComment = itemView.findViewById(R.id.userPhotoComment);
            commentEdit = itemView.findViewById(R.id.commentEdit);
            updateComment = itemView.findViewById(R.id.updateComment);
            commentLayout = itemView.findViewById(R.id.commentLayout);
        }
    }
    public void toggleTheme() {
        Boolean isDarkMode = FeedPage.isDarkMode;
        backgroundColor = isDarkMode ?
                ContextCompat.getColor(FeedPage.feedAdapter.getContext(), R.color.BACKGROUND_POST_DARK) :
                ContextCompat.getColor(FeedPage.feedAdapter.getContext(), R.color.BACKGROUND_POST_LIGHT);
        TextColor = isDarkMode ?
                ContextCompat.getColor(FeedPage.feedAdapter.getContext(), R.color.white) :
                ContextCompat.getColor(FeedPage.feedAdapter.getContext(), R.color.black);
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public void addComment(Comment comment) {
        comments.add(0, comment);
        post.addComment(comment);
        notifyItemInserted(0);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
        postsListAdapter.refreshComments();
        notifyDataSetChanged();
    }

    public void editComment(Comment comment) {
        EditText commentEdit = postLayout.findViewById(R.id.commentEdit);
        String commentInserted = commentEdit.getText().toString();
        comment.setContext(commentInserted);
        commentEdit.setText(commentInserted);
        postsListAdapter.refreshComments();
        notifyDataSetChanged();
    }

    public void setPostLayout(View postLayout) {
        this.postLayout = postLayout;
    }
}