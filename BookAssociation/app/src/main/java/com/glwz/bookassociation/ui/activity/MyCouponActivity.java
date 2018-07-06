package com.glwz.bookassociation.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.CardNumBean;
import com.glwz.bookassociation.ui.adapter.MyCouponAdapter;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class MyCouponActivity extends BaseActivity implements HttpAPICallBack{

    private RelativeLayout btn_back;
    private RecyclerView recyclerView;
    private RefreshLayout mRefreshLayout;
    private MyCouponAdapter adapter;
    private ArrayList<String> dataList = new ArrayList<>();
    private SharePreferenceUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        sp = new SharePreferenceUtil(this, MyData.SAVE_USER);
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

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyCouponAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

        HomeAPI.getHaveCoupon(this, sp.getUserName());
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_IsHaveCoupon){
            CardNumBean beanC = (CardNumBean)response;
            if (beanC != null){
                if (beanC.getCardNum() ==null){
                    return;
                }
                MyData.CouponNum = beanC.getCardNum();

                dataList.clear();
                for (int i=0; i<Integer.parseInt(MyData.CouponNum); i++){
                    dataList.add("data "+i);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(BaseBean response) {

    }
}
