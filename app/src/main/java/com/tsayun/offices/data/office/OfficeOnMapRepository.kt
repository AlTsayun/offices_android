package com.tsayun.offices.data.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsayun.offices.data.office.models.OfficeOnMap

class OfficeOnMapRepository(private val dataSource: OfficeDataSource){

    private val _officesOnMap = MutableLiveData<List<OfficeOnMap>>()
    val officesOnMap: LiveData<List<OfficeOnMap>> = _officesOnMap

    init {
        dataSource.offices.observeForever(Observer {
            _officesOnMap.value = it.map {
                OfficeOnMap(it.value.id, it.value.name, it.value.coordinates)
            }
        })
    }
}