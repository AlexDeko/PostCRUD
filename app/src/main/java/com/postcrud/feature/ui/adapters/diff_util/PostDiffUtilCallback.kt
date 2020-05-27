package com.postcrud.feature.ui.adapters.diff_util

import androidx.recyclerview.widget.DiffUtil
import com.postcrud.feature.data.dto.PostResponseDto

class PostDiffUtilCallback(
    private val oldList: List<PostResponseDto>,
    private val newList: List<PostResponseDto>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldPost = oldList[oldItemPosition]
        val newPost = newList[newItemPosition]
        return oldPost.id == newPost.id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        val oldPost = oldList[oldItemPosition]
        val newPost = newList[newItemPosition]
        return (oldPost.type == newPost.type
                && oldPost.content == newPost.content
                && oldPost.author == newPost.author
                && oldPost.repost == newPost.repost )
    }
}