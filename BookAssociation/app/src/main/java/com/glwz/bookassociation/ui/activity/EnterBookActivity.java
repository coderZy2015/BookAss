package com.glwz.bookassociation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.Net.HttpUrl;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.CardNumBean;
import com.glwz.bookassociation.ui.Entity.LoginBean;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;

public class EnterBookActivity extends BaseActivity implements View.OnClickListener,
        HttpAPICallBack {

    private Button btn_open;
    private RelativeLayout btn_back;
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_book);

        sharePreferenceUtil = new SharePreferenceUtil(this, MyData.SAVE_USER);

        btn_open = findViewById(R.id.btn_open);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_open.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sharePreferenceUtil.getUserName().equals("")) {
            if (MyData.isMiguMember) {
                btn_open.setClickable(false);
                btn_open.setText("已开通");
            } else {
                HomeAPI.Login(this, HttpUrl.Login_Url, sharePreferenceUtil.getUserName(),
                        sharePreferenceUtil.getPassword());
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                finish();
                break;
            case R.id.btn_open:
                if (sharePreferenceUtil.getUserName().equals("")) {
                    Intent intent = new Intent();
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(EnterBookActivity.this, BookAssociation.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_Login) {
            LoginBean bean = (LoginBean) response;
            if (bean != null) {
                //错误
                if (bean.getMark().equals("1")) {
                    sharePreferenceUtil.setUserName("");
                    sharePreferenceUtil.setPassword("");
                }

                //登录成功  mark = 3咪咕会员
                if (bean.getMark().equals("0") || bean.getMark().equals("3")) {
                    if (bean.getMark().equals("3")) {
                        MyData.isMiguMember = true;
                        btn_open.setClickable(false);
                        HomeAPI.getHaveCoupon(this, sharePreferenceUtil.getUserName());
                        btn_open.setText("已开通");
                    } else {
                        MyData.isMiguMember = false;
                        btn_open.setClickable(true);

                        btn_open.setText("立即开通");
                    }

                }

            }
        }
        if (url_id == HomeAPI.NET_IsHaveCoupon) {
            CardNumBean bean = (CardNumBean) response;
            if (bean != null) {
                MyData.CouponNum = bean.getCardNum();
            }
        }
    }

    @Override
    public void onError(BaseBean response) {

    }
}
