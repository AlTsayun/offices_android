package com.tsayun.offices.data.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.tsayun.offices.data.office.models.OfficePreview
import java.util.*

class OfficePreviewsRepository(private val dataSource: OfficeDataSource) {

    private val _officePreviews = MutableLiveData<List<OfficePreview>>()
    val officePreviews: LiveData<List<OfficePreview>> = _officePreviews

    init {
        dataSource.offices.observeForever(Observer {
            _officePreviews.value = it.map {
                OfficePreview(
                    it.value.id,
                    it.value.name,
                    it.value.area,
                    it.value.address,
                    it.value.roomCount
                )
            }
        })
    }
    fun createNew(): UUID {
        return dataSource.create(
            name = "",
            area = 0.0,
            address = "",
            roomCount = 0,
            description = "",
            floor = 0,
            numberOfFloors = 0,
            hasBathroom = false,
            lastRenovationDate = Date(),
            coordinates = LatLng(0.0, 0.0),
            imagesUrls = listOf()
        )
    }

    fun removeSingleItem(id: UUID){

    }


}