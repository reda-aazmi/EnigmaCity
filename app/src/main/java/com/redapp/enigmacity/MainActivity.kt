package com.redapp.enigmacity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
const val LOCATION_PERMISSION_REQUEST=1

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment


        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        checkRequirements()
    }

    private fun checkRequirements() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        } else {
            checkLocationSettings()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationPermission() {
        val isLocationGranted = this.checkSelfPermission(LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED
        if (!isLocationGranted)
            this.requestPermissions( arrayOf(LOCATION_PERMISSION), LOCATION_PERMISSION_REQUEST )
        else{
            checkLocationSettings()
        }
    }


    /** this callback will be triggered when the user responds to a permission request **/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            LOCATION_PERMISSION_REQUEST -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    showGrantPermissionSnackBar()
                }else{
                    checkLocationSettings()
                }
            }
        }
    }

    private fun showGrantPermissionSnackBar() {
        Snackbar.make(findViewById(R.id.map_fragment), getString(R.string.request_grant_permission), Snackbar.LENGTH_INDEFINITE)
            // display a settings button to redirect the user to the application settings in order to grant the necessary permission
            .setAction(getString(R.string.settings)) {
                startActivity(Intent().apply {
                    //intent to start this application details settings
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    //Start the activity in a new task
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
            .show()
    }


    private fun checkLocationSettings() {
        if(!isLocationEnabled()){
            showEnableLocationSnackBar()
        }
    }

    private fun showEnableLocationSnackBar() {
        Snackbar.make(findViewById(R.id.map_fragment), getString(R.string.turn_on_location_setting), Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.ok)) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }.show()
    }

    private fun isLocationEnabled() : Boolean{
        val locationManager: LocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var locationEnabled = false
        try {
            locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            Log.e(TAG, "checkLocationSettings: ", e)
        }
        return locationEnabled
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}