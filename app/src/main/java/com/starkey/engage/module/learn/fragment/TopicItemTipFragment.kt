package com.starkey.engage.module.learn.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.starkey.engage.ui.learn.data.learn.SearchResultArticleModel
import com.brightcove.player.edge.Catalog
import com.brightcove.player.edge.CatalogError
import com.brightcove.player.edge.VideoListener
import com.brightcove.player.model.Video
import com.starkey.engage.R
import com.starkey.engage.Utils.SharedPrefUtils
import com.starkey.engage.Utils.clean
import com.starkey.engage.Utils.isNonEmptyString
import com.starkey.engage.databinding.FragmentTopicItemTipBinding
import com.starkey.engage.ui.learn.utils.Utility
import com.google.gson.Gson
import com.starkey.engage.Utils.makeGone
import com.starkey.engage.ui.learn.view.BaseBrightcovePlayerFragment

class TopicItemTipFragment : BaseBrightcovePlayerFragment(R.layout.fragment_topic_item_tip) {

    lateinit var binding: FragmentTopicItemTipBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTopicItemTipBinding.inflate(inflater)
        baseVideoView = binding.brightcoveVideoView
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initArguments()
        this.initViews()
        this.setListenerOnWidgets()
    }

    private fun setListenerOnWidgets() {
    }

    private fun initViews() {
        binding.tvSummery.text = Utility.fromHtml(searchResultArticleModel?.description?.clean())
        binding.tvTitle.text = Utility.fromHtml(searchResultArticleModel?.title?.clean())
        if (searchResultArticleModel?.videoID.isNonEmptyString())
            loadVideo()
        else
            binding.brightcoveVideoView.makeGone()
    }

    private fun loadVideo() {
        val eventEmitter = binding.brightcoveVideoView.eventEmitter
        val catalog = Catalog(eventEmitter, getString(R.string.account), getString(R.string.policy))
        catalog.findVideoByID(searchResultArticleModel?.videoID.clean(), object : VideoListener() {
            override fun onVideo(video: Video?) {
                binding.brightcoveVideoView.add(video)
//                brightcoveVideoExoView.start()
                binding.brightcoveVideoView.seekTo(SharedPrefUtils.getProgress(searchResultArticleModel?.videoID.clean()))
            }
            override fun onError(errors: MutableList<CatalogError>) {
                super.onError(errors)
            }

        })
    }

    var searchResultArticleModel: SearchResultArticleModel? = null
    private fun initArguments() {
        if (arguments != null && arguments?.containsKey(LESSON_ARTICLE_OBJ) == true)
            searchResultArticleModel = Gson().fromJson(arguments?.getString(LESSON_ARTICLE_OBJ, "{}") ?: "{}", SearchResultArticleModel::class.java)
    }

    companion object {
        private const val LESSON_ARTICLE_OBJ = "LESSON_ARTICLE_OBJ"
        const val TAG = "TopicItemTipFragment"
        fun newInstance(articleModel: SearchResultArticleModel) = TopicItemTipFragment().apply {
            arguments = getBundle(articleModel)
        }


        private fun getBundle(articleModel: SearchResultArticleModel) = Bundle().apply {
            putString(LESSON_ARTICLE_OBJ, Gson().toJson(articleModel))
        }
    }
}