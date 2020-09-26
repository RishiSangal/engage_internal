package com.example.sew.apis;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.sew.common.MyConstants;
import com.example.sew.MyApplication;
import com.example.sew.helpers.SLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.chuck.ChuckInterceptor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.SSLContext;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class BaseServiceable {
    private HashMap<String, String> params;
    private static HashMap<String, String> headers;
    JSONObject _mjsonParams;
    HashMap<String, RequestBody> _mfiles;
    private final String TAG = "API_Request_Call";
    String _url;
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public BaseServiceable() {


        OkHttpClient client = new OkHttpClient.Builder()
//                .sslSocketFactory(getSSLContext().getSocketFactory())
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {

                        Request original = chain.request();
                        Request.Builder request = original.newBuilder();
                        try {
                            for (Map.Entry<String, String> curr : getHeaders().entrySet())
                                request.header(curr.getKey(), curr.getValue());
                            request.method(original.method(), original.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return chain.proceed(request.build());
                    }
                })
                .addInterceptor(new ChuckInterceptor(MyApplication.getContext()))
                .readTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(0, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.google.com")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            apiInterface = retrofit.create(APIInterface.class);
        }

        this._mfiles = new HashMap<>();
        headers = new HashMap<>();
        if (params == null)
            params = new HashMap<>();
        else
            params.clear();
    }

    private static SSLContext sslContext;


    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public enum REQUEST_TYPE {GET, POST, PUT, DELETE}

    public REQUEST_TYPE requestType = REQUEST_TYPE.GET;

    public final void setRequestType(REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }

    public final void setUrl(String url) {
        if (TextUtils.isEmpty(url))
            MyConstants.init();
        this._url = url;
    }

    public String get_url() {
        return _url;
    }

    public synchronized final void addHeader(String key, String value) {
        headers.remove(key);
        headers.put(key, value);
    }

    public final void addFile(String key, File file) {
        if (TextUtils.isEmpty(key) || file == null || !file.exists())
            return;
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        // MultipartBody.Part is used to send also the actual file name
//        body = MultipartBody.Part.createFormData(key, file.getTagName(), requestFile);
        _mfiles.put(key + "\"; filename=\"" + file.getName(), requestFile);
    }

    public final void addParam(String key, String value) {
        params.remove(key);
        params.put(key, value);
    }

    public final void addJson(String key, Object value) {
        if (this._mjsonParams == null)
            this._mjsonParams = new JSONObject();
        try {
            this._mjsonParams.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected HashMap<String, String> getParams() {
        return params;
    }

    public void setJson(JSONObject jsonObject) {
        this._mjsonParams = jsonObject;
    }

    static Retrofit retrofit;
    static APIInterface apiInterface;

    public void runAsync(OnApiFinishListener onApiFinishListener) {
        if (_url == null)
            return;
        this.onApiFinishListener = onApiFinishListener;
        Call<ResponseBody> responseBodyCall = null;
        switch (requestType) {
            case GET:
                responseBodyCall = apiInterface.getAPI(_url, params);
                break;
            case POST:
                if (_mjsonParams != null) {
                    HashMap<String, Object> jsonParams = new HashMap<>(_mjsonParams.names().length());
                    JSONArray array = _mjsonParams.names();
                    for (int i = 0; i < array.length(); i++)
                        jsonParams.put(array.optString(i), _mjsonParams.opt(array.optString(i)));
                    if (params != null && params.size() > 0) {
                        responseBodyCall = apiInterface.postJSONAPI(_url, jsonParams, params);
                    } else
                        responseBodyCall = apiInterface.postJSONAPI(_url, jsonParams);

                } else {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue());
                        _mfiles.put(entry.getKey(), requestBody);
                    }
                    responseBodyCall = apiInterface.postAPI(_url, _mfiles);
                }
                break;
            case DELETE:
                if (_mjsonParams != null)
                    responseBodyCall = apiInterface.deleteJSONAPI(_url, _mjsonParams.toString());
                else {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue());
                        _mfiles.put(entry.getKey(), requestBody);
                    }
                    responseBodyCall = apiInterface.deleteSomething(_url, _mfiles);
                }
                break;
            case PUT:
                if (_mjsonParams != null)
                    responseBodyCall = apiInterface.putJSONAPI(_url, _mjsonParams.toString());
                else {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue());
                        _mfiles.put(entry.getKey(), requestBody);
                    }
                    responseBodyCall = apiInterface.putAPI(_url, _mfiles);
                }
                break;
        }
        if (responseBodyCall != null)
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    new AsyncParseResponse(call, response).execute();
                    BaseServiceable.this.requestCall = call;
                    BaseServiceable.this.response = response;
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    new AsyncParseResponse(call, t).execute();
                }
            });
    }

    public interface OnApiFinishListener<T extends BaseServiceable> {
        public void onComplete(T t);
    }

    private Call<ResponseBody> requestCall;
    Response<ResponseBody> response;
    private OnApiFinishListener onApiFinishListener;
    private String apiResponseData;

    public final Response<ResponseBody> getBodyResponse() {
        return response;
    }

    public final Call<ResponseBody> getRequest() {
        return requestCall;
    }

    public final Response<ResponseBody> getResponse() {
        return response;
    }

    public class AsyncParseResponse extends AsyncTask<Void, Void, Void> {
        Call<ResponseBody> call;
        Response<ResponseBody> response;
        Throwable t;
        boolean isDataProvided = false;

        public AsyncParseResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            this.call = call;
            this.response = response;
        }

        public AsyncParseResponse(Call<ResponseBody> call, Throwable t) {
            this.call = call;
            this.t = t;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (response != null) {
                    int code = response.code();
                    apiResponseData = response.isSuccessful() ? response.body().string() : response.errorBody().string();

                    SLog.i(TAG, String.format("API_Request_Call URL: %s", getRequest().request().url()));
                    SLog.i(TAG, String.format("API_Request_Call Request_Body: %s", stringifyRequestBody(getRequest().request())));
                    SLog.i(TAG, String.format("API_Request_Call Request Header: %s", getRequestHeaderJson().toString()));
                    SLog.i(TAG, String.format("API_Request_Call Response: %s", apiResponseData));

//                    Logger.d(call.request().url());
//                    Logger.d(apiResponseData);
                    onPostRun(code, apiResponseData);
                } else {
                    onError(call, t);
                    SLog.e(TAG, String.format("API_Request_Call URL: %s", getRequest().request().url()));
                    SLog.e(TAG, String.format("API_Request_Call Request_Body: %s", stringifyRequestBody(getRequest().request())));
                    SLog.e(TAG, String.format("API_Request_Call Error: %s", t.getLocalizedMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (onApiFinishListener != null)
                onApiFinishListener.onComplete(BaseServiceable.this);
            isDataProvided = true;
        }

        private String stringifyRequestBody(Request request) {
            if (request.body() != null) {
                try {
                    final Request copy = request.newBuilder().build();
                    final Buffer buffer = new Buffer();
                    copy.body().writeTo(buffer);
                    return buffer.readUtf8();
                } catch (final IOException e) {
                    SLog.e(TAG, "Failed to stringify request body: " + e.getMessage());
                }
            }
            return "";
        }
    }

    private JSONObject getRequestHeaderJson() {
        JSONObject headerJson = new JSONObject();
        for (Map.Entry<String, String> currObj : BaseServiceable.headers.entrySet()) {
            try {
                headerJson.put(currObj.getKey(), currObj.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return headerJson;
    }

    public final String debugRequest() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("requestType", getRequest().request().method());
            jsonObject.put("url", getRequest().request().url().toString());
            /********Headers*********/
            /*JSONObject headerJson = new JSONObject();
            for (Map.Entry<String, String> currObj : BaseServiceable.headers.entrySet()) {
                headerJson.put(currObj.getKey(), currObj.getValue());
            }*/
            jsonObject.put("headers", getRequestHeaderJson());
            /********Headers*********/
            jsonObject.put("response", apiResponseData);
            jsonObject.put("code", getBodyResponse().code());
            /********Files*********/
            if (_mfiles != null && _mfiles.size() > 0) {
                JSONArray files = new JSONArray();
                for (Map.Entry<String, RequestBody> currObj : _mfiles.entrySet()) {
                    files.put(currObj.getKey());
                }
                jsonObject.put("files", files);
            }
            /********Files*********/
            /********Input Parameters*********/
            if (params != null && params.size() > 0 && requestType != REQUEST_TYPE.GET) {
                JSONArray paramArray = new JSONArray();
                for (Map.Entry<String, String> currObj : params.entrySet()) {
                    JSONObject currHeaderJson = new JSONObject();
                    currHeaderJson.put(currObj.getKey(), currObj.getValue());
                    paramArray.put(currHeaderJson);
                }
                jsonObject.put("input_params", paramArray);
            }
            /********Input Parameters*********/
            /********Input Parameters*********/
            if (_mjsonParams != null)
                jsonObject.put("json_params", _mjsonParams);
            /********Input Parameters*********/
        } catch (Exception ignored) {
        }
        return jsonObject.toString();
    }

    public abstract void onPostRun(int statusCode, String response);

    public abstract void onError(Call<ResponseBody> requestBodyCall, Throwable t);
}
