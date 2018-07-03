package io.o2mc.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getO2mc().track("MainActivityCreated");
    }

    public void onCreateEventHandler(View v) {
        EditText editText = findViewById(R.id.editText);
        String text = editText.getText().toString();

        App.getO2mc().trackWithProperties("Clicked button: 'Create Track Event'", text);
    }

    public void onCreateAliasHandler(View v) {
        // Not sure what anyone would do with this.
    }

    public void onSetIdentityHandler(View v) {
        Log.d(TAG, "onSetIdentityHandler executed");
        // Not sure what anyone would do with this.
    }

    public void onResetTrackingHandler(View v) {
        Log.d(TAG, "onResetTrackingHandler executed");
        App.getO2mc().reset();
    }
}
