package com.tribune.feature.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tribune.R
import com.tribune.feature.data.dto.user.UserResponseDto
import com.tribune.feature.data.model.PostModel

import com.tribune.feature.ui.adapters.holders.*
import com.tribune.feature.data.model.PostType

private const val VIEW_TYPE_POST = 1
private const val VIEW_TYPE_REPOST = 2

fun viewTypeToPostType(viewType: Int) = when (viewType) {
    VIEW_TYPE_POST -> PostType.POST
    VIEW_TYPE_REPOST -> PostType.REPOST
    else -> TODO("unknown view type")
}

class PostRecyclerAdapter(
    var list: MutableList<PostModel>,
    val onApproveClicked: (PostModel) -> Unit,
    val onNotApproveClicked: (PostModel) -> Unit,
    val unselectedApprovesClicked: (PostModel) -> Unit,
    val onRepostClicked: (PostModel) -> Unit,
    val setBadge: (id: Long, isApprovedThisPost: Boolean, cancelApproved: Boolean,
                   cancelNotApproved: Boolean) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewTypeToPostType(viewType)) {

            PostType.POST -> PostViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.tribune_list_post_item,
                    parent,
                    false
                )
            )
            PostType.REPOST -> RepostViewHolder(
                this,
                LayoutInflater.from(parent.context).inflate(
                    R.layout.tribune_list_repost_item,
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
                    Payload.APPROVE_CHANGE -> {
                        with(holder as BaseViewHolder) {
                            bindApproveChange(
                                list[position]
                            )
                        }
                    }
                    Payload.UNSELECTED_APPROVES_BUTTON -> {
                        with(holder as BaseViewHolder) {
                            bindUnselectedApprovesButton(
                                list[position]
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
    APPROVE_CHANGE,
    UNSELECTED_APPROVES_BUTTON
}



