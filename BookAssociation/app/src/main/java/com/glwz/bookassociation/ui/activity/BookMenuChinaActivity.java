package com.glwz.bookassociation.ui.activity;

import android.app.AlertDialog;
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
import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.AllChinaData;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.BookIsBuyBean;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;
import com.glwz.bookassociation.ui.Entity.GetPreOrderBean;
import com.glwz.bookassociation.ui.adapter.BookMenuChinaAdapter;
import com.glwz.bookassociation.ui.utils.MediaPlayControl;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;

public class BookMenuChinaActivity extends BaseActivity implements HttpAPICallBack {

    private RefreshLayout mRefreshLayout;
    private String keycode = "";
    private String title_name = "";
    private String pic_name = "";
    private String book_id = "";
    private String price = "";
    /**
     * 支付弹窗dialog
     */
    private AlertDialog costDialog;
    /**
     * 显示dialog
     */
    private boolean isShowDialog = false;

    private SharePreferenceUtil sharePreferenceUtil;
    private ArrayList<BookMenuChinaBean.MessageBean.CatalogBean> dataList = new ArrayList<>();

    private RecyclerView recyclerView;
    private BookMenuChinaAdapter adapter;
    private RelativeLayout btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_menu_china);

        sharePreferenceUtil = new SharePreferenceUtil(this, MyData.SAVE_USER);
        keycode = getIntent().getStringExtra("keycode");
        title_name = getIntent().getStringExtra("title_name");
        pic_name = getIntent().getStringExtra("pic_name");
        book_id = getIntent().getStringExtra("book_id");
        price = getIntent().getStringExtra("price");

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        initView();

        if (price.equals("")){
            adapter.setPriceAndButtonShow(false);
        }
        if (!sharePreferenceUtil.getUserName().equals("")) {
            HomeAPI.GetBookIsBuy(BookMenuChinaActivity.this, book_id, sharePreferenceUtil.getUserName());
        }

        HomeAPI.getBookChinaInfo(this, keycode);
    }

    public void initView() {
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(800);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
            }
        });

        recyclerView = findViewById(R.id.book_menu_china_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookMenuChinaAdapter(this, dataList, title_name, pic_name, price, book_id);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_BookIsBuy) {
            BookIsBuyBean bean = (BookIsBuyBean) response;
            if (bean != null) {
                if (bean.getMsg().equals("true")) {
                    MediaPlayControl.getInstance().setBookState(true);
                    adapter.setPriceAndButtonShow(false);
                    adapter.notifyDataSetChanged();

                } else {
                    MediaPlayControl.getInstance().setBookState(false);
                    adapter.setPriceAndButtonShow(true);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        if (url_id == HomeAPI.NET_getBookChinaInfo){
            BookMenuChinaBean info = (BookMenuChinaBean) response;
            if (info != null) {
                AllChinaData.getInstance().setBookMenuChinaBean(info);
                if (info.getMessage().getCatalog() != null) {
                    dataList.clear();
                    dataList.addAll(info.getMessage().getCatalog());

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
    }

    @Override
    public void onError(BaseBean response) {
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
                HomeAPI.createOrder(BookMenuChinaActivity.this, sharePreferenceUtil.getUserName(), ""
                        + book_id);
            }
        });
    }

    //发起微信支付
    private void weChatPay(GetPreOrderBean weChatBean) {
        IWXAPI payApi = WXAPIFactory.createWXAPI(BookMenuChinaActivity.this, weChatBean.getAppid(),
                false);

        payApi.registerApp(weChatBean.getAppid());

        PayReq payReq = new PayReq();
        payReq.appId = weChatBean.getAppid();
        payReq.partnerId = weChatBean.getPartnerid();
        payReq.prepayId = weChatBean.getPrepayid();
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = weChatBean.getNoncestr();
        payReq.timeStamp = weChatBean.getTimestamp();
        payReq.sign = weChatBean.getSign();

        payApi.sendReq(payReq);
    }

}
