package io.o2mc.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import io.o2mc.sdk.O2MC;


public class MainActivity extends AppCompatActivity {
    private O2MC oo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oo = App.getO2mc();

        App.getO2mc().tracker.track("MainActivityCreated");
    }

    public void onCreateEventHandler(View v) {
        EditText text = (EditText) findViewById(R.id.editText);

        Log.d("O2MC_EVENT", "Created a click event");
        App.getO2mc().tracker.track(text.getText().toString());
    }

    public void onCreateAliasHandler(View v) {
        EditText text = (EditText) findViewById(R.id.editText);

        Log.d("O2MC_EVENT", "Created alias" + text.getText().toString());
        App.getO2mc().tracker.createAlias(text.getText().toString());
    }

    public void onSetIdentityHandler(View v) {
        EditText text = (EditText) findViewById(R.id.editText);

        Log.d("O2MC_EVENT", "Set identity" + text.getText().toString());
        App.getO2mc().tracker.identify(text.getText().toString());
    }

    public void onResetTrackingHandler(View v) {
        Log.d("O2MC_EVENT", "Reset tracking");

        App.getO2mc().tracker.reset();
    }
}
