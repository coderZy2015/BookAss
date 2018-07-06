package com.glwz.bookassociation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.BuyBookListBean;
import com.glwz.bookassociation.ui.activity.BookMenuActivity;
import com.glwz.bookassociation.ui.activity.BuyAllBookActivity;
import com.glwz.bookassociation.ui.activity.LoginActivity;
import com.glwz.bookassociation.ui.activity.MyCouponActivity;
import com.glwz.bookassociation.ui.adapter.BuyBookAdapter;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by zy on 2018/4/23.
 */

public class MineFragment extends SupportFragment implements View.OnClickListener, HttpAPICallBack {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    private RecyclerView buy_recyclerView;
    private BuyBookAdapter buy_adapter;

    private RelativeLayout buy_more;
    private Button mine_btn_login;
    private LinearLayout btn_my_coupon;
    private RefreshLayout mRefreshLayout;
    private SharePreferenceUtil sharePreferenceUtil;

    private List<BuyBookListBean.BookListBean> bookList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mine,
                container, false);
        initView(view);
        return view;
    }


    public void initView(View view) {
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

        mine_btn_login = view.findViewById(R.id.mine_btn_login);
        mine_btn_login.setOnClickListener(this);

        btn_my_coupon = view.findViewById(R.id.btn_my_coupon);
        btn_my_coupon.setOnClickListener(this);

        sharePreferenceUtil = new SharePreferenceUtil(_mActivity, MyData.SAVE_USER);
        if (!sharePreferenceUtil.getUserName().equals("")) {
            mine_btn_login.setVisibility(View.GONE);
        }

        buy_more = view.findViewById(R.id.btn_buy_more);
        buy_more.setOnClickListener(this);

        buy_recyclerView = view.findViewById(R.id.mine_buy_recyclerView);

        RecyclerView.LayoutManager manager = new GridLayoutManager(_mActivity, 3);
        // 需要注意的是GridLayoutManager要设置setAutoMeasureEnabled(true)成自适应高度
        manager.setAutoMeasureEnabled(true);
        buy_recyclerView.setLayoutManager(manager);

        buy_adapter = new BuyBookAdapter(_mActivity, bookList, false);
        buy_recyclerView.setAdapter(buy_adapter);
        buy_adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(_mActivity, BookMenuActivity.class);
                intent.putExtra("keycode", bookList.get(position).getKeycode());
                intent.putExtra("title_name", bookList.get(position).getTitle());
                intent.putExtra("pic_name", sharePreferenceUtil.getImgData(bookList.get(position).getId()));
                intent.putExtra("book_id", bookList.get(position).getId());
                intent.putExtra("price", "");
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharePreferenceUtil != null) {
            if (!sharePreferenceUtil.getUserName().equals("")) {
                mine_btn_login.setVisibility(View.GONE);
                HomeAPI.GetBuyBookList(this, sharePreferenceUtil.getUserName());
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_buy_more:
                // 发布粘性事件
                _mActivity.startActivity(new Intent().setClass(_mActivity, BuyAllBookActivity
                        .class));
                break;
            case R.id.mine_btn_login:
                _mActivity.startActivity(new Intent().setClass(_mActivity, LoginActivity.class));
                break;
            case R.id.btn_my_coupon:
                if (checkUserName())
                _mActivity.startActivity(new Intent().setClass(_mActivity, MyCouponActivity.class));
                break;
            default:
                break;
        }

    }

    /*
     * 检查登录名是否存在
     */
    public boolean checkUserName() {
        if (sharePreferenceUtil.getUserName().equals("")) {
            Intent intent = new Intent();
            intent.setClass(_mActivity, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_BuyBookList) {
            BuyBookListBean bean = (BuyBookListBean) response;
            if (bean != null) {
                bookList.clear();
                bookList.addAll(bean.getBookList());
                buy_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(BaseBean response) {

    }
}
