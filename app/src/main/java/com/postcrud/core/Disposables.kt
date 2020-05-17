package com.postcrud.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.CompositeDisposable

fun Fragment.disposables(): Lazy<CompositeDisposable> = Disposables {
    viewLifecycleOwner
}

class Disposables (
    private val lifecycleOwner: () -> LifecycleOwner
) : Lazy<CompositeDisposable>, LifecycleEventObserver {

    private var cached: CompositeDisposable? = null

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                cached?.clear()
                source.lifecycle.removeObserver(this)
            }
            else -> Unit
        }
    }

    override val value: CompositeDisposable
        get() = cached ?: CompositeDisposable().also {
            cached = it
            lifecycleOwner().lifecycle.addObserver(this)
        }

    override fun isInitialized(): Boolean = cached != null
}