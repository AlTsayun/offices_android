package com.tsayun.offices.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tsayun.offices.data.common.RepositoryFactory
import com.tsayun.offices.ui.item.itemsOverview.ItemsOverviewViewModel
import com.tsayun.offices.ui.authentication.login.LoginViewModel
import com.tsayun.offices.ui.authentication.signup.SignupViewModel
import com.tsayun.offices.ui.imageFull.ImageFullViewModel
import com.tsayun.offices.ui.imageGallery.ImageGalleryViewModel
import com.tsayun.offices.ui.item.itemDetails.ItemDetailsViewModel
import com.tsayun.offices.ui.item.itemEdit.ItemEditViewModel
import com.tsayun.offices.ui.map.MapsViewModel
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
            return ItemsOverviewViewModel(repositoryFactory.officePreviewsRepository) as T
        }

        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(repositoryFactory.signupRepository) as T
        }

        if (modelClass.isAssignableFrom(ItemDetailsViewModel::class.java)) {
            return ItemDetailsViewModel(repositoryFactory.officeRepository) as T
        }

        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(repositoryFactory.officeOnMapRepository) as T
        }

        if (modelClass.isAssignableFrom(ImageGalleryViewModel::class.java)) {
            return ImageGalleryViewModel() as T
        }

        if (modelClass.isAssignableFrom(ImageFullViewModel::class.java)) {
            return ImageFullViewModel() as T
        }

        if (modelClass.isAssignableFrom(ItemEditViewModel::class.java)) {
            return ItemEditViewModel(repositoryFactory.officeRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}