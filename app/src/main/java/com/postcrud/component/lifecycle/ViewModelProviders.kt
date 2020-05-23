package com.postcrud.component.lifecycle

import androidx.lifecycle.ViewModel
import javax.inject.Provider

typealias ViewModelProviders = Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>