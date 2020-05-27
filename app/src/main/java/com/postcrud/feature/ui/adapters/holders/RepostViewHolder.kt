package com.postcrud.feature.ui.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.R
import com.postcrud.component.formatter.DateFormatter
import com.postcrud.feature.ui.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import kotlinx.android.synthetic.main.list_post_item.view.*
import kotlinx.android.synthetic.main.list_repost_item.view.*
import kotlinx.android.synthetic.main.list_repost_item.view.countComments
import kotlinx.android.synthetic.main.list_repost_item.view.countLikes
import kotlinx.android.synthetic.main.list_repost_item.view.countReply
import kotlinx.android.synthetic.main.list_repost_item.view.dateItem
import kotlinx.android.synthetic.main.list_repost_item.view.imageButtonLike
import kotlinx.android.synthetic.main.list_repost_item.view.imageButtonRepost
import kotlinx.android.synthetic.main.list_repost_item.view.titleItem

class RepostViewHolder(adapter: PostRecyclerAdapter, view: View) : BaseViewHolder(adapter, view) {
    init {
        with(itemView) {
            imageButtonLike.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.isLike) adapter.onLikeClicked(item)
                    else adapter.onDislikeClicked(item)

                    item.isLike = !item.isLike
                    if (item.isLike) {
                        imageButtonLike.setImageResource(R.drawable.ic_favorite_24dp)
                        item.countLike += 1L

                    } else {
                        imageButtonLike.setImageResource(R.drawable.ic_favorite_border_24dp)
                        item.countLike -= 1L
                    }
                    adapter.notifyItemChanged(adapterPosition)
                }
            }

            imageButtonRepost.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    adapter.onRepostClicked(adapter.list[adapterPosition])
            }
        }
    }

    override fun bind(post: PostResponseDto) {
        with(itemView) {
            //toDo() need add fun for created new content in repost
            //this.textItem.text = post.content
            this.titleItem.text = post.author
            this.dateItem.text = DateFormatter().getFormatDate(post.createdDate)
            this.replyDateItem.text = DateFormatter().getFormatDate(post.repost!!.createdDate)
            this.replyTitleItem.text = post.repost!!.author
            this.replyTextItem.text = post.repost!!.content
            this.countLikes.text = post.countLike.toString()
            this.countReply.text = post.countRepost.toString()
            this.countComments.text = post.countComment.toString()

            if (adapter.imageBitmap != null) imagePost.setImageBitmap(adapter.imageBitmap)


            imageButtonLike.setImageResource(if (post.isLike) R.drawable.ic_favorite_24dp else R.drawable.ic_favorite_border_24dp)

            if (countLikes.text == "0") {
                countLikes.visibility = View.INVISIBLE
            } else countLikes.visibility = View.VISIBLE

            if (countComments.text == "0") {
                countComments.visibility = View.INVISIBLE
            } else countComments.visibility = View.VISIBLE

            if (countReply.text == "0") {
                countReply.visibility = View.INVISIBLE
            } else countReply.visibility = View.VISIBLE
        }
    }
}