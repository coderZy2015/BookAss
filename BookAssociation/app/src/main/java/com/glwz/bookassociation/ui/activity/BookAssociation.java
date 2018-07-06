package com.glwz.bookassociation.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.glwz.bookassociation.R;

/**
 * 悦听会
 */
public class BookAssociation extends BaseActivity implements View.OnClickListener {

    private RelativeLayout btn_back;
    private WebView mAgentWeb;
    private LinearLayout mLinearLayout;

    private String url = "http://wap.cmread.com/r/p/index.jsp?cm=M3B50001&vt=3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_association);

        mAgentWeb = findViewById(R.id.webview);
        mAgentWeb.loadUrl(url);//加载url
        mAgentWeb.getSettings().setJavaScriptEnabled(true);  //加上这一行网页为响应式的
        mAgentWeb.setWebViewClient(new WebViewClient());

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
        if(keyCode == KeyEvent.KEYCODE_BACK && mAgentWeb.canGoBack()){
            mAgentWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
