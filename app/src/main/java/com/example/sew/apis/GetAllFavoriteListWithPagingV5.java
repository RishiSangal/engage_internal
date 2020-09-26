package com.example.sew.apis;

import android.text.TextUtils;

import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.BaseOtherFavModel;
import com.example.sew.models.FavoriteDictionary;
import com.example.sew.models.FavoritePoet;
import com.example.sew.models.ShayariImage;
import com.google.android.gms.common.util.CollectionUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class GetAllFavoriteListWithPagingV5 extends Base {
    private Enums.FAV_TYPES favType;

    public GetAllFavoriteListWithPagingV5() {
        setUrl(MyConstants.getGetAllFavoriteListWithPagingV5());
        setRequestType(REQUEST_TYPE.POST);
        addParam("keyword", "");
        addParam("lastFetchDate", "");
    }

    private Enums.FAV_TYPES getFavType() {
        return favType;
    }

    public GetAllFavoriteListWithPagingV5 setFavType(Enums.FAV_TYPES favType) {
        if (favType == null)
            favType = Enums.FAV_TYPES.CONTENT;
        this.favType = favType;
        addParam("favType", favType.getKey());
        return this;
    }

    private ArrayList<BaseOtherFavModel> favoriteContents;
    private ArrayList<String> collectionFavorite;

    /*
{"S":1,"Me":null,"Mh":null,"Mu":null,"R":{"TC":1,"FC":[{"FD":"2019-06-23T15:04:05.8","FL":1,"FM":"2019-06-23T15:04:05.8","FS":true,"I":"d792f6fa-448e-4799-a60d-58dcab2bca43","T":"43d60a15-0b49-4caf-8b74-0fcdddeb9f83","PI":"6b5a8bdc-739b-4916-acd6-3c3085f5ad0d","PE":"Adil Mansuri","PH":"आदिल मंसूरी","PU":"عادل منصوری","P":null,"TE":"ab TuuTne hii vaalaa hai tanhaa.ii kaa hisaar","TH":"अब टूटने ही वाला है तन्हाई का हिसार","TU":"اب ٹوٹنے ہی والا ہے تنہائی کا حصار","SE":null,"SH":null,"SU":null,"R":"ر ,اب ٹوٹنے ہی والا ہے تنہائی کا حصار","S":"ab-tuutne-hii-vaalaa-hai-tanhaaii-kaa-hisaar-adil-mansuri-ghazals","SI":3094,"N":false,"EC":false,"PC":false,"AU":null,"VI":null,"AC":null,"VC":null,"HE":false,"HH":false,"HU":false,"UE":"http://rek.ht/a/0a3q","UH":"http://rek.ht/a/0a3q/2","UU":"http://rek.ht/a/0a3q/3","FC":null,"SC":null}],"FS":[{"I":"43d60a15-0b49-4caf-8b74-0fcdddeb9f83","NE":"Ghazal","NH":"ग़ज़ल","NU":"غزل","S":1,"C":1}]},"T":"2019-06-23T15:43:24.4637051+05:30"}


      https://world-staging.rekhta.org/api/v5/shayari/GetAllFavoriteListWithPaging?favType=[favType]&keyword=[keyword]&pageIndex=[ PageIndex]
      https://world-staging.rekhta.org/api/v5/shayari/GetAllFavoriteListWithPaging
     */
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            int totalCount = MyHelper.convertToInt(getData().optString("TC"));
            if (isFirstPage() || favoriteContents == null)
                favoriteContents = new ArrayList<>(totalCount);
            if (isFirstPage() || collectionFavorite == null)
                collectionFavorite = new ArrayList<>(totalCount);
            JSONArray array = null;
            int size;
            BaseOtherFavModel otherFavModel = null;
            String currentContentId = null;
            ArrayList<String> collectionFavorite = new ArrayList<>();
            switch (getFavType()) {
                case CONTENT:
                    break;
                case IMAGE_SHAYRI:
                    array = getData().optJSONArray("FI");
                    break;
                case WORD:
                    array = getData().optJSONArray("FW");
                    break;
                case T20:
                    array = getData().optJSONArray("FT");
                    break;
                case OCCASION:
                    array = getData().optJSONArray("FO");
                    break;
                case SHAYARI_COLLECTION:
                    array = getData().optJSONArray("FS");
                    break;
                case PROSE_COLLECTION:
                    array = getData().optJSONArray("FP");
                    break;
                case ENTITY:
                    array = getData().optJSONArray("FE");
            }

            if (array == null)
                array = new JSONArray();
            size = array.length();

            for (int i = 0; i < size; i++) {
                switch (getFavType()) {
                    case IMAGE_SHAYRI:
                        otherFavModel = new ShayariImage(array.optJSONObject(i));
                        break;
                    case WORD:
                        otherFavModel = new FavoriteDictionary(array.optJSONObject(i));
                        break;
                    case ENTITY:
                        otherFavModel = new FavoritePoet(array.optJSONObject(i));
                        break;
                    case T20:
                    case OCCASION:
                    case SHAYARI_COLLECTION:
                    case PROSE_COLLECTION:
                        JSONObject object = array.optJSONObject(i);
                        if (object == null)
                            object = new JSONObject();
                        currentContentId = object.optString("I");
                        break;
                }
                if (otherFavModel != null && !TextUtils.isEmpty(otherFavModel.getId())) {
                    favoriteContents.add(otherFavModel);
                    if (!MyService.isFavorite(otherFavModel.getId()) || !MyService.isFavoriteListOtherAvailable(otherFavModel.getId())) {
                        MyService.addFavoriteOther(otherFavModel);
                    }
                } else if (!TextUtils.isEmpty(currentContentId)) {
                    collectionFavorite.add(currentContentId);
                    if (!MyService.isFavorite(currentContentId) || !MyService.isFavoriteListOtherAvailable(currentContentId)) {
                        MyService.addFavoriteOther(currentContentId, favType);
                    }
                }
            }

            if (size > 0 && !CollectionUtils.isEmpty(favoriteContents) && totalCount > favoriteContents.size()) {
                loadMoreData();
            } else if (size > 0 && !CollectionUtils.isEmpty(collectionFavorite) && totalCount > collectionFavorite.size()) {
                loadMoreData();
            } else {
                ArrayList<? extends BaseOtherFavModel> savedFavoriteContent = null;
                ArrayList<String> savedFavoriteCollection = null;
                switch (getFavType()) {
                    case IMAGE_SHAYRI:
                        savedFavoriteContent = MyService.getAllSavedImageShayari();
                        break;
                    case WORD:
                        savedFavoriteContent = MyService.getAllFavoriteWordDictionary();
                        break;
                    case T20:
                        savedFavoriteCollection = MyService.getAllFavoriteT20Shayari();
                        break;
                    case OCCASION:
                        savedFavoriteCollection = MyService.getAllFavoriteOccasion();
                        break;
                    case SHAYARI_COLLECTION:
                        savedFavoriteCollection = MyService.getAllFavoriteShayariCollection();
                        break;
                    case PROSE_COLLECTION:
                        savedFavoriteCollection = MyService.getAllFavoriteProseCollection();
                        break;
                    case ENTITY:
                        savedFavoriteContent = MyService.getAllFavoritePoet();
                }

                boolean isAnyFavoriteRemoved = false;
                if (!CollectionUtils.isEmpty(savedFavoriteContent)) {
                    for (int i = savedFavoriteContent.size() - 1; i >= 0; i--) {
                        if (!favoriteContents.contains(savedFavoriteContent.get(i))) {
                            MyService.removeFavoriteOther(savedFavoriteContent.get(i));
                            isAnyFavoriteRemoved = true;
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(savedFavoriteCollection)) {
                    for (int i = savedFavoriteCollection.size() - 1; i >= 0; i--) {
                        if (!collectionFavorite.contains(savedFavoriteCollection.get(i))) {
                            MyService.removeFavoriteOther(savedFavoriteCollection.get(i), favType);
                            isAnyFavoriteRemoved = true;
                        }
                    }
                }
                if (favType == Enums.FAV_TYPES.IMAGE_SHAYRI || favType == Enums.FAV_TYPES.WORD || favType == Enums.FAV_TYPES.ENTITY)
                    if (isAnyFavoriteRemoved)
                        BaseActivity.sendBroadCast(ICommonValues.BROADCAST_FAVORITE_UPDATED);
            }
        }
    }
}
