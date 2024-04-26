package com.example.facebook_iso.api;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.MyApplication;
import com.example.facebook_iso.R;
import com.example.facebook_iso.common.ProgressDialogManager;
import com.example.facebook_iso.common.UIToast;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.repositories.PostsRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public PostAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

    }


    public void index(User user, MutableLiveData<List<Post>> allPosts) {
        ProgressDialogManager.showProgressDialog(FeedPage.context, "Getting Posts", "Please Wait...");
        String token = user.getToken();
        Call<ArrayList<JsonObject>> call = webServiceAPI.getPosts("Bearer " + token);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                if (response.isSuccessful()) {

                    ArrayList<JsonObject> jsonPostsList = response.body();
                    Log.d("Api Call", response.body().toString());

                    if (jsonPostsList != null) {
                        List<Post> posts = new ArrayList<>();
                        CompletableFuture<Void> allUsersFetched = new CompletableFuture<>();
                        for (int i = 0; i < jsonPostsList.size(); i++) {
                            JsonObject jsonPost = jsonPostsList.get(i);
                            String postId = jsonPost.get("_id").getAsString();
                            String username = jsonPost.get("username").getAsString();
                            String create_date = jsonPost.get("create_date").getAsString();
                            String description = jsonPost.get("description").getAsString();
                            String imgBase64 = jsonPost.get("img").getAsString();
                            String title = jsonPost.get("title").getAsString();
                            String date = create_date.substring(0, 10);
                            String imgString = imgBase64.substring(imgBase64.indexOf(',') + 1);
                            String img = Converters.base64ToString(imgString);
                            Post post = new Post(postId, title, description, date, img, null);

                            FeedPage.userViewModel.getUser(username, post, allUsersFetched, i == jsonPostsList.size() - 1);
                            posts.add(post);
                        }


                        allUsersFetched.thenAccept((Void) -> {
                            allPosts.postValue(posts);
                            UIToast.showToast(FeedPage.context, "Posts Fetched");
                            FeedPage.feedAdapter.setPosts(posts);
                            FeedPage.postsViewModel.getUserPosts(FeedPage.owner);
                            ProgressDialogManager.dismissProgressDialog();
                            new Thread(() -> {
                                for (Post post : PostsRepository.postDao.index()) {
                                    Converters.deleteImageFromStorage(post.getImg());
                                    Converters.deleteImageFromStorage(post.getProfilePic());
                                    PostsRepository.postDao.delete(post);
                                }
                                for (Post post0 : posts) {
                                    PostsRepository.postDao.insert(post0);
                                }
                            }).start();
                        }).exceptionally(ex -> {
                            Log.e("Api Call", "Failed to fetch user data: " + ex.getMessage());
                            ProgressDialogManager.dismissProgressDialog();
                            UIToast.showToast(FeedPage.context, "Failed to fetch user data: " + ex.getMessage());
                            return null;
                        });
                    } else {
                        Log.e("Api Call", "Failed to save image to internal storage");
                        UIToast.showToast(FeedPage.context, "Failed to save image to internal storage");
                    }
                }

            }


            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                ProgressDialogManager.dismissProgressDialog();
                UIToast.showToast(FeedPage.context, "Error " + t.getMessage());
            }
        });
    }


    public void indexUser(User user) {
        String token = user.getToken();
        Call<ArrayList<JsonObject>> call = webServiceAPI.getUserPosts(user.getUsername(), "Bearer " + user.getToken());
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                if (response.isSuccessful()) {

                    ArrayList<JsonObject> jsonPostsList = response.body();
                    if (jsonPostsList != null) {
                        List<Post> posts = new ArrayList<>();
                        for (JsonObject jsonPost : jsonPostsList) {
                            String id = jsonPost.get("_id").getAsString();
                            String create_date = jsonPost.get("create_date").getAsString();
                            String description = jsonPost.get("description").getAsString();
                            String imgBase64 = jsonPost.get("img").getAsString();
                            String title = jsonPost.get("title").getAsString();

                            String date = create_date.substring(0, 10);
                            String imgString = imgBase64.substring(imgBase64.indexOf(',') + 1);
                            String img = Converters.base64ToString(imgString);

                            Post post = new Post(id, title, description, date, img, user);
                            posts.add(post);

                        }
                        user.setUserPosts(posts);
                    }
                }

            }
            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
            }
        });
    }

    public void add(Post postToAdd, MutableLiveData<List<Post>> allPosts) {
        postToAdd.setUser(FeedPage.owner);
        try {
            String token = postToAdd.getUser().getToken();
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("username", FeedPage.owner.getUsername());
            requestBodyJson.put("description", postToAdd.getDescription());
            requestBodyJson.put("img", postToAdd.getImg());
            requestBodyJson.put("title", postToAdd.getTitle());
            requestBodyJson.put("profilePic", FeedPage.owner.getProfilePic());
            Object jsonParser = JsonParser.parseString(requestBodyJson.toString());


            Call<JsonObject> call = webServiceAPI.createPost(FeedPage.owner.getUsername(), jsonParser, "Bearer " + token);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                            JsonObject jsonObject = response.body();
                            if (jsonObject != null) {
                                if (jsonObject.has("insertedId")) {
                                    new Thread(() -> PostsRepository.postDao.insert(postToAdd)).start();
                                }

                            }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void edit(Post postToEdit, MutableLiveData<List<Post>> allPosts) {
        String token = postToEdit.getUser().getToken();
        JSONObject requestBodyJson = new JSONObject();
        try {
            requestBodyJson.put("_id", postToEdit.getPostId());
            requestBodyJson.put("username", FeedPage.owner.getUsername());
            requestBodyJson.put("create_date", postToEdit.getCreateDate());
            requestBodyJson.put("description", postToEdit.getDescription());
            requestBodyJson.put("img", postToEdit.getImg());
            requestBodyJson.put("title", postToEdit.getTitle());
            requestBodyJson.put("profilePic", postToEdit.getProfilePic());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Object jsonParser = JsonParser.parseString(requestBodyJson.toString());

        Log.d("Api call EditDebug", "Sending update request for post with ID: " + postToEdit.getPostId());
        Log.d("Api call EditDebug", "User token: " + token);

        Call<JsonObject> call = webServiceAPI.updatePost(postToEdit.getPostId(), postToEdit.getPostId(), jsonParser, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("Api call EditDebug", "Update post API call successful");
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        if (jsonObject.has("modifiedCount")) {
                            Log.d("Api call EditDebug", "Post successfully updated");
                            new Thread(() -> {
                                Log.d("Api call EditDebug", "Updating post in local database");
                                PostsRepository.postDao.update(postToEdit);
                            }).start();
                            index(FeedPage.owner, allPosts);
                            Toast.makeText(FeedPage.feedAdapter.getContext(), "Post updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Api call EditDebug", "No 'modifiedCount' field found in response JSON");
                        }
                    } else {
                        Log.d("Api call EditDebug", "Response body is null");
                    }
                } else {
                    Log.d("Api call EditDebug", "Update post API call unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Api call EditDebug", "Failed to update post. Error: " + t.getMessage());
            }
        });
    }


    public void delete(Post postToRemove, MutableLiveData<List<Post>> allPosts) {
        String id = postToRemove.getPostId();
        String token = postToRemove.getUser().getToken();
        Log.d("Api call DeleteDebug", "Deleting post with ID: " + id);
        Log.d("Api call DeleteDebug", "User token: " + token);

        Call<JsonObject> call = webServiceAPI.deletePost(FeedPage.owner.getUsername(), id, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("Api call DeleteDebug", "Delete post API call successful");
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null) {
                        if (jsonObject.has("deletedCount")) {
                            Converters.deleteImageFromStorage(postToRemove.getImg());
                            new Thread(() -> {
                                Log.d("Api call DeleteDebug", "Deleting post from local database");
                                PostsRepository.postDao.delete(postToRemove);
                            }).start();
                            index(postToRemove.getUser(), allPosts);
                            indexUser(postToRemove.getUser());
                            Toast.makeText(FeedPage.feedAdapter.getContext(), "Post deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Api call DeleteDebug", "No 'deletedCount' field found in response JSON");
                        }
                    } else {
                        Log.d("Api call DeleteDebug", "Response body is null");
                    }
                } else {
                    Log.d("Api call DeleteDebug", "Delete post API call unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Api call DeleteDebug", "Failed to delete post. Error: " + t.getMessage());
            }
        });
    }


    public void friendsRequest(User user) {
        JSONObject requestBodyJson = new JSONObject();
        Object jsonParser = JsonParser.parseString(requestBodyJson.toString());
        String token = FeedPage.owner.getToken();


        Call<JsonObject> call = webServiceAPI.friendsRequest(user.getUsername(), jsonParser, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                        JsonObject jsonObject = response.body();
                        if (jsonObject != null) {
                            if (jsonObject.has("modifiedCount")) {
                                Toast.makeText(FeedPage.feedAdapter.getContext(), "friend request sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

    }
}
