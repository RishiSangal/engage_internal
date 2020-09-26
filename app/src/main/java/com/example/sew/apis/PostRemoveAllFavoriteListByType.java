package com.example.sew.apis;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.MyConstants;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.models.ShayariImage;

import org.json.JSONArray;

import java.util.ArrayList;

public class PostRemoveAllFavoriteListByType extends Base {


    public PostRemoveAllFavoriteListByType() {
        setUrl(MyConstants.getRemoveAllFavoriteImageShayari());
        setRequestType(REQUEST_TYPE.POST);
        setFavType();
    }

    private void setFavType() {
        addParam("favType", Enums.FAV_TYPES.IMAGE_SHAYRI.getKey());
    }

    public PostRemoveAllFavoriteListByType setShayariImages(ArrayList<ShayariImage> shayariImages) {
        StringBuilder shayariIds = new StringBuilder();
        for (ShayariImage shayariImage : shayariImages) {
            if (shayariIds.length() > 1)
                shayariIds.append(",");
            shayariIds.append(shayariImage.getId());
//            MyService.MyFavService.removeFav(shayariImage.getId(), Enums.FAV_TYPES.IMAGE_SHAYRI);
        }
        addJson("contentIds", shayariIds);
//        BaseActivity.sendBroadCast(BROADCAST_FAVORITE_UPDATED);
//        BaseActivity.showToast(MyHelper.getString(R.string.removed_from_favorite));
        return this;
    }

    public void onPostRun(int statusCode, String response) {
        super.onPostRun(statusCode, response);
        if (isValidResponse()) {
            BaseActivity.showToast(MyHelper.getString(R.string.removed_from_favorite));
            JSONArray jsonArray = getDataArray();
            if (jsonArray == null)
                jsonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++)
                MyService.removeFavoriteOther(jsonArray.optString(i), Enums.FAV_TYPES.IMAGE_SHAYRI);
            BaseActivity.sendBroadCast(ICommonValues.BROADCAST_FAVORITE_UPDATED);

        }
    }

}
