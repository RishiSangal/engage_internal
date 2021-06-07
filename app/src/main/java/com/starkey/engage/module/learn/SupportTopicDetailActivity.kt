package com.starkey.engage.module.learn

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProviders
import com.applications.engage.network.NetworkAPIConstants
import com.applications.engage.view.fragments.TopicItemQuizFragment
import com.starkey.engage.R
import com.starkey.engage.Utils.clean
import com.starkey.engage.databinding.ActivitySupportTopicDetailBinding
import com.starkey.engage.ui.learn.utils.Utility
import com.starkey.engage.module.learn.fragment.ExperienceFragment
import com.starkey.engage.ui.learn.data.learn.*
import com.starkey.engage.ui.learn.viewmodel.LearnViewModel
import org.json.JSONObject


class SupportTopicDetailActivity : BaseActivity(), TopicDetailsFragment.OnStartTopicClickListener {
    private var videoModel: SearchResultVideoModel? = null
    //    private var lessonData: SearchResultLessonModel? = null
    private var quizModel: SearchResultQuizModel? = null
    private var articleData: SearchResultArticleModel? = null
    lateinit var binding: ActivitySupportTopicDetailBinding
    var searchContentId = ""
    var searchContentType = ""
    private var viewModel: LearnViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initArguments()
//        this.showTopicDetails()
        this.setListenerOnWidgets()
//        showLessonDetails()
        this.getDetailData()
    }

    var searchResultLesson : SearchResultLessonModel? = null
    private fun getDetailData() {
        showLoader()
        val requestTag = when (searchContentType) {
            SearchResultModel.CONTENT_TYPE_ARTICLE -> NetworkAPIConstants.Tag.GET_SEARCH_RESULT_ARTICLE_DETAIL_DATA
            SearchResultModel.CONTENT_TYPE_LESSON -> NetworkAPIConstants.Tag.GET_SEARCH_RESULT_LESSON_DETAIL_DATA
            SearchResultModel.CONTENT_TYPE_QUIZ -> NetworkAPIConstants.Tag.GET_SEARCH_RESULT_QUIZ_DETAIL_DATA
            SearchResultModel.CONTENT_TYPE_VIDEO -> NetworkAPIConstants.Tag.GET_SEARCH_RESULT_VIDEO_DETAIL_DATA
            else -> NetworkAPIConstants.Tag.GET_SEARCH_RESULT_LESSON_DETAIL_DATA
        }
        viewModel!!.getDetail(searchContentId)

        viewModel!!.detailLiveData.observe( this, { apiResponse ->
            hideLoader()
            when (requestTag){
                NetworkAPIConstants.Tag.GET_SEARCH_RESULT_LESSON_DETAIL_DATA ->{
                    var wholedata = JSONObject(apiResponse)
                    wholedata = wholedata.optJSONObject("Model") ?: JSONObject()
                    searchResultLesson = SearchResultLessonModel.mapWithJson(wholedata)
                    showLessonDetails()
                }
            }
        })
//        SupportRepository(this).getSearchResultDetailData(requestTag, searchContentId)
    }


    private fun showLessonDetails() {
        searchResultLesson?.let {
            Utility.addOrReplaceFragment(supportFragmentManager,
                R.id.fragmentContainer, TopicDetailsFragment.newInstance(it),
                TopicDetailsFragment.TAG)
        } ?: finish()
    }

    private fun showVideoDetails() {
//        videoModel?.let {
//            Utility.addOrReplaceFragment(supportFragmentManager, R.id.fragmentContainer, TopicItemVideoFragment.newInstance(it), TopicDetailsFragment.TAG)
//        } ?: finish()
    }

    private fun showQuizDetails() {
        quizModel?.let {
            Utility.addOrReplaceFragment(supportFragmentManager, R.id.fragmentContainer, TopicItemQuizFragment.newInstance(it), TopicDetailsFragment.TAG)
        } ?: finish()
    }

    private fun showArticleDetails() {
//        articleData?.let {
//            Utility.addOrReplaceFragment(supportFragmentManager, R.id.fragmentContainer, TopicItemTipFragment.newInstance(it), TopicDetailsFragment.TAG)
//        } ?: finish()
    }

    private fun startTopicTips(lessonData: SearchResultLessonModel) {
//        this.searchResultLesson?.let {
//            Utility.addOrReplaceFragment(supportFragmentManager, R.id.fragmentContainer, TopicStartQuizFragment.newInstance(it, null), TopicStartQuizFragment.TAG)
//        } ?: finish()
    }

    public fun startExperienceScreen() {
//        this.lessonData?.let {
        Utility.addOrReplaceFragment(supportFragmentManager, R.id.fragmentContainer,
            ExperienceFragment.newInstance(),
            ExperienceFragment.TAG)
//        } ?: finish()
    }
    private fun setListenerOnWidgets() {
        viewModel = ViewModelProviders.of(this).get(LearnViewModel::class.java)
//        binding.tvClose.setOnClickListener {
//            finish()
//        }
//        binding.tvClose.setForegroundRipple()
    }

    //    var topicModel: TopicModel? = null
//    var nextTopicModel: TopicModel? = null
    private fun initArguments() {
//        if (intent.extras != null && intent.extras?.containsKey(TOPIC_MODEL_OBJ) == true)
//            topicModel = Gson().fromJson(intent?.extras?.getString(TOPIC_MODEL_OBJ, "{}") ?: "{}", TopicModel::class.java)
//        if (intent.extras != null && intent.extras?.containsKey(NEXT_TOPIC_MODEL_OBJ) == true)
//            nextTopicModel = Gson().fromJson(intent?.extras?.getString(NEXT_TOPIC_MODEL_OBJ, "{}") ?: "{}", TopicModel::class.java)
        if (intent.extras != null && intent?.hasExtra(SEARCH_CONTENT_ID) == true)
            searchContentId = intent.extras?.getString(SEARCH_CONTENT_ID, "").clean()
        if (intent.extras != null && intent?.hasExtra(SEARCH_CONTENT_TYPE) == true)
            searchContentType = intent.extras?.getString(SEARCH_CONTENT_TYPE, "").clean()
    }

    companion object {
        private const val TOPIC_MODEL_OBJ = "TOPIC_MODEL_OBJ"
        private const val NEXT_TOPIC_MODEL_OBJ = "NEXT_TOPIC_MODEL_OBJ"
        private const val SEARCH_CONTENT_ID = "SEARCH_CONTENT_ID"
        private const val SEARCH_CONTENT_TYPE = "SEARCH_CONTENT_TYPE"
//        fun getBundle(topicModel: TopicModel, nextTopicModel: TopicModel?) = Bundle().apply {
//            putString(TOPIC_MODEL_OBJ, Gson().toJson(topicModel))
//            nextTopicModel?.let {
//                putString(NEXT_TOPIC_MODEL_OBJ, Gson().toJson(nextTopicModel))
//            }
//        }

        fun getBundle(searchContentId: String, searchContentType: String) = Bundle().apply {
            putString(SEARCH_CONTENT_ID, searchContentId)
            putString(SEARCH_CONTENT_TYPE, searchContentType)
        }
    }

    override fun getViewBinder(inflater: LayoutInflater) = ActivitySupportTopicDetailBinding.inflate(inflater).apply { binding = this }
    override fun onStartTopicClick(lessonModel: SearchResultLessonModel) {
        startTopicTips(lessonModel)
    }

}