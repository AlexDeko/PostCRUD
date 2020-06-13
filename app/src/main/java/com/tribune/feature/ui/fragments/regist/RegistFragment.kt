package com.tribune.feature.ui.fragments.regist


import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tribune.PREFS_TOKEN
import com.tribune.R
import com.tribune.core.api.AuthApi
import com.tribune.core.utils.isValidPassword
import com.tribune.core.utils.putString
import com.tribune.core.utils.toast
import com.tribune.feature.data.dto.AuthenticationRequestDto
import com.tribune.feature.data.dto.AuthenticationResponseDto
import kotlinx.android.synthetic.main.fragment_regist.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class RegistFragment : Fragment(R.layout.fragment_regist) {

    private val prefs: SharedPreferences = get()
    private val authApi: AuthApi = get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signUpButton.setOnClickListener {
            emailTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false
            repeatPasswordTextInputLayout.isErrorEnabled = false

            if (isNotValidInput()) return@setOnClickListener
            val authenticationRequestDto = getAuthDto()

            prepareAuth()

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val token = authApi.signUp(authenticationRequestDto)
                    onAuthSuccess(token)
                } catch (e: Exception) {
                    onAuthError(e)
                }
            }
        }
    }

    private fun onAuthSuccess(authenticationResponseDto: AuthenticationResponseDto) {
        prefs.putString(PREFS_TOKEN, authenticationResponseDto.token)
        progressBar.hide()
        signUpButton.isEnabled = true
        findNavController().navigate(R.id.action_registFragment_to_mainFragment)
    }

    //toDo does't register, if input email or username (String) someone using
    private fun onAuthError(throwable: Throwable) {
        progressBar.hide()
        signUpButton.isEnabled = true
        toast(throwable.localizedMessage!!)
    }

    private fun getAuthDto(): AuthenticationRequestDto {
        return AuthenticationRequestDto(
            username = emailInput.text.toString(),
            password = passwordInput.text.toString()
        )
    }

    private fun prepareAuth() {
        progressBar.show()
        signUpButton.isEnabled = false
    }


    private fun isNotValidInput(): Boolean {
        if (emailInput.text.toString().isEmpty() && passwordInput.text.toString().isEmpty()
            && repeatPasswordInput.text.toString().isEmpty()
        ) {
            emailTextInputLayout.error = getString(R.string.errorEmptyEditText)
            passwordTextInputLayout.error = getString(R.string.errorEmptyEditText)
            repeatPasswordTextInputLayout.error = getString(R.string.errorEmptyEditText)
            return true
        } else if (emailInput.text.toString().isEmpty() && passwordInput.text.toString()
                .isEmpty()
        ) {
            emailTextInputLayout.error = getString(R.string.errorEmptyEditText)
            passwordTextInputLayout.error = getString(R.string.errorEmptyEditText)
        } else if (emailInput.text.toString().isEmpty() && repeatPasswordInput.text.toString()
                .isEmpty()
        ) {
            emailTextInputLayout.error = getString(R.string.errorEmptyEditText)
            repeatPasswordTextInputLayout.error = getString(R.string.errorEmptyEditText)
        } else if (passwordInput.text.toString()
                .isEmpty() && repeatPasswordInput.text.toString().isEmpty()
        ) {
            passwordTextInputLayout.error = getString(R.string.errorEmptyEditText)
            repeatPasswordTextInputLayout.error = getString(R.string.errorEmptyEditText)
        }

        if (emailInput.text.toString().isEmpty()) {
            emailTextInputLayout.error = getString(R.string.errorEmptyEditText)
            return true
        }
        if (passwordInput.text.toString().isEmpty()) {
            passwordTextInputLayout.error = getString(R.string.errorEmptyEditText)
            return true
        }

        if (repeatPasswordInput.text.toString().isEmpty()) {
            repeatPasswordTextInputLayout.error = getString(R.string.errorEmptyEditText)
            return true
        }

        if (!isValidPassword(password = passwordInput.text.toString())) {
            toast(getString(R.string.errorValidPassword))
            return true
        }

        if (passwordInput.text.toString() != repeatPasswordInput.text.toString()) {
            toast(getString(R.string.errorEqualsPassword))
            return true
        }

        return false
    }
}