package com.postcrud.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.postcrud.PREFS_TOKEN

import com.postcrud.R
import com.postcrud.client.BaseFragment
import com.postcrud.client.api.AuthApi
import com.postcrud.client.utils.putString
import com.postcrud.client.utils.toast
import com.postcrud.data.dto.AuthenticationRequestDto
import com.postcrud.data.dto.AuthenticationResponseDto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.android.ext.android.get



class AuthFragment : BaseFragment() {

    private val prefs: SharedPreferences = get()
    private val authApi: AuthApi = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInButton.setOnClickListener {
            emailTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false

            if (emailInput.text.toString().isEmpty()) {
                emailTextInputLayout.error = "Заполните поле"
                return@setOnClickListener
            }
            if (passwordInput.text.toString().isEmpty()) {
                passwordTextInputLayout.error = "Заполните поле"
                return@setOnClickListener
            }

            val authenticationRequestDto = getAuthDto() ?: return@setOnClickListener
            prepareAuth()

            authApi.signIn(authenticationRequestDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onAuthSuccess(it)
                }, {
                    onAuthError(it)
                })
                .addTo(compositeDisposable)
        }

        signUpButton.setOnClickListener {
            val authenticationRequestDto = getAuthDto() ?: return@setOnClickListener

            prepareAuth()

            authApi.signUp(authenticationRequestDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onAuthSuccess(it)
                }, {
                    onAuthError(it)
                })
                .addTo(compositeDisposable)
        }
    }

    private fun getAuthDto(): AuthenticationRequestDto? {
        val email = emailInput.text?.toString()
        val password = passwordInput.text?.toString()

        if (email == null || email == "") {
            toast("Заполните поле почты")
            return null
        }
        if (password == null || password == "") {
            toast("Заполните поле пароля")
            return null
        }

        return AuthenticationRequestDto(email, password)
    }

    private fun prepareAuth() {
        progressBar.visibility = View.VISIBLE
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
        progressBar.visibility = View.GONE
        signUpButton.isEnabled = true
        signInButton.isEnabled = true
        toast(throwable.localizedMessage!!)
    }
}
