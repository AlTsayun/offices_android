package com.tsayun.offices.ui.item.itemDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.Observer
import com.tsayun.offices.data.office.OfficeRepository
import java.util.*

class ItemDetailsViewModel(private val officeRepository: OfficeRepository) : ViewModel() {

    private val _item = MutableLiveData<ItemDetailsView?>()
    val item: LiveData<ItemDetailsView?> = _item

    private val _editRequest = MutableLiveData<ItemDetailsView?>()
    val editRequest: LiveData<ItemDetailsView?> = _editRequest

    private val _openImagesRequest = MutableLiveData<ItemDetailsView?>()
    val openImagesRequest: LiveData<ItemDetailsView?> = _openImagesRequest


    fun setItem(id: UUID) {
        _item.value = null

        officeRepository.setByIdOrNull(id)
        officeRepository.office.observeForever(Observer {
            _item.value = if (it != null) {
                ItemDetailsView(
                    id = it.id,
                    name = it.name,
                    area = it.area,
                    address = it.address,
                    roomCount = it.roomCount,
                    description = it.description,
                    floor = it.floor,
                    numberOfFloors = it.numberOfFloors,
                    hasBathroom = it.hasBathroom,
                    lastRenovationDate = it.lastRenovationDate,
                    imagesUrls = it.imagesUrls
                )
            } else {
                null
            }  
        })
        

    }

    fun requestEditCurrentItem() {
        _editRequest.value = _item.value
    }

    fun requestOpenImages(){
        _openImagesRequest.value = _item.value
    }

}