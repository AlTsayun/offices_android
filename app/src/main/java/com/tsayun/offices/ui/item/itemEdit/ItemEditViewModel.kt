package com.tsayun.offices.ui.item.itemEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.tsayun.offices.data.office.OfficeRepository
import java.util.*

class ItemEditViewModel(private val officeRepository: OfficeRepository) : ViewModel() {

    private val _item = MutableLiveData<ItemEditView>()
    val item: LiveData<ItemEditView> = _item

    private val _saveItemRequest = MutableLiveData<ItemEditView>()
    val saveItemRequest: LiveData<ItemEditView> = _saveItemRequest

    init {
        officeRepository.office.observeForever(Observer {
            if (it != null) {
                _item.value = ItemEditView(
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
                    coordinates = it.coordinates,
                    imagesUrls = it.imagesUrls
                    )
            }
        })
    }

    fun setByIdOrNull(id: UUID){
        officeRepository.setByIdOrNull(id)
    }

    fun itemChanged(
        name: String,
        area: Double,
        address: String,
        roomCount: Int,
        description: String,
        floor: Int,
        numberOfFloors: Int,
        hasBathroom: Boolean,
        lastRenovationDate: Date,
        coordinates: LatLng,
        imagesUrls: List<String>
    ){
        val item = item.value
        if (item != null){
            _item.value = ItemEditView(
                id = item.id,
                name = name,
                area = area,
                address = address,
                roomCount = roomCount,
                description = description,
                floor = floor,
                numberOfFloors = numberOfFloors,
                hasBathroom = hasBathroom,
                lastRenovationDate = lastRenovationDate,
                coordinates = coordinates,
                imagesUrls = imagesUrls
            )
        }
    }

    fun requestSaveCurrentItem(){
        val item = item.value
        if (item != null){
            officeRepository.update(
                name = item.name,
                area = item.area,
                address = item.address,
                roomCount = item.roomCount,
                description = item.description,
                floor = item.floor,
                numberOfFloors = item.numberOfFloors,
                hasBathroom = item.hasBathroom,
                lastRenovationDate = item.lastRenovationDate,
                coordinates = item.coordinates,
                imagesUrls = item.imagesUrls
            )
        }
        _saveItemRequest.value = this.item.value
    }
}