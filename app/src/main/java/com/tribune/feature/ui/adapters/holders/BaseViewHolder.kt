package com.tribune.feature.ui.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tribune.feature.data.model.PostModel
import com.tribune.feature.ui.adapters.PostRecyclerAdapter

abstract class BaseViewHolder(val adapter: PostRecyclerAdapter, view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun bind(post: PostModel)
    abstract fun bindApproveChange(post: PostModel)
    abstract fun bindUnselectedApprovesButton(post: PostModel)
}