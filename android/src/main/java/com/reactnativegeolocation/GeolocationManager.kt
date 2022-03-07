package com.reactnativegeolocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.reactnativebglocation.DEFAULT_DISTANCE
import com.reactnativebglocation.DEFAULT_MILLISECONDS
import com.reactnativegeolocation.exceptions.PermissionException

class GeolocationManager {
  var reactContext: Context
  var locationManager: LocationManager
  var locationListener: LocationListener

  var currentLocation: Location
  var isRunning: Boolean = false


  @SuppressLint("MissingPermission")
  constructor(reactContext: Context) {
    this.reactContext = reactContext;
    // Get locationManager
    this.locationManager = this.reactContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    this.currentLocation = Location(LocationManager.GPS_PROVIDER)
    // Create a locationListener
    this.locationListener = object : LocationListener {
      override fun onLocationChanged(location: Location) {
        println("Geolocation data: $location")
        currentLocation = location
      }
      override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        println("Status changed")
        println(p0)
        println(p1)
        println(p2)
      }
      override fun onProviderEnabled(p0: String?) {
        println("Provider enabled")
      }
      override fun onProviderDisabled(p0: String?) {
        println("Provider disabled")
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun startLocationUpdates() {
    // Start location update once the class is instantiated
    if (this.checkPermissions()) {
      // Start location updates
      this.locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        DEFAULT_MILLISECONDS,
        DEFAULT_DISTANCE,
        this.locationListener
      )
      // Get last known location
      this.currentLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
      // Once everything is done, set isRunning to true
      this.isRunning = true
    } else {
      throw PermissionException(Manifest.permission.ACCESS_FINE_LOCATION)
    }
  }


  fun checkPermissions(): Boolean {
    return ActivityCompat.checkSelfPermission(this.reactContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
  }

}
