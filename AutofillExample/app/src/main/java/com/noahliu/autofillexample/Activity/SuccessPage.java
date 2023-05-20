package com.noahliu.autofillexample.Activity;

import static java.lang.Math.toIntExact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.noahliu.autofillexample.R;

public class SuccessPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_page);
        TextView countdownText = findViewById(R.id.countdownText);
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = toIntExact(millisUntilFinished / 1000);
                countdownText.setText(getResources().getQuantityString
                        (R.plurals.welcome_page_countdown, secondsRemaining, secondsRemaining));
            }

            @Override
            public void onFinish() {
                if (!SuccessPage.this.isFinishing()) {
                    Intent intent = new Intent(SuccessPage.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }
}