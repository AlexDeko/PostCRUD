package com.postcrud.core.utils

import java.util.regex.Pattern

fun isValid(password: String): Boolean =
    Pattern.compile("(?=.*[A-Z])(?!.*[^a-zA-Z0-9])(.{6,})\$").matcher(password).matches()