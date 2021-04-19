package com.tsayun.offices.ui.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    private val _navigation = MutableLiveData<NavigationState>()
    val navigation: LiveData<NavigationState> = _navigation

    fun navigationChanged(selected: NavigationItem){
        _navigation.value = NavigationState(selected)
    }

}