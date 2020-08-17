package com.example.retrofit;

import org.w3c.dom.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    @GET("posts")
    Call<List<com.example.retrofit.Comment>> getPosts();
    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments();
}
