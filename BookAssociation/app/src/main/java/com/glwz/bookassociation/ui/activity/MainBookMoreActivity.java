package com.glwz.bookassociation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.glwz.bookassociation.ui.Entity.BookTypeListBean;
import com.glwz.bookassociation.ui.adapter.MainBookMoreDataItemAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class MainBookMoreActivity extends BaseActivity implements View.OnClickListener, HttpAPICallBack{

    private RefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private MainBookMoreDataItemAdapter adapter;
    private RelativeLayout btn_back;
    private TextView type_title;
    private ArrayList<BookTypeListBean.DataBean> dataList = new ArrayList<>();
    private String cate_id = "";
    private String title_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_book_more);
        initData();
        initView();
    }


    public void initView(){
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(800/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
            }
        });

        recyclerView = findViewById(R.id.main_book_more_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        // 需要注意的是GridLayoutManager要设置setAutoMeasureEnabled(true)成自适应高度
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MainBookMoreDataItemAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(MainBookMoreActivity.this, BookMenuActivity.class);
                intent.putExtra("keycode", dataList.get(position).getKeycode());

                intent.putExtra("pic_name", dataList.get(position).getImg());
                intent.putExtra("title_name", dataList.get(position).getTitle());

                intent.putExtra("book_id", dataList.get(position).getBook_id());
                intent.putExtra("price", dataList.get(position).getPrice());

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
        cate_id = getIntent().getStringExtra("cate_id");
        title_name = getIntent().getStringExtra("title_name");

        HomeAPI.getBookTypeList(this, HttpUrl.BookTypeList_Url, cate_id);
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
        BookTypeListBean bean = (BookTypeListBean)response;
        if (bean != null){
            if (bean.getData()!=null){
                dataList.clear();
                dataList.addAll(bean.getData());
                adapter.notifyDataSetChanged();

                type_title.setText(title_name);
            }
        }
    }

    @Override
    public void onError(BaseBean response) {

    }
}
