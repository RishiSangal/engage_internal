package com.example.sew.common;

import android.text.TextUtils;

import com.example.sew.helpers.MyService;

public class MyConstants {

    public static final int APP_SERVER_STAGING = 2;
    public static final int APP_SERVER_PRODUCTION = 1;
    public static final String TMP_POET_PROFILE_ID = "-1";
    public static final String TMP_AUDIO_ID = "-2";
    public static final String TMP_VIDEO_ID = "-3";
    public static final String TMP_IMAGE_SHAYRI_ID = "-4";
    public static final String GHAZAL_ID = "43d60a15-0b49-4caf-8b74-0fcdddeb9f83";
    public static final String GHAZAL_ID_1 = "37ebbfb5-7ea3-4a00-b71f-3b9d62ce784b";
    public static final String NAZM_ID = "c54c4d8b-7e18-4f70-8312-e1c2cc028b0b";
    public static final String SHER_ID = "f722d5dc-45da-41ec-a439-900df702a3d6";
    public static final String GEET_ID = "2abe2a3a-a4b5-4f98-9971-197e61312202";
    public static final String RUBAAI_ID = "94ec8d36-9c2f-4847-a577-3fe31ddbbc62";
    public static final String QITA_ID = "a13c352e-03d3-43fe-ace0-f64918f6d28a";
    public static final String DOHA_ID = "a12d7cf4-60c2-4d2b-85e4-3f342d008472";
    public static final String FAV_IMAGE_SHAYARI_CONTENT_TYPE_ID = "36dbb939-f84f-4720-94ca-9941176IMAGE_SHAYRI";
    public static final String FAV_WORD_CONTENT_TYPE_ID = "36dbb939-f84f-4720-94ca-9941176WORD";
    public static final String FAV_POET_CONTENT_TYPE_ID = "36dbb939-f84f-4720-94ca-9941176POET";
    public static final String FAV_T20_CONTENT_TYPE_ID = "36dbb939-f84f-4720-94ca-9941176baT20";
    public static final String FAV_OCCASION_CONTENT_TYPE_ID = "36dbb939-f84f-4720-94ca-9941176baOCCASION";
    public static final String FAV_SHAYARI_COLLECTION_CONTENT_TYPE_ID = "36dbb939-f84f-4720-94ca-9941176baSHAYARI_COLLECTION";
    public static final String FAV_PROSE_COLLECTION_CONTENT_TYPE_ID = "36dbb939-f84f-4720-94ca-9941176baPROSE_COLLECTION";
    public static final String SPLUNK_API_KEY = "c07b3887";

    public static final String GHAZAL_EDITOR_CHOICE_ID = "18be4e82-1a8d-4750-b1ff-47b7e95bd458";
    public static final String GHAZAL_BEGINNER_ID = "BFCE2895-551E-4C07-B9D7-F7FF0DA9F9F5";
    public static final String GHAZAL_100_FAMOUS__ID = "60e03626-6a76-481c-b4d6-cdd6173da417";
    public static final String GHAZAL_HUMOR_ID = "7DF9047A-D6B7-4CB0-8180-C29F9AD3B1B7";

    public static final String NAZM_EDITOR_CHOICE_ID = "18BE4E82-1A8D-4750-B1FF-47B7E95BD458";
    public static final String NAZM_BEGINNER_ID = "BFCE2895-551E-4C07-B9D7-F7FF0DA9F9F5";
    public static final String NAZM_50_FAMOUS__ID = "0DBC9550-9833-4E30-A695-A63679966C69";
    public static final String NAZM_HUMOR_ID = "7DF9047A-D6B7-4CB0-8180-C29F9AD3B1B7";
    public static final String GET_APP_CONFIG = "https://world.rekhta.org/api/v5/shayari/AppConfig";

    //    private static String HOST = "http://world-staging.rekhta.org/api";

    /*staging urls*/
//    public static final String COLLECTION_IMAGE_URL = "https://rekhta-staging.rekhta.org/images/Cms/CollectionSeries/Occasions/%s_Medium.png";
//    private static final String HOST = "https://world-staging.rekhta.org/api";//staging
    /*staging urls*/

