package com.tribune.feature.ui.fragments.splash

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tribune.PREFS_TOKEN

import com.tribune.R
import com.tribune.core.utils.getString
import org.koin.android.ext.android.get

class SplashFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        val prefs: SharedPreferences = get()

        if (prefs.getString(PREFS_TOKEN) != null) {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_authFragment)
        }
    }
}
