package com.noahliu.chatgptexample;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpRequest {


    public void sendPOST(String url, RequestBody requestBody,OnCallback callback){
        /**建立連線*/
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        /**設置傳送需求*/
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer "+MainActivity.YOUR_KEY)
                .post(requestBody)
                .build();
        /**設置回傳*/
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
               callback.onFailCall(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                callback.onOKCall(res);
            }
        });
    }
    interface OnCallback{
        void onOKCall(String respond);
        void onFailCall(String error);
    }
}
