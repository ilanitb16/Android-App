package com.example.facebook_iso.api;

import retrofit2.Call;

import com.google.gson.JsonObject;


import java.util.ArrayList;

import retrofit2.http.DELETE;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {
     @GET("/api/posts")
    Call<ArrayList<JsonObject>> getPosts(@Header("authorization") String token);
     @POST("api/users/{id}/friends")
    Call<JsonObject> friendsRequest(@Path("id") String userId, @Body Object jsonUser, @Header("authorization") String token);
    @GET("api/users/{id}/posts")
    Call<ArrayList<JsonObject>> getUserPosts(@Path("id") String userId, @Header("authorization") String token);




     @POST("api/users/{id}/posts")
    Call<JsonObject> createPost(@Path("id") String userId, @Body Object jsonUser, @Header("authorization") String token);


    @PUT("api/users/{id}/posts/{pid}")
    Call<JsonObject> updatePost(@Path("id") String userId, @Path("pid") int postId, @Body Object jsonUser, @Header("authorization") String token);
    @DELETE("api/users/{id}/posts/{pid}")
    Call<JsonObject> deletePost(@Path("id") String userId, @Path("pid") int postId, @Header("authorization") String token);


}
