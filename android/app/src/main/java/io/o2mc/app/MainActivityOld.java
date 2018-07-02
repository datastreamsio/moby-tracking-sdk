package io.o2mc.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class MainActivityOld extends AppCompatActivity {

    private static final String TAG = "MainActivityOld";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppOld.getO2mc().tracker.track("MainActivityCreated");
    }

    public void onCreateEventHandler(View v) {
        EditText text = findViewById(R.id.editText);

        Log.d("O2MC_EVENT", "Created a click event");
        AppOld.getO2mc().tracker.track(text.getText().toString());
    }

    public void onCreateAliasHandler(View v) {
        EditText text = findViewById(R.id.editText);

        Log.d("O2MC_EVENT", "Created alias" + text.getText().toString());
        AppOld.getO2mc().tracker.createAlias(text.getText().toString());
    }

    public void onSetIdentityHandler(View v) {
        EditText text = findViewById(R.id.editText);

        Log.d("O2MC_EVENT", "Set identity" + text.getText().toString());
        AppOld.getO2mc().tracker.identify(text.getText().toString());
    }

    public void onResetTrackingHandler(View v) {
        Log.d("O2MC_EVENT", "Reset tracking");

        AppOld.getO2mc().tracker.reset();
    }
}
