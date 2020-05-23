package com.postcrud.core.utils

import java.util.regex.Pattern

fun isValidPassword(password: String): Boolean =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,})\$").matcher(password).matches()

fun isValidEmail(email: String): Boolean =
    Pattern.compile("(^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}\$)").matcher(email).matches()