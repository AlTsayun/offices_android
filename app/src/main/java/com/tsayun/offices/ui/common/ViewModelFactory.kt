package com.tsayun.offices.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tsayun.offices.ui.itemsOverview.ItemsOverviewViewModel
import com.tsayun.offices.ui.login.LoginViewModel
import com.tsayun.offices.ui.navigation.NavigationViewModel

interface ViewModelFactory :ViewModelProvider.Factory

class ViewModelFactoryImpl(private val repositoryFactory: RepositoryFactory): ViewModelFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repositoryFactory.loginRepository) as T
        }

        if (modelClass.isAssignableFrom(NavigationViewModel::class.java)) {
            return NavigationViewModel() as T
        }

        if (modelClass.isAssignableFrom(ItemsOverviewViewModel::class.java)) {
            return ItemsOverviewViewModel(repositoryFactory.officesRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}