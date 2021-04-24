package com.tsayun.offices.data.authentication.signup

import com.tsayun.offices.data.authentication.signup.model.SignedUpUser
import com.tsayun.offices.data.common.model.Result

class SignupRepository(private val signUpDataSource: SignUpDataSource) {
    fun signup(displayName: String, username: String, password: String): Result<SignedUpUser>{
        return signUpDataSource.signup(displayName, username, password)
    }
}