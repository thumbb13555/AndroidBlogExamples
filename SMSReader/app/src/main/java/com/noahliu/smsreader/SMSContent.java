package com.noahliu.smsreader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
//https://github.com/wei-gong/VerifyCodeView
//https://github.com/Wynsbin/VerificationCodeInputView
import java.util.regex.Pattern;


public class SMSContent extends ContentObserver {

    private final OnCallback callback;
    private final Context context;

    public SMSContent(Handler handler,Context context,OnCallback callback) {
        super(handler);
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void onChange(boolean selfChange, @Nullable Uri uri) {
        super.onChange(selfChange, uri);
        Cursor cursor = null;
        ContentResolver resolver = context.getContentResolver();
        //確認權限後，設置讀取SMS箱
        if (ContextCompat.checkSelfPermission(context, "android.permission.READ_SMS")
                == PackageManager.PERMISSION_GRANTED) {
            cursor = resolver.query(Uri.parse("content://sms"), null, null
                    , null, "_id desc");
        }
        if (cursor != null && cursor.getCount() > 0) {
            //讀取簡訊後，並將之設為已讀
            ContentValues values = new ContentValues();
            values.put("read", "1");
            cursor.moveToNext();

            assert uri != null;
            if ("content://sms/raw".equals(uri.toString())) {
                //取得動態簡訊內容
                int smsBodyColumn = cursor.getColumnIndex("body");
                String code = getDynamicCode(cursor.getString(smsBodyColumn));
                wait(1500);
                callback.callback(code);
                cursor.close();
            }
        }
    }
    /**驗證簡訊內容*/
    public String getDynamicCode(String str) {
        //檢測簡訊內容，以正規表達式抓出數字
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]");
        Matcher m = continuousNumberPattern.matcher(str);
        //填入數字
        StringBuilder stringBuilder = new StringBuilder();
        while (m.find()) {
            stringBuilder.append(m.group());
        }
        return stringBuilder.toString();
    }

    /**收到簡訊後的延遲時間*/
    private void wait(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    public interface OnCallback{
        void callback(String code);
    }
}
