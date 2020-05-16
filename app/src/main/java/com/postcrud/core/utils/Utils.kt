package com.postcrud.core.utils

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.postcrud.R
import java.util.regex.Pattern

fun isValid(password: String): Boolean =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,})\$").matcher(password).matches()


var backPressed = 0L
fun Fragment.onDoubleBackPressed() {

    if (backPressed + 2000 > System.currentTimeMillis()) {
        backPressed = 0L

        //эмулируем нажатие на HOME, сворачивая приложение
        val endWork = Intent(Intent.ACTION_MAIN)
        endWork.addCategory(Intent.CATEGORY_HOME)
        endWork.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        context!!.startActivity(endWork)

    } else toast("Нажмите ещё раз, чтобы выйти.")

    backPressed = System.currentTimeMillis()
}