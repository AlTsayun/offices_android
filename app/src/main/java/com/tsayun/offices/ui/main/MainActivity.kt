package com.tsayun.offices.ui.main

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.tasyun.offices.R
import com.tsayun.offices.ui.common.RepositoryFactory
import com.tsayun.offices.ui.common.RepositoryFactoryImpl
import com.tsayun.offices.ui.common.ViewModelFactory
import com.tsayun.offices.ui.common.ViewModelFactoryImpl
import com.tsayun.offices.ui.itemsOverview.ItemsOverviewFragment
import com.tsayun.offices.ui.itemsOverview.ItemsOverviewViewModel
import com.tsayun.offices.ui.login.LoginFragment
import com.tsayun.offices.ui.login.LoginViewModel
import com.tsayun.offices.ui.navigation.NavigationFragment
import com.tsayun.offices.ui.navigation.NavigationItem
import com.tsayun.offices.ui.navigation.NavigationViewModel
import com.tsayun.offices.ui.settings.SettingsFragment
import com.tsayun.offices.ui.settings.SettingsViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var loginFragment: LoginFragment
    private lateinit var itemsOverviewFragment: ItemsOverviewFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var homeFragment: Fragment

    private lateinit var repoFactory: RepositoryFactory

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var navigationViewModel: NavigationViewModel
    private lateinit var itemsOverviewViewModel: ItemsOverviewViewModel

    private lateinit var vmProvider: ViewModelProvider

    private lateinit var onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repoFactory = RepositoryFactoryImpl()
        vmProvider = ViewModelProvider(this, ViewModelFactoryImpl(repoFactory))

        // view models
        loginViewModel = vmProvider.get(LoginViewModel::class.java)
        navigationViewModel = vmProvider.get(NavigationViewModel::class.java)
        itemsOverviewViewModel = vmProvider.get(ItemsOverviewViewModel::class.java)

        // fragments
        loginFragment = LoginFragment()
        itemsOverviewFragment = ItemsOverviewFragment()
        settingsFragment = SettingsFragment()
        homeFragment = loginFragment


        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.success != null) {
                homeFragment = itemsOverviewFragment
                switchTo(homeFragment)
            }
            if (loginResult.error != null) {
                homeFragment = loginFragment
                switchTo(homeFragment)
            }
        })

        itemsOverviewViewModel.selectedItem.observe(this, Observer {
            val selectedItem = it ?: return@Observer
            Toast.makeText(
                applicationContext,
                "item ${selectedItem.name} is selected",
                Toast.LENGTH_SHORT
            ).show()
        })

        // need to be saved in field as gets removed by gc otherwise
        onSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == "font_size") {
                    val fontSize = sharedPreferences.getString("font_size", "a")
                    Toast.makeText(
                        applicationContext,
                        "font_size is set to $fontSize",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        val sharedPreferences = getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)

//        val editor = getDefaultSharedPreferences(this).edit()
//        editor.putString("font_bonk", "fff")
//        editor.apply()

        navigationViewModel.navigation.observe(this, Observer {
            val navigation = it ?: return@Observer
            if (navigation.selected == NavigationItem.SETTINGS) {
                switchTo(settingsFragment)
            }
            if (navigation.selected == NavigationItem.HOME) {
                switchTo(homeFragment)
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.main_fragment_container_view, homeFragment)
                add<NavigationFragment>(R.id.bottom_fragment_container_view)
            }
        }
    }

    private fun switchTo(mainFragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_fragment_container_view, mainFragment)
            addToBackStack(null)
        }
    }

}