    private static String POET_IMAGE_URL_SMALL;
    private static String USER_PROFILE_IMAGE_URL;
    private static String POET_IMAGE_URL_LARGE;
    private static String POET_IMAGE_URL_ROUND;
    private static String SHAYARI_IMAGE_URL;
    private static String SHAYARI_IMAGE_URL_SMALL;
    private static String SHAYARI_IMAGE_URL_LARGE;
    private static String COLLECTION_IMAGE_URL;
    private static String DICTIONARY_AUDIO_URL;
    private static String LOGIN_URL;
    private static String POET_LIST_WITH_PAGING;
    private static String FORGOT_PASSWORD;
    private static String SIGNUP;
    private static String SOCIAL_LOGIN;
    private static String LOGOUT_URL;
    private static String LOGOUT_V5_URL;
    private static String SHER_TAGS_LIST;
    @Deprecated
    private static String POET_PROFILE;
    private static String POET_COMPLETE_PROFILE;
    private static String CONTENT_LIST_PAGING;
    private static String COUPLET_LIST_WITH_PAGING;
    private static String AUDIO_LIST_BY_POET_WITH_PAGING;
    private static String VIDEO_LIST_BY_POET_WITH_PAGING;
    private static String SEARCH_ALL;
    private static String GET_CONTENT_TYPE_ID;
    private static String GET_CONTENT_TYPE_TAB_BY_COLLECTION_TYPE;
    private static String GET_COLLECTION_LIST_BY_COLLECTION_TYPE;
    private static String SEARCH_CONTENT_BY_TYPE;
    private static String GET_WORLD_MEANING;
    private static String GET_TAGS_LIST;
    private static String GET_IMAGE_INFO;
    private static String GET_T20_SHER;
    private static String GET_OCCASIONS;
    private static String GET_CONTENT_BY_ID;
    private static String GET_BOTTOM_CONTENT_BY_ID_SLUG;
    private static String GET_SHAYARI_IMAGE_WITH_SEARCH;
    private static String GET_SHAYARI_IMAGE_DETAIL;
    private static String POST_SUBMIT_CRITIQUE;
    private static String GET_WORD_OF_THE_DAY;
    private static String GET_DICTIONARY_MEANING;
    private static String GET_PLATTS_DICTIONARY_MEANING;
    private static String GET_IMAGE_DICTIONARY_MEANING;
    private static String GET_FAVORITE_LIST_WITH_SPECIFIC_CONTENT;
    private static String GET_FAVORITE_LIST_CONTENT_WITH_PAGING;
    private static String GET_FAVORITE_LIST_WITH_PAGING;
    private static String GET_ALL_FAVORITE_LIST_WITH_PAGING_V5;
    private static String MARK_FAVORITE;
    private static String REMOVE_FAVORITE;
    private static String REMOVE_ALL_FAVORITE_IMAGE_SHAYARI;
    private static String GET_HOME_PAGE_COLLECTION;
    private static String GET_EXPLORE_COLLECTION;
    private static String GET_SETTINGS;
    private static String SET_USER_SETTING;
    private static String POST_PUSH_REGISTRATION;
    private static String GET_APP_INFO;
    private static String POST_USER_APP_INFO;
    private static String GET_YOUTUBE_KEY;
    private static String GET_LAZY_CONTENT;
    private static String GET_CONTENT_TYPE_TABBY_TYPE;
    private static String POST_SEARCH_ONLOAD_DEMAND;
    private static String GET_COUNTING_SUMMARYBY_TARGETID;
    // public static final String GET_BOTTOM_CONTENT_BY_ID ;
    // public static final String REMOVE_FAVORITE ;
    //public static final String MARK_FAVORITE ;

    /*comment section api*/
    private static String GET_ALL_COMMENTS_BY_TARGET_ID;
    private static String GET_REPLY_BY_PARENT_ID;
    private static String POST_USER_COMMENTS;
    private static String GET_REMOVE_COMMENT;
    private static String GET_MARK_LIKE_DISLIKE;
    private static String POST_USER_COMPLAIN;
    private static String GET_COMPLAIN_TYPES;


