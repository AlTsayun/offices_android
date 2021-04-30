package com.tsayun.offices.ui.item.itemsOverview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tsayun.offices.data.office.OfficePreviewsRepository

class ItemsOverviewViewModel(private val officePreviewsRepository: OfficePreviewsRepository) : ViewModel() {
    private val _items = MutableLiveData<List<ItemOverviewView>>()
    val items: LiveData<List<ItemOverviewView>> = _items

    private val _selectedItem = MutableLiveData<ItemOverviewView?>()
    val selectedItem: LiveData<ItemOverviewView?> = _selectedItem

    init {
        officePreviewsRepository.officePreviews.observeForever(Observer {
            val officePreviews = it ?: return@Observer
            _items.value = officePreviews.map {
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

    fun createItem(){
        officePreviewsRepository.createNew()
    }

}