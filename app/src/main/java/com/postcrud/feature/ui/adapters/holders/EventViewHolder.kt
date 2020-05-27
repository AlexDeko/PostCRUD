package com.postcrud.feature.ui.adapters.holders

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.postcrud.R
import com.postcrud.feature.ui.adapters.PostRecyclerAdapter
import com.postcrud.feature.data.dto.PostResponseDto
import kotlinx.android.synthetic.main.list_event_item.view.*

class EventViewHolder(adapter: PostRecyclerAdapter, view: View) : BaseViewHolder(adapter, view) {
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

            imageButtonLocation.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(
                            if (isLetter(item.selectedLocation)
                                && isDigit(item.selectedLocation) || isLetter(item.selectedLocation)) {
                                resources.getString(R.string.geoAddressGM, item.selectedLocation)
                            } else resources.getString(R.string.geoCoordinatesGM, item.selectedLocation)
                        )
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun bind(post: PostResponseDto) {
        with(itemView) {
            this.textItem.text = post.content
            this.titleItem.text = post.author
            this.dateItem.text = post.createdDate.toString()
            locationTextView.text = post.selectedLocation
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

    private fun isLetter(query: String?): Boolean {
        if (query != null) {
            for (element in query) if (Character.isLetter(element)) {
                return true
            }
        }
        return false
    }

    private fun isDigit(query: String?): Boolean {
        if (query != null) {
            for (element in query) if (Character.isDigit(element)) {
                return true
            }
        }
        return false
    }
}