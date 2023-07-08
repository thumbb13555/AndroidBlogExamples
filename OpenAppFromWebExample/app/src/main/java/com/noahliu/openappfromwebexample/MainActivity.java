package com.noahliu.openappfromwebexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //一定要檢查是否空值，否則會閃退
        if (getIntent().getData()!= null){
            String fromWeb = getIntent().getData().getQueryParameter("info");
            TextView tvFromWeb = findViewById(R.id.textview_Main);
            tvFromWeb.setText(fromWeb);
        }
    }
}