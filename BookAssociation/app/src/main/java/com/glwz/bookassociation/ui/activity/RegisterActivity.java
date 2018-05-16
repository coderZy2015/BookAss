package com.glwz.bookassociation.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.LoginBean;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.glwz.bookassociation.ui.utils.TimeButton;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, HttpAPICallBack{
    private EditText edit_register_phone;
    private EditText edit_register_verification;
    private EditText edit_register_password;
    private TimeButton timeButton;
    private Button btn_register_makesure;
    private SharePreferenceUtil sharePreferenceUtil;
    private String username, password, verification;// 手机号 验证码
    private ImageView img_back_rd;

    RegisterActivity _activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        _activity = this;
        sharePreferenceUtil = new SharePreferenceUtil(RegisterActivity.this,
                MyData.SAVE_USER);
        timeButton = (TimeButton) findViewById(R.id.btn_register_verification);
        timeButton.onCreate(savedInstanceState);
        timeButton.setOnClickListener(this);
        timeButton.setTextBefore("发送验证码");
        edit_register_phone = (EditText) findViewById(R.id.edit_register_phone);
        edit_register_password = (EditText) findViewById(R.id.edit_register_phone);
        edit_register_verification = (EditText) findViewById(R.id.edit_register_verification);
        btn_register_makesure = (Button) findViewById(R.id.btn_register_makesure);
        btn_register_makesure.setOnClickListener(this);
        btn_register_makesure.setEnabled(true);
        img_back_rd = (ImageView) findViewById(R.id.img_back_rd);
        img_back_rd.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_register_verification:
                timeButton.Noclick();
                sendVerification();
                break;
            case R.id.btn_register_makesure:
                commitRegister();
                break;
            case R.id.img_back_rd:
                onBackPressed();
                finish();
                break;
            default:
                break;
        }
    }

    // 发送验证码
    public void sendVerification() {
        username = edit_register_phone.getText().toString().trim();
        if (NetworkUtils.isConnected()) {
            if (username.length() == 11) {
                // 验证码
                HomeAPI.sendMessage(this, username);
            } else if (TextUtils.isEmpty(username)) {
                ToastUtils.showShort("请输入手机号码！");
                timeButton.canclick();
            } else {
                ToastUtils.showShort("长度不够11位数字");
                timeButton.canclick();
            }
        } else {
            ToastUtils.showShort("请检查网络连接");
            timeButton.canclick();
        }

    }

    // 提交
    public void commitRegister() {
        if (NetworkUtils.isConnected()) {
            username = edit_register_phone.getText().toString();
            password = edit_register_password.getText().toString();
            verification = edit_register_verification.getText().toString()
                    .trim();
            if (!TextUtils.isEmpty(username)
                    && !TextUtils.isEmpty(verification) &&!TextUtils.isEmpty(password)) {
                HomeAPI.register(this, username, password, verification);

            } else {
                ToastUtils.showShort("不得有空选项，请输入！");
            }
        } else {
            ToastUtils.showShort("请检查网络连接");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           onBackPressed();
           finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if(url_id == HomeAPI.NET_sendMessage){
            LoginBean bean = (LoginBean)response;
            if(bean != null){
                ToastUtils.showShort(bean.getMessage());
            }
            timeButton.setTextAfter("s").setLenght(60 * 1000);
            timeButton.jishi();
        }
        if (url_id == HomeAPI.NET_register){
            LoginBean bean = (LoginBean)response;
            if(bean != null){
                if(bean.getMark().equals("0")){
                    ToastUtils.showShort("注册成功");
                    onBackPressed();
                    _activity.finish();
                }else{
                    ToastUtils.showShort(bean.getMessage());
                    onBackPressed();
                    _activity.finish();
                }
            }
        }

    }

    @Override
    public void onError(BaseBean response) {
        if (NetworkUtils.isConnected()) {
            ToastUtils.showShort("服务器断开，请稍后重试");
        } else {
            ToastUtils.showShort("网络连接失败");
        }
    }
}
