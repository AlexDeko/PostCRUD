package com.tribune.feature.ui.adapters.diff_util

import androidx.recyclerview.widget.DiffUtil
import com.tribune.feature.ui.adapters.PostRecyclerAdapter

class PostDiffUtilResult() {
    fun getDIffUtilResult(
        recyclerAdapter: PostRecyclerAdapter,
        baseListNote: List<com.tribune.feature.data.model.PostModel>
    ) {
        val notesDiffUtilCallback =
            PostDiffUtilCallback(recyclerAdapter.list, baseListNote)
        val notesDiffResult = DiffUtil.calculateDiff(notesDiffUtilCallback)
        recyclerAdapter.list = baseListNote.toMutableList()
        notesDiffResult.dispatchUpdatesTo(recyclerAdapter)
    }
}