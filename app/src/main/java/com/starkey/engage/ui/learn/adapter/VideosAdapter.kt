package com.starkey.engage.ui.learn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.starkey.engage.R
import com.starkey.engage.ui.learn.data.learn.SearchResultModel
import com.starkey.engage.ui.learn.utils.ImageUtil

class VideosAdapter(
    context: Context?,
    onClick: OnVideoClick,
    exploreMore: ArrayList<SearchResultModel>
) : RecyclerView.Adapter<VideosAdapter.CustomViewHolder>() {

    var activity : Context = context!!
    val explore: ArrayList<SearchResultModel> = exploreMore
    var onVideoClick : OnVideoClick = onClick

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(searchResultModel: SearchResultModel) {
            searchResultModel.let {
                val tv_title: TextView = view.findViewById(R.id.tv_title)
                val videoImage: ImageView = view.findViewById(R.id.videoImage)

                tv_title.text = it.title
                videoImage.clipToOutline = true
                it.thumbnailImageUrl.let { it1 ->
                    ImageUtil.loadVideoThumb(it1, videoImage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.videos_items, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if(explore.isNotEmpty()) {

            holder.bindData(explore[position])
//            holder.view.setTag(explore[position])
            holder.view.setOnClickListener {
                onVideoClick.onVideoClick(explore[position])
//                val intent = Intent(activity, SupportTopicDetailActivity::class.java)
//                activity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return explore.size
    }

    interface OnVideoClick {
        fun onVideoClick(searchResultModel: SearchResultModel);
    }
}