    public static void init() {
        GET_LAZY_CONTENT = "";

        String HOST_BASE_URL = String.format("%s/api", MyService.getBaseURL());
        String HOST_CDN_URL = String.format("%s/api", MyService.getCdnURL());
        String HOST_MEDIA_URL = MyService.getMediaURL();

        POET_IMAGE_URL_SMALL = HOST_MEDIA_URL + "/images/Shayar/%s.png";
        USER_PROFILE_IMAGE_URL = HOST_MEDIA_URL + "/Images/User/%s";
        POET_IMAGE_URL_LARGE = HOST_MEDIA_URL + "/images/Shayar/Large/%s.png";
        POET_IMAGE_URL_ROUND = HOST_MEDIA_URL + "/images/Shayar/Round/%s.png";
        SHAYARI_IMAGE_URL = HOST_MEDIA_URL + "/Images/ShayariImages/%s_medium.jpg";
        SHAYARI_IMAGE_URL_SMALL = HOST_MEDIA_URL + "/Images/ShayariImages/%s_small.png";
        SHAYARI_IMAGE_URL_LARGE = HOST_MEDIA_URL + "/Images/ShayariImages/%s_large.png";
        COLLECTION_IMAGE_URL = HOST_MEDIA_URL + "/images/Cms/CollectionSeries/Occasions/%s_Medium.png";
        DICTIONARY_AUDIO_URL = HOST_MEDIA_URL + "/Images/SiteImages/DictionaryAudio/%s.mp3";

        LOGIN_URL = HOST_BASE_URL + "/V4_ApiAccount/V4Login";
        FORGOT_PASSWORD = HOST_BASE_URL + "/V4_ApiAccount/V4ForgotPassword";
        SIGNUP = HOST_BASE_URL + "/V4_ApiAccount/V4Register";
        SOCIAL_LOGIN = HOST_BASE_URL + "/V4_ApiAccount/V4LoginExternal";
        LOGOUT_URL = HOST_BASE_URL + "/V4_ApiAccount/V4Logout";
        LOGOUT_V5_URL = HOST_BASE_URL + "/V4_ApiAccount/V5Logout";
        POST_SUBMIT_CRITIQUE = HOST_BASE_URL + "/v5/shayari/Critique";// Where we need to put this api as it is called by both
        GET_FAVORITE_LIST_WITH_SPECIFIC_CONTENT = HOST_BASE_URL + "/v5/shayari/GetFavoriteListWithSpecificContent";
        GET_FAVORITE_LIST_CONTENT_WITH_PAGING = HOST_BASE_URL + "/v4/shayari/GetFavoriteListContentWithPaging/";
        GET_FAVORITE_LIST_WITH_PAGING = HOST_BASE_URL + "/v5/shayari/GetFavoriteListWithPaging/";
        GET_ALL_FAVORITE_LIST_WITH_PAGING_V5 = HOST_BASE_URL + "/v5/shayari/GetAllFavoriteListWithPaging";
        MARK_FAVORITE = HOST_BASE_URL + "/v5/shayari/MarkAllFavorite";
        REMOVE_FAVORITE = HOST_BASE_URL + "/v5/shayari/RemoveAllFavorite";
        REMOVE_ALL_FAVORITE_IMAGE_SHAYARI = HOST_BASE_URL + "/v5/shayari/RemoveAllFavoriteListByType";
        GET_SETTINGS = HOST_BASE_URL + "/v5/shayari/GetUserSettings";
        SET_USER_SETTING = HOST_BASE_URL + "/v5/shayari/SetUserSetting";
        POST_PUSH_REGISTRATION = HOST_BASE_URL + "/v5/shayari/DeviceRegisterForPush";
        POST_USER_APP_INFO = HOST_BASE_URL + "/v5/shayari/UserAppInfo";
        GET_COUNTING_SUMMARYBY_TARGETID = HOST_BASE_URL + "/v5/shayari/GetCountingSummaryByTargetId";

        SHER_TAGS_LIST = HOST_CDN_URL + "/v5/shayari/GetTagsList";
        POET_LIST_WITH_PAGING = HOST_CDN_URL + "/v5/shayari/GetPoetsListWithPaging";
        POET_PROFILE = HOST_CDN_URL + "/v5/shayari/GetPoetProfile";
        POET_COMPLETE_PROFILE = HOST_CDN_URL + "/v5/shayari/GetPoetCompleteProfile";
        CONTENT_LIST_PAGING = HOST_CDN_URL + "/v5/shayari/GetContentListWithPaging";
        COUPLET_LIST_WITH_PAGING = HOST_CDN_URL + "/v5/shayari/GetCoupletListWithPaging";
        AUDIO_LIST_BY_POET_WITH_PAGING = HOST_CDN_URL + "/v5/shayari/GetAudioListByPoetIdWithPaging";
        VIDEO_LIST_BY_POET_WITH_PAGING = HOST_CDN_URL + "/v5/shayari/GetVideoListByPoetIdWithPaging";
        SEARCH_ALL = HOST_CDN_URL + "/v5/shayari/SearchAll";
        GET_CONTENT_TYPE_ID = HOST_CDN_URL + "/v5/shayari/GetContentTypeList";
        GET_CONTENT_TYPE_TAB_BY_COLLECTION_TYPE = HOST_CDN_URL + "/v5/shayari/GetContentTypeTabByCollectionType";
        GET_COLLECTION_LIST_BY_COLLECTION_TYPE = HOST_CDN_URL + "/v5/shayari/GetCollectionListByCollectionType";
        SEARCH_CONTENT_BY_TYPE = HOST_CDN_URL + "/v5/shayari/SearchContentByTypePageWise";
        GET_WORLD_MEANING = HOST_CDN_URL + "/v5/shayari/GetWordMeaning";
        GET_TAGS_LIST = HOST_CDN_URL + "/v5/shayari/GetTagsListWithTrendingTag";
        GET_IMAGE_INFO = HOST_CDN_URL + "/v5/shayari/GetShayariImageById";
        GET_T20_SHER = HOST_CDN_URL + "/v5/shayari/GetT20";
        GET_OCCASIONS = HOST_CDN_URL + "/v5/shayari/GetOccasionList";
        GET_CONTENT_BY_ID = HOST_CDN_URL + "/v5/shayari/GetContentById";
        GET_BOTTOM_CONTENT_BY_ID_SLUG = HOST_CDN_URL + "/v5/shayari/GetBottomContentByIdSlug";
        GET_SHAYARI_IMAGE_WITH_SEARCH = HOST_CDN_URL + "/v5/shayari/GetShayariImagesWithSearch";
        GET_SHAYARI_IMAGE_DETAIL = HOST_CDN_URL + "/v5/shayari/GetShayariImageById";
        GET_WORD_OF_THE_DAY = HOST_CDN_URL + "/v5/shayari/WordOfTheDay";
        GET_DICTIONARY_MEANING = HOST_CDN_URL + "/v5/shayari/GetRekhtaDictionaryMeanings";
        GET_PLATTS_DICTIONARY_MEANING = HOST_CDN_URL + "/v5/shayari/GetPlattsDictionaryMeanings";
        GET_IMAGE_DICTIONARY_MEANING = HOST_CDN_URL + "/v5/shayari/GetImageDictionaryMeanings";
        GET_HOME_PAGE_COLLECTION = HOST_CDN_URL + "/v5/shayari/GetHomePageCollection";
        GET_EXPLORE_COLLECTION = HOST_CDN_URL + "/v5/shayari/GetExplore";
        GET_APP_INFO = HOST_CDN_URL + "/v5/shayari/AppInfo";
        GET_YOUTUBE_KEY = HOST_CDN_URL + "/v5/shayari/GetYouTubeKey";
        GET_CONTENT_TYPE_TABBY_TYPE = HOST_CDN_URL + "/v5/shayari/GetContentTypeTabByType";
        POST_SEARCH_ONLOAD_DEMAND = HOST_CDN_URL + "/v4/shayari/SearchAllLoadOnDemand";
        GET_ALL_COMMENTS_BY_TARGET_ID = HOST_BASE_URL + "/v1/forum/GetAllCommentsByTargetId";
        GET_REPLY_BY_PARENT_ID = HOST_BASE_URL + "/v1/forum/GetReplyByParentId";
        POST_USER_COMMENTS = HOST_BASE_URL + "/v1/forum/SetUserComments";
        GET_REMOVE_COMMENT = HOST_BASE_URL + "/v1/forum/RemoveComments";
        GET_MARK_LIKE_DISLIKE = HOST_BASE_URL + "/v1/forum/MarkLikeDislike";
        POST_USER_COMPLAIN = HOST_BASE_URL + "/v1/forum/SetUserComplain";
        GET_COMPLAIN_TYPES = HOST_BASE_URL + "/v1/forum/GetComplainTypes";
    }

