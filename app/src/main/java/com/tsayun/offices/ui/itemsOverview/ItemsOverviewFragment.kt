package com.tsayun.offices.ui.itemsOverview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.tasyun.offices.R
import com.tasyun.offices.databinding.FragmentItemsOverviewBinding
import com.tasyun.offices.databinding.OverviewItemBinding


class ItemsOverviewFragment : Fragment(R.layout.fragment_items_overview) {

    private lateinit var binding: FragmentItemsOverviewBinding

    private val itemsOverviewViewModel: ItemsOverviewViewModel by activityViewModels()

    private lateinit var inflater: LayoutInflater

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        this.inflater = inflater
        binding = FragmentItemsOverviewBinding.inflate(inflater, container, false)

        binding.itemsListView.setOnItemClickListener{ parent, view, position, id ->
            itemsOverviewViewModel.selectItem(position)
        }

        itemsOverviewViewModel.items.observe(viewLifecycleOwner, Observer {
            val items = it ?: return@Observer
            binding.itemsListView.adapter = object : BaseAdapter() {
                override fun getCount(): Int = items.size


                override fun getItem(position: Int): Any = items[position]


                override fun getItemId(position: Int): Long = position.toLong()

                override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                    val binding: OverviewItemBinding
                    if (convertView == null) {
                        val view = this@ItemsOverviewFragment.inflater.inflate(R.layout.overview_item, null)
                        binding = OverviewItemBinding.bind(view)
                        binding.root.tag = binding
                    } else {
                        binding = convertView.tag as OverviewItemBinding
                    }
                    binding.item = items[position]
                    return binding.root
                }
            }
        })

        return binding.root
    }
}