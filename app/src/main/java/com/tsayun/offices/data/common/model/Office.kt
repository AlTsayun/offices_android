package com.tsayun.offices.data.common.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Office(val id: UUID, val name: String, val password: String, val email: String)