package com.tsayun.offices.data.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tsayun.offices.data.office.models.OfficeFull
import java.util.*

class OfficeRepository(private val dataSource: OfficeDataSource) {
    private var _office = MutableLiveData<OfficeFull?>()
    var office: LiveData<OfficeFull?> = _office

    fun setByIdOrNull(id: UUID) {
        _office.value = dataSource.getOfficeByIdOrNull(id)
    }

    fun update(
        name: String,
        area: Double,
        address: String,
        roomCount: Int,
        description: String,
        floor: Int,
        numberOfFloors: Int,
        hasBathroom: Boolean,
        lastRenovationDate: Date
    ) {
        if (office.value != null) {
            //todo: fix !!
            val id = office.value!!.id
            dataSource.update(
                id,
                name,
                area,
                address,
                roomCount,
                description,
                floor,
                numberOfFloors,
                hasBathroom,
                lastRenovationDate
            )
            _office.value = dataSource.getOfficeByIdOrNull(id)
        }
    }

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
    ) {
        val id = dataSource.create(
            name,
            area,
            address,
            roomCount,
            description,
            floor,
            numberOfFloors,
            hasBathroom,
            lastRenovationDate
        )
        setByIdOrNull(id)
    }

}