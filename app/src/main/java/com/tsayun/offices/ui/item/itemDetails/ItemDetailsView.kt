package com.tsayun.offices.ui.item.itemDetails

import java.util.*

data class ItemDetailsView(
    val id: UUID,
    val name: String,
    val area: Double,
    val address: String,
    val roomCount: Int,
    val description: String,
    val floor: Int,
    val numberOfFloors: Int,
    val hasBathroom: Boolean,
    val lastRenovationDate: Date,
    val imagesUrls: List<String>
)