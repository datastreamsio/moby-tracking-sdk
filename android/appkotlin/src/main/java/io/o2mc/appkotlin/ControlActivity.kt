package io.o2mc.appkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class ControlActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_control)
  }

  /**
   * Called on 'Stop Tracking' button click
   */
  fun onStopTracking(v: View) {
    App.o2mc.stop()
  }

  /**
   * Called on 'Resume Tracking' button click
   */
  fun onResumeTracking(v: View) {
    App.o2mc.resume()
  }
}
