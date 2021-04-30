package com.tsayun.offices.ui.main

import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.gson.Gson
import com.tsayun.offices.R
import com.tsayun.offices.data.common.RepositoryFactory
import com.tsayun.offices.data.common.RepositoryFactoryImpl
import com.tsayun.offices.ui.LocaleManager
import com.tsayun.offices.ui.authentication.login.LoggedInUserView
import com.tsayun.offices.ui.authentication.login.LoginFragment
import com.tsayun.offices.ui.authentication.login.LoginViewModel
import com.tsayun.offices.ui.authentication.signup.SignupFragment
import com.tsayun.offices.ui.authentication.signup.SignupViewModel
import com.tsayun.offices.ui.common.ViewModelFactoryImpl
import com.tsayun.offices.ui.imageFull.ImageFullFragment
import com.tsayun.offices.ui.imageFull.ImageFullView
import com.tsayun.offices.ui.imageFull.ImageFullViewModel
import com.tsayun.offices.ui.imageGallery.ImageGalleryFragment
import com.tsayun.offices.ui.imageGallery.ImageGalleryViewModel
import com.tsayun.offices.ui.imageGallery.ImagePreviewView
import com.tsayun.offices.ui.item.itemDetails.ItemDetailsFragment
import com.tsayun.offices.ui.item.itemDetails.ItemDetailsViewModel
import com.tsayun.offices.ui.item.itemEdit.ItemEditFragment
import com.tsayun.offices.ui.item.itemEdit.ItemEditViewModel
import com.tsayun.offices.ui.item.itemsOverview.ItemsOverviewFragment
import com.tsayun.offices.ui.item.itemsOverview.ItemsOverviewViewModel
import com.tsayun.offices.ui.map.MapsFragment
import com.tsayun.offices.ui.map.MapsViewModel
import com.tsayun.offices.ui.navigation.NavigationFragment
import com.tsayun.offices.ui.navigation.NavigationItem
import com.tsayun.offices.ui.navigation.NavigationViewModel
import com.tsayun.offices.ui.settings.SettingsFragment


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var loginFragment: LoginFragment
    private lateinit var signupFragment: SignupFragment
    private lateinit var itemsOverviewFragment: ItemsOverviewFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var homeFragment: Fragment
    private lateinit var mapsFragment: MapsFragment
    private lateinit var itemDetailsFragment: ItemDetailsFragment
    private lateinit var imageGalleryFragment: ImageGalleryFragment
    private lateinit var imageFullFragment: ImageFullFragment
    private lateinit var itemEditFragment: ItemEditFragment

    private lateinit var repoFactory: RepositoryFactory

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var navigationViewModel: NavigationViewModel
    private lateinit var itemsOverviewViewModel: ItemsOverviewViewModel
    private lateinit var itemDetailsViewModel: ItemDetailsViewModel
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var imageGalleryViewModel: ImageGalleryViewModel
    private lateinit var imageFullViewModel: ImageFullViewModel
    private lateinit var itemEditViewModel: ItemEditViewModel

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
        itemDetailsViewModel = vmProvider.get(ItemDetailsViewModel::class.java)
        mapsViewModel = vmProvider.get(MapsViewModel::class.java)
        imageGalleryViewModel = vmProvider.get(ImageGalleryViewModel::class.java)
        imageFullViewModel = vmProvider.get(ImageFullViewModel::class.java)
        itemEditViewModel = vmProvider.get(ItemEditViewModel::class.java)


        // fragments
        loginFragment = LoginFragment()
        signupFragment = SignupFragment()
        itemsOverviewFragment = ItemsOverviewFragment()
        settingsFragment = SettingsFragment()
        mapsFragment = MapsFragment()
        itemDetailsFragment = ItemDetailsFragment()
        imageGalleryFragment = ImageGalleryFragment()
        imageFullFragment = ImageFullFragment()
        itemEditFragment = ItemEditFragment()


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
            navigationViewModel.enableMapState()
            itemsOverviewFragment
        } else {
            navigationViewModel.disableMapState()
            loginFragment
        }


        signupViewModel.signupResult.observe(this, Observer {
            val signupResult = it ?: return@Observer
            if (signupResult.success != null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_text_user_signed_up, signupResult.success.displayName),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_text_error_while_signing_up),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        signupViewModel.loginRequest.observe(this, Observer {
            var credentials = it ?: return@Observer

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
                Toast.makeText(
                    applicationContext,
                    getString(R.string.toast_text_error_while_logging_in),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        loginViewModel.signupRequest.observe(this, Observer {
            var credentials = it ?: return@Observer

            homeFragment = signupFragment
            switchTo(homeFragment)
        })

        itemsOverviewViewModel.selectedItem.observe(this, Observer {
            val selectedItem = it ?: return@Observer

            Toast.makeText(
                applicationContext,
                getString(R.string.toast_text_item_selected, selectedItem.name),
                Toast.LENGTH_SHORT
            ).show()
            itemDetailsViewModel.setItem(selectedItem.id)
            homeFragment = itemDetailsFragment
            switchTo(homeFragment)
        })

        mapsViewModel.selectedItem.observe(this, Observer {
            val selectedItem = it ?: return@Observer

            Toast.makeText(
                applicationContext,
                getString(R.string.toast_text_item_selected_from_map, selectedItem.title),
                Toast.LENGTH_SHORT
            ).show()
            itemDetailsViewModel.setItem(selectedItem.id)
            homeFragment = itemDetailsFragment
            switchTo(homeFragment)
        })

        itemDetailsViewModel.openImagesRequest.observe(this, Observer {
            it ?: return@Observer

            homeFragment = imageGalleryFragment
            imageGalleryViewModel.setItems(it.imagesUrls.map { ImagePreviewView(it) })
            switchTo(homeFragment)
        })
        itemDetailsViewModel.editRequest.observe(this, Observer {
            it ?: return@Observer
            homeFragment = itemEditFragment
            switchTo(itemEditFragment)
        })

        imageGalleryViewModel.selectedItem.observe(this, Observer {
            it ?: return@Observer

            homeFragment = imageFullFragment
            imageFullViewModel.setImage(ImageFullView(it.url))
            switchTo(homeFragment)
        })

        itemEditViewModel.saveItemRequest.observe(this, Observer {
            it ?: return@Observer
            Toast.makeText(
                applicationContext,
                getString(R.string.toast_text_item_saved, it.name),
                Toast.LENGTH_SHORT
            ).show()
            homeFragment = itemsOverviewFragment
            switchTo(homeFragment)
        })

        // need to be saved in field as gets removed by gc otherwise
        onSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == getString(R.string.pref_key_application_theme)) {
                    val selectedTheme = sharedPreferences.getString(getString(R.string.pref_key_application_theme), "")
                    finish()
                    startActivity(intent)
                }
                if (key == getString(R.string.pref_key_logged_in_user)) {
                    val loggedInUserStr = sharedPreferences.getString(
                        getString(R.string.pref_key_logged_in_user),
                        null
                    )
                    if (loggedInUserStr != null) {
                        val model = Gson().fromJson(loggedInUserStr, LoggedInUserView::class.java)

                        navigationViewModel.enableMapState()
                        homeFragment = itemsOverviewFragment
                        switchTo(homeFragment)

                        Toast.makeText(
                            applicationContext,
                            "${getString(R.string.welcome)} ${model.displayName}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        navigationViewModel.disableMapState()
                        homeFragment = loginFragment
                        switchTo(homeFragment)
                    }

                }
                if (key == getString(R.string.pref_key_language)){
                    val languageCode = sharedPreferences.getString(
                        getString(R.string.pref_key_language),
                        null
                    )
                    if (languageCode == "ru"){
                        LocaleManager.setLocale(this, languageCode)
                    } else {
                        LocaleManager.setLocale(this, "en")
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
                if (homeFragment != loginFragment && homeFragment != signupFragment) {
                    homeFragment = itemsOverviewFragment
                }
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
        } else if (mainFragment == mapsFragment){
            navigationViewModel.setNavigation(NavigationItem.MAP)
        } else {
            navigationViewModel.setNavigation(NavigationItem.HOME)
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.main_fragment_container_view, mainFragment)
            addToBackStack(null)
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme: Resources.Theme = super.getTheme()
        if (getDefaultSharedPreferences(this).getString(getString(
                resources.getIdentifier(
                    "pref_key_application_theme",
                    "string",
                    packageName
                )
            ), "") == "light") {
            theme.applyStyle(R.style.Theme_Offices_Light, true)
        }
        return theme
    }

}