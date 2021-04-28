package com.tsayun.offices.data.common

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tsayun.offices.data.authentication.login.LoginRepository
import com.tsayun.offices.data.authentication.signup.SignupRepository
import com.tsayun.offices.data.office.OfficeOnMapRepository
import com.tsayun.offices.data.office.OfficePreviewsRepository
import com.tsayun.offices.data.office.OfficeRepository

interface RepositoryFactory {
    val loginRepository: LoginRepository
    val signupRepository: SignupRepository
    val officePreviewsRepository: OfficePreviewsRepository
    val officeRepository: OfficeRepository
    val officeOnMapRepository: OfficeOnMapRepository
}

class RepositoryFactoryImpl : RepositoryFactory {

    private val commonDataSource : CommonDataSource = FirebaseRealtimeDatabaseDataSource(Firebase.database.reference)

    override val loginRepository: LoginRepository = LoginRepository(commonDataSource)
    override val signupRepository: SignupRepository = SignupRepository(commonDataSource)
    override val officePreviewsRepository: OfficePreviewsRepository = OfficePreviewsRepository(commonDataSource)
    override val officeRepository: OfficeRepository = OfficeRepository(commonDataSource)
    override val officeOnMapRepository: OfficeOnMapRepository = OfficeOnMapRepository(commonDataSource)
}