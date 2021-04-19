package com.tsayun.offices.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tasyun.offices.R
import com.tasyun.offices.databinding.FragmentNavigationBinding

class SettingsFragment : Fragment(R.layout.fragment_navigation) {
    private lateinit var binding: FragmentNavigationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }
}