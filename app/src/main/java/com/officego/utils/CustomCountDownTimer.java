package com.officego.utils;

import android.os.CountDownTimer;

/**
 * Created by shijie
 * Date 2021/1/27
 **/
public class CustomCountDownTimer extends CountDownTimer {
    public TimerListener getListener() {
        return listener;
    }

    public void setListener(TimerListener listener) {
        this.listener = listener;
    }

    private TimerListener listener;

    public interface TimerListener {
        void onTick(long l);

        void timerFinish();
    }

    public CustomCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        if (listener != null) {
            listener.onTick(l);
        }
    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        if (listener != null) {
            listener.timerFinish();
        }
    }
}

