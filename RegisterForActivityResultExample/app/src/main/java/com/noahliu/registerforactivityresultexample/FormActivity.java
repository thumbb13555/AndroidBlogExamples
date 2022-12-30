package com.noahliu.registerforactivityresultexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class FormActivity extends AppCompatActivity {
    public static final String NAME = "NAME";
    public static final String AGE = "AGE";
    public static final String STUDENT = "STUDENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Button btOk,btCancel;
        btOk = findViewById(R.id.button_OK);
        btCancel = findViewById(R.id.button_Cancel);
        EditText edName,edAge;
        CheckBox cbIsStudent;
        edName = findViewById(R.id.ediTtext_Name);
        edAge = findViewById(R.id.editText_age);
        cbIsStudent = findViewById(R.id.checkBox_Student);


        btOk.setOnClickListener(v -> {
            getIntent().putExtra(NAME,edName.getText().toString());
            getIntent().putExtra(AGE,edAge.getText().toString());
            getIntent().putExtra(STUDENT,cbIsStudent.isChecked());

            setResult(MainActivity.RESULT_CODE,getIntent());
            finish();
        });
        btCancel.setOnClickListener(v -> {
            setResult(MainActivity.CANCEL_CODE);
            finish();
        });
    }
}