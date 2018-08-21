package io.o2mc.appkotlin

import android.content.Intent
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
   * Called on 'Set Endpoint' button click
   */
  fun onSetEndpointHandler(v: View) {
    val editText: EditText = findViewById(R.id.editTextEndpointIp)
    val text: String = editText.text.toString() // access text by property syntax

    App.o2mc.setEndpoint(text)
  }

  /**
   * Called on 'Open' button click
   */
  fun onOpenControls(v: View) {
    startActivity(Intent(this, ControlActivity::class.java))
  }
}
