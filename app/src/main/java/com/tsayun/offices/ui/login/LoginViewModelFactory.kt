package com.tsayun.offices.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tsayun.offices.data.common.dataSource.OfficeDataSource
import com.tsayun.offices.data.login.LoginDataSource
import com.tsayun.offices.data.login.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = OfficeDataSource(Firebase.database.getReference("offices"))
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}