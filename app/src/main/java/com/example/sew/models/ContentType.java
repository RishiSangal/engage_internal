package com.example.sew.models;

import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONObject;

import static com.example.sew.common.MyConstants.GHAZAL_ID;
import static com.example.sew.common.MyConstants.NAZM_ID;
import static com.example.sew.common.MyConstants.SHER_ID;

public class ContentType extends BaseModel implements Comparable<ContentType> {

    private String contentId;
    private String nameEng;
    private String nameHin;
    private String nameUr;
    private String slug;
    private String contentListType;
    private String contentCompositionType;
    private String sequence = "";
    private int contentCount;
    JSONObject jsonObject;

    public ContentType(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
        if (jsonObject.has("TI"))
            contentId = optString(jsonObject, "TI");
        else
            contentId = optString(jsonObject, "I");
        nameEng = optString(jsonObject, "NE");
        nameHin = optString(jsonObject, "NH");
        nameUr = optString(jsonObject, "NU");
        if (jsonObject.has("TS"))
            slug = optString(jsonObject, "TS");
        else
            slug = optString(jsonObject, "SS");
        contentCount = MyHelper.convertToInt(optString(jsonObject, "C"));
        contentListType = optString(jsonObject, "LT");
        contentCompositionType = optString(jsonObject, "CT");
        sequence = optString(jsonObject, "S") + optString(jsonObject, "NE");
//        if (jsonObject.has("CS"))
//            sequence = MyHelper.convertToInt(optString(jsonObject, "CS"));
//        else {
//            ArrayList<ContentType> customContentTypes = MyHelper.getContentTypes();
//            for (ContentType currContentType : customContentTypes) {
//                if (currContentType.getContentId().equalsIgnoreCase(contentId))
//                    sequence = currContentType.getSequence();
//            }
//            if (sequence == 0)
//                sequence = contentId.hashCode();
//        }
    }

    @Override
    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getNameEng() {
        return nameEng;
    }

    public String getName() {
        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return getNameEng();
            case HINDI:
                return getNameHin();
            case URDU:
                return getNameUr();
        }
        return nameEng;
    }

    public String getNameHin() {
        return nameHin;
    }

    public String getNameUr() {
        return nameUr;
    }

    public String getContentId() {
        return contentId;
    }

    public String getSlug() {
        return slug;
    }

    /**
     * 1=Title <br>2=Full
     */
    public String getContentListType() {
        return contentListType;
    }

    /**
     * 1=Poetry <br>2=Prose
     */
    public String getContentCompositionType() {
        return contentCompositionType;
    }

    public Enums.LIST_RENDERING_FORMAT getListRenderingFormat() {
        if (getContentListType().contentEquals("1")) {
            if (NAZM_ID.contentEquals(getContentId()))
                return Enums.LIST_RENDERING_FORMAT.NAZM;
            else
                return Enums.LIST_RENDERING_FORMAT.GHAZAL;
        } else if (getContentListType().contentEquals("2")) {
            if (getContentCompositionType().contentEquals("2"))
                return Enums.LIST_RENDERING_FORMAT.QUOTE;
            else
                return Enums.LIST_RENDERING_FORMAT.SHER;
        } else if (getContentListType().contentEquals(MyConstants.TMP_POET_PROFILE_ID))
            return Enums.LIST_RENDERING_FORMAT.PROFILE;
        else if (getContentListType().contentEquals(MyConstants.TMP_AUDIO_ID))
            return Enums.LIST_RENDERING_FORMAT.AUDIO;
        else if (getContentListType().contentEquals(MyConstants.TMP_VIDEO_ID))
            return Enums.LIST_RENDERING_FORMAT.VIDEO;
        else if (getContentListType().contentEquals(MyConstants.TMP_IMAGE_SHAYRI_ID))
            return Enums.LIST_RENDERING_FORMAT.IMAGE_SHAYRI;
        return Enums.LIST_RENDERING_FORMAT.NAZM;
    }

    public void setContentListType(String contentListType) {
        this.contentListType = contentListType;
        updateKey(jsonObject, "LT", contentListType);
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
        updateKey(jsonObject, "NE", nameEng);
    }

    public void setNameHin(String nameHin) {
        this.nameHin = nameHin;
        updateKey(jsonObject, "NH", nameHin);
    }

    public void setNameUr(String nameUr) {
        this.nameUr = nameUr;
        updateKey(jsonObject, "NU", nameUr);
    }

    public int getContentCount() {
        return contentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !(o instanceof ContentType))
            return false;
        ContentType contentType = (ContentType) o;
        return getContentId().equalsIgnoreCase(contentType.getContentId());
    }

//    @Override
//    public int hashCode() {
//        return jsonObject.hashCode();
//    }

    public String getSequence() {
        return sequence;
    }

    @Override
    public int compareTo(ContentType o) {
        return (getSequence().compareTo(o.getSequence()));//>= 0 ? 1 : -1;
    }

    public int getPriority() {
        if (getContentId().equalsIgnoreCase(GHAZAL_ID))
            return 0;
        if (getContentId().equalsIgnoreCase(NAZM_ID))
            return 1;
        if (getContentId().equalsIgnoreCase(SHER_ID))
            return 2;
        return hashCode();
    }
}
