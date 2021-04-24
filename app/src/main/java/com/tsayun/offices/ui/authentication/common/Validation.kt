package com.tsayun.offices.ui.authentication.common

import android.util.Patterns

class Validation {
    companion object{
        // A placeholder username validation check
        fun isUserNameValid(username: String): Boolean {
            return if (username.contains('@')) {
                Patterns.EMAIL_ADDRESS.matcher(username).matches()
            } else {
                false //username.isNotBlank()
            }
        }

        // A placeholder password validation check
        fun isPasswordValid(password: String): Boolean {
            return password.length > 5
        }

        fun isDisplayNameValid(displayName: String): Boolean {
            return displayName.length > 2 && displayName.matches("""^[a-zA-Z0-9 ]*${'$'}""".toRegex())
        }
    }
}