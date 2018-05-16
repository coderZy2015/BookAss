package com.glwz.bookassociation.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.ChildInfo;
import com.glwz.bookassociation.ui.adapter.SeeBookAdapter;

import java.util.ArrayList;

public class SeeAllBookActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<ChildInfo> childInfoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SeeBookAdapter adapter;
    private RelativeLayout btn_back;
    private TextView mTile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_book);

        initView();
    }

    public void initData() {
        childInfoList = new ArrayList<>();
        for (int j = 0; j < 8; j++) {
            ChildInfo childInfo = new ChildInfo();
            childInfo.setMenuName("书本-" + j);
            childInfo.setIconImgID(R.drawable.book);
            childInfoList.add(childInfo);
        }
    }

    public void initView() {
        initData();

        recyclerView = findViewById(R.id.main_see_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        // 需要注意的是GridLayoutManager要设置setAutoMeasureEnabled(true)成自适应高度
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SeeBookAdapter(this, childInfoList, true);
        recyclerView.setAdapter(adapter);

        btn_back = (RelativeLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        mTile = findViewById(R.id.type_title);
        mTile.setText("看过的书籍");

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
}
