package com.geekbrains.filmroulette.view

import android.Manifest
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.geekbrains.filmroulette.PLACE_OF_BIRTH_KEY
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    companion object {
        fun newInstance(bundle: Bundle) = MapsFragment().apply { arguments = bundle }
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding: FragmentMapsBinding
        get() :FragmentMapsBinding {
            return _binding!!
        }

    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val location = Geocoder(context)
            .getFromLocationName(arguments?.getString(PLACE_OF_BIRTH_KEY), 1)
        val latLngLocation = LatLng(location[0].latitude, location[0].longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngLocation, 10f))
        map.addMarker(
            MarkerOptions().position(latLngLocation).title(
                arguments?.getString(
                    PLACE_OF_BIRTH_KEY
                )
            )
        )

        myLocation(googleMap)
        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun myLocation(map: GoogleMap) {
        context?.let {
            val status = ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PermissionChecker.PERMISSION_GRANTED
            map.isMyLocationEnabled = status
            map.uiSettings.isMyLocationButtonEnabled = status
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.buttonSearch.setOnClickListener {
            val geoCoder = Geocoder(context)
            val searchText = binding.searchAddress.text.toString()
            val address = geoCoder.getFromLocationName(searchText, 1)
            val location = LatLng(address[0].latitude, address[0].longitude)

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            map.addMarker(MarkerOptions().position(location))
        }
    }
}