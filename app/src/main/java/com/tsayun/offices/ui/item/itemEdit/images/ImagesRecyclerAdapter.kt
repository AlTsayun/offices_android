package com.tsayun.offices.ui.item.itemEdit.images

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tsayun.offices.databinding.ImagePreviewBinding
import com.tsayun.offices.ui.imageGallery.ImagePreviewView
import com.tsayun.offices.ui.item.itemEdit.ItemEditViewModel

class ImagesRecyclerAdapter(private val _dataSet: MutableList<ImagePreviewView>, private val itemEditViewModel: ItemEditViewModel) :
    RecyclerView.Adapter<ImagesRecyclerAdapter.ViewHolder>() {

    private val _imagesToRemove: MutableList<ImagePreviewView>  = mutableListOf()
    val imagesToRemove: List<ImagePreviewView> = _imagesToRemove

    val dataSet: List<ImagePreviewView> = _dataSet


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

        viewHolder.binding.image = dataSet[position]

        viewHolder.binding.root.setOnClickListener{
            val image = viewHolder.binding.image
            if (image != null) {
                if (imagesToRemove.contains(image)) {
                    viewHolder.binding.imagePreview.clearColorFilter()
                    _imagesToRemove.remove(image)
                } else {
                    _imagesToRemove.add(image)
                    viewHolder.binding.imagePreview.setColorFilter(
                        Color.GRAY,
                        PorterDuff.Mode.MULTIPLY
                    )
                }
            }
        }
        Picasso.get().load(dataSet[position].url).into(viewHolder.binding.imagePreview)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun addItem(imagePreviewView: ImagePreviewView){
        _dataSet.add(imagePreviewView)
        notifyItemInserted(_dataSet.size - 1)
    }

}