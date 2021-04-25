package com.tsayun.offices.ui.item.itemsOverview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tsayun.offices.data.office.OfficesRepository

class ItemsOverviewViewModel(private val officesRepository: OfficesRepository) : ViewModel() {
    private val _items = MutableLiveData<List<ItemOverviewView>>()
    val items: LiveData<List<ItemOverviewView>> = _items

    private val _selectedItem = MutableLiveData<ItemOverviewView?>()
    val selectedItem: LiveData<ItemOverviewView?> = _selectedItem

    init {
        officesRepository.offices.observeForever(Observer {
            val offices = it ?: return@Observer
            _items.value = offices.values.map {
                ItemOverviewView(
                    it.id,
                    it.name,
                    it.area,
                    it.address,
                    it.roomCount
                )
            }
            val selected = selectedItem.value
            val loadedItems = items.value
            if (selected != null && loadedItems != null && !loadedItems.contains(selected)) {
                _selectedItem.value = null
            }
        })
    }

    fun selectItem(position: Int) {
        _selectedItem.value = _items.value?.get(position)
    }

}