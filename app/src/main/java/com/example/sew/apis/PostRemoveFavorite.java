package com.example.sew.apis;

import android.text.TextUtils;
import android.widget.Toast;

import com.example.sew.MyApplication;
import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.FavContentPageModel;

public class PostRemoveFavorite extends Base {

    private Enums.FAV_TYPES favType;

    public PostRemoveFavorite() {
        setUrl(MyConstants.getRemoveFavorite());
        setRequestType(REQUEST_TYPE.POST);
        addParam("language", String.valueOf(MyService.getSelectedLanguageInt()));
    }

    private String contentId;

    public PostRemoveFavorite setContentId(String contentId) {
        addParam("targetId", contentId);
        this.contentId = contentId;
        return this;
    }

    public PostRemoveFavorite setFavType(String favType) {
        this.favType = Enums.FAV_TYPES.getFavTypeFromKey(favType);
        addParam("favType", favType);
        return this;
    }

    @Override
    public void runAsync(OnApiFinishListener onApiFinishListener) {
        super.runAsync(onApiFinishListener);
        if (!TextUtils.isEmpty(contentId)) {
            MyService.removeFavorite(contentId);
            MyService.MyFavService.removeFav(contentId, favType);
            BaseActivity.sendBroadCast(BROADCAST_FAVORITE_UPDATED);
            BaseActivity.showToast(MyHelper.getString(R.string.removed_from_favorite));
        }
    }

    /*

     */
    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            if (favType != null && favType != Enums.FAV_TYPES.CONTENT)
                MyService.removeFavoriteOther(contentId, favType);
            else
                MyService.removeFavorite(MyService.getFavorite(contentId));
//            BaseActivity.sendBroadCast(ICommonValues.BROADCAST_FAVORITE_UPDATED);
        } else {
            MyService.addFavorite(contentId);
            BaseActivity.sendBroadCast(ICommonValues.BROADCAST_FAVORITE_UPDATED);
        }
    }

}
