package com.example.facebook_iso.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.json.JSONStringer;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface UserServiceAPI {
    @POST("/api/users")
    Call<JsonObject> signUp(@Body Object jsonUser);
    @POST("/api/tokens")
    Call<JsonObject> signIn(@Body Object jsonUser);
    @PUT("api/users/{id}")
    Call<JsonObject> updateUser(@Path("id") String userId, @Body Object jsonUser, @Header("authorization") String token);
    @GET("api/users/{id}")
    Call<JsonObject> getUser(@Path("id") String userId, @Header("authorization") String token);
    @GET("api/users/{id}/friends")
    Call<JsonObject> getUserFriends(@Path("id") String userId, @Header("authorization") String token);
    @DELETE("api/users/{id}/friends/{fid}")
    Call<JsonObject> deleteFriendsRequest(@Path("id") String userId, @Path("fid") String friendId, @Header("authorization") String token);
    @PATCH("api/users/{id}/friends/{fid}")
    Call<JsonObject> approveFriendsRequest(@Path("id") String userId, @Path("fid") String friendId, @Header("authorization") String token);




    @DELETE("api/users/{id}")
    Call<JsonObject> deleteUser(@Path("id") String username, @Header("authorization") String token);
   }

