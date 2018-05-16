package com.glwz.bookassociation.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.glwz.bookassociation.MyAppaction;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/3.
 */

@SuppressLint("AppCompatCustomView")
public class TimeButton extends Button implements View.OnClickListener {
    private long lenght = 0 * 1000;// 初始时间，默认毫秒
    private String textafter = "s";             //点击按钮之后显示的字段
    private String textbefore = "点击获取验证码";   //点击按钮之前显示的字段
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private View.OnClickListener mOnclickListener;
    private Timer t;
    private TimerTask tt;
    private long time;

    public TimeButton(Context context) {
        super(context);
        setOnClickListener(this);
    }


    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText(time / 1000 + textafter);
            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                TimeButton.this.setText(textbefore);
                clearTimer();
            }
        };
    };

    private void initTimer() {
        time = lenght;
        t = new Timer();
        tt = new TimerTask() {

            @Override
            public void run() {
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        if (l instanceof TimeButton) {
            super.setOnClickListener(l);
        } else
            this.mOnclickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mOnclickListener != null)
            mOnclickListener.onClick(v);
        //		initTimer();
        //		this.setText(time / 1000 + textafter);
        //		this.setEnabled(false);
        //		t.schedule(tt, 0, 1000);
    }

    public void jishi(){    //按钮倒计时
        initTimer();
        this.setText(time / 1000 + textafter);
        //		this.setEnabled(false);
        t.schedule(tt, 0, 1000);
    }
    public void Noclick(){
        this.setEnabled(false);
    }
    public void canclick(){
        this.setEnabled(true);
    }
    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (MyAppaction.map == null)
            MyAppaction.map = new HashMap<String, Long>();
        MyAppaction.map.put(TIME, time);
        MyAppaction.map.put(CTIME, System.currentTimeMillis());
        clearTimer();
        //		Log.e("yung", "onDestroy");
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate(Bundle bundle) {
        Log.e("yung", MyAppaction.map + "");
        if (MyAppaction.map == null)
            return;
        if (MyAppaction.map.size() <= 0)// �����ʾû���ϴ�δ��ɵļ�ʱ
            return;
        long time = System.currentTimeMillis() - MyAppaction.map.get(CTIME)
                - MyAppaction.map.get(TIME);
        MyAppaction.map.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            this.setText(time + textafter);
            this.setEnabled(false);
        }
    }

    /** * 设置计时时候显示的文本 */
    public TimeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /** * 设置点击之前的文本 */
    public TimeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }
    /**
     * 设置到计时长度
     *
     * @param lenght
     *            时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }
}
