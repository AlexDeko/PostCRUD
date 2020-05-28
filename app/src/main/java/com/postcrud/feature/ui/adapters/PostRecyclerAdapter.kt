package com.postcrud.feature.ui.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.R

import com.postcrud.feature.ui.adapters.holders.*
import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.data.model.PostType

private const val VIEW_TYPE_POST = 1
private const val VIEW_TYPE_REPOST = 2
private const val VIEW_TYPE_ADS = 3
private const val VIEW_TYPE_VIDEO = 4
private const val VIEW_TYPE_EVENT = 5

fun viewTypeToPostType(viewType: Int) = when (viewType) {
    VIEW_TYPE_POST -> PostType.POST
    VIEW_TYPE_REPOST -> PostType.REPOST
    VIEW_TYPE_ADS -> PostType.ADS
    VIEW_TYPE_VIDEO -> PostType.VIDEO
    VIEW_TYPE_EVENT -> PostType.EVENT
    else -> TODO("unknown view type")
}

class PostRecyclerAdapter(
    var list: MutableList<PostResponseDto>,
    val onLikeClicked: (PostResponseDto) -> Unit,
    val onDislikeClicked: (PostResponseDto) -> Unit,
    val onRepostClicked: (PostResponseDto) -> Unit,
    val imageBitmap: Bitmap?
) :
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

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            for (i in payloads.indices) {
                when (payloads[i]) {
                    Payload.LIKE_CHANGE -> {
                        with(holder as BaseViewHolder) {
                            bindLike(
                                list[position],
                                list[position].isLike,
                                list[position].countLike
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as BaseViewHolder) {
            bind(list[position])
        }
    }

}

enum class Payload {
    LIKE_CHANGE
}



