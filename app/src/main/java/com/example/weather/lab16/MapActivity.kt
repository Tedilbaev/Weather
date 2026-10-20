package com.example.weather.lab16

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weather.BuildConfig
import com.example.weather.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private var userLocationLayer: UserLocationLayer? = null

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val granted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            enableUserLocation()
        } else {
            Toast.makeText(this, R.string.lab16_permission_denied, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // ключ — до initialize
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab16_activity_map)

        mapView = findViewById(R.id.mapview)

        // стартовая камера (Москва), пока нет GPS
        mapView.mapWindow.map.move(
            CameraPosition(Point(55.75, 37.62), 12.0f, 0f, 0f)
        )

        findViewById<FloatingActionButton>(R.id.btnMyLocation).setOnClickListener {
            requestLocationAndShow()
        }

        requestLocationAndShow()
    }

    private fun requestLocationAndShow() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun enableUserLocation() {
        val mapKit = MapKitFactory.getInstance()
        if (userLocationLayer == null) {
            userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        }
        userLocationLayer?.isVisible = true
        userLocationLayer?.isHeadingModeActive = true

        // если уже есть позиция — приблизить камеру
        userLocationLayer?.cameraPosition()?.let { pos ->
            mapView.mapWindow.map.move(pos)
        }

        Toast.makeText(this, R.string.lab16_location_on, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}