package com.postcrud.feature.ui.adapters.diff_util

import androidx.recyclerview.widget.DiffUtil
import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.ui.adapters.PostRecyclerAdapter

class PostDiffUtilResult() {
    fun getDIffUtilResult(
        recyclerAdapter: PostRecyclerAdapter,
        baseListNote: List<PostResponseDto>
    ) {
        val notesDiffUtilCallback =
            PostDiffUtilCallback(recyclerAdapter.list, baseListNote)
        val notesDiffResult = DiffUtil.calculateDiff(notesDiffUtilCallback)
        recyclerAdapter.list = baseListNote.toMutableList()
        notesDiffResult.dispatchUpdatesTo(recyclerAdapter)
    }
}