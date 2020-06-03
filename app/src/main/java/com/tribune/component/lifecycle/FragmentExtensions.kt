package com.tribune.component.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline viewModelProviders: () -> ViewModelProviders
): ViewModelProperty<VM> =
    ViewModelProperty(
        VM::class,
        ownerProducer
    ) {
        ViewModelFactory(viewModelProviders())
    }