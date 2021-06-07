package com.starkey.engage.ui.learn.viewmodel

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.starkey.engage.R
import com.starkey.engage.module.NavDispatcher
import com.starkey.engage.ui.learn.repository.LearnRepository
import com.starkey.engage.ui.learn.utils.Utility.Companion.getCountryLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LearnViewModel
@Inject constructor(
    @ApplicationContext val context: Context,
    private val navDispatcher: NavDispatcher
) : ViewModel() {
    private var learnRepository = LearnRepository()
    var learnLiveData = MutableLiveData<String>()
    var detailLiveData = MutableLiveData<String>()
    var detailVideoLiveData = MutableLiveData<String>()

    fun navigateToVideoDetail(
        searchContentId: String,
        videosToExplore: String,
        view: View
    ) {
        var bundle = Bundle()
        bundle.putString("arg_search_content_id", searchContentId)
        bundle.putString("arg_explore_content_more", videosToExplore)
        Navigation.findNavController(view).navigate(R.id.action_to_videoFragment, bundle)
//        navDispatcher.emit { navigate(LearnFragmentDirections.actionToVideoFragment(searchContentId, videosToExplore)) }
    }
    fun navigateFromVideoToVideoDetail(
        searchContentId: String,
        videosToExplore: String,
        view: View
    ) {
        var bundle = Bundle()
        bundle.putString("arg_search_content_id", searchContentId)
        bundle.putString("arg_explore_content_more", videosToExplore)
        Navigation.findNavController(view).navigate(R.id.action_videoFragment, bundle)
//        navDispatcher.emit { navigate(LearnFragmentDirections.actionToVideoFragment(searchContentId, videosToExplore)) }
    }

    fun getAllNewForYou() {
        val countryLabel = getCountryLocale()
        learnRepository.learnService.getAllNews("", countryLabel)
            .enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    learnLiveData.value = response.body()
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                }
            })
    }

    fun getDetail(searchContentId: String) {
        val countryLabel = getCountryLocale()
        learnRepository.learnService.getDetail(searchContentId, countryLabel).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                detailLiveData.setValue(response.body())
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        })
    }

    fun getVideoDetail(searchContentId: String) {
        val countryLabel = getCountryLocale()
        learnRepository.learnService.getVideoDetail(searchContentId, countryLabel).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                detailVideoLiveData.value = response.body()
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        })
    }
}