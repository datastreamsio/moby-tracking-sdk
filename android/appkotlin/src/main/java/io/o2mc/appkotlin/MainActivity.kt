package io.o2mc.appkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    App.o2mc.track("MainActivityCreated") // access o2mc by property syntax
  }

  /**
   * Called on 'Create Track Event' button click
   */
  fun onCreateEventHandler(v: View) {
    val editText: EditText = findViewById(R.id.editText)
    val text: String = editText.text.toString() // access text by property syntax

    App.o2mc.trackWithProperties("Clicked button: 'Create Track Event'", text)
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
