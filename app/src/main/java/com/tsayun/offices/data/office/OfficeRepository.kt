package com.tsayun.offices.data.office

import com.tsayun.offices.data.office.models.OfficeDetails
import java.util.*

class OfficeRepository(private val dataSource: OfficeDataSource) {
    private var office: OfficeDetails? = null

    fun getByIdOrNull(id: UUID): OfficeDetails?{
        office = dataSource.getOfficeByIdOrNull(id)
        return office
    }

}