    public static void initIfRequired() {
        if (TextUtils.isEmpty(LOGIN_URL))
            init();
    }

    public static String getPoetImageUrlSmall() {
        initIfRequired();
        return POET_IMAGE_URL_SMALL;
    }

    public static String getUserProfileImageUrl() {
        initIfRequired();
        return USER_PROFILE_IMAGE_URL;
    }

    public static String getPoetImageUrlLarge() {
        initIfRequired();
        return POET_IMAGE_URL_LARGE;
    }

    public static String getPoetImageUrlRound() {
        initIfRequired();
        return POET_IMAGE_URL_ROUND;
    }

    public static String getShayariImageUrl() {
        initIfRequired();
        return SHAYARI_IMAGE_URL;
    }

    public static String getShayariImageUrlSmall() {
        initIfRequired();
        return SHAYARI_IMAGE_URL_SMALL;
    }

    public static String getShayariImageUrlLarge() {
        initIfRequired();
        return SHAYARI_IMAGE_URL_LARGE;
    }

    public static String getCollectionImageUrl() {
        initIfRequired();
        return COLLECTION_IMAGE_URL;
    }

    public static String getDictionaryAudioUrl() {
        initIfRequired();
        return DICTIONARY_AUDIO_URL;
    }

    public static String getLoginUrl() {
        initIfRequired();
        return LOGIN_URL;
    }

