package com.tsayun.offices.data.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tsayun.offices.data.office.models.OfficeDetails
import com.tsayun.offices.data.office.models.OfficePreview

class OfficesRepository(private val dataSource: OfficeDataSource) {

    val offices: LiveData<MutableMap<String, OfficeDetails>> = dataSource.offices

    fun getAllOfficePreviews(): List<OfficePreview> {
        return dataSource.getAllOfficePreviews()
    }
}