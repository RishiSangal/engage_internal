package com.example.sew.apis;

import android.text.TextUtils;

import com.example.sew.activities.BaseActivity;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ContentType;
import com.example.sew.models.FavContentPageModel;

import org.json.JSONArray;

import java.util.ArrayList;

public class GetFavoriteListWithPaging extends Base {

    public GetFavoriteListWithPaging() {
        setUrl(MyConstants.getGetFavoriteListWithPaging());
        setRequestType(REQUEST_TYPE.POST);
        addCommonHeaders();
        addParam("keyword", "");
        addParam("lastFetchDate", "");
    }

    ArrayList<FavContentPageModel> favoriteContents;
    ArrayList<ContentType> favContentTypes;

    /*
{"S":1,"Me":null,"Mh":null,"Mu":null,"R":{"TC":1,"FC":[{"FD":"2019-06-23T15:04:05.8","FL":1,"FM":"2019-06-23T15:04:05.8","FS":true,"I":"d792f6fa-448e-4799-a60d-58dcab2bca43","T":"43d60a15-0b49-4caf-8b74-0fcdddeb9f83","PI":"6b5a8bdc-739b-4916-acd6-3c3085f5ad0d","PE":"Adil Mansuri","PH":"आदिल मंसूरी","PU":"عادل منصوری","P":null,"TE":"ab TuuTne hii vaalaa hai tanhaa.ii kaa hisaar","TH":"अब टूटने ही वाला है तन्हाई का हिसार","TU":"اب ٹوٹنے ہی والا ہے تنہائی کا حصار","SE":null,"SH":null,"SU":null,"R":"ر ,اب ٹوٹنے ہی والا ہے تنہائی کا حصار","S":"ab-tuutne-hii-vaalaa-hai-tanhaaii-kaa-hisaar-adil-mansuri-ghazals","SI":3094,"N":false,"EC":false,"PC":false,"AU":null,"VI":null,"AC":null,"VC":null,"HE":false,"HH":false,"HU":false,"UE":"http://rek.ht/a/0a3q","UH":"http://rek.ht/a/0a3q/2","UU":"http://rek.ht/a/0a3q/3","FC":null,"SC":null}],"FS":[{"I":"43d60a15-0b49-4caf-8b74-0fcdddeb9f83","NE":"Ghazal","NH":"ग़ज़ल","NU":"غزل","S":1,"C":1}]},"T":"2019-06-23T15:43:24.4637051+05:30"}
     */
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            int totalCount = MyHelper.convertToInt(getData().optString("TC"));
            if (isFirstPage())
                favoriteContents = new ArrayList<>(totalCount);
            /*fail safe*/
            if (favoriteContents == null)
                favoriteContents = new ArrayList<>();
            JSONArray array = getData().optJSONArray("FC");
            if (array == null)
                array = new JSONArray();
            int size = array.length();
            String idsToFetchData = "";
            for (int i = 0; i < size; i++) {
                FavContentPageModel favContentPageModel = new FavContentPageModel(array.optJSONObject(i));
                favoriteContents.add(favContentPageModel);
                if (!MyService.isFavorite(favContentPageModel.getId()) || !MyService.isFavoriteListContentAvailable(favContentPageModel.getId())) {
                    MyService.addFavoriteListContent(favContentPageModel);
                    if (!MyService.isFavoriteContentDetailAvailable(favContentPageModel.getId())) {
                        if (TextUtils.isEmpty(idsToFetchData))
                            idsToFetchData += favContentPageModel.getId();
                        else
                            idsToFetchData += "," + favContentPageModel.getId();
                    }
                }
            }
            if (!TextUtils.isEmpty(idsToFetchData) && MyService.isOfflineSaveEnable()) {
                new GetFavoriteListWithSpecificContent()
                        .setContentId(idsToFetchData)
                        .runAsync(null);
            }
            boolean isAnyFavoriteRemoved = false;
            if (totalCount > favoriteContents.size()) {
                loadMoreData();
            } else {
                ArrayList<FavContentPageModel> savedFavoriteContent = MyService.getAllFavorite();
                for (int i = savedFavoriteContent.size() - 1; i >= 0; i--) {
                    if (!favoriteContents.contains(savedFavoriteContent.get(i))) {
                        MyService.removeFavorite(savedFavoriteContent.get(i));
                        isAnyFavoriteRemoved = true;
                    }
                }
            }
            if (isAnyFavoriteRemoved)
                BaseActivity.sendBroadCast(ICommonValues.BROADCAST_FAVORITE_UPDATED);
            array = getData().optJSONArray("FS");
            if (array == null)
                array = new JSONArray();
            size = array.length();
            favContentTypes = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                favContentTypes.add(new ContentType(array.optJSONObject(i)));
            MyService.setUserLoggedin(false);
            BaseActivity.sendBroadCast(BROADCAST_ALL_FAVORITE_LOAD_COMPLETED);
        }
    }

    public ArrayList<ContentType> getFavContentTypes() {
        return favContentTypes;
    }

    public ArrayList<FavContentPageModel> getFavoriteContents() {
        return favoriteContents;
    }
}
