package com.example.sew.helpers;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sew.BuildConfig;
import com.example.sew.R;
import com.example.sew.MyApplication;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.ActivityManager;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.common.MyConstants;
import com.example.sew.models.ContentType;
import com.example.sew.models.HomeSherCollection;
import com.example.sew.models.Line;
import com.example.sew.models.OccasionCollection;
import com.example.sew.models.Para;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static android.content.Context.CLIPBOARD_SERVICE;

public class MyHelper {

    public static final Pattern passwordPatternUpperCase = Pattern.compile("(?=.*?[A-Z])");
    public static final Pattern passwordPatternNumber = Pattern.compile("(?=.*?[0-9])");
    private static final Pattern passwordPatternSpecialChar = Pattern.compile("(?=.*?[#?!@$%^&*-])");

    public static String readRawFile(int rawId) {
        InputStream inputStream = MyApplication.getContext().getResources().openRawResource(rawId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

    private static JSONObject rekhtaJsonObject;

    public static String getString(int resId) {
        if (rekhtaJsonObject == null)
            try {
                rekhtaJsonObject = new JSONObject(MyHelper.readRawFile(R.raw.rekhta));
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        JSONObject contentObject = rekhtaJsonObject.optJSONObject(MyApplication.getContext().getResources().getString(resId));
        if (contentObject == null)
            contentObject = new JSONObject();

        switch (MyService.getSelectedLanguage()) {
            case ENGLISH:
                return contentObject.optString("eng");
            case HINDI:
                return contentObject.optString("hin");
            case URDU:
                return contentObject.optString("urdu");
        }
        return "";
    }

    public static JSONObject parseResponse(String response) {
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public static boolean isResponseValid(JSONObject response) {
        if (response == null)
            return false;
        return response.optString("S", "0").contentEquals("1") || response.optBoolean("S");
    }

    public static String getErrorMessage(JSONObject response) {
        if (response == null)
            return "";
        return response.optString("Me");
    }

    public static int convertToInt(String inputString) {
        try {
            return Integer.parseInt(inputString);
        } catch (NumberFormatException ignored) {
        }
        return 0;
    }

    public static int convertToInt(String inputString, int radix) {
        try {
            return Integer.parseInt(inputString, radix);
        } catch (NumberFormatException ignored) {
        }
        return 0;
    }

    public static long convertToLong(String inputString) {
        try {
            return Long.parseLong(inputString);
        } catch (NumberFormatException ignored) {
        }
        return 0;
    }

    public static float convertToFloat(String inputString) {
        try {
            return Float.parseFloat(inputString);
        } catch (NumberFormatException ignored) {
        }
        return 0;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
                return isValidFirstPartOfMail(target.toString());
            } else {
                return false;
            }
        }
    }

    public static boolean isValidPassword(CharSequence password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        } else {
            if (passwordPatternNumber.matcher(password).find() && passwordPatternUpperCase.matcher(password).find() && password.length() > 5) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isValidFirstPartOfMail(String input) {
        int index = input.indexOf("@");
        String fText = input.substring(0, index);
        String lText = input.substring(index, input.length());
        int count = lText.length() - lText.replaceAll("[;\\\\/:*.?\\\"<>|&']", "").length();
        //int dotCount = lText.length() - fText.replaceAll(".","").length();
        if (passwordPatternSpecialChar.matcher(fText).find()) { // before @ check Special Charc
            return false;
        } else {
            if (count > 2) { // check for single
                return false;
            } else {
                return true;
            }
        }
    }

    public static boolean isValidName(String name) {
        return !passwordPatternNumber.matcher(name).find() && !passwordPatternSpecialChar.matcher(name).find() && !name.equalsIgnoreCase("");
    }

    private static int[] tagsColors = {0xFFD96FB1, 0xFF53C8FE, 0xFFBC5CFE, 0xFF10D1B8, 0xFFD983F0, 0xFF85D15B, 0xFFC0916B, 0xFFD961BB, 0xFF45BAFE, 0xFFD9838A, 0xFF6CE397, 0xFF599BFE, 0xFF9F66FE, 0xFFD966DE, 0xFFF3CF63, 0xFFFF75EC, 0xFF5CFFA3, 0xFFFF5E73, 0xFF43E9E5, 0xFFD69946, 0xFFBA60FF, 0xFF65DA50, 0xFFFF737D, 0xFFD3C537};

    public static int getTagColor(int position) {

        return tagsColors[position % tagsColors.length];
    }

    private static int[] tagColor = {0xFF00C28F, 0xFFFFA834, 0xFFF15E88, 0xFFC965F7, 0xFF65B2F7, 0xFF00C28F, 0xFFFFA834, 0xFFF15E88, 0xFFC965F7, 0xFF65B2F7};

    public static int getTagColorForSearch(int position) {

        return tagColor[position % tagColor.length];
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static ContentType getContentById(String contentId) {
//        ArrayList<ContentType> contentTypes = getContentTypes();
        ArrayList<ContentType> contentTypes = MyService.getAllContentType();
        for (ContentType contentType : contentTypes)
            if (contentType.getContentId().equalsIgnoreCase(contentId))
                return contentType;
        return new ContentType(null);
    }

    public static ContentType getContentByIdNew(String contentId){
        ArrayList<ContentType> saveContentType = MyService.getAllContentType();
        for (int j = 0; j < saveContentType.size(); j++) {
            ContentType contentTypeSave = saveContentType.get(j);
            if (contentTypeSave.getContentId().equalsIgnoreCase(contentId)) {
                return contentTypeSave;
            }
        }
        return new ContentType(null);
    }

    public static ContentType getContentByFavType(Enums.FAV_TYPES favType) {
        String contentTypeId = "";
        switch (favType) {
            case IMAGE_SHAYRI:
                contentTypeId = MyConstants.FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID;
                break;
            case WORD:
                contentTypeId = MyConstants.FAV_WORD_CONTENT_TYPE_ID;
                break;
            case T20:
                contentTypeId = MyConstants.FAV_T20_CONTENT_TYPE_ID;
                break;
            case OCCASION:
                contentTypeId = MyConstants.FAV_OCCASION_CONTENT_TYPE_ID;
                break;
            case SHAYARI_COLLECTION:
                contentTypeId = MyConstants.FAV_SHAYARI_COLLECTION_CONTENT_TYPE_ID;
                break;
            case PROSE_COLLECTION:
                contentTypeId = MyConstants.FAV_PROSE_COLLECTION_CONTENT_TYPE_ID;
                break;
            case ENTITY:
                contentTypeId = MyConstants.FAV_POET_CONTENT_TYPE_ID;
                break;
        }
        return getContentById(contentTypeId);
    }

    public static ContentType getContentBySlug(String contentSlug) {
        ArrayList<ContentType> contentTypes = MyService.getAllContentType();
        for (ContentType contentType : contentTypes)
            if (contentType.getSlug().equalsIgnoreCase(contentSlug))
                return contentType;
        return new ContentType(null);
    }

    public static ContentType getDummyContentTypeProfile() {
        ContentType contentType = new ContentType(null);
        contentType.setContentListType(MyConstants.TMP_POET_PROFILE_ID);
        contentType.setNameEng("Profile");
        contentType.setNameHin("परिचय");
        contentType.setNameUr("تعارف");
        return contentType;
    }

    public static ContentType getDummyContentTypeAudio() {
        ContentType contentType = new ContentType(null);
        contentType.setContentListType(MyConstants.TMP_AUDIO_ID);
        contentType.setNameEng("Audio");
        contentType.setNameHin("ऑडियो");
        contentType.setNameUr("آڈیو");
        return contentType;
    }

    public static ContentType getDummyContentTypeVideo() {
        ContentType contentType = new ContentType(null);
        contentType.setContentListType(MyConstants.TMP_VIDEO_ID);
        contentType.setNameEng("Video");
        contentType.setNameHin("वीडियो");
        contentType.setNameUr("ویڈیو");
        return contentType;
    }
    public static ContentType getDummyContentTypeImageShayari() {
        ContentType contentType = new ContentType(null);
        contentType.setContentListType(MyConstants.TMP_IMAGE_SHAYRI_ID);
        contentType.setNameEng("Image Shayari");
        contentType.setNameHin("चित्र शायरी");
        contentType.setNameUr("تصویری شاعری");
        return contentType;
    }

    public static HomeSherCollection getDummyT20SherCollection(String contentId, String name) {
        HomeSherCollection sherCollection = new HomeSherCollection(null);
        sherCollection.setId(contentId);
        sherCollection.setName(name);
        sherCollection.setType("t20");
        return sherCollection;
    }

    public static HomeSherCollection getDummyOccasionCollection(String contentId, String name) {
        HomeSherCollection sherCollection = new HomeSherCollection(null);
        sherCollection.setId(contentId);
        sherCollection.setName(name);
        sherCollection.setType("occasion");
        return sherCollection;
    }

    public static HomeSherCollection getDummyDefaultSherCollection(String contentId, String name) {
        HomeSherCollection sherCollection = new HomeSherCollection(null);
        sherCollection.setId(contentId);
        sherCollection.setName(name);
        sherCollection.setType("default_sher");
        return sherCollection;
    }

    public static HomeSherCollection getDummyTagCollection(String contentId, String name) {
        HomeSherCollection sherCollection = new HomeSherCollection(null);
        sherCollection.setId(contentId);
        sherCollection.setName(name);
        sherCollection.setType("tag");
        return sherCollection;
    }

    private static ArrayList<ContentType> contentTypes;

    public static ArrayList<ContentType> getContentTypes() {
        if (CollectionUtils.isEmpty(contentTypes)) {
            try {
                JSONArray jsonArray = new JSONArray(readRawFile(R.raw.content_type_ids));
                int size = jsonArray.length();
                contentTypes = new ArrayList<>(size);
                for (int i = 0; i < size; i++)
                    contentTypes.add(new ContentType(jsonArray.optJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                contentTypes = new ArrayList<>();
            }
        }
        return contentTypes;
    }


    public static void shareContent(String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.setType("text/plain");
        MyApplication.getContext().startActivity(sendIntent);
    }

    public static String saveTempBitmap(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            return saveImage(bitmap);
        }
        return "";
    }

    private static String saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        //File myDir = new File(root + "/rekhta_saved_images");
        File myDir = new File(root + "/RekhtaImages");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fname = "Rekhta_" + timeStamp + ".jpg";

        File file = new File(myDir, fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return file.getAbsolutePath();
    }

    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static String getSherContentText(ArrayList<Para> paras) {
        StringBuilder strBuilder = new StringBuilder();
        if (!CollectionUtils.isEmpty(paras)) {
            for (Para para : paras) {
                for (Line line : para.getLines()) {
                    strBuilder.append(line.getFullText());
                    strBuilder.append("\n");
                }
                strBuilder.append("\n");
            }
        }
        return strBuilder.toString();
    }

    public static String getSherContentText(Para para) {
        ArrayList<Para> paras = new ArrayList<>(1);
        paras.add(para);
        return getSherContentText(paras);
    }

    private static boolean isMediaPlaying = false;

    public static void playAudio(String myUrl, BaseActivity activity, ImageView imageView) {
        if (isMediaPlaying) {
            BaseActivity.showToast("Please wait");
            return;
        }
        isMediaPlaying = true;
        BaseActivity.showToast("Playing audio, Please wait");
        imageView.setColorFilter(Color.argb(224, 224, 224, 224));//md_grey_300
        Uri myUri = null;
        try {
            myUri = Uri.parse(myUrl);
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(activity, myUri);
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.start();
            isMediaPlaying = false;
            mp.setOnCompletionListener(mp1 -> {
                        imageView.setColorFilter(Color.argb(254, 0, 191, 255));//dark_blue
                        isMediaPlaying = false;
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
            isMediaPlaying = false;
        } catch (Exception ignored) {
        }
    }

    public static void shareTheText(String text, BaseActivity baseActivity) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "https://Rekhta.org");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        baseActivity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void copyToClipBoard(String sherContentText, BaseActivity baseActivity) {
        ClipboardManager clipboard = (ClipboardManager) baseActivity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Sher", sherContentText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(baseActivity, AppErrorMessage.poetsher_adapter_copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }

    public static boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static int getPrimaryTextColor(Activity activity) {
        return getColorFromAttribute(R.attr.primaryTextColor, activity);
    }

    public static int getAppBackgroundColor(Activity activity) {
        return getColorFromAttribute(R.attr.appBackgroundColor);
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(long timeStamp) {

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeStamp);
        Date date = cal.getTime();
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public int getDarkGreyTextColor() {
        return getColorFromAttribute(R.attr.darkGreyTextColor);
    }

    public static int getAppDividerColor(Activity activity) {
        return getColorFromAttribute(R.attr.appDividerColor, activity);
    }

    public static int getAppIconColor(Activity activity) {
        return getColorFromAttribute(R.attr.appIconColor, activity);
    }

    public static int getColorFromAttribute(int attId, Activity activity) {
        TypedValue a = new TypedValue();
        activity.getTheme().resolveAttribute(attId, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return a.data;
        } else {
            return Color.WHITE;
        }
    }

    public static int getColorFromAttribute(int attId) {
        TypedValue a = new TypedValue();
        MyApplication.getContext().getTheme().resolveAttribute(attId, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            return a.data;
        } else {
            return Color.WHITE;
        }
    }
    public static void showSnackBar(String msg, int bgColor, boolean isShowTillNetwork, Activity activity) {
        Snackbar snackbar;
        if(isShowTillNetwork)
            snackbar = Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE);
        else
            snackbar = Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(bgColor);
        snackbar.show();
    }
    public static boolean isConfigLoaded() {
        return !TextUtils.isEmpty(MyService.getMediaURL()) &&
                !TextUtils.isEmpty(MyService.getCdnURL()) &&
                !TextUtils.isEmpty(MyService.getBaseURL());
    }

    public static void resetConfig() {
        MyService.setAppServer(BuildConfig.APP_SERVER);
        MyService.setBaseURL("");
        MyService.setCdnURL("");
        MyService.setMediaURL("");
    }
}
