package com.example.sew.models;

import android.text.TextUtils;

import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchContentAll extends BaseModel {
    private ArrayList<SearchPoet> poets;
    private String PoetsTotal;
    private String GhazalsTotal;
    private String CoupletsTotal;
    private String NazmsTotal;
    //    private ArrayList<Content> content;
    private ArrayList<SearchContent> searchContents;
    private String ContentTotal;
    private ArrayList<SearchDictionary> dictionary;
    private ArrayList<String> tags;
    private String AppPages;
    private String Banners;
    private String T20;
    private String Message;

    public SearchContentAll(JSONObject jsonObject) {
        if (jsonObject == null)
            jsonObject = new JSONObject();
        JSONArray jsonArray = jsonObject.optJSONArray("Poets");
        int size = 0;
        if (jsonArray == null)
            jsonArray = new JSONArray();
        size = jsonArray.length();
        poets = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            poets.add(new SearchPoet(jsonArray.optJSONObject(i)));

        jsonArray = jsonObject.optJSONArray("Content");
        size = 0;
        if (jsonArray == null)
            jsonArray = new JSONArray();
        size = jsonArray.length();
        searchContents = new ArrayList<>(size);
        ArrayList<ContentType> allContentTypes=MyService.getAllContentType();
        for (int i = 0; i < size; i++) {
            // searchContents.add(new SearchContent(jsonArray.optJSONObject(i)));
            SearchContent searchContent= new SearchContent(jsonArray.optJSONObject(i));
            searchContents.add(searchContent);
            for (int j = 0; j < allContentTypes.size(); j++) {
                if (allContentTypes.get(j).getContentId().equalsIgnoreCase(searchContent.getTypeId()))
                    searchContent.setContentTitleColor(MyHelper.getTagColor(j));
            }
        }
        jsonArray = jsonObject.optJSONArray("Dictionary");
        size = 0;
        if (jsonArray == null)
            jsonArray = new JSONArray();
        size = jsonArray.length();
        dictionary = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            dictionary.add(new SearchDictionary(jsonArray.optJSONObject(i)));

        jsonArray = jsonObject.optJSONArray("Tags");
        size = 0;
        if (jsonArray == null)
            jsonArray = new JSONArray();
        size = jsonArray.length();
        tags = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            tags.add(jsonArray.optString(i));

        PoetsTotal = optString(jsonObject, "PoetsTotal");
        GhazalsTotal = optString(jsonObject, "GhazalsTotal");
        CoupletsTotal = optString(jsonObject, "CoupletsTotal");
        NazmsTotal = optString(jsonObject, "NazmsTotal");
        ContentTotal = optString(jsonObject, "ContentTotal");
        AppPages = optString(jsonObject, "AppPages");
        Banners = optString(jsonObject, "Banners");
        T20 = optString(jsonObject, "T20");
        Message = optString(jsonObject, "Message");
    }

    public ArrayList<SearchPoet> getPoets() {
        return poets;
    }

    public String getPoetsTotal() {
        return PoetsTotal;
    }

    public String getGhazalsTotal() {
        return GhazalsTotal;
    }

    public String getCoupletsTotal() {
        return CoupletsTotal;
    }

    public String getNazmsTotal() {
        return NazmsTotal;
    }

    public ArrayList<SearchContent> getSearchContents() {
        return searchContents;
    }

    public String getContentTotal() {
        return ContentTotal;
    }

    public ArrayList<SearchDictionary> getDictionary() {
        return dictionary;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getAppPages() {
        return AppPages;
    }

    public String getBanners() {
        return Banners;
    }

    public String getT20() {
        return T20;
    }

    public String getMessage() {
        return Message;
    }

    public class SearchPoet {
        private String EntityId;
        private String Name;
        private String GazalCount;
        private String IsNew;
        private String NazmCount;
        private String SEO_Slug;
        private String ImageUrl;
        private String fromDate;
        private String toDate;
        private String enitiyYearRange;
        private String proseCount;
        private String poetryCount;
        private String shortUrlIndex;

        public SearchPoet(JSONObject jsonObject) {
            if (jsonObject == null)
                jsonObject = new JSONObject();
            EntityId = optString(jsonObject, "EntityId");
            Name = optString(jsonObject, "Name");
            GazalCount = optString(jsonObject, "GazalCount");
            IsNew = optString(jsonObject, "IsNew");
            NazmCount = optString(jsonObject, "NazmCount");
            SEO_Slug = optString(jsonObject, "SEO_Slug");
            ImageUrl = optString(jsonObject, "ImageUrl");
            fromDate = optString(jsonObject, "FromDate");
            toDate = optString(jsonObject, "ToDate");
            enitiyYearRange = optString(jsonObject, "EntityYearRange");
            proseCount = optString(jsonObject, "ProseCount");
            poetryCount = optString(jsonObject, "PoetryCount");
            shortUrlIndex = optString(jsonObject, "ShortUrlIndex");
        }

        public String getEntityId() {
            return EntityId;
        }

        public String getName() {
            return Name;
        }

        public String getGazalCount() {
            return GazalCount;
        }

        public String getIsNew() {
            return IsNew;
        }

        public String getNazmCount() {
            return NazmCount;
        }

        public String getSEO_Slug() {
            return SEO_Slug;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        private String getFromDate() {
            return fromDate;
        }

        private String getToDate() {
            return toDate;
        }

        public String getPoetTenure() {
            StringBuilder builder = new StringBuilder();
            builder.append(getFromDate());
            if (!TextUtils.isEmpty(getToDate().trim())) {
                builder.append(" - ");
                builder.append(getToDate().trim());
            }

            return builder.toString();
        }

        public String getEnitiyYearRange() {
            return enitiyYearRange;
        }

        public String getProseCount() {
            return proseCount;
        }

        public String getPoetryCount() {
            return poetryCount;
        }

        public String getShortUrlIndex() {
            return shortUrlIndex;
        }
    }
}
