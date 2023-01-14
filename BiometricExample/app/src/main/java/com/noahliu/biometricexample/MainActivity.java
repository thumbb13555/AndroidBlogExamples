package com.noahliu.biometricexample;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btPIN, btFinger;
        btFinger = findViewById(R.id.btFinger);
        btPIN = findViewById(R.id.btPIN);


        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(this, "本裝置可以使用Pin Code或者指紋登錄", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "本裝置不支援指紋登錄", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "生物認證功能目前不可用", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                //表示該裝置使用者沒有登錄指紋（但裝置支援）
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivity(enrollIntent);
                break;
        }
        btFinger.setOnClickListener(v -> {
            //如果登錄指紋就會直接跳成PIN碼登錄
            startBiometricPrompt(getInfo(BIOMETRIC_STRONG | DEVICE_CREDENTIAL));
        });

        btPIN.setOnClickListener(v -> {
            startBiometricPrompt(getInfo(DEVICE_CREDENTIAL));
        });


    }

    private BiometricPrompt.PromptInfo getInfo(int method) {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("登錄")
                .setSubtitle("Balabala")
                .setAllowedAuthenticators(method)
                .build();
    }

    private void startBiometricPrompt(BiometricPrompt.PromptInfo promptInfo) {
        Executor executor = ContextCompat.getMainExecutor(this);


        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor
                , new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Toast.makeText(MainActivity.this, "登錄成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        biometricPrompt.authenticate(promptInfo);
    }
}