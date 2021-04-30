package com.tsayun.offices.ui.imageGallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageGalleryViewModel : ViewModel() {

    private val _selectedItem = MutableLiveData<ImagePreviewView?>()
    val selectedItem: LiveData<ImagePreviewView?> = _selectedItem

    private val _items = MutableLiveData<List<ImagePreviewView>>()
    val items: LiveData<List<ImagePreviewView>> = _items

    fun selectItem(item: ImagePreviewView?){
        _selectedItem.value = item
    }

    fun setItems(items: List<ImagePreviewView>){
        _items.value = items
    }

}