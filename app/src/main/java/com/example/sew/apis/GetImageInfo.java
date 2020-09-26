package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.models.Poet;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class GetImageInfo extends Base {


    public GetImageInfo() {
        setUrl(MyConstants.getGetImageInfo());
        setRequestType(REQUEST_TYPE.POST);
        setLang("1");
        setImageId("D46EA583-8B6D-4883-AB4C-EAE532C9D1DF");
    }

    public GetImageInfo setImageId(String imageId) {
        addParam("shayariIngId", imageId);
        return this;
    }

    public GetImageInfo setLang(String lang) {
        addParam("lang", lang);
        return this;
    }

    private int totalCount;
    private ArrayList<Poet> poets = new ArrayList<>();

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            totalCount = getData().optInt("TC");
            JSONArray poetsArray = getData().optJSONArray("P");
            if (poetsArray == null)
                poetsArray = new JSONArray();
            poets = new ArrayList<>(poetsArray.length());
            for (int i = 0; i < poetsArray.length(); i++)
                poets.add(new Poet(poetsArray.optJSONObject(i)));
        }
    }

    @Override
    public void onError(Call<ResponseBody> requestBodyCall, Throwable t) {
        super.onError(requestBodyCall, t);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public ArrayList<Poet> getPoets() {
        return poets;
    }
}
