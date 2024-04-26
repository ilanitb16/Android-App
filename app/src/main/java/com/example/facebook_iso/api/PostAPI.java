package com.example.facebook_iso.api;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.facebook_iso.Converters;
import com.example.facebook_iso.FeedPage;
import com.example.facebook_iso.MyApplication;
import com.example.facebook_iso.R;
import com.example.facebook_iso.entities.Post;
import com.example.facebook_iso.entities.User;
import com.example.facebook_iso.login.Login_Page;
import com.example.facebook_iso.repositories.PostsRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        String token = user.getToken();
        Call<ArrayList<JsonObject>> call = webServiceAPI.getPosts("Bearer " + token);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                if (response.isSuccessful()) {

                    ArrayList<JsonObject> jsonPostsList = response.body();
                    if (jsonPostsList != null) {
                        List<Post> posts = new ArrayList<>();
                        for (JsonObject jsonPost : jsonPostsList) {
                            String postId = jsonPost.get("_id").getAsString();
                            String username = jsonPost.get("username").getAsString();

                            String create_date = jsonPost.get("create_date").getAsString();
                            String description = jsonPost.get("description").getAsString();
                            String imgBase64 = jsonPost.get("img").getAsString();
                            String title = jsonPost.get("title").getAsString();

                            String date = create_date.substring(0, 10);
                            String imgString = imgBase64.substring(imgBase64.indexOf(',') + 1);
                            String img = Converters.base64ToString(imgString);

                            Post post = new Post(title, description, date, img, null);
                            Login_Page.userViewModel.getUser(username, post);
                            posts.add(post);


                        }

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
                        allPosts.postValue(posts);
                    } else {
                        Log.e("PostAPI", "Failed to save image to internal storage");
                    }
                }

            }


            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
            }
        });
    }

    public void indexUser(User user) {
        String token = user.getToken();
        Call<ArrayList<JsonObject>> call = webServiceAPI.getUserPosts(user.getUsername(), "Bearer " + FeedPage.owner.getToken());
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                if (response.isSuccessful()) {

                    ArrayList<JsonObject> jsonPostsList = response.body();
                    if (jsonPostsList != null) {
                        List<Post> posts = new ArrayList<>();
                        for (JsonObject jsonPost : jsonPostsList) {
                            String create_date = jsonPost.get("create_date").getAsString();
                            String description = jsonPost.get("description").getAsString();
                            String imgBase64 = jsonPost.get("img").getAsString();
                            String title = jsonPost.get("title").getAsString();

                            String date = create_date.substring(0, 10);
                            String imgString = imgBase64.substring(imgBase64.indexOf(',') + 1);
                            String img = Converters.base64ToString(imgString);

                            Post post = new Post(title, description, date, img, user);
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
                                    index(FeedPage.owner, allPosts);
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
            requestBodyJson.put("id", FeedPage.owner.getUsername());
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

        Call<JsonObject> call = webServiceAPI.updatePost(postToEdit.getUsername(), postToEdit.getId(), jsonParser, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                        JsonObject jsonObject = response.body();
                        if (jsonObject != null) {
                            if (jsonObject.has("modifiedCount")) {
                                new Thread(() -> PostsRepository.postDao.update(postToEdit)).start();
                                index(FeedPage.owner, allPosts);
                                Toast.makeText(FeedPage.adapter.getContext(), "post updated", Toast.LENGTH_SHORT).show();
                            }

                        }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public void delete(Post postToRemove, MutableLiveData<List<Post>> allPosts) {
        String usernameId = postToRemove.getUsername();
        int id = postToRemove.getId();
        String token = postToRemove.getUser().getToken();
        Call<JsonObject> call = webServiceAPI.deletePost(usernameId, id, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                        JsonObject jsonObject = response.body();
                        if (jsonObject != null) {
                            if (jsonObject.has("deletedCount")) {
                                Converters.deleteImageFromStorage(postToRemove.getImg());
                                new Thread(() -> PostsRepository.postDao.delete(postToRemove)).start();
                                index(postToRemove.getUser(), allPosts);
                                indexUser(postToRemove.getUser());
                                Toast.makeText(FeedPage.adapter.getContext(), "post deleted", Toast.LENGTH_SHORT).show();
                            }

                        }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

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
                                Toast.makeText(FeedPage.adapter.getContext(), "friend request sent", Toast.LENGTH_SHORT).show();
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
