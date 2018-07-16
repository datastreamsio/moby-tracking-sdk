package io.o2mc.appkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

//class App : AppCompatActivity() {
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_main)
//  }
//}
var O2MC o2mc;

public App() {
  o2mc = new O2MC(this, "http://10.0.2.2:5000/events");
}

public static O2MC getO2mc() {
  return o2mc;
}
}
