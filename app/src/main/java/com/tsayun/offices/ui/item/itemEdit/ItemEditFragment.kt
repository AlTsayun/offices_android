package com.tsayun.offices.ui.item.itemEdit

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.tsayun.offices.R
import com.tsayun.offices.databinding.FragmentItemEditBinding
import com.tsayun.offices.ui.imageGallery.ImagePreviewView
import com.tsayun.offices.ui.item.itemEdit.images.ImagesRecyclerAdapter
import java.util.*


class ItemEditFragment : Fragment(R.layout.fragment_item_edit) {

    private val itemEditViewModel: ItemEditViewModel by activityViewModels()
    private lateinit var binding: FragmentItemEditBinding
    private lateinit var inflater: LayoutInflater
    private lateinit var imagesRecyclerAdapter: ImagesRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.inflater = inflater
        binding = FragmentItemEditBinding.inflate(inflater, container, false)
        itemEditViewModel.item.observe(viewLifecycleOwner, Observer {
            binding.item = it
            val date = it.lastRenovationDate
            val cal: Calendar = Calendar.getInstance()
            cal.time = date
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH)
            val dayOfMonth: Int = cal.get(Calendar.DAY_OF_MONTH)
            binding.itemEditRenovationDatePicker.init(year, month, dayOfMonth, null)

            binding.itemEditImages.layoutManager = GridLayoutManager(context, 3)
            imagesRecyclerAdapter = ImagesRecyclerAdapter(it.imagesUrls.map { ImagePreviewView(it) }
                .toMutableList(), itemEditViewModel)
            binding.itemEditImages.adapter = imagesRecyclerAdapter

            binding.itemEditLoadImage.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle(getString(R.string.action_enter_image_url))
                val input = EditText(context)
                builder.setView(input)
                builder.setPositiveButton(getString(R.string.action_ok)) { dialog, which ->
                    imagesRecyclerAdapter.addItem(ImagePreviewView(input.text.toString()))
                }
                builder.setNegativeButton(getString(R.string.action_cancel)) { dialog, which ->
                    dialog.cancel()
                }

                builder.show()
            }

        })

        binding.itemEditSave.setOnClickListener {
            it ?: return@setOnClickListener
            itemChanged()
            itemEditViewModel.requestSaveCurrentItem()
        }

        return binding.root
    }

    fun itemChanged(
        name: String = binding.itemEditName.text.toString(),
        area: Double = binding.itemEditArea.text.toString().replace(',', '.').toDouble(),
        address: String = binding.itemEditAddress.text.toString(),
        roomCount: Int = binding.itemEditRoomCount.text.toString().toInt(),
        description: String = binding.itemEditDescription.text.toString(),
        floor: Int = binding.itemEditFloor.text.toString().toInt(),
        numberOfFloors: Int = binding.itemEditTotalFloors.text.toString().toInt(),
        hasBathroom: Boolean = binding.itemEditHasBathroom.isChecked,
        lastRenovationDate: Date = Date(
            binding.itemEditRenovationDatePicker.year - 1900,
            binding.itemEditRenovationDatePicker.month,
            binding.itemEditRenovationDatePicker.dayOfMonth
        ),
        coordinates: LatLng = LatLng(
            binding.itemEditLatitude.text.toString().replace(',', '.').toDouble(),
            binding.itemEditLongitude.text.toString().replace(',', '.').toDouble()
        ),
        imagesUrls: List<String> = imagesRecyclerAdapter.dataSet.filter {
            !imagesRecyclerAdapter.imagesToRemove.contains(
                it
            )
        }.map { it.url }
    ) {
        itemEditViewModel.itemChanged(
            name,
            area,
            address,
            roomCount,
            description,
            floor,
            numberOfFloors,
            hasBathroom,
            lastRenovationDate,
            coordinates,
            imagesUrls
        )
    }
}