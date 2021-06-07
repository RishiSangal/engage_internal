
package com.applications.engage.network



object NetworkAPIConstants {

    interface Tag {
        companion object {
            const val GET_SUPPORT_SEARCH_DATA = "GET_SUPPORT_SEARCH_DATA"
            const val GET_SEARCH_RESULT_DATA = "GET_SEARCH_RESULT_DATA"
            const val GET_SEARCH_RESULT_VIDEO_DETAIL_DATA="GET_SEARCH_RESULT_VIDEO_DETAIL_DATA"
            const val GET_SEARCH_RESULT_LESSON_DETAIL_DATA="GET_SEARCH_RESULT_LESSON_DETAIL_DATA"
            const val GET_SEARCH_RESULT_ARTICLE_DETAIL_DATA="GET_SEARCH_RESULT_ARTICLE_DETAIL_DATA"
            const val GET_SEARCH_RESULT_QUIZ_DETAIL_DATA="GET_SEARCH_RESULT_QUIZ_DETAIL_DATA"
        }
    }

    interface Url {
        companion object {
            //urls
            const val GET_SUPPORT_SEARCH_DATA = ""
            const val GET_SEARCH_RESULT_DATA = ""
            const val GET_SEARCH_RESULT_DETAIL_DATA = ""

//            const val GET_SUPPORT_SEARCH_DATA = BuildConfig.CONFIG_BASE_URL_SITECORE + "sitecore/api/layout/render/jss"
//            const val GET_SEARCH_RESULT_DATA = BuildConfig.CONFIG_BASE_URL_SITECORE + "api/search/getitems"
//            const val GET_SEARCH_RESULT_DETAIL_DATA = BuildConfig.CONFIG_BASE_URL_SITECORE + "api/search/getdetail"
        }
    }
}