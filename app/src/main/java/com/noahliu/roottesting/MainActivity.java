package com.noahliu.roottesting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)(findViewById(R.id.text_hello))).setText("裝置是否刷機？： "+isRoot());
    }

    private boolean isRoot(){
        try{
            Process process = Runtime.getRuntime().exec("su");
            process.destroy();
            return true;
        }catch (IOException e){
            return false;
        }
    }
}