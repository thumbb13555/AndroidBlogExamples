package com.noahliu.chatgptexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.WeakHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {
    public static final String YOUR_KEY = "";
    public static final String URL = "https://api.openai.com/v1/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvAnswer = findViewById(R.id.textView_Answer);

        ((Button)findViewById(R.id.button_Send)).setOnClickListener(view -> {
            String question = ((EditText)findViewById(R.id.edittext_Input)).getText().toString();
            if (question.isEmpty()) return;
            ((TextView)findViewById(R.id.textView_Question)).setText(question);
            tvAnswer.setText("請稍候..");
            //設置Header中的Content-Type
            MediaType mediaType = MediaType.parse("application/json");
            //寫入body
            String content = new Gson().toJson(makeRequest(question));
            RequestBody requestBody = RequestBody.create(mediaType, content);
            //發送請求
            new HttpRequest().sendPOST(URL, requestBody, new HttpRequest.OnCallback() {
                @Override
                public void onOKCall(String respond) {
                    Log.d("TAG", "onOKCall: "+respond);
                    ChatGPTRespond chatGPTRespond = new Gson().fromJson(respond,ChatGPTRespond.class);
                    runOnUiThread(()->{
                        tvAnswer.setText(chatGPTRespond.getChoices().get(0).getText());
                    });

                }
                @Override
                public void onFailCall(String error) {
                    Log.e("TAG", "onFailCall: "+error);
                    tvAnswer.setText(error);
                }
            });
        });
    }
    //寫入body
    private WeakHashMap<String,Object> makeRequest(String input){
        WeakHashMap<String,Object> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("model","text-davinci-003");
        weakHashMap.put("prompt",input);
        weakHashMap.put("temperature",0.5);
        weakHashMap.put("max_tokens",1000);
        weakHashMap.put("top_p",1);
        weakHashMap.put("frequency_penalty",0);
        weakHashMap.put("presence_penalty",0);
        return weakHashMap;
    }
}