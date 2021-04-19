package com.tsayun.offices.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tasyun.offices.R
import com.tasyun.offices.databinding.FragmentNavigationBinding
import com.tsayun.offices.ui.login.LoginViewModel

class NavigationFragment : Fragment(R.layout.fragment_navigation) {

    private val loginViewModel: LoginViewModel by activityViewModels()
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

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.error != null) {
                bottomNavigationView.menu.findItem(R.id.nav_map).isEnabled = false
            }
            if (loginResult.success != null) {
                bottomNavigationView.menu.findItem(R.id.nav_map).isEnabled = true
            }

        })

        bottomNavigationView.setOnNavigationItemSelectedListener {
            navigationViewModel.navigationChanged(when (it.itemId) {
                R.id.nav_home -> NavigationItem.HOME
                R.id.nav_map -> NavigationItem.MAP
                R.id.nav_settings -> NavigationItem.SETTINGS
                else -> NavigationItem.HOME
            })
            true
        }
        return binding.root
    }
}