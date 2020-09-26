package com.example.sew.models;

import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class CountSummary {
    JSONObject jsonObject;
    private String id, favCount, shareCount;
    private boolean originalIsFav;
    public CountSummary(JSONObject jsonObject) {
        if(jsonObject==null)
            jsonObject= new JSONObject();
        this.jsonObject= jsonObject;
        id = jsonObject.optString("I");
        favCount = jsonObject.optString("FC");
        shareCount = jsonObject.optString("SC");
        this.originalIsFav = MyService.isFavorite(getId());
    }
    public String getId() {
        return id;
    }

    public String getFavCount() {
        String updatedFavCount = favCount;
        if (isFavoriteCountANumber()) {
            try {
                if (!(originalIsFav == MyService.isFavorite(getId()))) {
                    if (originalIsFav)
                        updatedFavCount = String.valueOf(Integer.parseInt(favCount) - 1);
                    else
                        updatedFavCount = String.valueOf(Integer.parseInt(favCount) + 1);
                }
            } catch (NumberFormatException e) {
                updatedFavCount = favCount;
            }
        }
        return updatedFavCount;
    }
    private boolean isFavoriteCountANumber() {
        try {
            Integer.parseInt(favCount);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public String getShareCount() {
        return shareCount;
    }
}
