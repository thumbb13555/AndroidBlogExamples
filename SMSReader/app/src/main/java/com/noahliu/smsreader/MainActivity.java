package com.noahliu.smsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.chaos.view.PinView;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PinView pinView = findViewById(R.id.pin_view);
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}
                , REQUEST_CODE_ASK_PERMISSIONS);
        SMSContent content = new SMSContent(new Handler(), this, new SMSContent.OnCallback() {
            @Override
            public void callback(String code) {
                pinView.setText(code);
            }
        });
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/")
                , true, content);
    }
}