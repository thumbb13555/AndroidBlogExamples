package com.noahliu.catcherrorrepoter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler{
    private static final String pathSD = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String  fileNameSuffix = ".txt";
    private final static CrashHandler instance = new CrashHandler();
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private Context mContext;
    public static final String TAG = CrashHandler.class.getSimpleName();

    public static CrashHandler getInstance(){
        return instance;
    }
    public void init(Context context){
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }
    private String appendPhoneInfo() throws PackageManager.NameNotFoundException
    {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("APP Version Name: ");
        stringBuilder.append(pi.versionName).append("\n");

        stringBuilder.append("APP Version Code: ");
        stringBuilder.append(pi.versionCode).append("\n");

        stringBuilder.append("Mobile SDK: ");
        stringBuilder.append(Build.VERSION.SDK_INT).append("\n");

        stringBuilder.append("Mobile  Factory: ");
        stringBuilder.append(Build.MANUFACTURER).append("\n");

        stringBuilder.append("Type: ");
        stringBuilder.append(Build.MODEL).append("\n");

        stringBuilder.append("CPU: ");
        stringBuilder.append(Arrays.toString(Build.SUPPORTED_ABIS)).append("\n\n");
        stringBuilder.append("Error Code: ");
        return stringBuilder.toString();
    }

    @SuppressLint("SimpleDateFormat")
    private void exportReport(Throwable throwable){
        @SuppressLint("SimpleDateFormat")
        String time = new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date());
        //坑：Android 11以上不允許檔案名稱有":"
        //參考：https://stackoverflow.com/questions/61406818/filenotfoundexception-open-failed-eperm-operation-not-permitted-during-saving
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Log.e(TAG, "SD卡不存在");
            return;
        }
        File file = new File(pathSD+ File.separator
                + time +mContext.getString(R.string.app_name)+ fileNameSuffix);

        try{
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            printWriter.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            printWriter.println(appendPhoneInfo());
            printWriter.println(throwable);
            printWriter.close();
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "exportReport: 寫出失敗"+e);
        }
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        try{
            exportReport(throwable);
        }catch (Exception e){
            e.printStackTrace();
        }
        throwable.printStackTrace();
        if (uncaughtExceptionHandler != null)
        {
            uncaughtExceptionHandler.uncaughtException(thread, throwable);
        } else
        {
            Process.killProcess(Process.myPid());
        }

    }
}
