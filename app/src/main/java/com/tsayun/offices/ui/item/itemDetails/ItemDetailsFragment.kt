package com.tsayun.offices.ui.item.itemDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.tsayun.offices.R
import com.tsayun.offices.databinding.FragmentItemDetailsBinding

class ItemDetailsFragment : Fragment(R.layout.fragment_item_details) {

    private lateinit var binding: FragmentItemDetailsBinding

    private val itemsDetailsViewModel: ItemDetailsViewModel by activityViewModels()

    private lateinit var inflater: LayoutInflater

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        this.inflater = inflater
        binding = FragmentItemDetailsBinding.inflate(inflater, container, false)

        itemsDetailsViewModel.item.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer

        })

        return binding.root
    }
}