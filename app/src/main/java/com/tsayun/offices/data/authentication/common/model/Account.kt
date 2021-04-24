package com.tsayun.offices.data.authentication.common.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Account(val id: UUID, val name: String, val password: String, val email: String)