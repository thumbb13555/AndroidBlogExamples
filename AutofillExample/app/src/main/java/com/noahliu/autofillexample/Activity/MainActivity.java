package com.noahliu.autofillexample.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.autofill.AutofillManager;
import android.widget.EditText;

import com.noahliu.autofillexample.R;

public class MainActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //啟用AutofillManager
        getSystemService(AutofillManager.class);
        //跳入設定自動填入供應商的設定畫面
        Intent intent = new Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE);
        intent.setData(Uri.parse("package:com.android.settings"));
        startActivityForResult(intent, 100);
        mUsernameEditText = findViewById(R.id.usernameField);
        mPasswordEditText = findViewById(R.id.passwordField);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutofillManager afm = getSystemService(AutofillManager.class);
                if (afm != null) {
                    afm.cancel();
                }
                mUsernameEditText.setText("");
                mPasswordEditText.setText("");
            }
        });

    }

    private void login() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if (username.length() == 0 || password.length() == 0) return;
        Intent intent = new Intent(this, SuccessPage.class);
        startActivity(intent);
        finish();

    }
}