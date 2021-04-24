package com.tsayun.offices.ui.itemDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsayun.offices.data.office.OfficeRepository
import java.util.*

class ItemDetailsViewModel(private val officeRepository: OfficeRepository) : ViewModel() {

    private val _item = MutableLiveData<ItemDetailsView?>()
    val item: LiveData<ItemDetailsView?> = _item

    fun setItem(id:UUID){
        val persistedItem = officeRepository.getByIdOrNull(id)
        _item.value = if (persistedItem != null) {
             ItemDetailsView(
                id = persistedItem.id,
                name = persistedItem.name,
                area = persistedItem.area,
                address = persistedItem.address,
                roomCount = persistedItem.roomCount
            )
        }else {
            null
        }
    }

}