    public static String getPoetListWithPaging() {
        initIfRequired();
        return POET_LIST_WITH_PAGING;
    }

    public static String getForgotPassword() {
        initIfRequired();
        return FORGOT_PASSWORD;
    }

    public static String getSIGNUP() {
        initIfRequired();
        return SIGNUP;
    }

    public static String getSocialLogin() {
        initIfRequired();
        return SOCIAL_LOGIN;
    }

    public static String getLogoutUrl() {
        initIfRequired();
        return LOGOUT_URL;
    }

    public static String getLogoutV5Url() {
        initIfRequired();
        return LOGOUT_V5_URL;
    }

    public static String getSherTagsList() {
        initIfRequired();
        return SHER_TAGS_LIST;
    }

    public static String getPoetProfile() {
        initIfRequired();
        return POET_PROFILE;
    }

    public static String getPoetCompleteProfile() {
        initIfRequired();
        return POET_COMPLETE_PROFILE;
    }

    public static String getContentListPaging() {
        initIfRequired();
        return CONTENT_LIST_PAGING;
    }

    public static String getCoupletListWithPaging() {
        initIfRequired();
        return COUPLET_LIST_WITH_PAGING;
    }

    public static String getAudioListByPoetWithPaging() {
        initIfRequired();
        return AUDIO_LIST_BY_POET_WITH_PAGING;
    }

    public static String getVideoListByPoetWithPaging() {
        initIfRequired();
        return VIDEO_LIST_BY_POET_WITH_PAGING;
    }

    public static String getSearchAll() {
        initIfRequired();
        return SEARCH_ALL;
    }

    public static String getGetContentTypeId() {
        initIfRequired();
        return GET_CONTENT_TYPE_ID;
    }

    public static String getGetContentTypeTabByCollectionType() {
        initIfRequired();
        return GET_CONTENT_TYPE_TAB_BY_COLLECTION_TYPE;
    }

    public static String getGetCollectionListByCollectionType() {
        initIfRequired();
        return GET_COLLECTION_LIST_BY_COLLECTION_TYPE;
    }

    public static String getSearchContentByType() {
        initIfRequired();
        return SEARCH_CONTENT_BY_TYPE;
    }

    public static String getGetWorldMeaning() {
        initIfRequired();
        return GET_WORLD_MEANING;
    }


    public static String getGetTagsList() {
        initIfRequired();
        return GET_TAGS_LIST;
    }

    public static String getGetImageInfo() {
        initIfRequired();
        return GET_IMAGE_INFO;
    }

    public static String getGetT20Sher() {
        initIfRequired();
        return GET_T20_SHER;
    }

    public static String getGetOccasions() {
        initIfRequired();
        return GET_OCCASIONS;
    }

    public static String getGetContentById() {
        initIfRequired();
        return GET_CONTENT_BY_ID;
    }

    public static String getGetBottomContentByIdSlug() {
        initIfRequired();
        return GET_BOTTOM_CONTENT_BY_ID_SLUG;
    }

