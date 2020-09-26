package com.example.sew.apis;

import android.text.TextUtils;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.BaseOtherFavModel;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.FavoriteDictionary;
import com.example.sew.models.FavoritePoet;
import com.example.sew.models.ShayariImage;

public class PostMarkFavorite extends Base {

    private Enums.FAV_TYPES favType;
    private String contentId;

    public PostMarkFavorite() {
        setUrl(MyConstants.getMarkFavorite());
        setRequestType(REQUEST_TYPE.POST);
        addParam("language", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    public PostMarkFavorite setContentId(String contentId) {
        this.contentId = contentId;
        addParam("targetId", contentId);
        return this;
    }

    public PostMarkFavorite setFavType(String favType) {
        this.favType = Enums.FAV_TYPES.getFavTypeFromKey(favType);
        addParam("favType", favType);
        return this;
    }

    @Override
    public void runAsync(OnApiFinishListener onApiFinishListener) {
        super.runAsync(onApiFinishListener);
        if (!TextUtils.isEmpty(contentId)) {
            MyService.addFavorite(contentId);
            BaseActivity.sendBroadCast(BROADCAST_FAVORITE_UPDATED);
            showSuccessMessage();
        }
    }

    /*
        {"S":1,"Me":null,"Mh":null,"Mu":null,"R":{"FD":"2019-06-23T15:04:05.8","FL":1,"FM":null,"FS":null,"I":"d792f6fa-448e-4799-a60d-58dcab2bca43","T":"43d60a15-0b49-4caf-8b74-0fcdddeb9f83","PI":"6b5a8bdc-739b-4916-acd6-3c3085f5ad0d","PE":null,"PH":null,"PU":null,"P":null,"TE":"ab TuuTne hii vaalaa hai tanhaa.ii kaa hisaar","TH":"अब टूटने ही वाला है तन्हाई का हिसार","TU":"اب ٹوٹنے ہی والا ہے تنہائی کا حصار","SE":null,"SH":null,"SU":null,"R":"ر ,اب ٹوٹنے ہی والا ہے تنہائی کا حصار","S":"ab-tuutne-hii-vaalaa-hai-tanhaaii-kaa-hisaar-adil-mansuri-ghazals","SI":3094,"N":false,"EC":false,"PC":false,"AU":null,"VI":null,"AC":null,"VC":null,"HE":false,"HH":false,"HU":false,"UE":"http://rek.ht/a/0a3q","UH":"http://rek.ht/a/0a3q/2","UU":"http://rek.ht/a/0a3q/3","FC":null,"SC":null},"T":"2019-06-23T15:04:05.9711306+05:30"}
         */
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            BaseOtherFavModel favModel = null;
            String contentId = "";
            switch (favType) {
                case CONTENT:
                    FavContentPageModel favContentPageModel = new FavContentPageModel(getData());
                    if (!TextUtils.isEmpty(favContentPageModel.getId())) {
                        MyService.addFavoriteListContent(favContentPageModel);
                        BaseActivity.sendBroadCast(BROADCAST_FAVORITE_UPDATED);
                        if (MyService.isOfflineSaveEnable())
                            new GetFavoriteListWithSpecificContent()
                                    .setContentId(favContentPageModel.getId())
                                    .runAsync(null);
//                        showSuccessMessage();
                    }
                    break;
                case ENTITY:
                    favModel= new FavoritePoet(getData());
                    break;
                case IMAGE_SHAYRI:
                    favModel = new ShayariImage(getData());
                    break;
                case WORD:
                    favModel = new FavoriteDictionary(getData());
                    break;
                case T20:
                case OCCASION:
                case SHAYARI_COLLECTION:
                case PROSE_COLLECTION:
                    contentId = getData().optString("I");
                    break;
            }
            // TODO comment this line by Anuj Agrawal    BaseActivity.showToast(MyHelper.getString(R.string.added_to_favorite));
            if (favModel != null && !TextUtils.isEmpty(favModel.getId())) {
                MyService.addFavoriteOther(favModel);
//                BaseActivity.sendBroadCast(BROADCAST_FAVORITE_UPDATED);
//                showSuccessMessage();
            } else if (!TextUtils.isEmpty(contentId) && favType != Enums.FAV_TYPES.CONTENT) {
                MyService.addFavoriteOther(contentId, favType);
//                BaseActivity.sendBroadCast(BROADCAST_FAVORITE_UPDATED);
//                showSuccessMessage();
            }

        }else{
            MyService.removeFavorite(contentId);
            BaseActivity.sendBroadCast(BROADCAST_FAVORITE_UPDATED);
        }
    }

    private void showSuccessMessage() {
        BaseActivity.showToast(MyHelper.getString(R.string.added_to_favorite));
    }
}
