package com.postcrud.feature.data.adapters.holders

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.feature.data.Post
import com.postcrud.R
import com.postcrud.feature.data.adapters.PostRecyclerAdapter
import kotlinx.android.synthetic.main.list_repost_item.view.*

class RepostViewHolder(adapter: PostRecyclerAdapter, view: View) : BaseViewHolder(adapter, view) {
    init {
        with(itemView) {
            imageButtonLike.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
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

            imageButtonReply.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT, """
                                ${item.author} (${item.created})
    
                                ${item.content}
                            """.trimIndent()
                        )
                        type = "text/plain"
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun bind(post: Post) {
        with(itemView) {
            this.textItem.text = post.content
            this.titleItem.text = post.author
            this.dateItem.text = post.created.toString()
            //toDo repost
            this.replyDateItem.text = post.ownerId.toString()
            this.replyTitleItem.text = post.ownerId.toString()
            this.replyTextItem.text = post.ownerId.toString()
            countLikes.text = post.countLike.toString()
            countReply.text = post.countRepost.toString()
            countComments.text = post.countComment.toString()

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