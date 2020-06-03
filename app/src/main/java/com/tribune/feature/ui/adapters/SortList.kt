package com.tribune.feature.ui.adapters

import com.tribune.feature.data.model.PostType
import java.util.*

object SortList {

    fun sortList(list: MutableList<com.tribune.feature.data.model.PostModel>): MutableList<com.tribune.feature.data.model.PostModel> {
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