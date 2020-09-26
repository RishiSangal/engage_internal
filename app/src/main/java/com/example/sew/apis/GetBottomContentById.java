package com.example.sew.apis;

import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentPoet;
import com.example.sew.models.KeepReading;
import com.example.sew.models.MaybeLike;
import com.example.sew.models.PluralContentName;
import com.example.sew.models.PreviousNextContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetBottomContentById extends Base {


    public GetBottomContentById() {
        setUrl(MyConstants.getGetBottomContentByIdSlug());
        setRequestType(REQUEST_TYPE.GET);
        addParam("lang", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    public GetBottomContentById setContentId(String contentId) {
        addParam("contentId", contentId);
        return this;
    }
    public GetBottomContentById setListSlug(String listSlug) {
        addParam("listSlug", listSlug);
        return this;
    }



    private ContentPoet contentPoet;
    private PreviousNextContent nextContent;
    private PreviousNextContent previousContent;
    private ArrayList<MaybeLike> maybeLikes;
    private ArrayList<KeepReading> keepReadings;
    private PluralContentName pluralContentNames;

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        JSONObject data = getData();
        if (isValidResponse()) {
            contentPoet = new ContentPoet(data.optJSONObject("PoetDetail"));
            JSONArray jsonArray = data.optJSONArray("nextPrevContent");
            if (jsonArray == null)
                jsonArray = new JSONArray();
            int size = jsonArray.length();
            for (int i = 0; i < size; i++) {
                PreviousNextContent previousNextContent = new PreviousNextContent(jsonArray.optJSONObject(i));
                if (previousNextContent.isNext())
                    nextContent = previousNextContent;
                else
                    previousContent = previousNextContent;
            }
            jsonArray = data.optJSONArray("mayBeLike");
            if (jsonArray == null)
                jsonArray = new JSONArray();
            size = jsonArray.length();
            maybeLikes = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                maybeLikes.add(new MaybeLike(jsonArray.optJSONObject(i)));

            jsonArray = data.optJSONArray("keepReading");
            if (jsonArray == null)
                jsonArray = new JSONArray();
            size = jsonArray.length();
            keepReadings = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                keepReadings.add(new KeepReading(jsonArray.optJSONObject(i)));
            pluralContentNames= new PluralContentName(data.optJSONObject("pluralContent"));

        }
    }

    public ContentPoet getContentPoet() {
        return contentPoet;
    }

    public PreviousNextContent getPreviousContent() {
        return previousContent;
    }

    public PreviousNextContent getNextContent() {
        return nextContent;
    }

    public ArrayList<MaybeLike> getMaybeLikes() {
        return maybeLikes;
    }

    public ArrayList<KeepReading> getKeepReadings() {
        return keepReadings;
    }

    public PluralContentName getPluralContentNames() {
        return pluralContentNames;
    }
}
