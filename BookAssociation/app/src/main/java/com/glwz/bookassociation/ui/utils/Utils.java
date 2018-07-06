package com.glwz.bookassociation.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.blankj.utilcode.util.ToastUtils;
import com.glwz.bookassociation.ui.Entity.GetPreOrderBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

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

    public static void weChatPay(Context context, GetPreOrderBean weChatBean){
        IWXAPI payApi = WXAPIFactory.createWXAPI(context, weChatBean.getAppid(),
                false);
        if(!payApi.isWXAppInstalled()){
            //未安装的处理
            ToastUtils.showShort("未安装微信");
        }
        payApi.registerApp(weChatBean.getAppid());

        PayReq payReq = new PayReq();
        payReq.appId = weChatBean.getAppid();
        payReq.partnerId = weChatBean.getPartnerid();
        payReq.prepayId = weChatBean.getPrepayid();
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = weChatBean.getNoncestr();
        payReq.timeStamp = weChatBean.getTimestamp();
        payReq.sign = weChatBean.getSign();

        payApi.sendReq(payReq);
    }

}
