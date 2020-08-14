package com.example.usercaranimation.map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface interface1 {

   @GET
    Call<String> getdataFromGoogleApi(@Url String url);



}
