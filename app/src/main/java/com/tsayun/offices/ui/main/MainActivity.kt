package com.tsayun.offices.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tasyun.offices.R
import com.tsayun.offices.ui.login.LoginFragment
import com.tsayun.offices.ui.login.LoginViewModel
import com.tsayun.offices.ui.login.LoginViewModelFactory
import com.tsayun.offices.ui.navigation.NavigationFragment
import com.tsayun.offices.ui.navigation.NavigationItem
import com.tsayun.offices.ui.navigation.NavigationViewModel
import com.tsayun.offices.ui.navigation.NavigationViewModelFactory
import com.tsayun.offices.ui.settings.SettingsFragment
import com.tsayun.offices.ui.settings.SettingsViewModel
import com.tsayun.offices.ui.settings.SettingsViewModelFactory

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var loginViewModel: LoginViewModel
//    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var navigationViewModel: NavigationViewModel

    private lateinit var loginFragment: LoginFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        navigationViewModel = ViewModelProvider(this, NavigationViewModelFactory())
            .get(NavigationViewModel::class.java)

        loginFragment = LoginFragment()
        settingsFragment = SettingsFragment()

        navigationViewModel.navigation.observe(this, Observer {
            val navigation = it ?: return@Observer
            if (navigation.selected == NavigationItem.SETTINGS){
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.main_fragment_container_view, settingsFragment)
//                    addToBackStack(null)
                }
            }
            if (navigation.selected == NavigationItem.HOME){
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.main_fragment_container_view, loginFragment)
                    addToBackStack(null)
                }
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.main_fragment_container_view, loginFragment)
                add<NavigationFragment>(R.id.bottom_fragment_container_view)
            }
        }
    }
}