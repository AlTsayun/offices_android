package com.tsayun.offices.data.office

import androidx.lifecycle.LiveData
import com.tsayun.offices.data.office.models.OfficeDetails
import com.tsayun.offices.data.office.models.OfficePreview
import java.util.*

interface OfficeDataSource{
    val offices: LiveData<MutableMap<String, OfficeDetails>>
    fun getOfficeByIdOrNull(id: UUID): OfficeDetails?
    fun getAllOfficePreviews(): List<OfficePreview>
}