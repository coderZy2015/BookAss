package com.glwz.bookassociation.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;
import com.glwz.bookassociation.ui.Entity.BookMenuInfo;
import com.glwz.bookassociation.ui.Entity.GetPreOrderBean;
import com.glwz.bookassociation.ui.adapter.BookMenuChinaContentAdapter;
import com.glwz.bookassociation.ui.event.BookChinaEvent;
import com.glwz.bookassociation.ui.event.EventBusMessageModel;
import com.glwz.bookassociation.ui.utils.MediaPlayControl;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 经典中国 最后的子类显示
 */
public class BookChinaMenuListActivity extends BaseActivity implements HttpAPICallBack {

    private SwipeRefreshLayout mRefreshLayout;
    private String title_name = "";
    private String pic_name;
    private String book_id = "";
    private String price = "";
    private RecyclerView recyclerView;
    private BookMenuChinaContentAdapter adapter;
    private ArrayList<BookMenuInfo.MessageBean.CatalogBean> xdataList = new ArrayList<>();
    private SharePreferenceUtil sharePreferenceUtil;
    private RelativeLayout btn_back;
    private boolean useCoupon = false;
    //子类
    private List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean.ChildBeanZ> dataList = new ArrayList<>();
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
        setContentView(R.layout.activity_book_china_menu_list);
        sharePreferenceUtil = new SharePreferenceUtil(this, MyData.SAVE_USER);
        initView();
    }

    public void initView() {
        recyclerView = findViewById(R.id.book_china_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        adapter = new BookMenuChinaContentAdapter(this, dataList);
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
                intent.setClass(BookChinaMenuListActivity.this, BookPlayingSceneActivity.class);
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

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        // 注册订阅者
        EventBus.getDefault().register(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(BookChinaEvent event) {

        book_id = event.getBook_id();
        pic_name = event.getPic_name();
        price = event.getPrice();
        title_name = event.getName();
        dataList.clear();
        dataList.addAll(event.getObject());
        adapter.notifyDataSetChanged();
        convertData();
        if (!sharePreferenceUtil.getUserName().equals("")) {
            HomeAPI.GetBookIsBuy(BookChinaMenuListActivity.this, book_id, sharePreferenceUtil.getUserName());
        }

        // 移除粘性事件
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessageModel event) {
        if (event.getType().equals(AppConstant.SELECT_ITEM)) {
            if (adapter != null) {
                adapter.setSelectedItem(event.getState());
            }
        } else if (event.getType().equals(AppConstant.PAY_SUCCESS)) {
            HomeAPI.GetBookIsBuy(BookChinaMenuListActivity.this, book_id, sharePreferenceUtil.getUserName());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }

    public void convertData(){
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
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_BookIsBuy) {
            BookIsBuyBean bean = (BookIsBuyBean) response;
            if (bean != null) {
                if (bean.getMsg().equals("true")) {
                    MediaPlayControl.getInstance().setBookState(true);
                    adapter.setOpenAllBook(true);
                    adapter.notifyDataSetChanged();

                } else {
                    MediaPlayControl.getInstance().setBookState(false);
                    adapter.setOpenAllBook(false);
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

        TextView book_new_price = layout.findViewById(R.id.book_new_price);

        TextView txt_no_daijin = layout.findViewById(R.id.txt_no_daijin);
        CheckBox btn_select_daijin = layout.findViewById(R.id.btn_selet_daijin);
        if (Integer.parseInt(MyData.CouponNum) > 0){
            txt_no_daijin.setVisibility(View.GONE);
            btn_select_daijin.setVisibility(View.VISIBLE);
        }
        btn_select_daijin.setChecked(false);

        btn_select_daijin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    useCoupon = true;
                }else{
                    useCoupon = false;
                }
            }
        });

        if (!price.equals("")){
            float pr = Integer.parseInt(price);
            book_price.setText(""+ pr/100);
            if (MyData.isMiguMember){
                pr = pr*0.88f;
                pr = pr/100;
                BigDecimal bg = new BigDecimal(pr);
                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                book_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
                book_price.setTextColor(getResources().getColor(R.color.cost_indruce));
                book_new_price.setVisibility(View.VISIBLE);
                book_new_price.setText(""+ f1);
            }
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

                if (!useCoupon){
                    HomeAPI.createOrder(BookChinaMenuListActivity.this, sharePreferenceUtil.getUserName(), ""
                            + book_id, "0");
                }else{
                    HomeAPI.createOrder(BookChinaMenuListActivity.this, sharePreferenceUtil.getUserName(), ""
                            + book_id, "1");
                }

            }
        });
    }

    //发起微信支付
    private void weChatPay(GetPreOrderBean weChatBean) {
        IWXAPI payApi = WXAPIFactory.createWXAPI(BookChinaMenuListActivity.this, weChatBean.getAppid(),
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

