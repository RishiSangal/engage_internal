package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

public class ContentPoet extends BaseModel {
    private String DedicatedPageSlug;
    private String HaveLargeImage;
    private String ImageUrl;
    private String Name;
    private String Slug;
    private String poetID;

    private String shortDescription;
    private String yearOfBirth;
    private String yearOfDeath;
    private String birthPlace;
    private String pD;
    private String audioCount;
    private String videoCount;

    public ContentPoet(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        if (jsonObject.has("CS"))
            this.Slug = optString(jsonObject, "CS");
        else
            this.Slug = optString(jsonObject, "PS");
        this.DedicatedPageSlug = optString(jsonObject, "DS");
        this.Name = optString(jsonObject, "PN");
        this.ImageUrl = optString(jsonObject, "IU");
        this.HaveLargeImage = optString(jsonObject, "LI");
        if (jsonObject.has("PI"))
            this.poetID = optString(jsonObject, "PI");
        else
            this.poetID = optString(jsonObject, "I");
        shortDescription = optString(jsonObject, "DE");
        yearOfBirth = optString(jsonObject, "FD");
        yearOfDeath = optString(jsonObject, "TD");
        birthPlace = optString(jsonObject, "BP");
        pD = optString(jsonObject, "PD");
        audioCount = optString(jsonObject, "AC");
        videoCount = optString(jsonObject, "VC");
    }

    public String getSlug() {
        return this.Slug;
    }

    public String getDedicatedPageSlug() {
        return this.DedicatedPageSlug;
    }

    public String getName() {
        return this.Name;
    }

    public String getImageUrl() {
        return String.format("%s%s", ImageUrl.startsWith("http") ? "" : MyService.getMediaURL(), this.ImageUrl);
    }

    public String getPoetTenure() {

        if(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU){
            StringBuilder builder = new StringBuilder();
            builder.append(getYearOfBirth());
            if (!TextUtils.isEmpty(getYearOfDeath().trim())) {
                builder.append(" - ");
                builder.append(getYearOfDeath().trim());
            }
            String[] split = builder.toString().split(" ");
            String result = "";
            for (int i = split.length - 1; i >= 0; i--) {
                result += (split[i] + " ");
            }

            StringBuilder builder1 = new StringBuilder();
            builder1.append(result.trim());
//            if(!TextUtils.isEmpty(builder1))
//                builder1.append("   |   ");
//            builder1.append(getpD());
            return builder1.toString();
        }else{
            StringBuilder builder = new StringBuilder();
            builder.append(getYearOfBirth());
            if (!TextUtils.isEmpty(getYearOfDeath().trim())) {
                builder.append(" - ");
                builder.append(getYearOfDeath().trim());
            }
//            if (!TextUtils.isEmpty(getpD())) {
//                builder.append(" | ");
//                builder.append(getpD());
//            }
            return builder.toString();
        }


//        StringBuilder builder = new StringBuilder();
//        builder.append(getYearOfBirth());
//        if (!TextUtils.isEmpty(getYearOfDeath().trim())) {
//            builder.append(" - ");
//            builder.append(getYearOfDeath().trim());
//        }
//        if (!TextUtils.isEmpty(getBirthPlace())) {
//            builder.append(" | ");
//            builder.append(getBirthPlace());
//        }

//        return builder.toString();
    }
    public String getPoetPlace() {
        return this.pD= getpD();
    }
    public String getHaveLargeImage() {
        return this.HaveLargeImage;
    }

    public String getPoetID() {
        return poetID;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public String getYearOfDeath() {
        return yearOfDeath;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getpD() {
        return pD;
    }

    public String getAudioCount() {
        return audioCount;
    }

    public String getVideoCount() {
        return videoCount;
    }
}
