package com.applications.engage.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brightcove.player.edge.Catalog
import com.brightcove.player.edge.CatalogError
import com.brightcove.player.edge.VideoListener
import com.brightcove.player.event.EventType
import com.brightcove.player.model.Video
import com.starkey.engage.R
import com.starkey.engage.Utils.SharedPrefUtils
import com.starkey.engage.Utils.clean
import com.starkey.engage.databinding.FragmentTopicItemVideoBinding
import com.starkey.engage.module.SearchResultVideoModel
import com.starkey.engage.ui.learn.utils.Utility
import com.google.gson.Gson
import com.starkey.engage.Utils.parseInt
import com.starkey.engage.ui.learn.view.BaseBrightcovePlayerFragment


class TopicItemVideoFragment : BaseBrightcovePlayerFragment(R.layout.fragment_topic_item_video) {

    lateinit var binding: FragmentTopicItemVideoBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTopicItemVideoBinding.inflate(inflater)
        baseVideoView = binding.brightcoveVideoView
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initArguments()
        this.initViews()
        this.setListenerOnWidgets()
        this.loadVideo()
    }

    private fun loadVideo() {
        val eventEmitter = binding.brightcoveVideoView.eventEmitter
        val catalog = Catalog(eventEmitter, getString(R.string.account), getString(R.string.policy))
        catalog.findVideoByID(videoModel?.videoID.clean(), object : VideoListener() {
            override fun onVideo(video: Video?) {
                binding.brightcoveVideoView.add(video)
//                brightcoveVideoExoView.start()
                binding.brightcoveVideoView.seekTo(SharedPrefUtils.getProgress(videoModel?.videoID.clean()))
            }

            override fun onError(errors: MutableList<CatalogError>) {
                super.onError(errors)
            }
        })
    }

    private fun setListenerOnWidgets() {
        binding.brightcoveVideoView.eventEmitter.on(EventType.PROGRESS) { event ->
            val currentProgress = event?.properties?.get("playheadPosition")?.toString().clean().parseInt()
            SharedPrefUtils.setProgress(videoModel?.videoID.clean(), currentProgress)
        }
        binding.brightcoveVideoView.eventEmitter.on(EventType.ENTER_FULL_SCREEN) { event ->
//        showToast("full screen enter")

        }
    }

    private fun initViews() {
        binding.tvSummery.text = Utility.fromHtml(videoModel?.description?.clean())
        binding.tvTitle.text = Utility.fromHtml(videoModel?.title?.clean())
    }

    var videoModel: SearchResultVideoModel? = null
    private fun initArguments() {
        if (arguments != null && arguments?.containsKey(LESSON_VIDEO_OBJ) == true)
            videoModel = Gson().fromJson(arguments?.getString(LESSON_VIDEO_OBJ, "{}") ?: "{}", SearchResultVideoModel::class.java)
    }

    companion object {
        private const val LESSON_VIDEO_OBJ = "LESSON_VIDEO_OBJ"
        const val TAG = "TopicItemTipFragment"
        fun newInstance(videoModel: Any) = TopicItemVideoFragment().apply {
            arguments = getBundle(videoModel as SearchResultVideoModel)
        }


        private fun getBundle(videoModel: SearchResultVideoModel) = Bundle().apply {
            putString(LESSON_VIDEO_OBJ, Gson().toJson(videoModel))
        }
    }
}