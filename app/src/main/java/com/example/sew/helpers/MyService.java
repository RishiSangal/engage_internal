package com.example.sew.helpers;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.example.sew.MyApplication;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.Enums;
import com.example.sew.common.ICommonValues;
import com.example.sew.common.MyConstants;
import com.example.sew.models.BaseOtherFavModel;
import com.example.sew.models.ContentType;
import com.example.sew.models.FavContentPageModel;
import com.example.sew.models.FavoriteDictionary;
import com.example.sew.models.FavoritePoet;
import com.example.sew.models.ShayariImage;
import com.example.sew.models.User;
import com.google.android.gms.common.util.CollectionUtils;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.paperdb.Paper;

public class MyService {

    private static final String DB_USER = "DB_USER";
    private static final String DB_DEVICE = "DB_DEVICE";
    /**
     * contains all the content_ids and content_type_ids that has been favorited
     */
    private static final String IS_OFFLINE_SAVE_ACTIVE = "IS_OFFLINE_SAVE_ACTIVE";
    private static final String DB_FAVORITE = "DB_FAVORITE";
    private static final String DB_APP_VERSIONING = "DB_APP_VERSIONING";
    private static final String DB_SAVED_IMAGE_SHAYARI = "DB_SAVED_IMAGE_SHAYARI";
    private static final String DB_SAVED_IMAGE_SHAYARI_LIST_CONTENT = "DB_SAVED_IMAGE_SHAYARI_LIST_CONTENT";
    private static final String DB_FAVORITE_DETAILED_CONTENT = "DB_FAVORITE_DETAILED_CONTENT";
    private static final String DB_FAVORITE_LIST_CONTENT = "DB_FAVORITE_LIST_CONTENT";
    private static final String DB_FAVORITE_LIST_OTHER = "DB_FAVORITE_LIST_OTHER";
    private static final String DB_SEARCH_KEYWORDS = "DB_SEARCH_KEYWORDS";
    private static final String USER = "AUTH_TOKEN";
    private static final String CONTENT_IDS = "CONTENT_IDS";
    private static final String SAVED_IMAGE_SHAYARI_IDS = "SAVED_IMAGE_SHAYARI_IDS ";
    private static final String KEYWORDS = "KEYWORDS";
    private static final String DEVICE_PARAMS = "DEVICE_PARAMS";
    private static final String UNIQUE_ID = "UNIQUE_ID";
    private static final String EMAIL = "EMAIL";
    private static final String FCM_TOKEN = "FCM_TOKEN";
    private static final String DEVICE_ID = "DEVICE_ID";
    private static final String YOUTUBE_KEY = "YOUTUBE_KEY";
    private static final String LAST_APP_VERSION = "LAST_APP_VERSION";
    private static final String LAST_UPDATE_DIALOG_SHOW_TIME = "LAST_UPDATE_DIALOG_SHOW_TIME";
    private static final String LAST_UPDATE_CONFIG_TIME = "LAST_UPDATE_CONFIG_TIME";
    private static final String LAST_SKIPPED_VERSION = "LAST_SKIPPED_VERSION";
    private static final String SELECTED_LAST_LANGUAGE = "selected_last_language";
    private static final String SHOULD_SHOW_LANGUAGE_POPUP_SPLASH = "should_show_language_popup";
    private static final String SELECTED_DARK_THEME = "SELECTED_DARK_THEME";
    private static final String APP_SERVER = "APP_SERVER";
    private static final String IS_FIRST_TIME_OPEN_APP = "IS_FIRST_TIME_OPEN_APP";
    private static final String DARK_MODE_PREF = "DARK_MODE_PREF";
    private static final String DB_SAVED_ALL_CONTENT_TYPE = "DB_SAVED_ALL_CONTENT_TYPE";
    private static final String DB_SAVED_ALL_CONTENT_TYPE_LIST = "DB_SAVED_ALL_CONTENT_TYPE_LIST";
    private static final String SAVED_CONTENT_TYPE_IDS = "SAVED_CONTENT_TYPE_IDS ";

    private static final String BASE_URL = "BASE_URL";
    private static final String CDN_URL = "CDN_URL";
    private static final String MEDIA_URL = "MEDIA_URL";

    public static void setUser(User user) {
        Paper.book(DB_USER).write(USER, user);
    }


    public static void setFcmToken(String fcmToken) {
        Paper.book(DB_USER).write(FCM_TOKEN, fcmToken);
    }

    public static String getFcmToken() {
        return Paper.book(DB_USER).read(FCM_TOKEN);
    }

    public static void setDarkModePref(String darkModePref) {
        Paper.book(DB_USER).write(DARK_MODE_PREF, darkModePref);
    }

    public static String getDarkModePref() {
        return Paper.book(DB_USER).read(DARK_MODE_PREF, ThemeHelper.DEFAULT_MODE);
    }

    public static void setYouTubeKey(String fcmToken) {
        Paper.book(DB_USER).write(YOUTUBE_KEY, fcmToken);
    }

