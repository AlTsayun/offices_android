package com.tsayun.offices.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapsViewModel: ViewModel() {

    private val _selectedItem = MutableLiveData<ItemMapView>()
    val selectedItem: LiveData<ItemMapView> = _selectedItem

    private val _items = MutableLiveData<List<ItemMapView>>()
    val items: LiveData<List<ItemMapView>> = _items

    fun selectItem(position: Int) {
        _selectedItem.value = _items.value?.get(position)
    }
}