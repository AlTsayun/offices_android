package com.tsayun.offices.data.authentification.login.model

import java.util.*

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
        val userId: UUID,
        val displayName: String
)