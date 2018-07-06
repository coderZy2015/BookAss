package com.glwz.bookassociation.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.glwz.bookassociation.AppConstant;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.event.EventBusMessageModel;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    //微信appId
    public static String APPID = "wx583b2452e62d22da";

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("zy_code", "WX Pay onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        Log.i("zy_code", "WX Pay onCreate");
        api = WXAPIFactory.createWXAPI(this, APPID,false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        Log.i("zy_code", "WX Pay onNewIntent");
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.i("zy_code", "WX Pay onReq");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.i("zy_code", "WX Pay onResp");
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK://成功
                    ToastUtils.showShort("支付成功");

                    EventBusMessageModel event = new EventBusMessageModel();
                    event.setType(AppConstant.PAY_SUCCESS);
                    EventBus.getDefault().post(event);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://取消支付
                    ToastUtils.showShort("支付取消");
                    break;
                case BaseResp.ErrCode.ERR_COMM:// -1
                    ToastUtils.showShort("支付失败");
                    break;
                default:
                    ToastUtils.showShort("支付失败");
                    break;
            }
            finish();
        }
    }
}
