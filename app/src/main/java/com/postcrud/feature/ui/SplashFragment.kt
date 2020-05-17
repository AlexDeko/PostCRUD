package com.postcrud.feature.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.postcrud.PREFS_TOKEN

import com.postcrud.R
import com.postcrud.core.utils.getString
import org.koin.android.ext.android.get

class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs: SharedPreferences = get()

        if (prefs.getString(PREFS_TOKEN) != null) {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_authFragment)
        }
    }
}
