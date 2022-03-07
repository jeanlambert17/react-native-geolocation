package com.reactnativegeolocation

import android.os.Handler
import android.os.Looper
import android.os.Message


class GeolocationHandler(looper: Looper) : Handler(looper) {

  override fun handleMessage(msg: Message?) {
    super.handleMessage(msg)
    println("HandleMessage message: $msg")
  }
}
