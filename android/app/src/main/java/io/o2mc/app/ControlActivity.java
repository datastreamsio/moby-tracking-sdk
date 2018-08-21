package io.o2mc.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ControlActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_control);
  }

  /**
   * Called on 'Stop Tracking' button click
   */
  public void onStopTracking(View v) {
    App.getO2mc().stop();
  }

  /**
   * Called on 'Resume Tracking' button click
   */
  public void onResumeTracking(View v) {
    App.getO2mc().resume();
  }
}
