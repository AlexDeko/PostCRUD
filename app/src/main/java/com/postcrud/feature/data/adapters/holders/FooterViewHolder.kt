package com.postcrud.feature.data.adapters.holders

import android.view.View
import com.postcrud.feature.data.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import kotlinx.android.synthetic.main.list_footer_item.view.*

class FooterViewHolder(adapter: PostRecyclerAdapter, view: View) : BaseViewHolder(adapter, view) {

    init {
        with(itemView) {
            loadMoreBtn.setOnClickListener {
                progressbar.show()

                progressbar.hide()
            }
        }
    }

    override fun bind(post: PostResponseDto) {

    }
}