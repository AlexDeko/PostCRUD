package com.tribune.feature.ui.adapters.holders

import android.view.View
import com.bumptech.glide.Glide
import com.tribune.R
import com.tribune.component.formatter.DateFormatter
import com.tribune.feature.data.model.PostModel
import com.tribune.feature.data.model.UserBadge
import com.tribune.feature.ui.adapters.Payload
import com.tribune.feature.ui.adapters.PostRecyclerAdapter
import kotlinx.android.synthetic.main.tribune_list_repost_item.view.*

class RepostViewHolder(adapter: PostRecyclerAdapter, view: View) : BaseViewHolder(adapter, view) {

    init {

        with(itemView) {

            approveImgBtn.setOnClickListener {
                val item = adapter.list[adapterPosition]

                if (item.isApprove) {
                    adapter.unselectedApprovesClicked(item)
                    item.isApprove = false
                    item.count_approve.dec()
                    adapter.notifyItemChanged(adapterPosition, Payload.UNSELECTED_APPROVES_BUTTON)

                } else {
                    adapter.onApproveClicked(adapter.list[adapterPosition])
                    item.isNotApprove = false
                    item.isApprove = true
                    item.count_not_approve.dec()
                    item.count_approve.inc()

                    adapter.notifyItemChanged(adapterPosition, Payload.APPROVE_CHANGE)

                }
            }

            notApproveImgBtn.setOnClickListener {
                val item = adapter.list[adapterPosition]

                if (item.isNotApprove) {
                    adapter.unselectedApprovesClicked(item)
                    item.isNotApprove = false
                    item.count_not_approve.dec()
                    adapter.notifyItemChanged(adapterPosition, Payload.UNSELECTED_APPROVES_BUTTON)

                } else {
                    adapter.onApproveClicked(adapter.list[adapterPosition])
                    item.isApprove = false
                    item.isNotApprove = true
                    //  approveImgBtn.setImageResource(R.drawable.ic_thumb_up_defualt_24dp)
                    item.count_approve.dec()
                    //    notApproveImgBtn.setImageResource(R.drawable.ic_thumb_down_selected_24dp)
                    item.count_not_approve.inc()
                    adapter.notifyItemChanged(adapterPosition, Payload.APPROVE_CHANGE)

                }
            }

            imgBtnRepost.setOnClickListener {
                adapter.onRepostClicked(adapter.list[adapterPosition])
            }

            linkPost.setOnClickListener {
                //toDo()
            }
        }
    }


    override fun bind(post: PostModel) {
        with(itemView) {
            //toDo() need add fun for created new content in repost
            textItem.text = post.content
            titleItem.text = post.author
            dateItem.text = DateFormatter().getFormatDate(post.createdDate)
            countViews.text = post.countViews.toString()
            countApprove.text = post.count_approve.toString()
            countNotApprove.text = post.count_not_approve.toString()
            countReply.text = post.countRepost.toString()
            replyDateItem.text = DateFormatter().getFormatDate(post.repost!!.createdDate)
            replyTitleItem.text = post.repost!!.author
            replyTextItem.text = post.repost!!.content

            when {
                post.isApprove -> {
                    notApproveImgBtn.setImageResource(R.drawable.ic_thumb_down_defualt_24dp)
                    approveImgBtn.setImageResource(R.drawable.ic_thumb_up_selected_24dp)
                }
                post.isNotApprove -> {
                    approveImgBtn.setImageResource(R.drawable.ic_thumb_up_defualt_24dp)
                    notApproveImgBtn.setImageResource(R.drawable.ic_thumb_down_selected_24dp)
                }
                else -> {
                    notApproveImgBtn.setImageResource(R.drawable.ic_thumb_down_defualt_24dp)
                    approveImgBtn.setImageResource(R.drawable.ic_thumb_up_defualt_24dp)
                }
            }

            if (post.count_approve > 100 || post.count_not_approve > 100) {
                badgeText.text =
                    if (post.count_approve > 100) UserBadge.PROMOTER.name else UserBadge.HATER.name
                badgeCardView.visibility = View.VISIBLE
            }
            linkPost.visibility = if (post.urlLink.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            countApprove.visibility =
                if (countApprove.text == "0") View.INVISIBLE else View.VISIBLE
            countNotApprove.visibility =
                if (countNotApprove.text == "0") View.INVISIBLE else View.VISIBLE
            countReply.visibility = if (countReply.text == "0") View.INVISIBLE else View.VISIBLE

            if (post.imageId != null) {
                imagePost.visibility = View.VISIBLE
                Glide.with(context)
                    .load("https://server-post.herokuapp.com/api/v1/static/${post.imageId}")
                    .centerCrop()
                    .placeholder(R.drawable.ic_photo_black_96dp)
                    .into(imagePost)

            } else imagePost.visibility = View.GONE
        }
    }

    override fun bindApproveChange(post: PostModel) {
        with(itemView) {
            if (post.isApprove) {
                notApproveImgBtn.setImageResource(R.drawable.ic_thumb_down_defualt_24dp)
                approveImgBtn.setImageResource(R.drawable.ic_thumb_up_selected_24dp)
            } else {
                approveImgBtn.setImageResource(R.drawable.ic_thumb_up_defualt_24dp)
                notApproveImgBtn.setImageResource(R.drawable.ic_thumb_down_selected_24dp)
            }
            countApprove.text = post.count_approve.toString()
            countNotApprove.text = post.count_not_approve.toString()
        }
    }

    override fun bindUnselectedApprovesButton(post: PostModel) {
        with(itemView) {
            if (!post.isApprove) {
                approveImgBtn.setImageResource(R.drawable.ic_thumb_up_defualt_24dp)
                countApprove.text = post.count_approve.toString()
            } else {
                notApproveImgBtn.setImageResource(R.drawable.ic_thumb_down_defualt_24dp)
                countNotApprove.text = post.count_not_approve.toString()
            }
        }

    }

}