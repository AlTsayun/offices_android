package com.tsayun.offices.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tsayun.offices.R

class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private var markerToItemMapView: MutableMap<Marker, ItemMapView> = mutableMapOf()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        mapsViewModel.items.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            googleMap.clear()
            it.forEach {
                val marker =
                    googleMap.addMarker(MarkerOptions().position(it.position).title(it.title))
                marker.showInfoWindow()
                markerToItemMapView[marker] = it
            }
        })

//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.setOnMarkerClickListener {
            it ?: return@setOnMarkerClickListener false
            mapsViewModel.selectItem(markerToItemMapView[it])
            return@setOnMarkerClickListener true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}