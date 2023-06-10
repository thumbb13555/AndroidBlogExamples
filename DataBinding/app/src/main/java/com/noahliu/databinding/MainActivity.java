package com.noahliu.databinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.noahliu.databinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //將mainBinding綁定給介面
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setMyText("綁定變數測試");
        //也可以直接使用Binding來賦予點擊事件
        mainBinding.tvOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "🦢🦢🦢🦢🦢", Toast.LENGTH_SHORT).show();
            }
        });
        mainBinding.edBindExample.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                MyViewModel values = new MyViewModel();
                if (editable.toString().equals("")){
                    values.setZero();
                    //可以使用viewBinding來賦值
                    mainBinding.setMyText("綁定變數測試");
                } else{
                    int num = Integer.parseInt(editable.toString());
                    int total = 0;
                    total += (num * 11)+(num * 21)+(num * 31)+(num * 41)+(num * 51);
                    values.setMultipliedBy11(editable.toString() + '*' + 11 + '=' + num * 11);
                    values.setMultipliedBy21(editable.toString() + '*' + 21 + '=' + num * 21);
                    values.setMultipliedBy31(editable.toString() + '*' + 31 + '=' + num * 31);
                    values.setMultipliedBy41(editable.toString() + '*' + 41 + '=' + num * 41);
                    values.setMultipliedBy51(editable.toString() + '*' + 51 + '=' + num * 51);
                    //當然，也可以普通地使用
                    mainBinding.tvOutput.setText("總和: "+total);
                }

                mainBinding.setMyViewModel(values);
            }
        });
    }
}