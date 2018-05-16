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
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.BuyBookListBean;
import com.glwz.bookassociation.ui.adapter.BuyBookAdapter;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class BuyAllBookActivity extends BaseActivity implements View.OnClickListener, HttpAPICallBack{

    private RecyclerView recyclerView;
    private RelativeLayout btn_back;
    private TextView mTile;

    private BuyBookAdapter buy_adapter;
    private SharePreferenceUtil sharePreferenceUtil;
    private List<BuyBookListBean.BookListBean> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_all_book);
        sharePreferenceUtil = new SharePreferenceUtil(this, MyData.SAVE_USER);
        initView();
    }


    public void initView(){
        recyclerView = findViewById(R.id.main_buy_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        // 需要注意的是GridLayoutManager要设置setAutoMeasureEnabled(true)成自适应高度
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        buy_adapter = new BuyBookAdapter(this, bookList, true);
        recyclerView.setAdapter(buy_adapter);
        buy_adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setClass(BuyAllBookActivity.this, BookMenuActivity.class);
                intent.setClass(BuyAllBookActivity.this, BookMenuActivity.class);
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

        HomeAPI.GetBuyBookList(this, sharePreferenceUtil.getUserName());
        btn_back = (RelativeLayout)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        mTile = findViewById(R.id.type_title);
        mTile.setText("买过的书籍");
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
