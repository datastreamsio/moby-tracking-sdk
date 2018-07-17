package io.o2mc.appkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import io.o2mc.sdk.util.LogUtil.LogD

class MainActivity : AppCompatActivity() {

  private val TAG = "MainActivity" // declare tag

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    App.o2mc.track("MainActivityCreated") // access o2mc by property syntax
    LogD(TAG, "onCreate executed")
  }

  /**
   * Called on 'Create Track Event' button click
   */
  fun onCreateEventHandler(v: View) {
    val editText: EditText = findViewById(R.id.editText)
    val text: String = editText.text.toString() // access text by property syntax

    App.o2mc.trackWithProperties("Clicked button: 'Create Track Event'", text)
    LogD(TAG, "onCreateEventHandler executed")
  }

  /**
   * Called on 'Stop Tracking' button click
   */
  fun onStopTracking(v: View) {
    App.o2mc.stop()
    LogD(TAG, "onStopTracking executed")
  }

  /**
   * Called on 'Reset Tracking' button click
   */
  fun onResetTrackingHandler(v: View) {
    App.o2mc.reset()
    LogD(TAG, "onResetTrackingHandler executed")
  }
}
