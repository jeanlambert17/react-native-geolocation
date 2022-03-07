package com.reactnativegeolocation

import android.app.Activity
import android.content.Intent
import com.facebook.react.bridge.*
import com.reactnativebglocation.DEFAULT_DISTANCE
import com.reactnativebglocation.DEFAULT_DISTANCE_KEY
import com.reactnativebglocation.DEFAULT_MILLISECONDS
import com.reactnativebglocation.DEFAULT_MILLISECONDS_KEY

class GeolocationModule: ReactContextBaseJavaModule {
  override fun getName(): String = "Geolocation"
  var reactContext: ReactApplicationContext

  companion object {
    @JvmStatic lateinit var activity: Activity
  }

  private val mActivityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent) {
      println("onActivityResult")
//      Intent(reactApplicationContext, GeolocationBackgroundService::class.java).also { intent ->
//        reactApplicationContext.startService(intent)
//      }
    }
  }


  constructor(reactContext: ReactApplicationContext): super(reactContext) {
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(mActivityEventListener);
  }

  override fun getConstants(): Map<String, Any>? {
    val constants: MutableMap<String, Any> = HashMap()
    constants[DEFAULT_MILLISECONDS_KEY] = DEFAULT_MILLISECONDS
    constants[DEFAULT_DISTANCE_KEY] = DEFAULT_DISTANCE
    return constants
  }

  @ReactMethod
  fun getCurrentLocation(promise: Promise) {
      var location: WritableMap = Arguments.createMap()
      location.putDouble("latitude", 12.12)
      location.putDouble("longitude", 12.12)
      promise.resolve(location)
  }

  @ReactMethod
  fun startLocationUpdates(promise: Promise) {
    activity = this.currentActivity!!
    Intent(reactContext, GeolocationBackgroundService::class.java).also { intent ->
      reactContext.startService(intent)
    }
  }


}
