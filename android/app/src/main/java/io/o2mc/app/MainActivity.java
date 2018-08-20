package io.o2mc.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    App.getO2mc().track("MainActivityCreated");
  }

  /**
   * Called on 'Create Track Event' button click
   */
  public void onCreateEventHandler(View v) {
    EditText editText = findViewById(R.id.editText);
    String text = editText.getText().toString();

    App.getO2mc().trackWithProperties("Clicked button: 'Create Track Event'", text);
  }

  /**
   * Called on 'Set Endpoint' button click
   */
  public void onSetEndpointHandler(View v) {
    EditText editText = findViewById(R.id.editTextEndpointIp);
    String text = editText.getText().toString();

    App.getO2mc().setEndpoint(text);
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
