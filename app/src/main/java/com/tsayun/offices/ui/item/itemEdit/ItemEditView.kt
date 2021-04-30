package com.tsayun.offices.ui.item.itemEdit

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class ItemEditView(
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
    val coordinates: LatLng,
    val imagesUrls: List<String>
)