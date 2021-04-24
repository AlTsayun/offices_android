package com.tsayun.offices.ui.authentication.signup

data class SignupFormState(val displayNameError: Int? = null,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)