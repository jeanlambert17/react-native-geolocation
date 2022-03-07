package com.reactnativegeolocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.reactnativebglocation.DEFAULT_DISTANCE
import com.reactnativebglocation.DEFAULT_MILLISECONDS
import com.reactnativegeolocation.constants.CHANNEL_ID
import com.reactnativegeolocation.constants.CHANNEL_NAME
import com.reactnativegeolocation.constants.NOTIFICATION_ID
import com.reactnativegeolocation.exceptions.PermissionException


class GeolocationBackgroundService : Service(), LocationListener {
  private lateinit var locationManager: LocationManager
  private lateinit var notificationManager: NotificationManager
  lateinit var currentLocation: Location

  // Service methods
  // Disable binding
  override fun onBind(intent: Intent?): IBinder? = null
  override fun onCreate() {
    super.onCreate()
    // Get locationManager
    this.locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    this.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    this.currentLocation = Location(LocationManager.GPS_PROVIDER)
  }
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    this.startLocationUpdates()
    if(GeolocationModule.activity !== null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val pendingIntent: PendingIntent = Intent(applicationContext, GeolocationModule.activity::class.java).let { notificationIntent ->
        PendingIntent.getActivity(this, 0, notificationIntent, 0)
      }
      val notification = createNotification(pendingIntent)
      startForeground(NOTIFICATION_ID, notification)
    }
    return super.onStartCommand(intent, flags, startId)
  }
  override fun onDestroy() {
    super.onDestroy()
    // Cancel the notification
    this.notificationManager.cancel(NOTIFICATION_ID)
  }


  // LocationListener methods
  override fun onLocationChanged(location: Location?) {
    println("Geolocation data: $location")
    // Update the current location
    if (location != null) {
      this.currentLocation = location
    }
  }
  override fun onStatusChanged(provider: String?, status: Int, p2: Bundle?) {
    if(status == 1) {
      println("${provider!!.toUpperCase()} Temporary unavailable")
    } else if(status == 2) {
      println("${provider!!.toUpperCase()} Available")
    }
    println(p2)
  }
  override fun onProviderEnabled(p0: String?) = println("Provider enabled")
  override fun onProviderDisabled(p0: String?) = println("Provider disabled")


  // Custom methods
  private fun checkPermissions(): Boolean {
    return ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
  }
  @SuppressLint("MissingPermission")
  private fun startLocationUpdates() {
    // Start location update once the class is instantiated
    if (this.checkPermissions()) {
      // Start location updates
      this.locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        DEFAULT_MILLISECONDS,
        DEFAULT_DISTANCE,
        this
      )
      // Get last known location
      this.currentLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    } else {
      throw PermissionException(Manifest.permission.ACCESS_FINE_LOCATION)
    }
  }
  @RequiresApi(Build.VERSION_CODES.O)
  private fun createNotificationChannel(channelId: String, channelName: String): String{
    val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
    channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
    this.notificationManager.createNotificationChannel(channel)
    return channelId
  }
  @RequiresApi(Build.VERSION_CODES.O)
  private fun createNotification(pendingIntent: PendingIntent): Notification {
//    val bigTextStyle: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
//    bigTextStyle.setBigContentTitle("My unique title")
//    bigTextStyle.bigText("This is an amazing description")
    val builder = Notification.Builder(applicationContext, createNotificationChannel(CHANNEL_ID, CHANNEL_NAME))
//    builder.setStyle(bigTextStyle as Notification.Style)
      .setContentTitle("Title")
      .setContentText("Message")
      .setContentIntent(pendingIntent)

    return builder.build()
  }
}
