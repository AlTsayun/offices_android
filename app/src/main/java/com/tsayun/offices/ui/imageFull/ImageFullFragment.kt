package com.tsayun.offices.ui.imageFull

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.tsayun.offices.R
import com.tsayun.offices.databinding.FragmentImageFullBinding

class ImageFullFragment : Fragment(R.layout.fragment_image_full) {

    private val imageFullViewModel: ImageFullViewModel by activityViewModels()
    private lateinit var binding: FragmentImageFullBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageFullBinding.inflate(inflater, container, false)
        binding.image = null

        imageFullViewModel.item.observeForever(Observer {
            it ?: return@Observer
            Picasso.get().load(it.url).into(binding.imageFullView)
        })

        return binding.root
    }
}