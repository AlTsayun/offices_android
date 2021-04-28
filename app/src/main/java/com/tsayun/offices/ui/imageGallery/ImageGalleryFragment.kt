package com.tsayun.offices.ui.imageGallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tsayun.offices.R
import com.tsayun.offices.databinding.FragmentGalleryBinding
import com.tsayun.offices.databinding.FragmentItemDetailsBinding

class ImageGalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val imageGalleryViewModel: ImageGalleryViewModel by activityViewModels()
    private lateinit var inflater: LayoutInflater
    private lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }
}