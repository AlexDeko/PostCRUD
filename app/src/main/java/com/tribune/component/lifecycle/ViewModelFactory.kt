package com.tribune.component.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory constructor(
    private val providers: ViewModelProviders
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = providers[modelClass]
            ?: throw IllegalArgumentException("unknown model class $modelClass")

        return creator.get() as T
    }
}