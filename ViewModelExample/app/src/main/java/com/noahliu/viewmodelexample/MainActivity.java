package com.noahliu.viewmodelexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView tvStopwatch;
    private StopwatchModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStopwatch = findViewById(R.id.text_stopwatch);
        Button btStopwatch = findViewById(R.id.button_stopwatch);
        viewModel = new ViewModelProvider(this)
                .get(StopwatchModel.class);

        viewModel.setCallBack(time -> tvStopwatch.setText(time));
        btStopwatch.setOnClickListener(view ->viewModel.stopwatchSwitch());
    }
}