package com.reactnativegeolocation.exceptions

import java.lang.Exception

class PermissionException(permission: String) : Exception("The required permission $permission is missing")
