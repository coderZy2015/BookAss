package com.glwz.bookassociation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.Net.HttpUrl;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.CardNumBean;
import com.glwz.bookassociation.ui.Entity.LoginBean;
import com.glwz.bookassociation.ui.Entity.MainBookListBean;
import com.glwz.bookassociation.ui.adapter.MainDataItemAdapter;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by zy on 2018/4/23.
 */

public class MainPageFragment extends SupportFragment implements HttpAPICallBack {

    private RefreshLayout mRefreshLayout;

    private MainDataItemAdapter adapter;

    private RecyclerView recyclerView;

    private ArrayList<MainBookListBean.DataBean> dataList = new ArrayList<>();

    private SharePreferenceUtil sp;

    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mainpage,
                container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        sp = new SharePreferenceUtil(_mActivity, MyData.SAVE_USER);

        mRefreshLayout = view.findViewById(R.id.refreshLayout);
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

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        recyclerView.setLayoutManager(layoutManager);

        if (!sp.getUserName().equals("")){
            HomeAPI.Login(this, HttpUrl.Login_Url, sp.getUserName(), sp.getPassword());
        }

        HomeAPI.getMainBookList(this, HttpUrl.MainBookList_Url);
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_getMainBookList){
            MainBookListBean mBean = (MainBookListBean) response;

            if (mBean != null) {
                if (mBean.getData() != null) {
                    dataList.clear();
                    dataList.addAll(mBean.getData());
                    adapter = new MainDataItemAdapter(_mActivity, dataList);
                    recyclerView.setAdapter(adapter);
                }

            }
        }
        if (url_id == HomeAPI.NET_Login){
            LoginBean bean = (LoginBean)response;
            if (bean != null){
                //错误
                if(bean.getMark().equals("1")){
                    sp.setUserName("");
                    sp.setPassword("");
                }

                //登录成功  mark = 3咪咕会员
                if (bean.getMark().equals("0") || bean.getMark().equals("3")){
                    if (bean.getMark().equals("3")){
                        MyData.isMiguMember = true;
                        HomeAPI.getHaveCoupon(this, sp.getUserName());
                    }else{
                        MyData.isMiguMember = false;
                    }

                    ToastUtils.showShort(bean.getMessage());
                }

            }
        }
        if (url_id == HomeAPI.NET_IsHaveCoupon){
            CardNumBean bean = (CardNumBean)response;
            if (bean != null){
                MyData.CouponNum = bean.getCardNum();
            }
        }

    }

    @Override
    public void onError(BaseBean response) {

    }

}
