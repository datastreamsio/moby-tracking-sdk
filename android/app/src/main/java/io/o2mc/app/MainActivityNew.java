package io.o2mc.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.SocketException;

import io.o2mc.sdk.current.O2mc;


public class MainActivityNew extends AppCompatActivity {

    private static final String TAG = "MainActivityNew";
    private O2mc o2mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        o2mc = new O2mc(getApplication(), "http://10.0.2.2:5000/events");
        o2mc.setDispatchInterval(10);
    }

    public void onClickCreateEvent(View v) {
        EditText text = findViewById(R.id.editText);
        Button button = findViewById(R.id.buttonCreateTrackEvent);
//        o2mc.buttonClicked(button, null);
        o2mc.buttonClicked(button, text);
    }

    public void onCreateAliasHandler(View v) {
        // Not sure what anyone would do with this.
    }

    public void onSetIdentityHandler(View v) {
        // Not sure what anyone would do with this.
    }

    public void onResetTrackingHandler(View v) {
        Log.d("O2MC_EVENT", "Reset tracking");
        // TODO: 29-6-18 implement reset of eventbus
    }
}
