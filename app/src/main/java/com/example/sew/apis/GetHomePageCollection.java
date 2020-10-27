package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.DidYouKnow;
import com.example.sew.models.HomeBannerCollection;
import com.example.sew.models.HomeFeatured;
import com.example.sew.models.HomeLookingMore;
import com.example.sew.models.HomeOtherWorldOfTheDay;
import com.example.sew.models.HomePromotionalBanner;
import com.example.sew.models.HomeProseCollection;
import com.example.sew.models.HomeShayari;
import com.example.sew.models.HomeShayariImage;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.HomeTodayTop;
import com.example.sew.models.HomeTopPoet;
import com.example.sew.models.HomeVideo;
import com.example.sew.models.HomeWordOfTheDay;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetHomePageCollection extends Base {


    public GetHomePageCollection() {
        setUrl(MyConstants.getGetHomePageCollection());
        setRequestType(REQUEST_TYPE.POST);
    }

    public GetHomePageCollection setCommonParams() {
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
        //addParam("lastFetchDate", "");
        addParam("lastFetchDate", "2020-10-23");
        return this;
    }


    /*
    "Promotion":[],
    "Carousels":[],
    "TopPoets":[],
    "WordOfTheDay":{},
    "OtherWordOfTheDay":[],
    "Video":{},
    "MoreVideos":[],
    "Featured":[],
    "TodaysTop":[],
    "ImgShayari":[],
    "T20Series":[],
    "ShayariCollection":[],
    "Discover":[],
    "SherCollection":[],
    "ProseCollection":[],
    "LookingMore":[]
     */
    private ArrayList<HomePromotionalBanner> promotionalBanners;
    private ArrayList<HomeBannerCollection> bannerCollections;
    private ArrayList<HomeTopPoet> topPoets;
    private ArrayList<HomeOtherWorldOfTheDay> otherWorldOfTheDays;
    private ArrayList<HomeVideo> videos;
    private ArrayList<HomeFeatured> featureds;
    private ArrayList<HomeTodayTop> todayTops;
    private ArrayList<HomeShayariImage> shayariImages;
    //    private ArrayList<HomeT20Series> t20Series;
    private ArrayList<HomeSherCollection> sherCollections;
    private ArrayList<HomeShayari> shayaris;
    private ArrayList<HomeProseCollection> proseCollections;
    private ArrayList<HomeLookingMore> lookingMores;
    private HomeWordOfTheDay wordOfTheDay;
    private HomeVideo video;
    private DidYouKnow didYouKnow;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
//        try {
//            data.put("WordOfTheDay", new JSONObject(MyHelper.readRawFile(R.raw.word_of_the_day_test)));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        if (isValidResponse()) {
            JSONArray array;
            int size = 0;
            array = getArray("Promotion");
            size = array.length();
            promotionalBanners = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                promotionalBanners.add(new HomePromotionalBanner(array.optJSONObject(i)));

            array = getArray("Carousels");
            size = array.length();
            bannerCollections = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                bannerCollections.add(new HomeBannerCollection(array.optJSONObject(i)));

            array = getArray("TopPoets");
            size = array.length();
            topPoets = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                topPoets.add(new HomeTopPoet(array.optJSONObject(i)));

            array = getArray("OtherWordOfTheDay");
            size = array.length();
            otherWorldOfTheDays = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                otherWorldOfTheDays.add(new HomeOtherWorldOfTheDay(array.optJSONObject(i)));

            array = getArray("MoreVideos");
            size = array.length();
            videos = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                videos.add(new HomeVideo(array.optJSONObject(i)));

            array = getArray("Featured");
            size = array.length();
            featureds = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                featureds.add(new HomeFeatured(array.optJSONObject(i)));

            array = getArray("TodaysTop");
            size = array.length();
            todayTops = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                todayTops.add(new HomeTodayTop(array.optJSONObject(i)));

            array = getArray("ImgShayari");
            size = array.length();
            shayariImages = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                shayariImages.add(new HomeShayariImage(array.optJSONObject(i)));


            array = getArray("ShayariCollection");
            size = array.length();
            shayaris = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                shayaris.add(new HomeShayari(array.optJSONObject(i)));

            array = getArray("SherCollection");
            size = array.length();
            sherCollections = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                sherCollections.add(new HomeSherCollection(array.optJSONObject(i)));

            array = getArray("ProseCollection");
            size = array.length();
            proseCollections = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                proseCollections.add(new HomeProseCollection(array.optJSONObject(i)));

            array = getArray("LookingMore");//LookingMore
            size = array.length();
            lookingMores = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                lookingMores.add(new HomeLookingMore(array.optJSONObject(i)));

            video = new HomeVideo(data.optJSONObject("Video"));
            wordOfTheDay = new HomeWordOfTheDay(data.optJSONObject("WordOfTheDay"));
            didYouKnow = new DidYouKnow(data.optJSONObject("DidYouKnow"));
        }
    }

    private JSONArray getArray(String key) {
        JSONArray array = getData().optJSONArray(key);
        if (array == null)
            array = new JSONArray();
        return array;
    }

    public ArrayList<HomePromotionalBanner> getPromotionalBanners() {
        return promotionalBanners;
    }

    public ArrayList<HomeBannerCollection> getBannerCollections() {
        return bannerCollections;
    }

    public ArrayList<HomeTopPoet> getTopPoets() {
        return topPoets;
    }

    public ArrayList<HomeOtherWorldOfTheDay> getOtherWorldOfTheDays() {
        return otherWorldOfTheDays;
    }

    public ArrayList<HomeVideo> getVideos() {
        return videos;
    }

    public ArrayList<HomeFeatured> getFeatureds() {
        return featureds;
    }

    public ArrayList<HomeTodayTop> getTodayTops() {
        return todayTops;
    }

    public ArrayList<HomeShayariImage> getShayariImages() {
        return shayariImages;
    }

//    public ArrayList<HomeT20Series> getT20Series() {
//        return t20Series;
//    }

    public ArrayList<HomeSherCollection> getSherCollections() {
        return sherCollections;
    }

    public ArrayList<HomeShayari> getShayaris() {
        return shayaris;
    }

    public ArrayList<HomeProseCollection> getProseCollections() {
        return proseCollections;
    }

    public ArrayList<HomeLookingMore> getLookingMores() {
        return lookingMores;
    }

    public HomeWordOfTheDay getWordOfTheDay() {
        return wordOfTheDay;
    }

    public HomeVideo getVideo() {
        return video;
    }

    public DidYouKnow getDidYouKnow() {
        return didYouKnow;
    }
}
