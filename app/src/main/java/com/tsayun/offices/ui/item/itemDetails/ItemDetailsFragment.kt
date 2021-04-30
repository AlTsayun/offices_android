package com.tsayun.offices.ui.item.itemDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tsayun.offices.R
import com.tsayun.offices.databinding.FragmentItemDetailsBinding
import com.tsayun.offices.databinding.ImagePreviewBinding
import com.tsayun.offices.ui.imageGallery.ImagePreviewView
import java.net.URL


class ItemDetailsFragment : Fragment(R.layout.fragment_item_details) {

    private lateinit var binding: FragmentItemDetailsBinding

    private val itemsDetailsViewModel: ItemDetailsViewModel by activityViewModels()

    private lateinit var inflater: LayoutInflater

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.inflater = inflater
        binding = FragmentItemDetailsBinding.inflate(inflater, container, false)

        setItem(itemsDetailsViewModel.item.value)

        itemsDetailsViewModel.item.observe(viewLifecycleOwner, Observer {
//            it ?: return@Observer
            setItem(it)
        })

        binding.itemDetailsEditButton.setOnClickListener{
            itemsDetailsViewModel.requestEditCurrentItem()
        }
        binding.itemDetailsOpenGalleryButton.setOnClickListener{
            itemsDetailsViewModel.requestOpenImages()
        }

        return binding.root
    }

    private fun setItem(item: ItemDetailsView?) {
        if (item != null) {
            binding.item = item
            binding.itemDetailsImages.layoutManager = GridLayoutManager(context, 3)
            binding.itemDetailsImages.adapter =
                CustomAdapter(item.imagesUrls.map { ImagePreviewView(it) })
        } else {
            binding.item = null
        }

    }
}

class CustomAdapter(private val dataSet: List<ImagePreviewView>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(val binding: ImagePreviewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Add listeners for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding =
            ImagePreviewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Picasso.get().load(dataSet[position].url).into(viewHolder.binding.imagePreview)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}