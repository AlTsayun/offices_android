package com.tsayun.offices.ui.imageFull

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsayun.offices.ui.imageGallery.ImagePreviewView

class ImageFullViewModel: ViewModel() {

    private val _item = MutableLiveData<ImageFullView?>()
    val item: LiveData<ImageFullView?> = _item

    fun setImage(image: ImageFullView){
        _item.value = image
    }
}
