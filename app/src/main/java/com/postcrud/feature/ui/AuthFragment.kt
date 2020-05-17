package com.postcrud.feature.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.postcrud.PREFS_TOKEN

import com.postcrud.R
import com.postcrud.core.BaseFragment
import com.postcrud.core.api.AuthApi
import com.postcrud.core.utils.*
import com.postcrud.feature.data.dto.AuthenticationRequestDto
import com.postcrud.feature.data.dto.AuthenticationResponseDto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.android.ext.android.get


class AuthFragment : BaseFragment() {

    private val prefs: SharedPreferences = get()
    private val authApi: AuthApi = get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInButton.setOnClickListener {
            emailTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false

            if (isNotValid()) return@setOnClickListener

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

    private fun isNotValid(): Boolean {
        if (emailInput.text.toString().isEmpty() && passwordInput.text.toString().isEmpty()) {
            emailTextInputLayout.error = "Заполните поле"
            passwordTextInputLayout.error = "Заполните поле"
            return true
        }

        if (emailInput.text.toString().isEmpty()) {
            emailTextInputLayout.error = "Заполните поле"
            return true
        }
        if (passwordInput.text.toString().isEmpty()) {
            passwordTextInputLayout.error = "Заполните поле"
            return true
        }

        if (isValid(password = passwordInput.text.toString())) {
            toast(
                """ Пароль должен содержать минимум 6 символов, иметь хотя бы одну заглавную букву.
                    Разрешены только английские символы и цифры.
                """".trimMargin()
            )
            return true
        }

        return false
    }

//    //двойным нажатием сворачивание экрана из фрагмента
//    override fun onBackPressed(): Boolean {
//        onDoubleBackPressed()
//        return true
//    }
}
