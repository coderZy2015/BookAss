package com.glwz.bookassociation.ui.activity;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.Net.HttpUrl;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.CardNumBean;
import com.glwz.bookassociation.ui.Entity.LoginBean;
import com.glwz.bookassociation.ui.event.EventBusMessageModel;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements HttpAPICallBack, View.OnClickListener{

    private EditText edit_phone = null;
    private EditText edit_password = null;
    private TextView btn_miss = null;
    private Button btn_login = null;
    private TextView btn_register = null;
    private static long exitTime = 0;
    private String phone;
    private String password;
    private boolean btn_remember_password = true;
    private SharePreferenceUtil sp;
    private LoginActivity _activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _activity = this;
        sp = new SharePreferenceUtil(this, MyData.SAVE_USER);

        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_password = (EditText) findViewById(R.id.edit_password);
        btn_miss = (TextView) findViewById(R.id.btn_miss);
        btn_miss.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_miss.getPaint().setAntiAlias(true);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_miss.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        if (btn_remember_password == true && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(password)) {
            edit_phone.setText(phone);
            edit_password.setText(password);
        }

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessageModel event) {
        // 收到通知后，结束当前view
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_login:
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort("请检查网络连接");
                } else {
                    login();
                }

                break;
            case R.id.btn_miss:
                missPassword();
                break;
            case R.id.btn_register:
                register();
                break;
            default:
                break;
        }
    };

    // 忘记密码
    public void missPassword() {
        Intent intent = new Intent(LoginActivity.this,
                ChangePasswordActivity.class);
        intent.putExtra("msg", 1);
        startActivity(intent);
    }

    // 登录
    public void login() {
        phone = edit_phone.getText().toString();
        password = edit_password.getText().toString();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {// 判断密码
            // 以字母开头，长度在6~18之间，只能包含字母、数字和下划线。
            ToastUtils.showShort("账号密码不能为空");
        } else {
            HomeAPI.Login(this, HttpUrl.Login_Url, phone, password);
        }
    }

    // 注册
    public void register() {
        Intent intent = new Intent(LoginActivity.this,
                RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_Login){
            LoginBean bean = (LoginBean)response;
            if (bean != null){
                //错误
                if(bean.getMark().equals("1")){
                    ToastUtils.showShort(bean.getMessage());
                }
                //登录成功  mark = 3咪咕会员
                if (bean.getMark().equals("0") || bean.getMark().equals("3")){
                    sp.setUserName(phone);
                    sp.setPassword(password);
                    if (bean.getMark().equals("3")){
                        MyData.isMiguMember = true;
                        HomeAPI.getHaveCoupon(this, sp.getUserName());
                    }else{
                        MyData.isMiguMember = false;
                        onBackPressed();
                        _activity.finish();
                    }

                    ToastUtils.showShort(bean.getMessage());
                }
            }
        }

        if (url_id == HomeAPI.NET_IsHaveCoupon){
            CardNumBean beanC = (CardNumBean)response;
            if (beanC != null){
                MyData.CouponNum = beanC.getCardNum();
            }
            onBackPressed();
            _activity.finish();
        }
    }

    @Override
    public void onError(BaseBean response) {
        ToastUtils.showShort(response.toString());
    }
}

