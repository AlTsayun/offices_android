package com.tsayun.offices.data.office

import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import com.tsayun.offices.data.office.models.OfficeFull
import com.tsayun.offices.data.office.models.OfficeOnMap
import com.tsayun.offices.data.office.models.OfficePreview
import java.util.*

interface OfficeDataSource {
    val offices: LiveData<MutableMap<String, OfficeFull>>
    fun getOfficeByIdOrNull(id: UUID): OfficeFull?
    fun create(
    name: String,
    area: Double,
    address: String,
    roomCount: Int,
    description: String,
    floor: Int,
    numberOfFloors: Int,
    hasBathroom: Boolean,
    lastRenovationDate: Date,
    coordinates: LatLng,
    imagesUrls: List<String>
    ): UUID

    fun update(
        id: UUID,
        name: String,
        area: Double,
        address: String,
        roomCount: Int,
        description: String,
        floor: Int,
        numberOfFloors: Int,
        hasBathroom: Boolean,
        lastRenovationDate: Date,
        coordinates: LatLng,
        imagesUrls: List<String>
    )

    fun remove(id: UUID)
}