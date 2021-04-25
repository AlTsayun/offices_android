package com.tsayun.offices.ui.main

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.gson.Gson
import com.tasyun.offices.R
import com.tsayun.offices.ui.common.RepositoryFactory
import com.tsayun.offices.ui.common.RepositoryFactoryImpl
import com.tsayun.offices.ui.common.ViewModelFactoryImpl
import com.tsayun.offices.ui.itemsOverview.ItemsOverviewFragment
import com.tsayun.offices.ui.itemsOverview.ItemsOverviewViewModel
import com.tsayun.offices.ui.authentication.login.LoggedInUserView
import com.tsayun.offices.ui.authentication.login.LoginFragment
import com.tsayun.offices.ui.authentication.login.LoginViewModel
import com.tsayun.offices.ui.authentication.signup.SignupFormState
import com.tsayun.offices.ui.authentication.signup.SignupFragment
import com.tsayun.offices.ui.authentication.signup.SignupViewModel
import com.tsayun.offices.ui.map.MapsFragment
import com.tsayun.offices.ui.navigation.NavigationFragment
import com.tsayun.offices.ui.navigation.NavigationItem
import com.tsayun.offices.ui.navigation.NavigationViewModel
import com.tsayun.offices.ui.settings.SettingsFragment
import kotlin.math.log

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var loginFragment: LoginFragment
    private lateinit var signupFragment: SignupFragment
    private lateinit var itemsOverviewFragment: ItemsOverviewFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var homeFragment: Fragment
    private lateinit var mapsFragment: MapsFragment

    private lateinit var repoFactory: RepositoryFactory

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var signupViewModel: SignupViewModel
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
        signupViewModel = vmProvider.get(SignupViewModel::class.java)
        navigationViewModel = vmProvider.get(NavigationViewModel::class.java)
        itemsOverviewViewModel = vmProvider.get(ItemsOverviewViewModel::class.java)

        // fragments
        loginFragment = LoginFragment()
        signupFragment = SignupFragment()
        itemsOverviewFragment = ItemsOverviewFragment()
        settingsFragment = SettingsFragment()
        mapsFragment = MapsFragment()

        //todo: fix getString(R.string)
        homeFragment = if (getDefaultSharedPreferences(this).contains(
                getString(
                    resources.getIdentifier(
                        "pref_key_logged_in_user",
                        "string",
                        packageName
                    )
                )
            )
        ) {
            itemsOverviewFragment
        } else {
            loginFragment
        }


        signupViewModel.signupResult.observe(this, Observer {
            val signupResult = it?: return@Observer
            if (signupResult.success != null){
                Toast.makeText(
                    applicationContext,
                    "User ${signupResult.success.displayName} signed up",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        signupViewModel.loginRequest.observe(this, Observer {
            var credentials = it?: return@Observer

            homeFragment = loginFragment
            switchTo(homeFragment)
        })


        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.success != null) {
                val editor = getDefaultSharedPreferences(this).edit()
                editor.putString(
                    getString(R.string.pref_key_logged_in_user),
                    Gson().toJson(loginResult.success)
                )
                editor.apply()
            }
            if (loginResult.error != null) {
                // todo: show error while login
//                homeFragment = loginFragment
//                switchTo(homeFragment)
            }
        })
        loginViewModel.signupRequest.observe(this, Observer {
            var credentials = it?: return@Observer

            homeFragment = signupFragment
            switchTo(homeFragment)
        })

        itemsOverviewViewModel.selectedItem.observe(this, Observer {
            val selectedItem = it ?: return@Observer



            Toast.makeText(
                applicationContext,
                "Item ${selectedItem.name} is selected",
                Toast.LENGTH_SHORT
            ).show()
        })

        // need to be saved in field as gets removed by gc otherwise
        onSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == getString(R.string.pref_key_font_size)) {
                    val fontSize = sharedPreferences.getString("font_size", "a")
                    Toast.makeText(
                        applicationContext,
                        "font_size is set to $fontSize",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (key == getString(R.string.pref_key_logged_in_user)) {
                    val loggedInUserStr = sharedPreferences.getString(
                        getString(R.string.pref_key_logged_in_user),
                        null
                    )
                    if (loggedInUserStr != null) {
                        val model = Gson().fromJson(loggedInUserStr, LoggedInUserView::class.java)
                        val welcome = getString(R.string.welcome)
                        val displayName = model.displayName

                        navigationViewModel.enableMapState()
                        homeFragment = itemsOverviewFragment
                        switchTo(homeFragment)

                        // TODO : initiate successful logged in experience
                        Toast.makeText(
                            applicationContext,
                            "$welcome $displayName",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        navigationViewModel.disableMapState()
                        homeFragment = loginFragment
                        switchTo(homeFragment)
                    }

                }
            }

        val sharedPreferences = getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)


        navigationViewModel.navigationClicked.observe(this, Observer {
            val navigation = it ?: return@Observer
            if (navigation.selected == NavigationItem.SETTINGS) {
                switchTo(settingsFragment)
            }
            if (navigation.selected == NavigationItem.HOME) {
                switchTo(homeFragment)
            }
            if (navigation.selected == NavigationItem.MAP) {
                switchTo(mapsFragment)
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
        if (mainFragment == settingsFragment) {
            navigationViewModel.setNavigation(NavigationItem.SETTINGS)
        } else {
            navigationViewModel.setNavigation(NavigationItem.HOME)
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_fragment_container_view, mainFragment)
            addToBackStack(null)
        }
    }

}