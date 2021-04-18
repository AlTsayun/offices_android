package com.tsayun.offices.data.login

import com.tsayun.offices.data.login.model.LoggedInUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
interface LoginDataSource {
    fun login(username: String, password: String): Result<LoggedInUser>
    fun logout()
}