    public static String getYouTubeKey() {
        return Paper.book(DB_USER).read(YOUTUBE_KEY);
    }


    public static void logoutUser() {
        setUser(new User(null));
        setDeviceParams("");
        setUniqueId("");
        setUniqueIdAndParamsIfNecessary();
        removeAllFavorite();
        setDeviceId("");
    }

    public static String getAuthToken() {
        return "Bearer " + getUser().getToken();
    }

    public static User getUser() {
        return Paper.book(DB_USER).read(USER, new User(null));
    }

    public static boolean isUserLogin() {
        return !TextUtils.isEmpty(getUser().getToken());
    }

    @SuppressLint("HardwareIds")
    public static void setUniqueIdAndParamsIfNecessary() {
        if (TextUtils.isEmpty(getDeviceParams()) || TextUtils.isEmpty(getUniqueId())) {
            String uniqueId = UUID.randomUUID().toString();
            String deviceParams = Settings.Secure.getString(MyApplication.getContext().
                    getContentResolver(), Settings.Secure.ANDROID_ID) + "," + Build.MANUFACTURER + "," + uniqueId + ",Android";
            String deviceId = Settings.Secure.getString(MyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            setDeviceId(deviceId);
            setDeviceParams(deviceParams);
            setUniqueId(uniqueId);
        }
    }

    private static void setDeviceParams(String deviceParams) {
        Paper.book(DB_DEVICE).write(DEVICE_PARAMS, deviceParams);
    }

    private static void setUniqueId(String uniqueId) {
        Paper.book(DB_DEVICE).write(UNIQUE_ID, uniqueId);
    }

    private static void setDeviceId(String deviceId) {
        Paper.book(DB_DEVICE).write(DEVICE_ID, deviceId);
    }

    public static void setAppServer(int darkTheme) {
        Paper.book(DB_DEVICE).write(APP_SERVER, darkTheme);
    }

    public static int getAppServer() {
        return Paper.book(DB_DEVICE).read(APP_SERVER, MyConstants.APP_SERVER_PRODUCTION);
    }

    public static void setBaseURL(String baseURL) {
        Paper.book(DB_DEVICE).write(BASE_URL, baseURL);
    }

    public static String getBaseURL() {
        return Paper.book(DB_DEVICE).read(BASE_URL, "");
    }

    public static void setMediaURL(String mediaURL) {
        Paper.book(DB_DEVICE).write(MEDIA_URL, mediaURL);
    }

    public static String getMediaURL() {
        return Paper.book(DB_DEVICE).read(MEDIA_URL, "");
    }

    public static void setCdnURL(String baseURL) {
        Paper.book(DB_DEVICE).write(CDN_URL, baseURL);
    }

    public static String getCdnURL() {
        return Paper.book(DB_DEVICE).read(CDN_URL, "");
    }

    public static void setEmail(String email) {
        Paper.book(DB_USER).write(EMAIL, email);
    }

    public static String getEmail() {
        return Paper.book(DB_USER).read(EMAIL);
    }


    public static void setDarkTheme(int darkTheme) {
        Paper.book(DB_USER).write(SELECTED_DARK_THEME, darkTheme);
    }

    public static int getDarkTheme() {
        return Paper.book(DB_USER).read(SELECTED_DARK_THEME, 0);
    }

    public static void setFirstTimeOpenApp(boolean isFirstTime) {
        Paper.book(DB_USER).write(IS_FIRST_TIME_OPEN_APP, isFirstTime);
    }

    public static boolean isFirstTimeOpenApp() {
        return Paper.book(DB_USER).read(IS_FIRST_TIME_OPEN_APP, true);
    }


    public static String getDeviceParams() {
        return Paper.book(DB_DEVICE).read(DEVICE_PARAMS);
    }

    public static String getUniqueId() {
        return Paper.book(DB_DEVICE).read(UNIQUE_ID);
    }

    public static String getDeviceId() {
        return Paper.book(DB_DEVICE).read(DEVICE_ID);
    }

    private static Enums.LANGUAGE selectedLanguage = getSelectedLastLanguage();

    public static void setSelectedLastLanguage(Enums.LANGUAGE selectedLastLanguage) {
        Paper.book(DB_USER).write(SELECTED_LAST_LANGUAGE, selectedLastLanguage);
    }

    public static Enums.LANGUAGE getSelectedLastLanguage() {
        return Paper.book(DB_USER).read(SELECTED_LAST_LANGUAGE, Enums.LANGUAGE.ENGLISH);
    }

    public static void setShouldShowLanguagePopupSplash(boolean shouldShowLanguagePopupSplash) {
        Paper.book(DB_USER).write(SHOULD_SHOW_LANGUAGE_POPUP_SPLASH, shouldShowLanguagePopupSplash);
    }

    public static boolean shouldShowLanguagePopupSplash() {
        return Paper.book(DB_USER).read(SHOULD_SHOW_LANGUAGE_POPUP_SPLASH, true);
    }

    public static Enums.LANGUAGE getSelectedLanguage() {
        if (selectedLanguage == null)
            selectedLanguage = getSelectedLastLanguage();
        return selectedLanguage;
    }

    public static int getSelectedLanguageInt() {
        switch (getSelectedLanguage()) {
            case ENGLISH:
                return 1;
            case HINDI:
                return 2;
            case URDU:
                return 3;
        }
        return 1;
    }

    public static void setSelectedLanguage(Enums.LANGUAGE selectedLanguage) {
        setShouldShowLanguagePopupSplash(false);
        MyService.selectedLanguage = selectedLanguage;
        setSelectedLastLanguage(MyService.selectedLanguage);
        BaseActivity.sendBroadCast(ICommonValues.BROADCAST_LANGUAGE_CHANGED);
    }

    public static void addFavorite(String contentId) {
        if (!MyService.isUserLogin())
            return;
        if (TextUtils.isEmpty(contentId))
            return;
        ArrayList<String> allFavContentIds = Paper.book(DB_FAVORITE).read(CONTENT_IDS, new ArrayList<>());
        if (!allFavContentIds.contains(contentId)) {
            allFavContentIds.add(contentId);
            Paper.book(DB_FAVORITE).write(CONTENT_IDS, allFavContentIds);
        }
    }

    public static void addFavorite(String contentId, String contentTypeId) {
        if (!MyService.isUserLogin())
            return;
        if (TextUtils.isEmpty(contentId) || TextUtils.isEmpty(contentTypeId))
            return;
        contentTypeId = contentTypeId.toUpperCase();
        ArrayList<String> allContentTypeFavorites = Paper.book(DB_FAVORITE).read(contentTypeId, new ArrayList<>());
        ArrayList<String> allFavContentIds = Paper.book(DB_FAVORITE).read(CONTENT_IDS, new ArrayList<>());
        if (!allContentTypeFavorites.contains(contentId)) {
            allContentTypeFavorites.add(contentId);
            Paper.book(DB_FAVORITE).write(contentTypeId, allContentTypeFavorites);
        }
        if (!allFavContentIds.contains(contentId)) {
            allFavContentIds.add(contentId);
            Paper.book(DB_FAVORITE).write(CONTENT_IDS, allFavContentIds);
        }
    }

    public static void addFavoriteListContent(FavContentPageModel favContentPageModel) {
        if (!MyService.isUserLogin())
            return;
        if (favContentPageModel == null || TextUtils.isEmpty(favContentPageModel.getId()))
            return;
        String contentId = favContentPageModel.getId();
        String contentTypeId = favContentPageModel.getContentTypeId();
        addFavorite(contentId, contentTypeId);
        if (!Paper.book(DB_FAVORITE_LIST_CONTENT).contains(favContentPageModel.getId()))
            Paper.book(DB_FAVORITE_LIST_CONTENT).write(favContentPageModel.getId(), favContentPageModel.getJsonObject());
    }

    public static void addFavoriteDetailedContent(FavContentPageModel favContentPageModel) {
        if (!MyService.isUserLogin())
            return;
        if (favContentPageModel == null || TextUtils.isEmpty(favContentPageModel.getId()) || TextUtils.isEmpty(favContentPageModel.getContentTypeId()))
            return;
        String contentId = favContentPageModel.getId();
        String contentTypeId = favContentPageModel.getContentTypeId().toUpperCase();
        addFavorite(contentId, contentTypeId);
        if (!Paper.book(DB_FAVORITE_DETAILED_CONTENT).contains(contentId))
            Paper.book(DB_FAVORITE_DETAILED_CONTENT).write(contentId, favContentPageModel.getJsonObject());
    }

    public static boolean isFavoriteContentDetailAvailable(String contentId) {
        if (TextUtils.isEmpty(contentId))
            return false;
        return Paper.book(DB_FAVORITE_DETAILED_CONTENT).contains(contentId);
    }

    public static void removeFavorite(FavContentPageModel favContentPageModel) {
        String contentId = favContentPageModel.getId();
        String contentTypeId = favContentPageModel.getContentTypeId();
        contentTypeId = contentTypeId.toUpperCase();
        removeFavorite(contentId, contentTypeId);
    }

    public static void removeFavorite(String contentId) {
        ArrayList<String> allFavContentIds = Paper.book(DB_FAVORITE).read(CONTENT_IDS, new ArrayList<>());
        allFavContentIds.remove(contentId);
        Paper.book(DB_FAVORITE).write(CONTENT_IDS, allFavContentIds);
//        Paper.book(DB_FAVORITE_DETAILED_CONTENT).delete(contentId);
//        Paper.book(DB_FAVORITE_LIST_CONTENT).delete(contentId);
//        Paper.book(DB_FAVORITE_LIST_OTHER).delete(contentId);
    }

    public static void removeFavorite(String contentId, String contentTypeId) {
        contentTypeId = contentTypeId.toUpperCase();
        ArrayList<String> allContentTypeFavorites = Paper.book(DB_FAVORITE).read(contentTypeId, new ArrayList<>());
        ArrayList<String> allFavContentIds = Paper.book(DB_FAVORITE).read(CONTENT_IDS, new ArrayList<>());
        allContentTypeFavorites.remove(contentId);
        allFavContentIds.remove(contentId);
        Paper.book(DB_FAVORITE).write(contentTypeId, allContentTypeFavorites);
        Paper.book(DB_FAVORITE).write(CONTENT_IDS, allFavContentIds);
        Paper.book(DB_FAVORITE_DETAILED_CONTENT).delete(contentId);
        Paper.book(DB_FAVORITE_LIST_CONTENT).delete(contentId);
        Paper.book(DB_FAVORITE_LIST_OTHER).delete(contentId);
    }

    public static void removeAllFavorite() {
        Paper.book(DB_FAVORITE).destroy();
        Paper.book(DB_FAVORITE_DETAILED_CONTENT).destroy();
        Paper.book(DB_FAVORITE_LIST_CONTENT).destroy();
        Paper.book(DB_SAVED_IMAGE_SHAYARI).destroy();
        Paper.book(DB_SAVED_IMAGE_SHAYARI_LIST_CONTENT).destroy();
        Paper.book(DB_FAVORITE_LIST_OTHER).destroy();
        // Paper.book(DB_SAVED_ALL_CONTENT_TYPE_LIST).destroy();
    }

    public static void removeAllDetailedFavoriteContent() {
        Paper.book(DB_FAVORITE_DETAILED_CONTENT).destroy();
    }

    public static boolean isFavorite(String contentId) {
        return Paper.book(DB_FAVORITE).read(CONTENT_IDS, new ArrayList<>()).contains(contentId);
    }

    public static boolean isFavoriteListContentAvailable(String contentId) {
        return Paper.book(DB_FAVORITE_LIST_CONTENT).read(contentId, null) != null;
    }

    public static boolean isFavoriteListOtherAvailable(String contentId) {
        return Paper.book(DB_FAVORITE_LIST_OTHER).read(contentId, null) != null;
    }

    public static FavContentPageModel getFavorite(String contentId) {
        return new FavContentPageModel(Paper.book(DB_FAVORITE_LIST_CONTENT).read(contentId, new JSONObject()));
    }

    public static <T> T getFavoriteOther(String contentId, Class<T> cls) {
        Constructor<?> cons = null;
        Object object = null;
        try {
            cons = cls.getConstructor(JSONObject.class);
            object = cons.newInstance(Paper.book(DB_FAVORITE_LIST_OTHER).read(contentId, new JSONObject()));
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (InstantiationException ignored) {
        } catch (InvocationTargetException ignored) {
        }
        return (T) object;
    }

    public static FavContentPageModel getDetailedFavorite(String contentId) {
        return new FavContentPageModel(Paper.book(DB_FAVORITE_DETAILED_CONTENT).read(contentId, new JSONObject()));
    }

    public static ArrayList<FavContentPageModel> getAllFavorite() {
        List<String> allKeys = Paper.book(DB_FAVORITE_LIST_CONTENT).getAllKeys();
        if (CollectionUtils.isEmpty(allKeys))
            return new ArrayList<>();
        ArrayList<FavContentPageModel> favContentPageModels = new ArrayList<>(allKeys.size());
        for (String contentId : allKeys) {
            FavContentPageModel favContentPageModel = getFavorite(contentId);
            if (!TextUtils.isEmpty(favContentPageModel.getId()) && isFavorite(favContentPageModel.getId()))
                favContentPageModels.add(favContentPageModel);
        }
        return favContentPageModels;
    }


    private static ArrayList<FavContentPageModel> getAllFavorite(String contentTypeId) {
        ArrayList<String> allContentTypeFavoritesKeys = Paper.book(DB_FAVORITE).read(contentTypeId, new ArrayList<>());
        if (CollectionUtils.isEmpty(allContentTypeFavoritesKeys))
            allContentTypeFavoritesKeys = new ArrayList<>();
        ArrayList<FavContentPageModel> favContentPageModels = new ArrayList<>(allContentTypeFavoritesKeys.size());
        for (String contentId : allContentTypeFavoritesKeys) {
            FavContentPageModel favContentPageModel = getFavorite(contentId);
            if (!TextUtils.isEmpty(favContentPageModel.getId()) && isFavorite(favContentPageModel.getId()))
                favContentPageModels.add(favContentPageModel);
        }
        return favContentPageModels;
    }

    public static TreeMap<ContentType, ArrayList<FavContentPageModel>> getAllFavoriteListWithSections() {
        List<String> allContentTypeIdsKeys = Paper.book(DB_FAVORITE).getAllKeys();
        if (CollectionUtils.isEmpty(allContentTypeIdsKeys))
            allContentTypeIdsKeys = new ArrayList<>();
        TreeMap<ContentType, ArrayList<FavContentPageModel>> contentTypeHashMap = new TreeMap<>();
        for (String contentTypeId : allContentTypeIdsKeys) {
            if (!isDiscardedContentType(contentTypeId)) {
                ArrayList<FavContentPageModel> favContentPageModels = getAllFavorite(contentTypeId);
                if (!CollectionUtils.isEmpty(favContentPageModels))
                    contentTypeHashMap.put(MyHelper.getContentById(contentTypeId), favContentPageModels);
            }
        }
        return contentTypeHashMap;
    }

    private static boolean isDiscardedContentType(String contentTypeId) {
        return (CONTENT_IDS.equalsIgnoreCase(contentTypeId) ||
                MyConstants.FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID.equalsIgnoreCase(contentTypeId) ||
                MyConstants.FAV_WORD_CONTENT_TYPE_ID.equalsIgnoreCase(contentTypeId) ||
                MyConstants.FAV_POET_CONTENT_TYPE_ID.equalsIgnoreCase(contentTypeId) ||
                MyConstants.FAV_T20_CONTENT_TYPE_ID.equalsIgnoreCase(contentTypeId) ||
                MyConstants.FAV_OCCASION_CONTENT_TYPE_ID.equalsIgnoreCase(contentTypeId) ||
                MyConstants.FAV_SHAYARI_COLLECTION_CONTENT_TYPE_ID.equalsIgnoreCase(contentTypeId) ||
                MyConstants.FAV_PROSE_COLLECTION_CONTENT_TYPE_ID.equalsIgnoreCase(contentTypeId));
    }

    public static void addSearchKeywordHistory(String keyword) {
        if (TextUtils.isEmpty(keyword))
            return;
        ArrayList<String> wordHistory = Paper.book(DB_SEARCH_KEYWORDS).read(KEYWORDS, new ArrayList<>());
        keyword = keyword.trim();
        for (String currentSavedKeyword : wordHistory) {
            if (currentSavedKeyword.toUpperCase().contentEquals(keyword.toUpperCase())) {
                wordHistory.remove(currentSavedKeyword);
                break;
            }
        }
        wordHistory.add(0, keyword);
        Paper.book(DB_SEARCH_KEYWORDS).write(KEYWORDS, wordHistory);
    }

    public static void deleteSearchKeywordHistory(String keyword) {
        if (TextUtils.isEmpty(keyword))
            return;
        ArrayList<String> wordHistory = Paper.book(DB_SEARCH_KEYWORDS).read(KEYWORDS, new ArrayList<>());
        keyword = keyword.trim();
        for (String currentSavedKeyword : wordHistory) {
            if (currentSavedKeyword.toUpperCase().contentEquals(keyword.toUpperCase())) {
                wordHistory.remove(currentSavedKeyword);
                break;
            }
        }
        wordHistory.remove(keyword);
        Paper.book(DB_SEARCH_KEYWORDS).write(KEYWORDS, wordHistory);
    }

    public static ArrayList<String> getSearchKeywordHistory() {
        return Paper.book(DB_SEARCH_KEYWORDS).read(KEYWORDS, new ArrayList<>());
    }

    public static void saveContentType(ContentType contentType) {
        String contentTypeId = contentType.getContentId();
        if (TextUtils.isEmpty(contentTypeId))
            return;
        contentTypeId = contentTypeId.toUpperCase();
        ArrayList<String> allContentTypeIds = Paper.book(DB_SAVED_ALL_CONTENT_TYPE).read(SAVED_CONTENT_TYPE_IDS, new ArrayList<>());
        if (!allContentTypeIds.contains(contentTypeId)) {
            allContentTypeIds.add(contentTypeId);
            Paper.book(DB_SAVED_ALL_CONTENT_TYPE).write(SAVED_CONTENT_TYPE_IDS, allContentTypeIds);
        }
        if (!Paper.book(DB_SAVED_ALL_CONTENT_TYPE_LIST).contains(contentTypeId))
            Paper.book(DB_SAVED_ALL_CONTENT_TYPE_LIST).write(contentTypeId, contentType.getJsonObject());
    }

    public static void removeContentType(String contentTypeId) {
        if (TextUtils.isEmpty(contentTypeId))
            return;
        contentTypeId = contentTypeId.toUpperCase();
        ArrayList<String> allFavContentIds = Paper.book(DB_SAVED_ALL_CONTENT_TYPE).read(SAVED_CONTENT_TYPE_IDS, new ArrayList<>());
        allFavContentIds.remove(contentTypeId);
        Paper.book(DB_SAVED_ALL_CONTENT_TYPE).write(SAVED_CONTENT_TYPE_IDS, allFavContentIds);
        Paper.book(DB_SAVED_ALL_CONTENT_TYPE_LIST).delete(contentTypeId);
    }

    public static ArrayList<ContentType> getAllContentType() {
        List<String> allKeys = Paper.book(DB_SAVED_ALL_CONTENT_TYPE_LIST).getAllKeys();
        if (CollectionUtils.isEmpty(allKeys))
            return new ArrayList<>();
        ArrayList<ContentType> contentTypesDetails = new ArrayList<>(allKeys.size());
        for (String contentTypeId : allKeys)
            contentTypesDetails.add(getContentType(contentTypeId));
        return contentTypesDetails;
    }


    public static boolean isImageShayariSaved(String imageShayariId) {
        if (TextUtils.isEmpty(imageShayariId))
            return false;
        imageShayariId = imageShayariId.toUpperCase();
        return Paper.book(DB_SAVED_IMAGE_SHAYARI).read(SAVED_IMAGE_SHAYARI_IDS, new ArrayList<>()).contains(imageShayariId);
    }

    public static void saveImageShayari(ShayariImage shayariImageDetail) {
        String imageShayariId = shayariImageDetail.getId();
        if (TextUtils.isEmpty(imageShayariId))
            return;
        imageShayariId = imageShayariId.toUpperCase();
        ArrayList<String> allFavImageShayariIds = Paper.book(DB_SAVED_IMAGE_SHAYARI).read(SAVED_IMAGE_SHAYARI_IDS, new ArrayList<>());
        if (!allFavImageShayariIds.contains(imageShayariId)) {
            allFavImageShayariIds.add(imageShayariId);
            Paper.book(DB_SAVED_IMAGE_SHAYARI).write(SAVED_IMAGE_SHAYARI_IDS, allFavImageShayariIds);
        }
        if (!Paper.book(DB_SAVED_IMAGE_SHAYARI_LIST_CONTENT).contains(imageShayariId))
            Paper.book(DB_SAVED_IMAGE_SHAYARI_LIST_CONTENT).write(imageShayariId, shayariImageDetail.getJsonObject());
    }

    public static void removeImageShayari(String imageShayariId) {
        if (TextUtils.isEmpty(imageShayariId))
            return;
        imageShayariId = imageShayariId.toUpperCase();
        ArrayList<String> allFavContentIds = Paper.book(DB_SAVED_IMAGE_SHAYARI).read(SAVED_IMAGE_SHAYARI_IDS, new ArrayList<>());
        allFavContentIds.remove(imageShayariId);
        Paper.book(DB_SAVED_IMAGE_SHAYARI).write(SAVED_IMAGE_SHAYARI_IDS, allFavContentIds);
        Paper.book(DB_SAVED_IMAGE_SHAYARI_LIST_CONTENT).delete(imageShayariId);
    }

    public static ArrayList<ShayariImage> getAllSavedImageShayari() {
        List<String> allKeys = Paper.book(DB_SAVED_IMAGE_SHAYARI_LIST_CONTENT).getAllKeys();
        if (CollectionUtils.isEmpty(allKeys))
            return new ArrayList<>();
        ArrayList<ShayariImage> shayariImageDetails = new ArrayList<>(allKeys.size());
        for (String imageShayariId : allKeys)
            shayariImageDetails.add(getImageShayari(imageShayariId));
        return shayariImageDetails;
    }

    private static ShayariImage getImageShayari(String imageShayariId) {
        return new ShayariImage(Paper.book(DB_SAVED_IMAGE_SHAYARI_LIST_CONTENT).read(imageShayariId, new JSONObject()));
    }

    private static ContentType getContentType(String contentTypeId) {
        return new ContentType(Paper.book(DB_SAVED_ALL_CONTENT_TYPE_LIST).read(contentTypeId, new JSONObject()));
    }

    public static boolean isOfflineSaveEnable() {
        return Paper.book(DB_USER).read(IS_OFFLINE_SAVE_ACTIVE, true);
    }

    public static void setIsOfflineSaveEnable(boolean isOfflineSaveEnable) {
        Paper.book(DB_USER).write(IS_OFFLINE_SAVE_ACTIVE, isOfflineSaveEnable);
    }

    public static ArrayList<ShayariImage> getAllFavoriteShayariImage() {
        return getAllFavoriteOther(MyConstants.FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID, ShayariImage.class);
    }

    public static ArrayList<FavoriteDictionary> getAllFavoriteWordDictionary() {
        return getAllFavoriteOther(MyConstants.FAV_WORD_CONTENT_TYPE_ID, FavoriteDictionary.class);
    }

    public static ArrayList<FavoritePoet> getAllFavoritePoet() {
        return getAllFavoriteOther(MyConstants.FAV_POET_CONTENT_TYPE_ID, FavoritePoet.class);
    }

    public static ArrayList<String> getAllFavoriteT20Shayari() {
        return Paper.book(DB_FAVORITE).read(MyConstants.FAV_T20_CONTENT_TYPE_ID.toUpperCase(), new ArrayList<>());
    }

    public static ArrayList<String> getAllFavoriteOccasion() {
        return Paper.book(DB_FAVORITE).read(MyConstants.FAV_OCCASION_CONTENT_TYPE_ID.toUpperCase(), new ArrayList<>());
    }

    public static ArrayList<String> getAllFavoriteShayariCollection() {
        return Paper.book(DB_FAVORITE).read(MyConstants.FAV_SHAYARI_COLLECTION_CONTENT_TYPE_ID.toUpperCase(), new ArrayList<>());
    }

    public static ArrayList<String> getAllFavoriteProseCollection() {
        return Paper.book(DB_FAVORITE).read(MyConstants.FAV_PROSE_COLLECTION_CONTENT_TYPE_ID.toUpperCase(), new ArrayList<>());
    }

    private static <T> ArrayList<T> getAllFavoriteOther(String contentTypeId, Class<T> cls) {
        if (TextUtils.isEmpty(contentTypeId))
            return null;
        contentTypeId = contentTypeId.toUpperCase();
        ArrayList<String> allContentTypeFavoritesKeys = Paper.book(DB_FAVORITE).read(contentTypeId, new ArrayList<>());
        if (CollectionUtils.isEmpty(allContentTypeFavoritesKeys))
            allContentTypeFavoritesKeys = new ArrayList<>();
        ArrayList<T> shayariImages = new ArrayList<>(allContentTypeFavoritesKeys.size());
        for (String contentId : allContentTypeFavoritesKeys) {
            if (!TextUtils.isEmpty(contentId) && isFavorite(contentId))
                shayariImages.add(getFavoriteOther(contentId, cls));
        }
        return shayariImages;
    }

    public static void addFavoriteOther(BaseOtherFavModel otherFavModel) {
        if (!MyService.isUserLogin())
            return;
        if (otherFavModel == null || TextUtils.isEmpty(otherFavModel.getId()) || TextUtils.isEmpty(otherFavModel.getContentTypeId()))
            return;
        String contentId = otherFavModel.getId();
        String contentTypeId = otherFavModel.getContentTypeId();
        contentTypeId = contentTypeId.toUpperCase();
        addFavorite(contentId, contentTypeId);
        if (!Paper.book(DB_FAVORITE_LIST_OTHER).contains(contentId))
            Paper.book(DB_FAVORITE_LIST_OTHER).write(contentId, otherFavModel.getJsonObject());
    }

    public static void addFavoriteOther(String contentId, Enums.FAV_TYPES favType) {
        if (!MyService.isUserLogin())
            return;
        if (favType == null || TextUtils.isEmpty(contentId))
            return;
        String contentTypeId = MyHelper.getContentByFavType(favType).getContentId().toUpperCase();
        addFavorite(contentId, contentTypeId);
    }

    public static void removeFavoriteOther(String contentId, Enums.FAV_TYPES favType) {
        String contentTypeId = MyHelper.getContentByFavType(favType).getContentId().toUpperCase();
        removeFavorite(contentId, contentTypeId);
    }

    public static void removeFavoriteOther(BaseOtherFavModel otherFavModel) {
        String contentId = otherFavModel.getId();
        String contentTypeId = otherFavModel.getContentTypeId();
        contentTypeId = contentTypeId.toUpperCase();
        removeFavorite(contentId, contentTypeId);
    }

    public static int getTotalFavoriteCount() {
        List<String> allContentTypeIdsKeys = Paper.book(DB_FAVORITE).getAllKeys();
        if (CollectionUtils.isEmpty(allContentTypeIdsKeys))
            allContentTypeIdsKeys = new ArrayList<>();
        int count = 0;
        for (String contentTypeId : allContentTypeIdsKeys)
            if (!isDiscardedContentType(contentTypeId))
                count += Paper.book(DB_FAVORITE).read(contentTypeId, new ArrayList<>()).size();
        count += Paper.book(DB_FAVORITE).read(MyConstants.FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID.toUpperCase(), new ArrayList<>()).size();
        count += Paper.book(DB_FAVORITE).read(MyConstants.FAV_WORD_CONTENT_TYPE_ID.toUpperCase(), new ArrayList<>()).size();
        count += Paper.book(DB_FAVORITE).read(MyConstants.FAV_POET_CONTENT_TYPE_ID.toUpperCase(), new ArrayList<>()).size();
        return count;
    }

    public static void setLastAppVersion(String lastAppVersion) {
        Paper.book(DB_APP_VERSIONING).write(LAST_APP_VERSION, lastAppVersion);
    }

    public static String getLastAppVersion() {
        return Paper.book(DB_APP_VERSIONING).read(LAST_APP_VERSION, "1.0");
    }

    public static void setLastSkippedVersion(String lastSkippedVersion) {
        Paper.book(DB_APP_VERSIONING).write(LAST_SKIPPED_VERSION, lastSkippedVersion);
    }

    public static String getLastSkippedVersion() {
        return Paper.book(DB_APP_VERSIONING).read(LAST_SKIPPED_VERSION, "");
    }

    public static void setLastUpdateDialogShowTime(long time) {
        Paper.book(DB_APP_VERSIONING).write(LAST_UPDATE_DIALOG_SHOW_TIME, time);
    }

    private static long getLastUpdateDialogShowTime() {
        return Paper.book(DB_APP_VERSIONING).read(LAST_UPDATE_DIALOG_SHOW_TIME, 0L);
    }

    public static boolean isLastUpdateDialogShowTimeExpired() {
        long lastPopupShowTime = getLastUpdateDialogShowTime();
        long updateDialogShowInterval = 7 * 24 * 60 * 60 * 1000;
        return System.currentTimeMillis() - lastPopupShowTime > updateDialogShowInterval;
    }

    public static class MyFavService {

        private static CopyOnWriteArrayList<ShayariImage> shayariImages = new CopyOnWriteArrayList<>();
        private static CopyOnWriteArrayList<FavoriteDictionary> favoriteDictionary = new CopyOnWriteArrayList<>();
        private static CopyOnWriteArrayList<FavoritePoet> favoritePoet = new CopyOnWriteArrayList<>();
        private static TreeMap<ContentType, ArrayList<FavContentPageModel>> allFavoriteSections = new TreeMap<>();
        private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        public static void resetAllFavorites() {
            shayariImages.clear();
            shayariImages.addAll(MyService.getAllFavoriteShayariImage());

            favoriteDictionary.clear();
            favoriteDictionary.addAll(MyService.getAllFavoriteWordDictionary());

            favoritePoet.clear();
            favoritePoet.addAll(MyService.getAllFavoritePoet());

            lock.writeLock().lock();
            allFavoriteSections.clear();
            allFavoriteSections.putAll(MyService.getAllFavoriteListWithSections());
            lock.writeLock().unlock();
        }

        public static void removeFav(String contentId, Enums.FAV_TYPES favType) {
            if (TextUtils.isEmpty(contentId) || favType == null)
                return;
            if (favType == Enums.FAV_TYPES.IMAGE_SHAYRI) {
                for (ShayariImage shayariImage : shayariImages) {
                    if (shayariImage.getId().toUpperCase().contentEquals(contentId.toUpperCase())) {
                        shayariImages.remove(shayariImage);
                        break;
                    }
                }
            } else if (favType == Enums.FAV_TYPES.WORD) {
                for (FavoriteDictionary currWord : favoriteDictionary) {
                    if (currWord.getId().toUpperCase().contentEquals(contentId.toUpperCase())) {
                        favoriteDictionary.remove(currWord);
                        break;
                    }
                }
            } else if (favType == Enums.FAV_TYPES.ENTITY) {
                for (FavoritePoet currPoet : favoritePoet) {
                    if (currPoet.getId().toUpperCase().contentEquals(contentId.toUpperCase())) {
                        favoritePoet.remove(currPoet);
                        break;
                    }
                }
            } else if (favType == Enums.FAV_TYPES.CONTENT) {
                ContentType contentType = getFavorite(contentId).getContentType();
                if (contentType != null && !TextUtils.isEmpty(contentType.getContentId())) {
                    ArrayList<FavContentPageModel> contentPageModels = getAllFavoriteSections().get(contentType);
                    if (!CollectionUtils.isEmpty(contentPageModels)) {
                        for (FavContentPageModel favContentPageModel : contentPageModels) {
                            if (favContentPageModel.getId().toUpperCase().contentEquals(contentId.toUpperCase())) {
                                contentPageModels.remove(favContentPageModel);
                                break;
                            }
                        }
                    }
                }
            }
        }

        public static TreeMap<ContentType, ArrayList<FavContentPageModel>> getAllFavoriteSections() {
            return allFavoriteSections;
        }

        public static CopyOnWriteArrayList<ShayariImage> getShayariImages() {
            return shayariImages;
        }

        public static CopyOnWriteArrayList<FavoriteDictionary> getFavoriteDictionary() {
            return favoriteDictionary;
        }

        public static CopyOnWriteArrayList<FavoritePoet> getFavoritePoet() {
            return favoritePoet;
        }
    }

    private static MediaPlayer renderContentMediaPlayer;

    public static MediaPlayer getRenderContentMediaPlayer() {
        if (renderContentMediaPlayer == null)
            renderContentMediaPlayer = new MediaPlayer();
        return renderContentMediaPlayer;
    }

    public static void createNewRenderContentMediaPlayer() {
        renderContentMediaPlayer = new MediaPlayer();
    }


    public static void setLastConfigUpdateTime(long time) {
        Paper.book(DB_DEVICE).write(LAST_UPDATE_CONFIG_TIME, time);
    }

    private static long getLastConfigUpdateTime() {
        return Paper.book(DB_DEVICE).read(LAST_UPDATE_CONFIG_TIME, 0L);
    }

    public static boolean isLastConfigUpdateTimeExpired() {
        long lastPopupShowTime = getLastConfigUpdateTime();
        return (System.currentTimeMillis() - lastPopupShowTime) > TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
    }
}
