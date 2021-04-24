package com.tsayun.offices.ui.itemDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tasyun.offices.R
import com.tasyun.offices.databinding.FragmentItemDetailsBinding
import com.tasyun.offices.databinding.FragmentItemsOverviewBinding
import com.tsayun.offices.ui.itemsOverview.ItemsOverviewViewModel

class ItemDetailsFragment : Fragment(R.layout.fragment_item_details) {

    private lateinit var binding: FragmentItemDetailsBinding

    private val itemsDetailsViewModel: ItemDetailsViewModel by activityViewModels()
}