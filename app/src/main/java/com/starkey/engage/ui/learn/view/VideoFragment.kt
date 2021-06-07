package com.starkey.engage.ui.learn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.starkey.engage.ui.learn.data.learn.SearchResultModel
import com.brightcove.player.edge.Catalog
import com.brightcove.player.edge.CatalogError
import com.brightcove.player.edge.VideoListener
import com.brightcove.player.model.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.starkey.engage.R
import com.starkey.engage.Utils.clean
import com.starkey.engage.Utils.observe
import com.starkey.engage.Utils.parseLong
import com.starkey.engage.databinding.FragmentVideoBinding
import com.starkey.engage.module.SearchResultVideoModel
import com.starkey.engage.ui.learn.adapter.VideosAdapter
import com.starkey.engage.ui.learn.data.learn.NewsType
import com.starkey.engage.ui.learn.utils.Utility
import com.starkey.engage.ui.learn.viewmodel.LearnViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.common_tool.view.*
import org.json.JSONObject

@AndroidEntryPoint
class VideoFragment : BaseBrightcovePlayerFragment(R.layout.fragment_video) {

    lateinit var binding: FragmentVideoBinding
    lateinit var searchContentId: String
    private val viewModel: LearnViewModel by viewModels()
    var videoModel: SearchResultVideoModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVideoBinding.inflate(inflater)
        baseVideoView = binding.brightcoveVideoView
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        view.let { super.onViewCreated(it, savedInstanceState) }
        binding = this
        initArguments()
        setObservers()
        getVideoData()
    }

    private fun setObservers() {
        observe(viewModel.detailVideoLiveData) { apiResponse ->
            hideLoader()
            var wholeData = JSONObject(apiResponse)
            wholeData = wholeData.optJSONObject("Model") ?: JSONObject()
            videoModel = SearchResultVideoModel.mapWithJson(wholeData)
            startVideo()
            updateUI()
        }
    }

    private lateinit var videosAdapter: VideosAdapter
    private fun updateUI() {
        binding.videoTool.root.leftImageBtn.setOnClickListener { activity?.onBackPressed() }
        videoModel?.let { videoModel ->
            binding.videoTitle.text = videoModel.title
            binding.videoDesc.text = Utility.fromHtml(videoModel.description)
        }

        if (videoList.size > 0){
            binding.recyVideos.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.HORIZONTAL, false)
            videosAdapter = VideosAdapter(activity, videoClick, videoList)
            binding.recyVideos.adapter = videosAdapter
        } else{
            binding.txtExplore.visibility = View.GONE
        }
    }

    val videoClick = object: VideosAdapter.OnVideoClick{

        override fun onVideoClick(searchResults: SearchResultModel) {

            val videosToExplore = videoList?.filter { it.newsType == NewsType.Video }?: arrayListOf()
            val exploreMoreVideos = videosToExplore.subList(videosToExplore.indexOf(searchResults)+1,videosToExplore.size)
            viewModel.navigateFromVideoToVideoDetail(searchResults.uniqueID, Gson().toJson(exploreMoreVideos), binding.recyVideos)
        }

    }

    private fun startVideo() {
        val eventEmitter = binding.brightcoveVideoView.eventEmitter
        val catalog = eventEmitter?.let { Catalog(it, getString(R.string.account), getString(R.string.policy)) }
        catalog?.findVideoByID(videoModel?.videoID.clean(), object : VideoListener() {
            override fun onVideo(video: Video?) {
                binding.txtVideoDuration.text =
                    Utility.convertToUserFriendlyTime(video?.duration.toString().parseLong())
                binding.brightcoveVideoView.add(video)
                binding.brightcoveVideoView.start()
            }

            override fun onError(errors: MutableList<CatalogError>) {
                super.onError(errors)
            }
        })
    }

    private fun getVideoData() {
        showLoader()
        viewModel.getVideoDetail(searchContentId)
    }


    var videoList = ArrayList<SearchResultModel>()
    private fun initArguments() {
        val myType = object : TypeToken<ArrayList<SearchResultModel>>() {}.type
        videoList = Gson().fromJson(arguments?.getString("arg_explore_content_more") ?: "[]", myType)
        searchContentId = arguments?.getString("arg_search_content_id").clean()
    }

}