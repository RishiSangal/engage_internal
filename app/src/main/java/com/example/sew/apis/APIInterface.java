package com.example.sew.apis;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Raman Kumar on 3/19/2016.
 */
public interface APIInterface {
    @GET
    Call<ResponseBody> getAPI(@Url String url, @QueryMap HashMap<String, String> requestParams);

    @Multipart
    @POST
    Call<ResponseBody> postAPI(@Url String url, @PartMap HashMap<String, RequestBody> filesParams);

    @Headers("Content-Type: application/json")
    @POST
    Call<ResponseBody> postJSONAPI(@Url String url, @Body HashMap body);

    @Headers("Content-Type: application/json")
    @POST
    Call<ResponseBody> postJSONAPI(@Url String url, @Body HashMap body, @QueryMap HashMap<String, String> requestParams);

    @DELETE
    Call<ResponseBody> deleteAPI(@Url String url, @PartMap HashMap<String, RequestBody> filesParams);

    @Multipart
    @HTTP(method = "DELETE", hasBody = true)
    Call<ResponseBody> deleteSomething(@Url String url, @PartMap HashMap<String, RequestBody> filesParams);

    @Headers("Content-Type: application/json")
    @DELETE
    Call<ResponseBody> deleteJSONAPI(@Url String url, @Body String body);

    @Multipart
    @PUT
    Call<ResponseBody> putAPI(@Url String url, @PartMap HashMap<String, RequestBody> filesParams);

    @Headers("Content-Type: application/json")
    @PUT
    Call<ResponseBody> putJSONAPI(@Url String url, @Body String body);
//
//    @Multipart
//    @POST
//    Call<ResponseBody> postImage(@Url String url, @Part("file\"; filename=\"pp.png\" ") RequestBody file);


    @POST
    Call<ResponseBody> postImage(@Url String url, @Body RequestBody file);

}
