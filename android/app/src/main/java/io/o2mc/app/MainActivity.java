package io.o2mc.app;

import android.content.Intent;
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
    App.getO2mc().setContextTracking(true);
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
   * Called on 'Open' button click
   */
  public void onOpenControls(View v) {
    startActivity(new Intent(this, ControlActivity.class));
  }
}
