package com.glwz.bookassociation.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.glwz.bookassociation.AppConstant;
import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.Net.HttpUrl;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.BookIsBuyBean;
import com.glwz.bookassociation.ui.Entity.BookMenuInfo;
import com.glwz.bookassociation.ui.Entity.GetPreOrderBean;
import com.glwz.bookassociation.ui.adapter.BookMenuListAdapter;
import com.glwz.bookassociation.ui.event.EventBusMessageModel;
import com.glwz.bookassociation.ui.utils.MediaPlayControl;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 书本的目录页
 * Created by zy on 2018/4/24.
 */
public class BookMenuActivity extends BaseActivity implements HttpAPICallBack {

    private RefreshLayout mRefreshLayout;
    private BookMenuListAdapter adapter;
    private SharePreferenceUtil sharePreferenceUtil;
    private String keycode = "";
    private String title_name = "";
    private String pic_name;
    private String book_id = "";
    private String price = "";
    private RecyclerView recyclerView;
    private ArrayList<BookMenuInfo.MessageBean.CatalogBean> dataList = new ArrayList<>();
    private RelativeLayout btn_back;
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
        setContentView(R.layout.activity_book_menu);

        sharePreferenceUtil = new SharePreferenceUtil(this, MyData.SAVE_USER);

        keycode = getIntent().getStringExtra("keycode");
        title_name = getIntent().getStringExtra("title_name");
        pic_name = getIntent().getStringExtra("pic_name");
        book_id = getIntent().getStringExtra("book_id");
        price = getIntent().getStringExtra("price");

        sharePreferenceUtil.setImgData(book_id, pic_name);

        initView();

        EventBus.getDefault().register(this);

        HomeAPI.getBookInfo(this, keycode);

