package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.common.Enums;
import com.example.sew.common.Utils;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONException;
import org.json.JSONObject;

public class FavContentPageModel extends BaseModel {
    /*
    "FD":"2019-06-23T15:04:05.8",
"FL":1,
"FM":null,
"FS":null,
"P":null,
"R":"ر ,اب ٹوٹنے ہی والا ہے تنہائی کا حصار",
"S":"ab-tuutne-hii-vaalaa-hai-tanhaaii-kaa-hisaar-adil-mansuri-ghazals",
"SI":3094,
"N":false,
"AU":null,
"VI":null,
"AC":null,
"VC":null,
"FC":null,
"SC":null
     */
    private String HaveEn;
    private String HaveHi;
    private String HaveUr;
    private String Id;
    private boolean IsEditorChoice;
    private boolean IsPopularChoice;
    private String TypeSlug;
    private String UrlUrdu;
    private String UrlHindi;
    private String UrlEnglish,
            titleEng,
            titleHin,
            titleUrdu,
            subtitleEng,
            subtitleHin,
            subtitleUrdu,
            poetID,
            poetNameEng,
            poetNameHin,
            poetUrdu,
            contentTypeId,
            RenderingAlignment,
            bodyEng,
            bodyHin,
            bodyUrdu,
            interestingFactEn,
            interestingFactHin,
            interestingFactUrdu;
    private RenderContent renderContentEn,
            renderContentHi,
            renderContentUrdu;
    private boolean isHTML, haveFactEng, haveFactHin, haveFactUrdu;

    private String htmlOrJsonRenderContentEn, htmlOrJsonRenderContentHi, htmlOrJsonRenderContentUrdu, FD;
    JSONObject jsonObject;

