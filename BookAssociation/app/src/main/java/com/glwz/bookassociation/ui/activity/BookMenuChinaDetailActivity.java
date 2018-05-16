package com.glwz.bookassociation.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.Net.HttpUrl;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.AllChinaData;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;
import com.glwz.bookassociation.ui.Entity.BookMenuInfo;
import com.glwz.bookassociation.ui.adapter.BookMenuChinaDetailAdapter;
import com.glwz.bookassociation.ui.utils.MediaPlayControl;

import java.util.ArrayList;
import java.util.List;

public class BookMenuChinaDetailActivity extends AppCompatActivity implements HttpAPICallBack {

    private SwipeRefreshLayout mRefreshLayout;
    private String keycode = "";
    private String title_name = "";
    private String pic_name;
    private String child_name;
    private int child_postion;
    private RecyclerView recyclerView;
    private BookMenuChinaDetailAdapter re_adapter;
    private ArrayList<BookMenuInfo.MessageBean.CatalogBean> xdataList = new ArrayList<>();
    //子类
    private List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean> dataList = new ArrayList<>();
    /**
     * 支付弹窗dialog
     */
    private AlertDialog costDialog;
    /**
     * 显示dialog
     */
    private boolean isShowDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_menu_china_detail);

        keycode = getIntent().getStringExtra("keycode");
        title_name = getIntent().getStringExtra("title_name");
        pic_name = getIntent().getStringExtra("pic_name");
        child_postion = getIntent().getIntExtra("child_postion", 0);
        child_name = getIntent().getStringExtra("child_name");
        initView();

        HomeAPI.getBookChinaInfo(this, keycode);
    }

    public void initView() {
        recyclerView = findViewById(R.id.book_menu_china_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        re_adapter = new BookMenuChinaDetailAdapter(this, xdataList, title_name, pic_name);
        recyclerView.setAdapter(re_adapter);
        re_adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (!MediaPlayControl.getInstance().getBookState() && position > 4) {
                    if (!isShowDialog)
                        showMakeSureDialog();
                    return;
                }
                //同一首音乐  继续播放
                String song_url = HttpUrl.RES_URL + xdataList.get(position).getFileUrl();
                if (song_url.equals(MediaPlayControl.getInstance().play_url)){
                }else{
                    MediaPlayControl.getInstance().setPlayIndex(position);
                }

                Intent intent = new Intent();
                intent.setClass(BookMenuChinaDetailActivity.this, BookPlayingSceneActivity.class);
                intent.putExtra("tsgId", xdataList.get(position).getTsgId());
                intent.putExtra("title_name", title_name);
                startActivity(intent);

            }
            @Override
            public void onLongClick(int position) {
            }
        });

    }

    public void setMediaPlayData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            list.add(HttpUrl.RES_URL + dataList.get(i).getFileUrl());
        }
        MediaPlayControl.getInstance().setSongList(list);
        MediaPlayControl.getInstance().setSongDataList(xdataList);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //确认支付界面的dialog
    public void showMakeSureDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_cost, null);
        isShowDialog = true;
        costDialog = new AlertDialog.Builder(this).create();
        costDialog.show();

        Window window = costDialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        costDialog.setContentView(layout);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = display.getWidth() * 5 / 6; // 设置dialog宽度为屏幕的4/5
        window.setAttributes(lp);

        TextView book_name = layout.findViewById(R.id.book_name);
        ImageView cancle = layout.findViewById(R.id.cost_close);
        Button sure = layout.findViewById(R.id.cost_sure);
        book_name.setText("书名：" + title_name);

        ViewGroup.LayoutParams param = sure.getLayoutParams();
        param.width = display.getWidth() * 3 / 5;
        sure.setLayoutParams(param);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowDialog = false;
                costDialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowDialog = false;
                costDialog.dismiss();
                //发起微信支付
            }
        });
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_getBookChinaInfo) {
            BookMenuChinaBean info = (BookMenuChinaBean) response;
            if (info != null) {
                AllChinaData.getInstance().setBookMenuChinaBean(info);

                dataList = AllChinaData.getInstance().getBookMenuChinaBean().getMessage().getCatalog().get(0).getChild().get(0).getChild();
                for (int i=0; i<dataList.size(); i++){
                    BookMenuInfo.MessageBean.CatalogBean bean = new BookMenuInfo.MessageBean.CatalogBean();
                    bean.setFileUrl(dataList.get(i).getFileUrl());
                    bean.setAuthor(dataList.get(i).getAuthor());
                    bean.setContent(dataList.get(i).getContent());
                    bean.setId(dataList.get(i).getId());
                    bean.setAudioTime(dataList.get(i).getAudioTime());
                    bean.setName(dataList.get(i).getName());
                    bean.setNameSub(dataList.get(i).getNameSub());
                    bean.setReader(dataList.get(i).getReader());
                    bean.setTsgId(dataList.get(i).getTsgId());
                    xdataList.add(bean);
                }
                setMediaPlayData();
                re_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(BaseBean response) {

    }
}
