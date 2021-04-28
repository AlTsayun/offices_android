package com.tsayun.offices.data.office.models

import java.util.*

data class OfficePreview(
    val id: UUID,
    val name: String,
    val area: Double,
    val address: String,
    val roomCount: Int
)