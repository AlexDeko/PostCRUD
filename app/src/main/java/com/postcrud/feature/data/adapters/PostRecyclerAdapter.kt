package com.postcrud.feature.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.R

import com.postcrud.feature.data.adapters.holders.*
import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.data.model.PostType

const val VIEW_TYPE_POST = 1
const val VIEW_TYPE_REPOST = 2
const val VIEW_TYPE_ADS = 3
const val VIEW_TYPE_VIDEO = 4
const val VIEW_TYPE_EVENT = 5

fun viewTypeToPostType(viewType: Int) = when (viewType) {
    VIEW_TYPE_POST -> PostType.POST
    VIEW_TYPE_REPOST -> PostType.REPOST
    VIEW_TYPE_ADS -> PostType.ADS
    VIEW_TYPE_VIDEO -> PostType.VIDEO
    VIEW_TYPE_EVENT -> PostType.EVENT
    else -> TODO("unknown view type")
}

class PostRecyclerAdapter(val list: MutableList<PostResponseDto>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewTypeToPostType(viewType)) {
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

    override fun getItemViewType(position: Int) = when (list[position].type) {
        PostType.POST -> VIEW_TYPE_POST
        PostType.REPOST -> VIEW_TYPE_REPOST
        PostType.ADS -> VIEW_TYPE_ADS
        PostType.VIDEO -> VIEW_TYPE_VIDEO
        PostType.EVENT -> VIEW_TYPE_EVENT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as BaseViewHolder) {
            bind(list[position])
        }
    }
}

