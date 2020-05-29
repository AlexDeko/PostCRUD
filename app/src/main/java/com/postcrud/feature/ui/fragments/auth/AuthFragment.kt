package com.postcrud.feature.ui.fragments.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.postcrud.PREFS_TOKEN

import com.postcrud.R
import com.postcrud.core.api.AuthApi
import com.postcrud.core.utils.*
import com.postcrud.feature.data.dto.AuthenticationRequestDto
import com.postcrud.feature.data.dto.AuthenticationResponseDto
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import java.lang.Exception


class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val prefs: SharedPreferences = get()
    private val authApi: AuthApi = get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signInButton.setOnClickListener {
            emailTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false

            if (isNotValidInput()) return@setOnClickListener

            val authenticationRequestDto = getAuthDto() ?: return@setOnClickListener
            prepareAuth()

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val token = authApi.signIn(authenticationRequestDto)

                    onAuthSuccess(token)
                } catch (e: Exception) {
                    onAuthError(e)
                }
            }
        }

        signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_registFragment)
        }
    }

    private fun getAuthDto(): AuthenticationRequestDto? {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        return AuthenticationRequestDto(username = email, password = password)
    }

    private fun prepareAuth() {
        progressBar.show()
        signUpButton.isEnabled = false
        signInButton.isEnabled = false
    }

    private fun onAuthSuccess(authenticationResponseDto: AuthenticationResponseDto) {
        prefs.putString(PREFS_TOKEN, authenticationResponseDto.token)
        progressBar.visibility = View.GONE
        signUpButton.isEnabled = true
        signInButton.isEnabled = true
        findNavController().navigate(R.id.action_authFragment_to_mainFragment)
    }

    private fun onAuthError(throwable: Throwable) {
        progressBar.hide()
        signUpButton.isEnabled = true
        signInButton.isEnabled = true
        toast(throwable.localizedMessage!!)
    }

    private fun isNotValidInput(): Boolean {
        if (emailInput.text.toString().isEmpty() && passwordInput.text.toString().isEmpty()) {
            emailTextInputLayout.error = getString(R.string.errorEmptyEditText)
            passwordTextInputLayout.error = getString(R.string.errorEmptyEditText)
            return true
        }

        if (emailInput.text.toString().isEmpty()) {
            emailTextInputLayout.error = getString(R.string.errorEmptyEditText)
            return true
        }
        if (passwordInput.text.toString().isEmpty()) {
            passwordTextInputLayout.error = getString(R.string.errorEmptyEditText)
            return true
        }

        if (!isValidPassword(password = passwordInput.text.toString())) {
            toast(getString(R.string.errorValidPassword))
            return true
        }

        return false
    }
}
