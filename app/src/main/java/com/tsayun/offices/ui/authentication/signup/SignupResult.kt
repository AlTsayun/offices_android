package com.tsayun.offices.ui.authentication.signup

data class SignupResult (
    val success: SignedUpUserView? = null,
    val error: Int? = null
)