package com.glwz.bookassociation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class ChangePasswordActivity extends BaseActivity implements
        View.OnClickListener, HttpAPICallBack {

    private EditText edit_change_password_phone;
    private EditText edit_change_password_verification;
    private EditText edit_change_password_ps;//新密码
    private TimeButton btn_change_password_verification;
    private SharePreferenceUtil sharePreferenceUtil;
    private static int TAG = 0; // 1标识从登陆界面跳转来的 2标识从个人中心界面跳转来的
    private String username, verification, password;
    private Button btn_changepass_next;
    private ImageView img_back_rd;
    ChangePasswordActivity _activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        _activity = this;
        sharePreferenceUtil = new SharePreferenceUtil(
                ChangePasswordActivity.this, MyData.SAVE_USER);
        edit_change_password_phone = (EditText) findViewById(R.id.edit_change_password_phone);
        edit_change_password_ps = (EditText) findViewById(R.id.edit_change_password_ps);
        edit_change_password_verification = (EditText) findViewById(R.id.edit_change_password_verification);
        btn_change_password_verification = (TimeButton) findViewById(R.id.btn_change_password_verification);
        img_back_rd = (ImageView) findViewById(R.id.img_back_rd);
        btn_changepass_next = (Button) findViewById(R.id.btn_changepass_next);
        btn_change_password_verification.onCreate(savedInstanceState);
        btn_change_password_verification.setTextBefore("发送验证码");
        btn_change_password_verification.setOnClickListener(this);
        btn_changepass_next.setOnClickListener(this);
        img_back_rd.setOnClickListener(this);
        Intent i = getIntent();
        TAG = i.getIntExtra("msg", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_changepass_next:
                commitNewKey();
                break;
            case R.id.btn_change_password_verification:
                btn_change_password_verification.Noclick();
                sendVerificationCode();
                break;
            case R.id.img_back_rd:
                onBackPressed();
                finish();
                break;

            default:
                break;
        }
    }

    // 提交
    public void commitNewKey() {

        username = edit_change_password_phone.getText().toString().trim();
        password = edit_change_password_ps.getText().toString().trim();
        verification = edit_change_password_verification.getText().toString()
                .trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(verification)&& !TextUtils.isEmpty(password)) {
            HomeAPI.changePassword(ChangePasswordActivity.this, username, password,
                    verification);
        } else {
            Toast.makeText(getApplicationContext(), "不得有空选择",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 发送验证码
    public void sendVerificationCode() {
        username = edit_change_password_phone.getText().toString().trim();
        if (username.length() == 11) {
            HomeAPI.sendMessage(this, username);//发送验证码
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "请输入手机号码！",
                    Toast.LENGTH_SHORT).show();
            btn_change_password_verification.canclick();
        } else {
            Toast.makeText(getApplicationContext(), "手机号码应为11位数字！",
                    Toast.LENGTH_SHORT).show();
            btn_change_password_verification.canclick();
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
            if (bean != null) {
                btn_change_password_verification.setTextAfter("s").setLenght(
                        60 * 1000);
                btn_change_password_verification.jishi();
                ToastUtils.showShort("发送成功！请注意查收短信。");
            } else {
                btn_change_password_verification.canclick();
            }
        }
        if(url_id == HomeAPI.NET_changePassword){
            LoginBean bean = (LoginBean)response;
            if (bean != null) {
                ToastUtils.showShort(bean.getMessage());
                onBackPressed();
                _activity.finish();
            }

        }
    }

    @Override
    public void onError(BaseBean response) {
        if (NetworkUtils.isConnected()) {
            Toast.makeText(this, "服务器断开，请稍后重试", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
        }
    }
}
