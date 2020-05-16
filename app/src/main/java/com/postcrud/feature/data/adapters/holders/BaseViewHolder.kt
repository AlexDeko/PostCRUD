package com.postcrud.feature.data.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.feature.data.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto

abstract class BaseViewHolder(val adapter: PostRecyclerAdapter, view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun bind(post: PostResponseDto)
}