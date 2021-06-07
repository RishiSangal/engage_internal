package com.starkey.engage.ui.learn.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.starkey.engage.ui.learn.data.learn.SearchResultModel
import com.google.gson.Gson
import com.starkey.engage.R
import com.starkey.engage.databinding.CellCardViewBinding
import com.starkey.engage.databinding.FragmentLearnBinding
import com.starkey.engage.module.LearnActivity
import com.starkey.engage.ui.learn.adapter.NewForYouAdapter
import com.starkey.engage.ui.learn.data.learn.NewsType
import com.starkey.engage.ui.learn.data.learn.PopularItems
import com.starkey.engage.ui.learn.viewmodel.LearnViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class LearnFragment : BaseFragment(R.layout.fragment_learn) {
    lateinit var binding: FragmentLearnBinding
    private val viewModel: LearnViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(
        FragmentLearnBinding.bind(view)) {
        view.let { super.onViewCreated(it, savedInstanceState) }
        this@LearnFragment.binding = this
        rvNewForYou.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val popularItems = ArrayList<PopularItems>()
        addPopularItem(popularItems, R.drawable.icon_toggle_flag_24_px, getString(R.string.learn_get_started))
        addPopularItem(popularItems, R.drawable.icon_toggle_ear_24_px, getString(R.string.learn_using_hearing_aids))
        addPopularItem(popularItems, R.drawable.icon_content_sensors_24_px, getString(R.string.learn_connectivity_pairing))
        addPopularItem(popularItems, R.drawable.icon_toggle_lightbulb_24_px, getString(R.string.learn_tips_advice))
        addPopularItem(popularItems, R.drawable.icon_alert_falls_battery_low_24_dp, getString(R.string.learn_battery_power))
        addPopularItem(popularItems, R.drawable.icon_toggle_wrench_checked_24_px, getString(R.string.learn_care_maintenance))
        addPopularItem(popularItems, R.drawable.icon_toggle_tv_streaming_24_px, getString(R.string.tv_streaming))
        addPopularItem(popularItems, R.drawable.icon_navigation_health_24_px, getString(R.string.learn_track_your_health))
        addPopularItem(popularItems, R.drawable.icon_alert_ask_assistant_24_dp_copy, getString(R.string.learn_ask_assistant))
        popularItems.add(PopularItems(R.drawable.icon_toggle_remote_help_off_24_px_copy, getString(R.string.learn_remote_help)))

        for (i in 0 until popularItems.size) {
            addLayout(i, popularItems[i].image, popularItems[i].text)
        }
        getAllNewForYouData()
    }

    private fun addPopularItem(items: ArrayList<PopularItems>, iconRes: Int, title: String) {
        items.add(PopularItems(iconRes, title))
    }

    var searchResults : ArrayList<SearchResultModel>? = null
    private fun getAllNewForYouData() {
        showLoader()
        viewModel.learnLiveData.observe(requireActivity(), { response ->
            hideLoader()
            var wholeData = JSONObject(response)
            wholeData = wholeData.optJSONObject("Model") ?: JSONObject()
            val itemArray = wholeData.optJSONArray("Items") ?: JSONArray()
            val size = itemArray.length()
            searchResults = ArrayList<SearchResultModel>(size)
            for (i in 0 until size)
                searchResults!!.add(SearchResultModel.mapWithJson(itemArray.optJSONObject(i)))
            newForYouAdapter = NewForYouAdapter(context, searchResults!!, onclick)
            binding.rvNewForYou.adapter = newForYouAdapter
        })
        viewModel.getAllNewForYou()
    }

    companion object {
        const val TAG_NAME = "LearnFragment"

        @JvmStatic
        fun newInstance() = LearnFragment()
    }

    private var learnActivity: LearnActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        learnActivity = activity as LearnActivity
    }

    private lateinit var newForYouAdapter: NewForYouAdapter
    private fun addLayout(pos: Int, image: Int, text: String) {
        val layout2: View = LayoutInflater.from(activity).inflate(
            R.layout.cell_card_view,
            binding.layCardViewContainer,
            false
        )
        val layoutBinding = CellCardViewBinding.bind(layout2)
        layoutBinding.txtHeading.text = text
        layoutBinding.imgPopularTopics.setImageResource(image)
        if (pos == 9) {
            layoutBinding.layDivider.visibility = View.GONE
        }
        binding.layCardViewContainer.addView(layout2)
    }

    val onclick = View.OnClickListener() { view->
        val searchResultModel = view.tag as SearchResultModel

        val videosToExplore = searchResults?.filter { it.newsType == NewsType.Video }?: arrayListOf()
        val exploreMoreVideos = videosToExplore.subList(videosToExplore.indexOf(searchResultModel)+1,videosToExplore.size)

        searchResultModel.let {
            activity?.let { activity ->

                if (searchResultModel.newsType == NewsType.Video) {
                    viewModel.navigateToVideoDetail(searchResultModel.uniqueID, Gson().toJson(exploreMoreVideos), view)
                } else if (searchResultModel.newsType == NewsType.Quiz) {
                    /*val intent = Intent(activity, SupportTopicDetailActivity::class.java)
                    intent.putExtras(SupportTopicDetailActivity.getBundle(searchResultModel.uniqueID.clean(), SearchResultModel.CONTENT_TYPE_LESSON))
                    activity.startActivity(intent)*/
                }
            }
        }
    }
}