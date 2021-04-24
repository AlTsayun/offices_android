package com.tsayun.offices.data.authentication.login

import com.tsayun.offices.data.authentication.login.model.LoggedInUser
import com.tsayun.offices.data.common.model.Result

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
interface LoginDataSource {
    fun login(username: String, password: String): Result<LoggedInUser>
    fun logout()
}