    public static String getGetShayariImageWithSearch() {
        initIfRequired();
        return GET_SHAYARI_IMAGE_WITH_SEARCH;
    }

    public static String getGetShayariImageDetail() {
        initIfRequired();
        return GET_SHAYARI_IMAGE_DETAIL;
    }

    public static String getPostSubmitCritique() {
        initIfRequired();
        return POST_SUBMIT_CRITIQUE;
    }

    public static String getGetWordOfTheDay() {
        initIfRequired();
        return GET_WORD_OF_THE_DAY;
    }

    public static String getGetDictionaryMeaning() {
        initIfRequired();
        return GET_DICTIONARY_MEANING;
    }

    public static String getGetPlattsDictionaryMeaning() {
        initIfRequired();
        return GET_PLATTS_DICTIONARY_MEANING;
    }

    public static String getGetImageDictionaryMeaning() {
        initIfRequired();
        return GET_IMAGE_DICTIONARY_MEANING;
    }

    public static String getGetFavoriteListWithSpecificContent() {
        initIfRequired();
        return GET_FAVORITE_LIST_WITH_SPECIFIC_CONTENT;
    }

    public static String getGetFavoriteListContentWithPaging() {
        initIfRequired();
        return GET_FAVORITE_LIST_CONTENT_WITH_PAGING;
    }

    public static String getGetFavoriteListWithPaging() {
        initIfRequired();
        return GET_FAVORITE_LIST_WITH_PAGING;
    }

    public static String getGetAllFavoriteListWithPagingV5() {
        initIfRequired();
        return GET_ALL_FAVORITE_LIST_WITH_PAGING_V5;
    }

    public static String getMarkFavorite() {
        initIfRequired();
        return MARK_FAVORITE;
    }

    public static String getRemoveFavorite() {
        initIfRequired();
        return REMOVE_FAVORITE;
    }

    public static String getRemoveAllFavoriteImageShayari() {
        initIfRequired();
        return REMOVE_ALL_FAVORITE_IMAGE_SHAYARI;
    }

    public static String getGetHomePageCollection() {
        initIfRequired();
        return GET_HOME_PAGE_COLLECTION;
    }

    public static String getGetExploreCollection() {
        initIfRequired();
        return GET_EXPLORE_COLLECTION;
    }

    public static String getGetSettings() {
        initIfRequired();
        return GET_SETTINGS;
    }

    public static String getSetUserSetting() {
        initIfRequired();
        return SET_USER_SETTING;
    }

    public static String getPostPushRegistration() {
        initIfRequired();
        return POST_PUSH_REGISTRATION;
    }

    public static String getGetAppInfo() {
        initIfRequired();
        return GET_APP_INFO;
    }

    public static String getPostUserAppInfo() {
        initIfRequired();
        return POST_USER_APP_INFO;
    }

    public static String getGetCountingSummarybyTargetid() {
        initIfRequired();
        return GET_COUNTING_SUMMARYBY_TARGETID;
    }

    public static String getGetYoutubeKey() {
        initIfRequired();
        return GET_YOUTUBE_KEY;
    }

    public static String getGetLazyContent() {
        initIfRequired();
        return GET_LAZY_CONTENT;
    }

    public static String getGetContentTypeTabbyType() {
        initIfRequired();
        return GET_CONTENT_TYPE_TABBY_TYPE;
    }

    public static String getPostSearchOnloadDemand() {
        initIfRequired();
        return POST_SEARCH_ONLOAD_DEMAND;
    }

    public static String getGetAllCommentsByTargetId() {
        initIfRequired();
        return GET_ALL_COMMENTS_BY_TARGET_ID;
    }

    public static String getGetReplyByParentId() {
        initIfRequired();
        return GET_REPLY_BY_PARENT_ID;
    }

    public static String getPostUserComments() {
        initIfRequired();
        return POST_USER_COMMENTS;
    }

    public static String getGetRemoveComment() {
        initIfRequired();
        return GET_REMOVE_COMMENT;
    }

    public static String getGetMarkLikeDislike() {
        initIfRequired();
        return GET_MARK_LIKE_DISLIKE;
    }

    public static String getPostUserComplain() {
        initIfRequired();
        return POST_USER_COMPLAIN;
    }

    public static String getGetComplainTypes() {
        initIfRequired();
        return GET_COMPLAIN_TYPES;
    }


}