package com.glwz.bookassociation.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/4/28.
 */

public class Utils {

    public static Bitmap createBitmap(Context context, int id){
        Bitmap bm = null;
        bm = BitmapFactory.decodeResource(context.getResources(), id);
        return bm;
    }

    public static String getTime_mm_ss(int num) {
        String _time = "00:00";

        int second = num / 1000;
        int minute = second / 60;

        minute = minute % 60;
        second = second % 60;

        String _minute = "";
        String _second = "";

        if (minute < 10) {
            _minute = "0" + minute;
        } else {
            _minute = "" + minute;
        }
        if (second < 10) {
            _second = "0" + second;
        } else {
            _second = "" + second;
        }
        _time = _minute + ":" + _second;
        return _time;
    }

}
