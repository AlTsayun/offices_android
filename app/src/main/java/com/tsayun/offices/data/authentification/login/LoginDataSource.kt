package com.tsayun.offices.data.authentification.login

import com.tsayun.offices.data.authentification.login.model.LoggedInUser
import com.tsayun.offices.data.common.model.Result

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
interface LoginDataSource {
    fun login(username: String, password: String): Result<LoggedInUser>
    fun logout()
}