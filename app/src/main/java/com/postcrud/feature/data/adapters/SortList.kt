package com.postcrud.feature.data.adapters

import com.postcrud.feature.data.dto.PostResponseDto
import com.postcrud.feature.data.model.PostType
import java.util.*

object SortList {

    fun sortList(list: MutableList<PostResponseDto>): MutableList<PostResponseDto> {
        val adsPost = list.indexOfLast {
            it.type == PostType.ADS
        }

        if (adsPost != 0) {
            for (i in 0 until list.size) {
                if ((i + 1) % 3 == 0) {
                    Collections.swap(list, i, adsPost)
                }
            }
        }
        return list
    }
}