package com.starkey.engage.module.learn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applications.engage.model.search.ItemModel
import com.applications.engage.model.search.ItemModel1
import com.starkey.engage.R
import com.starkey.engage.Utils.setForegroundRipple

class UserGuideSubListAdapter(context: Context?, val onGuideClick: OnGuideClick, val itemModel: ItemModel?) :
    RecyclerView.Adapter<UserGuideSubListAdapter.CustomViewHolder>() {
    var mContext = context

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var mContext = view.context
        var layItem: View? = null
        fun bindData(guideModel: ItemModel1?) {
            guideModel?.let {
                val tv_title: TextView = view.findViewById(R.id.guideName)
                layItem = view.findViewById(R.id.lay_cell_item)
                tv_title.text = guideModel.getDisplayName()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.userguide, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return itemModel?.getFields()?.getItems()?.size ?: 0
    }

    interface OnGuideClick {
        fun onGuideClick(itemModel: ItemModel1?)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        itemModel?.getFields()?.getItems()?.let { items ->
            if (items.isNotEmpty()) {
                holder.bindData(items[position])
                holder.layItem?.setOnClickListener {
                    onGuideClick.onGuideClick(items[position])
                }
                holder.layItem?.setForegroundRipple()
            }
        }

    }
}