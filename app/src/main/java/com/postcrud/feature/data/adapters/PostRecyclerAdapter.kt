package com.postcrud.feature.data.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.R

import com.postcrud.feature.data.adapters.holders.*
import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.data.model.PostType

private const val VIEW_TYPE_POST = 1
private const val VIEW_TYPE_REPOST = 2
private const val VIEW_TYPE_ADS = 3
private const val VIEW_TYPE_VIDEO = 4
private const val VIEW_TYPE_EVENT = 5
private const val ITEM_FOOTER = 6;
private const val ITEM_HEADER = 7

fun viewTypeToPostType(viewType: Int) = when (viewType) {
    VIEW_TYPE_POST -> PostType.POST
    VIEW_TYPE_REPOST -> PostType.REPOST
    VIEW_TYPE_ADS -> PostType.ADS
    VIEW_TYPE_VIDEO -> PostType.VIDEO
    VIEW_TYPE_EVENT -> PostType.EVENT
    ITEM_HEADER -> PostType.HEADER
    ITEM_FOOTER -> PostType.FOOTER
    else -> TODO("unknown view type")
}

class PostRecyclerAdapter(val list: MutableList<PostResponseDto>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewTypeToPostType(viewType)) {
            //   position == 0 -> ITEM_HEADER

            PostType.HEADER -> HeaderViewHolder(
             this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_header_item,
                    parent,
                    false
                )
            )
            PostType.FOOTER -> FooterViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_footer_item,
                    parent,
                    false
                )
            )
            PostType.POST -> PostViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_post_item,
                    parent,
                    false
                )
            )
            PostType.REPOST -> RepostViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_repost_item,
                    parent,
                    false
                )
            )
            PostType.ADS -> AdsViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_ads_item,
                    parent,
                    false
                )
            )
            PostType.VIDEO -> VideoViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_video_item,
                    parent,
                    false
                )
            )
            PostType.EVENT -> EventViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_event_item,
                    parent,
                    false
                )
            )
        }

    override fun getItemCount() = list.size

    override fun getItemId(position: Int) = list[position].id

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return ITEM_HEADER
        if (position == list.size + 1) return ITEM_FOOTER
        return when (list[position - 1].type) {
            PostType.POST -> VIEW_TYPE_POST
            PostType.REPOST -> VIEW_TYPE_REPOST
            PostType.ADS -> VIEW_TYPE_ADS
            PostType.VIDEO -> VIEW_TYPE_VIDEO
            PostType.EVENT -> VIEW_TYPE_EVENT
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as BaseViewHolder) {
            bind(list[position])
        }
    }
}

