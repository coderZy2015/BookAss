package com.glwz.bookassociation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.Net.HttpUrl;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.BookSearchBean;
import com.glwz.bookassociation.ui.adapter.BookSearchAdapter;

import java.util.ArrayList;

public class SearchFinishActivity extends BaseActivity implements View.OnClickListener, HttpAPICallBack {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private RelativeLayout btn_back;
    private TextView type_title;
    private BookSearchAdapter adapter;
    private ArrayList<BookSearchBean.DataBean> dataList = new ArrayList<>();
    private String title = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_finish);
        initData();
        initView();
    }

    public void initView(){
        recyclerView = findViewById(R.id.main_search_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        // 需要注意的是GridLayoutManager要设置setAutoMeasureEnabled(true)成自适应高度
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BookSearchAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(SearchFinishActivity.this, BookMenuActivity.class);
                intent.putExtra("keycode", dataList.get(position).getKeycode());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
            }
        });

        btn_back = (RelativeLayout)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        type_title = findViewById(R.id.type_title);
    }

    public void initData(){
        title = getIntent().getStringExtra("title");

        HomeAPI.getSearchBook(this, HttpUrl.BookSearch_Url, title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        BookSearchBean bean = (BookSearchBean)response;
        if(bean != null){
            dataList.clear();
            if(bean.getData() != null){
                dataList.addAll(bean.getData());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(BaseBean response) {

    }
}
