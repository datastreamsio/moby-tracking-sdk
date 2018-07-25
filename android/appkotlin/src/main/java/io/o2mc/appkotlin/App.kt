package io.o2mc.appkotlin

import android.app.Application
import android.util.Log
import io.o2mc.sdk.O2MC
import io.o2mc.sdk.exceptions.O2MCDeviceException
import io.o2mc.sdk.exceptions.O2MCDispatchException
import io.o2mc.sdk.exceptions.O2MCEndpointException
import io.o2mc.sdk.interfaces.O2MCExceptionListener

class App : Application(), O2MCExceptionListener {

  // Closest equivalent to Java's static (https://stackoverflow.com/a/43857895/5273299)
  companion object {
    const val TAG = "App"
    lateinit var o2mc: O2MC
  }

  // Called on constructor
  init {
    o2mc = O2MC(this, "http://10.0.2.2:5000/events") // init the 'static' field o2mc
    o2mc.setO2MCExceptionListener(this)
  }

  override fun onO2MCDispatchException(e: O2MCDispatchException) {
    Log.e(TAG, "Handling an O2MCDispatchException from App.kt: " + e.message)
  }

  override fun onO2MCDeviceException(e: O2MCDeviceException) {
    Log.e(TAG, "Handling an onO2MCDeviceException from App.kt: " + e.message)
  }

  override fun onO2MCEndpointException(e: O2MCEndpointException) {
    Log.e(TAG, "Handling an onO2MCEndpointException from App.kt: " + e.message)
  }
}
