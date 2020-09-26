package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class PoetDetail extends BaseModel {
    private int nazmCount;
    private int sherCount;
    private int ghazalCount;
    private boolean isEngDescriptionAvailable,
            isHinDescriptionAvailable,
            isUrDescriptionAvailable;
    private String poetId;
    private String poetName;
    private String description;
    private String shortDescription;
    private String poetImage;
    private String deathPlace;
    private String birthPlace;
    private String additionalInfo;
    private String realName;
    private String penName;
    private String dateOfBirth;
    private String dateOfDeath;
    private String yearOfBirth;
    private String yearOfDeath;
    private String audioCount;
    private String videoCount;
    private String imageShayriCount;
    private String editorChoiceCount;
    private String seoSlug;
    private String dedicatedPageSlug;
    private PoetDetailContentHeader contentHeader;
    private ArrayList<ContentType> contentTypes;

    private JSONObject jsonObject;

    public PoetDetail(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        poetId = optString(jsonObject, "I");
        poetName = optString(jsonObject, "NE");
        shortDescription = optString(jsonObject, "SP");
        isEngDescriptionAvailable = optString(jsonObject, "HPE").contentEquals("true");
        isHinDescriptionAvailable = optString(jsonObject, "HPH").contentEquals("true");
        isUrDescriptionAvailable = optString(jsonObject, "HPU").contentEquals("true");
        description = optString(jsonObject, "PE");
        poetImage = getPoetId();
        additionalInfo = optString(jsonObject, "AI");
        birthPlace = optString(jsonObject, "BP");
        deathPlace = optString(jsonObject, "DP");
        realName = optString(jsonObject, "RN");
        yearOfBirth = optString(jsonObject, "DOB");
        yearOfDeath = optString(jsonObject, "DOD");
        dateOfBirth = optString(jsonObject, "EB");
        dateOfDeath = optString(jsonObject, "ED");
        audioCount = optString(jsonObject, "AC");
        videoCount = optString(jsonObject, "VC");
        imageShayriCount= optString(jsonObject,"ISC");
        editorChoiceCount = optString(jsonObject, "EC");
        penName = optString(jsonObject, "PN");
        seoSlug = optString(jsonObject, "S");
        dedicatedPageSlug = optString(jsonObject, "PS");
        /* dynamic keys added from poet_complete_profile*/
        nazmCount = MyHelper.convertToInt(optString(jsonObject, "nazmCount"));
        sherCount = MyHelper.convertToInt(optString(jsonObject, "sherCount"));
        ghazalCount = MyHelper.convertToInt(optString(jsonObject, "ghazalCount"));
        contentHeader = new PoetDetailContentHeader(jsonObject.optJSONObject("CH"));
        JSONArray countArray = jsonObject.optJSONArray("CS");
        loadContentTypes(countArray);

    }

    private void loadContentTypes(JSONArray countArray) {
        if (countArray == null)
            countArray = new JSONArray();
        contentTypes = new ArrayList<>(countArray.length());
        for (int i = 0; i < countArray.length(); i++) {
            String id = countArray.optJSONObject(i).optString("I");
            ContentType contentType = MyHelper.getContentById(id);
            if (!TextUtils.isEmpty(contentType.getContentId()))
                contentTypes.add(contentType);
        }
    }

    public String getPoetImage() {
        if (TextUtils.isEmpty(poetImage))
            return "";
        return String.format(Locale.getDefault(), MyConstants.getPoetImageUrlSmall(), poetImage);
    }

    public String getPoetImageLarge() {
        if (TextUtils.isEmpty(poetImage))
            return "";
        return String.format(Locale.getDefault(), MyConstants.getPoetImageUrlLarge(), poetImage);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getPoetTenure() {
        StringBuilder builder = new StringBuilder();
        builder.append(getDateOfBirth());
        if (!TextUtils.isEmpty(getDateOfDeath().trim())) {
            builder.append(" - ");
            builder.append(getDateOfDeath().trim());
        }

        return contentHeader.getPoetTenure();
    }

    public int getNazmCount() {
        return nazmCount;
    }

    public int getSherCount() {
        return sherCount;
    }

    public int getGhazalCount() {
        return ghazalCount;
    }

    public boolean isEngDescriptionAvailable() {
        return isEngDescriptionAvailable;
    }

    public boolean isHinDescriptionAvailable() {
        return isHinDescriptionAvailable;
    }

    public boolean isUrDescriptionAvailable() {
        return isUrDescriptionAvailable;
    }

    public String getPoetId() {
        return poetId;
    }

    public String getPoetName() {
        return contentHeader.getName();
    }
    public String getShortUrl(){
        return contentHeader.getShortUrl();
    }
    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return contentHeader.getShortDescription();
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getRealName() {
        return realName;
    }

    public String getPenName() {
        return penName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public String getYearOfDeath() {
        return yearOfDeath;
    }

    public String getAudioCount() {
        return audioCount;
    }

    public String getVideoCount() {
        return videoCount;
    }
    public String getImageShayriCount() {
        return imageShayriCount;
    }

    public String getEditorChoiceCount() {
        return editorChoiceCount;
    }

    public String getSeoSlug() {
        return seoSlug;
    }

    public String getDedicatedPageSlug() {
        return dedicatedPageSlug;
    }

    void setNazmCount(int nazmCount) {
        this.nazmCount = nazmCount;
        updateKey(jsonObject, "nazmCount", nazmCount);
    }

    void setSherCount(int sherCount) {
        this.sherCount = sherCount;
        updateKey(jsonObject, "sherCount", sherCount);
    }

    void setGhazalCount(int ghazalCount) {
        this.ghazalCount = ghazalCount;
        updateKey(jsonObject, "ghazalCount", ghazalCount);

    }

    public void setContentHeader(PoetDetailContentHeader contentHeader) {
        this.contentHeader = contentHeader;
        updateKey(jsonObject, "CH", contentHeader.getJsonObject());
    }

    public String getDomicile() {
        return contentHeader.getDomicile();
    }

    public void setCountArray(JSONArray countArray) {
        loadContentTypes(countArray);
        updateKey(jsonObject, "CS", countArray);
    }

    public ArrayList<ContentType> getContentTypes() {
        return contentTypes;
    }


}
