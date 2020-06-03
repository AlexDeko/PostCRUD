package com.tribune.feature.ui.adapters.holders

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tribune.R
import com.tribune.feature.ui.adapters.PostRecyclerAdapter
import com.tribune.feature.data.dto.PostResponseDto
import com.tribune.feature.data.model.PostModel
import kotlinx.android.synthetic.main.list_ads_item.view.*

class AdsViewHolder(adapter: PostRecyclerAdapter, view: View) : BaseViewHolder(adapter, view) {
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

            imageButtonRepost.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT, """
                                ${item.author} (${item.createdDate})
    
                                ${item.content}
                            """.trimIndent()
                        )
                        type = "text/plain"
                    }
                    itemView.context.startActivity(intent)
                }
            }
            adsImageButton.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(item.adsUrl)
                    }
                    itemView.context.startActivity(intent)
                }
            }

            clearAds.setOnClickListener{
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    adapter.list.removeAt(adapterPosition)
                    adapter.notifyItemRemoved(adapterPosition)
                }
            }
        }
    }

    override fun bind(post: PostModel) {
        with(itemView) {
            this.textItem.text = post.content
            this.adsAuthor.text = post.author

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

    override fun bindLike(post: PostModel, isLikePost: Boolean, countLikePost: Long) {

    }
}