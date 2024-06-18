package com.iso.facebook.adapters;

import static com.iso.facebook.FeedScreen.currentUser;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.iso.facebook.R;
import com.iso.facebook.api.ApiService;
import com.iso.facebook.api.endPoints;
import com.iso.facebook.common.BottomSheet.BottomSheetUtils;
import com.iso.facebook.common.ProgressDialogManager;
import com.iso.facebook.common.UIToast;
import com.iso.facebook.entities.Post;
import com.iso.facebook.menu_pages.EditPost;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.PostViewHolder> {

    private List<Post> postList;
    public Context context;

    public MyPostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item, parent, false);
        return new PostViewHolder(view, context, postList);
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

    public class PostViewHolder extends RecyclerView.ViewHolder {

        CircleImageView feedPersonProfile;
        TextView feedPersonName;
        ImageView feedMenuButton;
        ImageView feedImage;
        TextView feedTitle;
        TextView feedDescription;
        TextView feedDate;
        TextView feedTime;

        public PostViewHolder(@NonNull View itemView, Context context, List<Post> postList) {
            super(itemView);

            feedPersonProfile = itemView.findViewById(R.id.feed_person_profile);
            feedPersonName = itemView.findViewById(R.id.feed_person_name);
            feedMenuButton = itemView.findViewById(R.id.add_friend);
            feedImage = itemView.findViewById(R.id.feed_image);
            feedTitle = itemView.findViewById(R.id.feed_title);
            feedDescription = itemView.findViewById(R.id.feed_description);
            feedDate = itemView.findViewById(R.id.feed_date);
            feedTime = itemView.findViewById(R.id.feed_time);

            feedMenuButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();
                    BottomSheetUtils.showEditBottomSheet(((AppCompatActivity) context).getSupportFragmentManager(), new View.OnClickListener()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View view)
                        {
                            if (position != RecyclerView.NO_POSITION) {
                                ProgressDialogManager.showProgressDialog(context, "Deleting Post", "Please wait...");
                                new ApiService(context).delete(endPoints.request+currentUser.getUsername() +"/posts/" + postList.get(position).get_id(), currentUser.getToken(), new ApiService.ApiCallback()
                                {
                                    @Override
                                    public void onSuccess(JSONObject response)
                                    {
                                        ProgressDialogManager.dismissProgressDialog();
                                        UIToast.showToast(context, "Post Deleted");
                                        removeItem(position);
                                    }

                                    @Override
                                    public void onSuccess(JSONArray response)
                                    {
                                        ProgressDialogManager.dismissProgressDialog();
                                    }

                                    @Override
                                    public void onError(String errorMessage)
                                    {
                                        UIToast.showToast(context, errorMessage);
                                        ProgressDialogManager.dismissProgressDialog();
                                    }
                                });
                            }
                        }
                    }, new View.OnClickListener()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View view)
                        {
                            Post post = postList.get(position);
                            Intent editPostIntent = new Intent(context, EditPost.class);
                            editPostIntent.putExtra("id", post.get_id());
                            editPostIntent.putExtra("title", post.getTitle());
                            editPostIntent.putExtra("description", post.getDescription());
                            editPostIntent.putExtra("imageUri", post.getImg(context));
                            context.startActivity(editPostIntent);
                        }
                    });;
                }
            });
        }
        private void removeItem(int position) {
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }
}

