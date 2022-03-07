package com.reactnativegeolocation.exceptions

import java.lang.Exception

class GeolocationNotRunningException(message: String = "Geolocation service is not running") : Exception(message)
