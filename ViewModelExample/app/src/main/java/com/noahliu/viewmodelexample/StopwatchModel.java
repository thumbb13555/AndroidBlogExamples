package com.noahliu.viewmodelexample;

import android.os.Handler;

import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class StopwatchModel extends ViewModel {
    private int nanoSeconds = 0;
    private boolean isRunning = false;
    private String time = "0:00:00";
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    private Handler handler = new Handler();
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            int min = nanoSeconds / 3600;
            int sec = (nanoSeconds % 3600) / 60;
            int nano = nanoSeconds % 60;

            time = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", min,
                    sec, nano);
            callBack.callbackListener(time);
            nanoSeconds++;

            handler.postDelayed(this, 10);
        }
    };
    public void stopwatchSwitch(){
        if (!isRunning) {
            handler.post(r);
            isRunning = true;
        } else {
            handler.removeCallbacks(r);
            isRunning = false;
            nanoSeconds = 0;
        }
    }
    interface CallBack{
        void callbackListener(String time);
    }
}
