package com.tsayun.offices.data.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsayun.offices.data.office.models.OfficePreview

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

}