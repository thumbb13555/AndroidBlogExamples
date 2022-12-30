package com.noahliu.registerforactivityresultexample;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CODE = 2;
    public static final int CANCEL_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btFillOut = findViewById(R.id.button_FillOutForm);
        btFillOut.setOnClickListener(v->{
            Intent intent = new Intent(this,FormActivity.class);
//            startActivity(intent); 單純跳轉
            resultLauncher.launch(intent); //需要註冊的跳轉
        });

    }


    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
//              ✭✭不採用正規表達式可以這樣寫✭✭
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//
//                }
//            }
            result ->{
                if (result.getResultCode() == RESULT_CANCELED||
                        result.getResultCode() == CANCEL_CODE)
                    Toast.makeText(this, "取消填寫表格", Toast.LENGTH_SHORT).show();
                else if(result.getResultCode() == RESULT_CODE){
                    TextView tvResult = findViewById(R.id.textView_Result);
                    String name = result.getData().getStringExtra(FormActivity.NAME);
                    String age = result.getData().getStringExtra(FormActivity.AGE);
                    boolean isStudent = result.getData().getBooleanExtra(FormActivity.STUDENT,false);
                    tvResult.setText("名字: "+name+"\n年齡: "+age+"\n是否為學生: "+isStudent);
                }
            }
    );

}