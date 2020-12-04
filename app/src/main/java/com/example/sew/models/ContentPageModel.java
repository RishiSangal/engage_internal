package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentPageModel extends BaseModel {
    private String AltSlug;
    private String AltTypeSlug;
    private String FootNote;
    private String HaveEn;
    private String HaveHi;
    private boolean HaveTranslation;
    private String HaveUr;
    private String Id;
    private boolean IsEditorChoice;
    private boolean IsPopularChoice;
    private String NotFound;
    private String ParentSlug;
    private String ParentTitle;
    private String ParentTypeSlug;
    private RenderContent Render;
    private RenderContent RenderFirstPara;
    private String RenderingAlignment;
    private String RenderingFormat;
    private String ShortId;
    private String Slug;
    private String SubTitle;
    private String Title;
    private String TranslatorDedicatedPageSlug;
    private String TranslatorName;
    private String TranslatorSlug;
    private String TypeSlug;
    private String UrlUrdu;
    private String UrlHindi;
    private String UrlEnglish;
    private ArrayList<RenderContentAudio> contentAudios;
    private ArrayList<RenderContentVideo> contentVideos;
    private ArrayList<RenderContentTag> contentTags;
    private ContentPoet poet;
    private boolean isHTML;
    private String htmlOrJsonRenderContent;
    private String favCount;
    private boolean originalIsFav;
    private String contentTyeName;
    private boolean haveFactEng;
    private boolean haveFactHin;
    private boolean haveFactUrdu;

    public ContentPageModel(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();

        this.UrlUrdu = optString(jsonObject, "UU");
        this.UrlHindi = optString(jsonObject, "UH");
        this.UrlEnglish = optString(jsonObject, "UE");
        this.NotFound = optString(jsonObject, "NF");
        this.AltSlug = optString(jsonObject, "AS");
        this.AltTypeSlug = optString(jsonObject, "ATS");
        this.IsEditorChoice = optString(jsonObject, "EC").contentEquals("true");
        this.IsPopularChoice = optString(jsonObject, "PC").contentEquals("true");
        this.Id = optString(jsonObject, "I");
        this.ShortId = optString(jsonObject, "SI");
        this.Slug = optString(jsonObject, "CS");
        this.Title = optString(jsonObject, "CT");
        this.SubTitle = optString(jsonObject, "ST");
        this.FootNote = optString(jsonObject, "FT");
        this.HaveEn = optString(jsonObject, "HN");
        this.HaveHi = optString(jsonObject, "HH");
        this.HaveUr = optString(jsonObject, "HU");
        this.HaveTranslation = optString(jsonObject, "HT").contentEquals("true");
        this.RenderingFormat = optString(jsonObject, "RF");
        this.RenderingAlignment = optString(jsonObject, "RA");
        isHTML = optString(jsonObject, "IH").contentEquals("true");
        this.favCount = optString(jsonObject, "FC");
        this.TypeSlug = optString(jsonObject, "TS");
        this.haveFactEng= optString(jsonObject, "HFE").contentEquals("true");
        this.haveFactHin= optString(jsonObject, "HFH").contentEquals("true");
        this.haveFactUrdu= optString(jsonObject, "HFU").contentEquals("true");
        this.contentTyeName = optString(jsonObject, "CTN");
        this.originalIsFav = MyService.isFavorite(getId());
        htmlOrJsonRenderContent = jsonObject.optString("CR");
        JSONObject jSONObject = null;
        try {
            jSONObject = new JSONObject(htmlOrJsonRenderContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        this.Render = new RenderContent(jSONObject);
        JSONObject jSONObject2 = null;
        try {
            jSONObject2 = new JSONObject(jsonObject.optString("RFP"));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        if (jSONObject2 == null) {
            jSONObject2 = new JSONObject();
        }
        this.RenderFirstPara = new RenderContent(jSONObject2);
        this.TranslatorSlug = optString(jsonObject, "CTS");
        this.TranslatorDedicatedPageSlug = optString(jsonObject, "TD");
        this.TranslatorName = optString(jsonObject, "TN");
        this.ParentTitle = optString(jsonObject, "PT");
        this.ParentSlug = optString(jsonObject, "PS");
        this.ParentTypeSlug = optString(jsonObject, "PTS");
        this.poet = new ContentPoet(jsonObject.optJSONObject("Poet"));
        JSONArray jsonArray = jsonObject.optJSONArray("Audios");
        if (jsonArray == null)
            jsonArray = new JSONArray();
        int size = jsonArray.length();
        contentAudios = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            contentAudios.add(new RenderContentAudio(jsonArray.optJSONObject(i)));
        jsonArray = jsonObject.optJSONArray("Videos");
        if (jsonArray == null)
            jsonArray = new JSONArray();
        size = jsonArray.length();
        contentVideos = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            contentVideos.add(new RenderContentVideo(jsonArray.optJSONObject(i)));

        jsonArray = jsonObject.optJSONArray("Tags");
        if (jsonArray == null)
            jsonArray = new JSONArray();
        size = jsonArray.length();
        contentTags = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            contentTags.add(new RenderContentTag(jsonArray.optJSONObject(i)));
    }

    public boolean isFavoriteCountANumber() {
        try {
            Integer.parseInt(favCount);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public ArrayList<RenderContentVideo> getContentVideos() {
        return contentVideos;
    }

    public ArrayList<RenderContentAudio> getContentAudios() {
        return contentAudios;
    }

    public void setHaveEn(String haveEn) {
        HaveEn = haveEn;
    }

    public void setHaveHi(String haveHi) {
        HaveHi = haveHi;
    }

    public void setHaveUr(String haveUr) {
        HaveUr = haveUr;
    }

    public String getUrlUrdu() {
        return UrlUrdu;
    }

    public String getUrlHindi() {
        return UrlHindi;
    }

    public String getUrlEnglish() {
        return UrlEnglish;
    }

    public String getUrl() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getUrlEnglish();
            case HINDI:
                return getUrlHindi();
            case URDU:
                return getUrlUrdu();
        }
        return UrlEnglish;
    }

    public String getNotFound() {
        return this.NotFound;
    }

    public String getAltSlug() {
        return this.AltSlug;
    }

    public String getAltTypeSlug() {
        return this.AltTypeSlug;
    }

    public boolean isEditorChoice() {
        return this.IsEditorChoice;
    }

    public boolean isPopularChoice() {
        return this.IsPopularChoice;
    }

    public String getId() {
        return this.Id;
    }

    public String getShortId() {
        return this.ShortId;
    }

    public String getSlug() {
        return this.Slug;
    }

    public String getTitle() {
        return this.Title;
    }

    public String getSubTitle() {
        return this.SubTitle;
    }

    public String getFootNote() {
        return this.FootNote;
    }

    public String getHaveEn() {
        return this.HaveEn;
    }

    public String getHaveHi() {
        return this.HaveHi;
    }

    public String getHaveUr() {
        return this.HaveUr;
    }

    public boolean isHaveTranslation() {
        return this.HaveTranslation;
    }

    public String getRenderingFormat() {
        return this.RenderingFormat;
    }

    public String getRenderingAlignment() {
        return this.RenderingAlignment;
    }

    public Enums.TEXT_ALIGNMENT getAlignment() {
        return (getRenderingAlignment().contentEquals("1") || getRenderingAlignment().contentEquals("1.0")) ? Enums.TEXT_ALIGNMENT.CENTER : Enums.TEXT_ALIGNMENT.LEFT;
//        return Enums.TEXT_ALIGNMENT.CENTER;
    }

    public String getTypeSlug() {
        return this.TypeSlug;
    }

    public RenderContent getRender() {
        return this.Render;
    }

    public RenderContent getRenderFirstPara() {
        return this.RenderFirstPara;
    }

    public String getTranslatorSlug() {
        return this.TranslatorSlug;
    }

    public String getTranslatorDedicatedPageSlug() {
        return this.TranslatorDedicatedPageSlug;
    }

    public String getTranslatorName() {
        return this.TranslatorName;
    }

    public String getParentTitle() {
        return this.ParentTitle;
    }

    public String getParentSlug() {
        return this.ParentSlug;
    }

    public String getParentTypeSlug() {
        return this.ParentTypeSlug;
    }

    public boolean isHTML() {
        return isHTML;
    }

    public String getHtmlOrJsonRenderContent() {
        return htmlOrJsonRenderContent;
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

    public ContentType getContentType() {
        return MyHelper.getContentBySlug(getTypeSlug());
//        if (getTypeSlug().equalsIgnoreCase("nazm") || getTypeSlug().equalsIgnoreCase("nazms"))
//            return Enums.CONTENT_TYPE.NAZM;
//        else if (getTypeSlug().equalsIgnoreCase("ghazals"))
//            return Enums.CONTENT_TYPE.GHAZAL;
//        else if (getTypeSlug().equalsIgnoreCase("geet"))
//            return Enums.CONTENT_TYPE.GEET;
//        else if (getTypeSlug().equalsIgnoreCase("rubaai"))
//            return Enums.CONTENT_TYPE.RUBAAI;
//        else if (getTypeSlug().equalsIgnoreCase("qita"))
//            return Enums.CONTENT_TYPE.QITA;
//        else if (getTypeSlug().equalsIgnoreCase("dohe"))
//            return Enums.CONTENT_TYPE.DOHA;
//        else
//            return Enums.CONTENT_TYPE.SHER;//couplets
    }

    public ArrayList<RenderContentTag> getContentTags() {
        return contentTags;
    }

    public ContentPoet getPoet() {
        return this.poet;
    }

    public String getContentTyeName() {
        return contentTyeName;
    }

    public boolean isHaveFactEng() {
        return haveFactEng;
    }

    public boolean isHaveFactHin() {
        return haveFactHin;
    }

    public boolean isHaveFactUrdu() {
        return haveFactUrdu;
    }
}
