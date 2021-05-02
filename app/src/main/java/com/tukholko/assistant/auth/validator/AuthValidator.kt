package com.tukholko.assistant.auth.validator

class AuthValidator {
    fun emailValid(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }
        return email.matches(".+[@].+".toRegex())
    }

    fun passwordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun nameValid(name: String): Boolean {
        return name.isNotEmpty()
    }
}