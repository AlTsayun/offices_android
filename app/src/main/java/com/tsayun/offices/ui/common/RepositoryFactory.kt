package com.tsayun.offices.ui.common

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tsayun.offices.data.common.FirebaseRealtimeDatabaseDataSource
import com.tsayun.offices.data.authentification.login.LoginRepository
import com.tsayun.offices.data.common.CommonDataSource
import com.tsayun.offices.data.office.OfficesRepository

interface RepositoryFactory {
    val loginRepository: LoginRepository
    val officesRepository: OfficesRepository}

class RepositoryFactoryImpl : RepositoryFactory {

    private val commonDataSource : CommonDataSource = FirebaseRealtimeDatabaseDataSource(Firebase.database.reference)

    override val loginRepository: LoginRepository = LoginRepository(commonDataSource)
    override val officesRepository: OfficesRepository = OfficesRepository(commonDataSource)

}