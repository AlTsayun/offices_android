package com.tsayun.offices.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tsayun.offices.data.office.OfficeOnMapRepository

class MapsViewModel(private val officeOnMapRepository: OfficeOnMapRepository): ViewModel() {

    private val _selectedItem = MutableLiveData<ItemMapView?>()
    val selectedItem: LiveData<ItemMapView?> = _selectedItem

    private val _items = MutableLiveData<List<ItemMapView>>()
    val items: LiveData<List<ItemMapView>> = _items

    init {
        officeOnMapRepository.officesOnMap.observeForever(Observer {
            _items.value = it.map { ItemMapView(it.id, it.position, it.title) }
        })
    }

    fun selectItem(item: ItemMapView?) {
        _selectedItem.value = item
    }
}