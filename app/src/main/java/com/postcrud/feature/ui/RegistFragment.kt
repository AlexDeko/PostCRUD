package com.postcrud.feature.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.postcrud.PREFS_TOKEN


import com.postcrud.R
import com.postcrud.core.BaseFragment
import com.postcrud.core.api.AuthApi
import com.postcrud.core.utils.isValid
import com.postcrud.core.utils.putString
import com.postcrud.core.utils.toast
import com.postcrud.feature.data.dto.AuthenticationRequestDto
import com.postcrud.feature.data.dto.AuthenticationResponseDto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_regist.*
import org.koin.android.ext.android.get

class RegistFragment : BaseFragment() {

    private val prefs: SharedPreferences = get()
    private val authApi: AuthApi = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_regist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpButton.setOnClickListener {
            emailTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false
            repeatPasswordTextInputLayout.isErrorEnabled = false


            if (isNotValid()) return@setOnClickListener
            val authenticationRequestDto = getAuthDto()

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

    private fun onAuthSuccess(authenticationResponseDto: AuthenticationResponseDto) {
        prefs.putString(PREFS_TOKEN, authenticationResponseDto.token)
        progressBar.hide()
        signUpButton.isEnabled = true
        findNavController().navigate(R.id.action_registFragment_to_mainFragment)
    }

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


    private fun isNotValid(): Boolean {
        if (emailInput.text.toString().isEmpty() && passwordInput.text.toString().isEmpty()
            && repeatPasswordInput.text.toString().isEmpty()
        ) {
            emailTextInputLayout.error = "Заполните поле"
            passwordTextInputLayout.error = "Заполните поле"
            repeatPasswordTextInputLayout.error = "Заполните поле"
            return true
        } else if (emailInput.text.toString().isEmpty() && passwordInput.text.toString()
                .isEmpty()
        ) {
            emailTextInputLayout.error = "Заполните поле"
            passwordTextInputLayout.error = "Заполните поле"
        } else if (emailInput.text.toString().isEmpty() && repeatPasswordInput.text.toString()
                .isEmpty()
        ) {
            emailTextInputLayout.error = "Заполните поле"
            repeatPasswordTextInputLayout.error = "Заполните поле"
        } else if (passwordInput.text.toString()
                .isEmpty() && repeatPasswordInput.text.toString().isEmpty()
        ) {
            passwordTextInputLayout.error = "Заполните поле"
            repeatPasswordTextInputLayout.error = "Заполните поле"
        }

        if (emailInput.text.toString().isEmpty()) {
            emailTextInputLayout.error = "Заполните поле"
            return true
        }
        if (passwordInput.text.toString().isEmpty()) {
            passwordTextInputLayout.error = "Заполните поле"
            return true
        }

        if (repeatPasswordInput.text.toString().isEmpty()) {
            repeatPasswordTextInputLayout.error = "Заполните поле"
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

        if (passwordInput.text.toString() != repeatPasswordInput.text.toString()) {
            toast("Пароли должны совпадать")
            return true
        }

        return false
    }
}