    public FavContentPageModel(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        this.FD = optString(jsonObject, "FD");
        this.UrlUrdu = optString(jsonObject, "UU");
        this.UrlHindi = optString(jsonObject, "UH");
        this.UrlEnglish = optString(jsonObject, "UE");

        this.titleEng = optString(jsonObject, "TE");
        this.titleHin = optString(jsonObject, "TH");
        this.titleUrdu = optString(jsonObject, "TU");

        this.bodyEng = optString(jsonObject, "BE");
        this.bodyHin = optString(jsonObject, "BH");
        this.bodyUrdu = optString(jsonObject, "BU");

        this.subtitleEng = optString(jsonObject, "SE");
        this.subtitleHin = optString(jsonObject, "SH");
        this.subtitleUrdu = optString(jsonObject, "SU");

        this.poetID = optString(jsonObject, "PI");
        this.poetNameEng = optString(jsonObject, "PE");
        this.poetNameHin = optString(jsonObject, "PH");
        this.poetUrdu = optString(jsonObject, "PU");
        this.contentTypeId = optString(jsonObject, "T");
        this.IsEditorChoice = optString(jsonObject, "EC").contentEquals("true");
        this.IsPopularChoice = optString(jsonObject, "PC").contentEquals("true");
        this.Id = optString(jsonObject, "I");
        this.HaveEn = optString(jsonObject, "HE");
        this.HaveHi = optString(jsonObject, "HH");
        this.HaveUr = optString(jsonObject, "HU");

        this.interestingFactEn = optString(jsonObject, "FTE");
        this.interestingFactHin = optString(jsonObject, "FTH");
        this.interestingFactUrdu = optString(jsonObject, "FTU");
        this.haveFactEng = optString(jsonObject, "HFE").contentEquals("true");
        this.haveFactHin = optString(jsonObject, "HFH").contentEquals("true");
        this.haveFactUrdu = optString(jsonObject, "HFU").contentEquals("true");
        this.RenderingAlignment = optString(jsonObject, "A");
        isHTML = optString(jsonObject, "IH").contentEquals("true");
        this.TypeSlug = optString(jsonObject, "S");
        htmlOrJsonRenderContentEn = jsonObject.optString("RE");
        JSONObject jSONObject = null;
        try {
            if (!htmlOrJsonRenderContentEn.isEmpty() && !htmlOrJsonRenderContentEn.equalsIgnoreCase("null"))
                jSONObject = new JSONObject(htmlOrJsonRenderContentEn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        this.renderContentEn = new RenderContent(jSONObject);
        htmlOrJsonRenderContentHi = jsonObject.optString("RH");
        jSONObject = null;
        try {
            if (!htmlOrJsonRenderContentHi.isEmpty() && !htmlOrJsonRenderContentHi.equalsIgnoreCase("null"))
                jSONObject = new JSONObject(htmlOrJsonRenderContentHi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        this.renderContentHi = new RenderContent(jSONObject);
        htmlOrJsonRenderContentUrdu = jsonObject.optString("RU");
        jSONObject = null;
        try {
            if (!htmlOrJsonRenderContentUrdu.isEmpty() && !htmlOrJsonRenderContentUrdu.equalsIgnoreCase("null"))
                jSONObject = new JSONObject(htmlOrJsonRenderContentUrdu);
            else
                jSONObject = new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        this.renderContentUrdu = new RenderContent(jSONObject);


//        JSONObject jSONObject = null;
//        try {
//            jSONObject = new JSONObject(jsonObject.optString("RE"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (jSONObject == null) {
//            jSONObject = new JSONObject();
//        }
//        this.renderContentEn = new RenderContent(jSONObject);
//        jSONObject = null;
//        try {
//            jSONObject = new JSONObject(jsonObject.optString("RH"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (jSONObject == null) {
//            jSONObject = new JSONObject();
//        }
//        this.renderContentHi = new RenderContent(jSONObject);
//        jSONObject = null;
//        try {
//            jSONObject = new JSONObject(jsonObject.optString("RU"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (jSONObject == null) {
//            jSONObject = new JSONObject();
//        }
//        this.renderContentUrdu = new RenderContent(jSONObject);
    }

    public String getFD() {
        if (FD.isEmpty())
            return Utils.getCurrentFM();
        else
            return FD;
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

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
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

    public String getTitle() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return !TextUtils.isEmpty(getTitleEng()) ? getTitleEng() : getAnyNonEmptyTitle();
            case HINDI:
                return !TextUtils.isEmpty(getTitleHin()) ? getTitleHin() : getAnyNonEmptyTitle();
            case URDU:
                return !TextUtils.isEmpty(getTitleUrdu()) ? getTitleUrdu() : getAnyNonEmptyTitle();
        }
        return titleEng;
    }

    public String getBody() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return !TextUtils.isEmpty(getBodyEng()) ? getBodyEng() : getAnyNonEmptyBody();
            case HINDI:
                return !TextUtils.isEmpty(getBodyHin()) ? getBodyHin() : getAnyNonEmptyBody();
            case URDU:
                return !TextUtils.isEmpty(getBodyUrdu()) ? getBodyUrdu() : getAnyNonEmptyBody();
        }
        return bodyEng;
    }

    public boolean isHTML() {
        return isHTML;
    }

    private String getAnyNonEmptyTitle() {
        return !TextUtils.isEmpty(getTitleEng()) ? getTitleEng() : !TextUtils.isEmpty(getTitleHin()) ? getTitleHin() : getTitleUrdu();
    }

    private String getAnyNonEmptyBody() {
        return !TextUtils.isEmpty(getBodyEng()) ? getBodyEng() : !TextUtils.isEmpty(getBodyHin()) ? getBodyHin() : getBodyUrdu();
    }

    public String getSubTitle() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getSubtitleEng();
            case HINDI:
                return getSubtitleHin();
            case URDU:
                return getSubtitleUrdu();
        }
        return subtitleEng;
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

    public ContentType getContentType() {
        return MyHelper.getContentById(getContentTypeId());
    }

    public String getTitleEng() {
        return titleEng;
    }

    public String getTitleHin() {
        return titleHin;
    }

    public String getTitleUrdu() {
        return titleUrdu;
    }

    public String getBodyEng() {
        return bodyEng;
    }

    public String getBodyHin() {
        return bodyHin;
    }

    public String getBodyUrdu() {
        return bodyUrdu;
    }

    public String getSubtitleEng() {
        return subtitleEng;
    }

    public String getSubtitleHin() {
        return subtitleHin;
    }

    public String getSubtitleUrdu() {
        return subtitleUrdu;
    }

    public String getPoetID() {
        return poetID;
    }

    public String getPoetName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getPoetNameEng();
            case HINDI:
                return getPoetNameHin();
            case URDU:
                return getPoetUrdu();
        }
        return poetNameEng;
    }

    public String getPoetNameEng() {
        return poetNameEng;
    }

    public String getPoetNameHin() {
        return poetNameHin;
    }

    public String getPoetUrdu() {
        return poetUrdu;
    }

    public String getContentTypeId() {
        return contentTypeId;
    }

    public RenderContent getRenderContentEn() {
        return renderContentEn;
    }

    public RenderContent getRenderContent() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getRenderContentEn();
            case HINDI:
                return getRenderContentHi();
            case URDU:
                return getRenderContentUrdu();
        }
        return getRenderContentEn();
    }

    public String getJsonOrHtmlContent() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getHtmlOrJsonRenderContentEn();
            case HINDI:
                return getHtmlOrJsonRenderContentHi();
            case URDU:
                return getHtmlOrJsonRenderContentUrdu();
        }
        return poetNameEng;
    }

    public RenderContent getRenderContentHi() {
        return renderContentHi;
    }

    public RenderContent getRenderContentUrdu() {
        return renderContentUrdu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !(o instanceof FavContentPageModel))
            return false;
        FavContentPageModel favContentPageModel = (FavContentPageModel) o;
        return getId().equalsIgnoreCase(favContentPageModel.getId());
    }

    @Override
    public int hashCode() {
        return getId().toUpperCase().hashCode();
    }

    public String getHtmlOrJsonRenderContentEn() {
        return htmlOrJsonRenderContentEn;
    }

    public String getHtmlOrJsonRenderContentHi() {
        return htmlOrJsonRenderContentHi;
    }

    public String getHtmlOrJsonRenderContentUrdu() {
        return htmlOrJsonRenderContentUrdu;
    }

    public String getInterestingFactEn() {
        return interestingFactEn;
    }

    public String getInterestingFactHin() {
        return interestingFactHin;
    }

    public String getInterestingFactUrdu() {
        return interestingFactUrdu;
    }

    public boolean isHaveFactUrdu() {
        return haveFactUrdu;
    }

    public boolean isHaveFactHin() {
        return haveFactHin;
    }

    public boolean isHaveFactEng() {
        return haveFactEng;
    }
}
