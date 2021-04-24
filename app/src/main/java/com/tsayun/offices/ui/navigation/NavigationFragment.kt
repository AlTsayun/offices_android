package com.tsayun.offices.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.tasyun.offices.R
import com.tasyun.offices.databinding.FragmentNavigationBinding

class NavigationFragment : Fragment(R.layout.fragment_navigation) {

    private val navigationViewModel: NavigationViewModel by activityViewModels()

    private lateinit var binding: FragmentNavigationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavigationBinding.inflate(inflater, container, false)
        val view = binding.root
        val bottomNavigationView = binding.bottomNavigation

        bottomNavigationView.menu.findItem(R.id.nav_map).isEnabled = false

        navigationViewModel.isMapStateActive.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            bottomNavigationView.menu.findItem(R.id.nav_map).isEnabled = it
        })

        navigationViewModel.navigationState.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

            //if to avoid double setting
            if (binding.bottomNavigation.selectedItemId != when (it.selected) {
                    NavigationItem.HOME -> R.id.nav_home
                    NavigationItem.MAP -> R.id.nav_map
                    NavigationItem.SETTINGS -> R.id.nav_settings
                    else -> R.id.nav_home
                }
            ) {
                binding.bottomNavigation.selectedItemId = when (it.selected) {
                    NavigationItem.HOME -> R.id.nav_home
                    NavigationItem.MAP -> R.id.nav_map
                    NavigationItem.SETTINGS -> R.id.nav_settings
                    else -> R.id.nav_home
                }
            }
        })

        bottomNavigationView.setOnNavigationItemSelectedListener {
            navigationViewModel.clickOnNavigation(
                when (it.itemId) {
                    R.id.nav_home -> NavigationItem.HOME
                    R.id.nav_map -> NavigationItem.MAP
                    R.id.nav_settings -> NavigationItem.SETTINGS
                    else -> NavigationItem.HOME
                }
            )
            true
        }
        return binding.root
    }
}