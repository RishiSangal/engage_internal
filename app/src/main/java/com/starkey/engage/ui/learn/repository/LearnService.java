package com.starkey.engage.ui.learn.repository;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LearnService {

//    @GET("sitecore/api/layout/render/jss?topicfilter=5659dda588c44416ae992d7eb09ed3ba&item=%2F&sc_apikey=%7B596102D9-AF50-464A-AB69-A7EB04F19544%7D&videofilter=36bc9028dcd84153a2863224cfdfc0a2%2C3bb5c65e7c8f4692886f4fc3e500ca18%2C78b1a8d30cca4c9bbb98446440d39d40")
//    Call<String> getTopics();


    @GET("api/search/getitems")
    Call<String> getAllNews( @Query("tt") String searchContentId,
                             @Query("l") String countryLabel);

    @GET("api/search/getdetail")
    Call<String> getDetail( @Query("id") String searchContentId,
                           @Query("l") String countryLabel);

    @GET("api/search/getdetail")
    Call<String> getVideoDetail( @Query("id") String searchContentId,
                            @Query("l") String countryLabel);
}
