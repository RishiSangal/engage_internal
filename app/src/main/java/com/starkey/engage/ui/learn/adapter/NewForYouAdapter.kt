package com.starkey.engage.ui.learn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.starkey.engage.ui.learn.data.learn.SearchResultModel
import com.starkey.engage.R
import com.starkey.engage.ui.learn.data.learn.NewsType
import com.starkey.engage.ui.learn.utils.ImageUtil

class NewForYouAdapter(
    context: Context?,
    newsForYou: ArrayList<SearchResultModel>,
    onclick: View.OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var topicModel: ArrayList<SearchResultModel> = newsForYou
    val activityContext = context
    val cellClick = onclick
    val CELL_ARTICAL = 0
    private val CELL_Video = 1

    val titles = arrayListOf("Reminders", "Translate", "Transcribe")
//    val icons = arrayListOf(R.drawable.calendar, R.drawable.translate, R.drawable.chat)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        if (viewType == CELL_ARTICAL) {
            itemView = LayoutInflater.from(activityContext).inflate(
                R.layout.cell_article,
                parent,
                false
            )
            return ArticalViewHolder(itemView)
        } else{
            itemView = LayoutInflater.from(activityContext).inflate(
                R.layout.cell_video,
                parent,
                false
            )
            return ImageViewHolder(itemView)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val searchResultModel = topicModel[position]

        if (holder is ArticalViewHolder) {
            val iconViewHolder = holder as ArticalViewHolder
//            searchResultModel.thumbnailImageUrl.let { it1 ->
//                ImageUtil.loadVideoThumb(it1, iconViewHolder.imgIcon)
//            }
            iconViewHolder.imgIcon.setImageResource(R.drawable.icon_navigation_learn_24_px)
            iconViewHolder.txtIconSubHeading.text = searchResultModel.title
            iconViewHolder.txtIconHeading.text = searchResultModel.contenttype
        } else{
            val imageViewHolder = holder as ImageViewHolder
            imageViewHolder.txtIconSubHeading.text = searchResultModel.contenttype
            imageViewHolder.imgImage.clipToOutline = true
            searchResultModel.thumbnailImageUrl.let { it1 ->
                ImageUtil.loadVideoThumb(it1, imageViewHolder.imgImage)
            }
            imageViewHolder.txtIconHeading.text = searchResultModel.title
        }
        holder.itemView.setTag(searchResultModel)
        holder.itemView.setOnClickListener(View.OnClickListener {
            cellClick.onClick(it)
        })
    }

    override fun getItemCount(): Int {
//        if (topicModel.size > 4)
//            return 4
//        else
        return topicModel.size
    }

    override fun getItemViewType(position: Int): Int {
        if (topicModel.get(position).newsType == NewsType.Article){
            return CELL_ARTICAL
        } else{
            return CELL_Video
        }
    }

    class ImageViewHolder(val view: View) : ViewHolder(view) {
        var txtIconHeading: TextView
        var txtIconSubHeading: TextView
        var imgImage: ImageView
        init {
            txtIconHeading = itemView.findViewById(R.id.txtIconHeading)
            txtIconSubHeading = itemView.findViewById(R.id.txtIconSubHeading)
            imgImage = itemView.findViewById(R.id.imgImage)
        }
    }

    class ArticalViewHolder(itemView: View) : ViewHolder(itemView) {
        var txtIconHeading: TextView
        var txtIconSubHeading: TextView
        var imgIcon: ImageView
        init {
            txtIconHeading = itemView.findViewById(R.id.txtIconHeading)
            txtIconSubHeading = itemView.findViewById(R.id.txtIconSubHeading)
            imgIcon = itemView.findViewById(R.id.imgIcon)
        }
    }
    open class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    ) {
        fun bindView(position: Int) {}
    }
}