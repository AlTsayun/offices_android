package com.tsayun.offices.ui.map

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class ItemMapView(val id: UUID, val position: LatLng, val title: String)