        if (price.equals("")){
            adapter.setPriceAndButtonShow(false);
        }
        if (!sharePreferenceUtil.getUserName().equals("")) {
            HomeAPI.GetBookIsBuy(BookMenuActivity.this, book_id, sharePreferenceUtil.getUserName());
        }

    }

    /*
     * 检查登录名是否存在
     */
    public boolean checkUserName() {
        if (sharePreferenceUtil.getUserName().equals("")) {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    public void initView() {
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

        recyclerView = findViewById(R.id.book_menu_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        adapter = new BookMenuListAdapter(this, dataList, title_name, pic_name, price);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (!MediaPlayControl.getInstance().getBookState() && position > 4) {
                    if (!isShowDialog) {
                        if (checkUserName()) {
                            showMakeSureDialog();
                        }
                    }
                    return;
                }
                //同一首音乐  继续播放
                String song_url = HttpUrl.RES_URL + dataList.get(position).getFileUrl();
                if (song_url.equals(MediaPlayControl.getInstance().play_url)) {
                } else {
                    MediaPlayControl.getInstance().setPlayIndex(position);
                }

                Intent intent = new Intent();
                intent.setClass(BookMenuActivity.this, BookPlayingSceneActivity.class);
                intent.putExtra("tsgId", dataList.get(position).getTsgId());
                intent.putExtra("title_name", title_name);
                intent.putExtra("book_id", book_id);
                intent.putExtra("price",price);
                startActivity(intent);

            }

            @Override
            public void onLongClick(int position) {
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessageModel event) {
        if (event.getType().equals(AppConstant.SELECT_ITEM)) {
            if (adapter != null) {
                adapter.setSelectedItem(event.getState());
            }
        } else if (event.getType().equals(AppConstant.PAY_SUCCESS)) {
            HomeAPI.GetBookIsBuy(BookMenuActivity.this, book_id, sharePreferenceUtil.getUserName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_BookIsBuy) {
            BookIsBuyBean bean = (BookIsBuyBean) response;
            if (bean != null) {
                if (bean.getMsg().equals("true")) {
                    adapter.setOpenAllBook(true);
                    MediaPlayControl.getInstance().setBookState(true);
                    adapter.setPriceAndButtonShow(false);
                    adapter.notifyDataSetChanged();

                } else {
                    adapter.setOpenAllBook(false);
                    MediaPlayControl.getInstance().setBookState(false);
                    adapter.setPriceAndButtonShow(true);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        if (url_id == HomeAPI.NET_GetPreOrder) {
            GetPreOrderBean bean = (GetPreOrderBean) response;
            if (bean != null) {

                if (bean.getReturn_code().equals("SUCCESS")) {
                    weChatPay(bean);
                } else {
                    ToastUtils.showShort(bean.getResult_code());
                }

            }
        }
        if (url_id == HomeAPI.NET_getBookInfo) {
            BookMenuInfo info = (BookMenuInfo) response;
            if (info != null) {
                if (info.getMessage().getCatalog() != null) {
                    dataList.clear();
                    dataList.addAll(info.getMessage().getCatalog());
                    adapter.notifyDataSetChanged();
                    setMediaPlayData();

                    int index = MediaPlayControl.getInstance().getPlayIndex();
                    if (index == -1) {
                        return;
                    }
                    //判断播放状态
                    if (index >= dataList.size()) {
                        return;
                    }
                    String song_url = HttpUrl.RES_URL + dataList.get(index).getFileUrl();
                    if (song_url.equals(MediaPlayControl.getInstance().play_url)) {
                        adapter.setSelectedItem(index);
                    }

                }
            }
        }

    }

    @Override
    public void onError(BaseBean response) {
    }

    /*
     * 设置mediaPlay数据
     */
    public void setMediaPlayData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            list.add(HttpUrl.RES_URL + dataList.get(i).getFileUrl());
        }
        MediaPlayControl.getInstance().setSongList(list);
        MediaPlayControl.getInstance().setSongDataList(dataList);
    }

    //确认支付界面的dialog
    public void showMakeSureDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_cost, null);
        isShowDialog = true;
        costDialog = new AlertDialog.Builder(this).create();
        costDialog.setCancelable(false);
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
        TextView book_price = layout.findViewById(R.id.book_price);
        Button sure = layout.findViewById(R.id.cost_sure);
        book_name.setText("书名：" + title_name);

        if (!price.equals("")){
            float pr = Integer.parseInt(price);
            book_price.setText(""+ pr/100);
        }

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
                //获取订单信息
                HomeAPI.createOrder(BookMenuActivity.this, sharePreferenceUtil.getUserName(), ""
                        + book_id);
            }
        });
    }

    //发起微信支付
    private void weChatPay(GetPreOrderBean weChatBean) {
        IWXAPI payApi = WXAPIFactory.createWXAPI(BookMenuActivity.this, weChatBean.getAppid(),
                false);
        if(!payApi.isWXAppInstalled()){
            //未安装的处理
            ToastUtils.showShort("未安装微信");
        }
        payApi.registerApp(weChatBean.getAppid());

        PayReq payReq = new PayReq();
        payReq.appId = weChatBean.getAppid();
        payReq.partnerId = weChatBean.getPartnerid();
        payReq.prepayId = weChatBean.getPrepayid();
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = weChatBean.getNoncestr();
        payReq.timeStamp = weChatBean.getTimestamp();
        payReq.sign = weChatBean.getSign();

        //        String signA = "appid=" + weChatBean.getAppid() + "&noncestr=" + weChatBean
        // .getNoncestr()
        //                + "&package=Sign=WXPay" + "&partnerid=" + weChatBean.getPartnerid() +
        //                "&prepayid=" + weChatBean.getPrepayid() + "&timestamp=" + weChatBean
        // .getTimestamp();
        //
        //        String signB = signA + "&k"+"ey=" + "y52nZlJSccjsIoDMPjgRuKjI9AfD8dFG";
        //        String signC = EncryptUtils.encryptMD5ToString(signB);
        //        payReq.sign = signC;
        //        Log.i("zy_code", "AppId = " + payReq.appId);
        //        Log.i("zy_code", "partnerId = " + payReq.partnerId);
        //        Log.i("zy_code", "prepayId = " + payReq.prepayId);
        //        Log.i("zy_code", "packageValue = " + payReq.packageValue);
        //        Log.i("zy_code", "nonceStr = " + payReq.nonceStr);
        //        Log.i("zy_code", "timeStamp = " + payReq.timeStamp);
        //        Log.i("zy_code", "sign = " + payReq.sign);
        //        Log.i("zy_code", "signB = " + signB);
        //        Log.i("zy_code", "signC = " + signC);

        payApi.sendReq(payReq);
    }

}
