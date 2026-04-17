package com.example.runtrail.feature.tracker.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.runtrail.domain.model.LocationPoint
import com.example.runtrail.ui.theme.RunTrailBlue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LiveMapView(
    locationPoints: List<LocationPoint>,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()

    // Auto-pan to latest location as user moves
    LaunchedEffect(locationPoints.lastOrNull()) {
        locationPoints.lastOrNull()?.let { last ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(last.latitude, last.longitude), 16f
                )
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(mapType = MapType.NORMAL),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        )
    ) {
        if (locationPoints.size >= 2) {
            Polyline(
                points = locationPoints.map { LatLng(it.latitude, it.longitude) },
                color = RunTrailBlue,
                width = 8f
            )
        }
        // Pulsing dot at current position
        locationPoints.lastOrNull()?.let {
            Marker(
                state = MarkerState(LatLng(it.latitude, it.longitude)),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        }
    }
}