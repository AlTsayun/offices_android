package com.tsayun.offices.ui.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    private val _navigationState = MutableLiveData<NavigationState>()
    val navigationState: LiveData<NavigationState> = _navigationState
    private val _navigationClicked = MutableLiveData<NavigationState>()
    val navigationClicked: LiveData<NavigationState> = _navigationClicked

    fun setNavigation(selected: NavigationItem){
        _navigationState.value = NavigationState(selected)
    }
    fun clickOnNavigation(selected: NavigationItem){
        _navigationClicked.value = NavigationState(selected)
        _navigationState.value = NavigationState(selected)

    }

}