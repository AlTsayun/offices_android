package com.tsayun.offices.data.office

import androidx.lifecycle.LiveData
import com.tsayun.offices.data.office.models.OfficeFull
import com.tsayun.offices.data.office.models.OfficeOnMap
import com.tsayun.offices.data.office.models.OfficePreview
import java.util.*

interface OfficeDataSource {
    val offices: LiveData<MutableMap<String, OfficeFull>>
    fun getOfficeByIdOrNull(id: UUID): OfficeFull?
//    fun getAllOfficePreviews(): List<OfficePreview>
    fun create(
        name: String,
        area: Double,
        address: String,
        roomCount: Int,
        description: String,
        floor: Int,
        numberOfFloors: Int,
        hasBathroom: Boolean,
        lastRenovationDate: Date
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
        lastRenovationDate: Date
    )

    fun getAllOfficesOnMap(): List<OfficeOnMap>
}