package io.o2mc.appkotlin

import android.app.Application
import io.o2mc.sdk.O2MC

class App : Application() {
  companion object {
    lateinit var o2mc: O2MC
  }

  // Called on constructor
  init {
    o2mc = O2MC(this, "http://10.0.2.2:5000/events") // init the 'static' field o2mc
  }
}
