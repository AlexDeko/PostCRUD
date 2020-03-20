package com.postcrud.data.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.data.Post
import com.postcrud.data.adapters.PostRecyclerAdapter

abstract class BaseViewHolder(val adapter: PostRecyclerAdapter, view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun bind(post: Post)
}