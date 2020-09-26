package com.example.sew.apis;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.sew.MyApplication;
import com.example.sew.activities.BaseActivity;
import com.example.sew.activities.LoginActivity;
import com.example.sew.common.ActivityManager;
import com.example.sew.common.ICommonValues;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * Created by Raman Kumar on 3/19/2016.
 */
public abstract class Base extends BaseServiceable implements ICommonValues {


    public final void addCommonHeaders() {
        //  boolean userLogin = false;
        addHeader("temptoken", MyService.getUniqueId());
        addHeader("content-type", "application/json");
        addJson("a", "a");
        if (MyService.isUserLogin())
            addHeader("Authorization", MyService.getAuthToken());
        else
            addHeader("Authorization", "");
    }

    boolean enablePostAPIs;
    private final int INITIAL_PAGE_COUNT = 1;
    private int pageCount = INITIAL_PAGE_COUNT;

    public boolean isFirstPage() {
        return pageCount == INITIAL_PAGE_COUNT;
    }

    public Base setPageCount(int pageCount) {
        this.pageCount = pageCount;
        addParam("pageIndex", String.valueOf(pageCount));
        return this;
    }


    public Base addPagination() {
        setPageCount(pageCount);
        return this;
    }

    //    public Base increasePageCount() {
//        setPageCount(++pageCount);
//        return this;
//    }
    public Base loadMoreData() {
        setPageCount(++pageCount);
        runAsync(onApiFinishListener);
        return this;
    }

    public Base() {
        addCommonHeaders();
    }

    private JSONObject data;
    private JSONArray dataArray;

    private String msg;


    public JSONObject getData() {
        return data;
    }

    public JSONArray getDataArray() {
        return dataArray;
    }

    private String errorMessage;
    JSONObject object;

    public String getResponseString() {
        return responseString;
    }

    private String responseString;

    @Override
    public void onPostRun(int statusCode, String response) {
        this.responseString = response;
//        Logger.d(get_url());
//        Logger.json(debugRequest());
        data = MyHelper.parseResponse(response);
        setValidResponse(MyHelper.isResponseValid(data));
        if (!isValidResponse()) {
//            if(data.optString("S", "0").contentEquals("2")){
//                MyService.logoutUser();
//                ActivityManager.getInstance().clearStack();
//                MyApplication.getContext().startActivity(LoginActivity.getInstance((Activity) MyApplication.getContext()));
//            }else
            errorMessage = MyHelper.getErrorMessage(data);
        }
        Object rawData = data.opt("R");
        if (rawData instanceof JSONObject)
            data = data.optJSONObject("R");
        else if (rawData instanceof JSONArray)
            dataArray = data.optJSONArray("R");
        if (data == null)
            data = new JSONObject();
        if (dataArray == null)
            dataArray = new JSONArray();
    }


    public void setValidResponse(boolean validResponse) {
        isValidResponse = validResponse;
    }

    private OnApiFinishListener onApiFinishListener;

    @Override
    public void runAsync(OnApiFinishListener onApiFinishListener) {
        this.onApiFinishListener = onApiFinishListener;
        super.runAsync(onApiFinishListener);
    }

    @Override
    public void onError(Call<ResponseBody> requestBodyCall, Throwable t) {
        errorMessage = t.getMessage();
    }

    private String res;

    public String Result() {
        return res;
    }

    private boolean isValidResponse;

    public boolean isValidResponse() {
        return isValidResponse;
    }

    public void shareIssue(Context context) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Rekhta Invalid token Issue");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, debugRequest());
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    public String getErrorMessage() {
        if (errorMessage == null || errorMessage.startsWith("Unable to resolve host") || errorMessage.startsWith("java.security.cert.CertPathValidatorException") || errorMessage.startsWith("Failed to connect to"))
            return "Oops, something went wrong, there seems to be a problem in your network.";
//        if(errorMessage.equalsIgnoreCase("Invalid temporary token")) {
//            shareIssue(MyApplication.getContext());
//            return errorMessage;
//        }
        return errorMessage;
    }

}
