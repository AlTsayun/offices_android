package com.tsayun.offices.data.authentication.signup

import com.tsayun.offices.data.authentication.signup.model.SignedUpUser
import com.tsayun.offices.data.common.model.Result

interface SignUpDataSource {
    fun signup(displayName: String, username: String, password: String): Result<SignedUpUser>
}