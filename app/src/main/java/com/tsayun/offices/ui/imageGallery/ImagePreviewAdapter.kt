package com.tsayun.offices.ui.imageGallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tsayun.offices.databinding.ImagePreviewBinding
import com.tsayun.offices.ui.item.itemDetails.CustomAdapter

class ImagePreviewAdapter(
    private val dataSet: List<ImagePreviewView>,
    private val imageGalleryViewModel: ImageGalleryViewModel
) :
    RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(val binding: ImagePreviewBinding, val imageGalleryViewModel: ImageGalleryViewModel) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Add listeners for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding =
            ImagePreviewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, imageGalleryViewModel)
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