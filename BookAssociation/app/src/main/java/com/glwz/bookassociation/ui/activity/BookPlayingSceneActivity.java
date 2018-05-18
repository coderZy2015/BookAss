package com.glwz.bookassociation.ui.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.glwz.bookassociation.AppConstant;
import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.Net.HomeAPI;
import com.glwz.bookassociation.Net.HttpUrl;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BaseBean;
import com.glwz.bookassociation.ui.Entity.BookContentBean;
import com.glwz.bookassociation.ui.Entity.GetPreOrderBean;
import com.glwz.bookassociation.ui.event.EventBusMessageModel;
import com.glwz.bookassociation.ui.utils.MediaPlayControl;
import com.glwz.bookassociation.ui.utils.MyScrollView;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.glwz.bookassociation.ui.utils.Utils;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;

/**
 * 书本的详情页
 * Created by zy on 2018/4/27.
 */
public class BookPlayingSceneActivity extends BaseActivity implements MyScrollView
        .OnScrollListener, View.OnClickListener, HttpAPICallBack {

    private static final int NO_1 = 0x1;
    int num = 1;//初始通知数量为1

    private MyScrollView myScrollView;
    private int searchLayoutTop;

    LinearLayout layout01, layout02;
    RelativeLayout rlayout;
    //--------------------------------------

    public static final String ACTION_PRE_SONG = "pre_song";
    public static final String ACTION_PLAY_AND_PAUSE = "play_song";
    public static final String ACTION_NEXT_SONG = "next_song";
    public static final String ACTION_EXIT = "exit_song";

    //---------------------------------
    private TextView book_play_title;
    private TextView book_play_introduce;
    private SeekBar book_play_seekbar;
    private TextView book_play_bar_time;
    private TextView book_play_bar_total;
    private ImageView book_play_tuihou, book_play_play, book_play_next, book_play_back;
    private LinearLayout layout;
    //private TextView book_content;
    private TextView html_text;//HtmlText
    private upDateSeekBar update; // 更新进度条用

    private SharePreferenceUtil sharePreferenceUtil;

    private String tsgId = "";
    private String title_name = "";
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

    private String detilUrl = "";

    private Handler uiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_playing_scene);
        sharePreferenceUtil = new SharePreferenceUtil(this, MyData.SAVE_USER);
        tsgId = getIntent().getStringExtra("tsgId");
        title_name = getIntent().getStringExtra("title_name");
        book_id = getIntent().getStringExtra("book_id");
        price = getIntent().getStringExtra("price");
        //初始化富文本控件
        RichText.initCacheDir(this);
        //初始化控件
        initView();
        initPlayView();
        //经典中国
        if (book_id.equals("73")){
            detilUrl = "http://student.hebeijiaoyu.com.cn/glwz/web/interface/view/1/";
        }else{
            detilUrl = HttpUrl.BookContent_Url;
        }

        HomeAPI.getBookContent(this, detilUrl, tsgId);
    }

    private void initView() {
        myScrollView = findViewById(R.id.myScrollView);
        layout01 = findViewById(R.id.layout01);
        layout02 =  findViewById(R.id.layout02);
        rlayout =  findViewById(R.id.rlayout);
        myScrollView.setOnScrollListener(this);
        //book_content = findViewById(R.id.book_content);
        html_text = findViewById(R.id.html_text);
        update = new upDateSeekBar(); // 创建更新进度条对象
        new Thread(update).start(); // 启动线程，更新进度条
    }

    public void initPlayView() {
        layout = findViewById(R.id.layout);
        book_play_title = findViewById(R.id.book_play_title);
        book_play_introduce = findViewById(R.id.book_play_introduce);
        book_play_seekbar = findViewById(R.id.book_play_seekbar);
        book_play_bar_time = findViewById(R.id.book_play_bar_time);
        book_play_bar_total = findViewById(R.id.book_play_bar_total);
        book_play_tuihou = findViewById(R.id.book_play_tuihou);
        book_play_play = findViewById(R.id.book_play_play);
        book_play_next = findViewById(R.id.book_play_next);
        book_play_back = findViewById(R.id.book_play_back);

        book_play_play.setImageResource(R.drawable.book_play_pause);

        // 进度条
        book_play_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int value = book_play_seekbar.getProgress()
                        * MediaPlayControl.getInstance().getDuration() // 计算进度条需要前进的位置数据大小
                        / book_play_seekbar.getMax();

                MediaPlayControl.getInstance().setCurrentPostion(value);
                book_play_bar_time.setText(Utils.getTime_mm_ss(value));
                book_play_bar_total.setText(Utils.getTime_mm_ss(MediaPlayControl
                        .getInstance().getDuration()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }
        });

        bindListener();

        // 发布事件
        EventBusMessageModel event = new EventBusMessageModel();
        event.setState(MediaPlayControl.getInstance().getPlayIndex());
        event.setType(AppConstant.SELECT_ITEM);
        EventBus.getDefault().post(event);

        //showNotification();
        //show();
    }

    public void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.message_song_control);
        rv.setTextViewText(R.id.tv, "泡沫");//修改自定义View中的歌名
        //修改自定义View中的图片(两种方法)
        //    rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
        rv.setImageViewBitmap(R.id.iv, BitmapFactory.decodeResource(getResources(), R.mipmap
                .ic_launcher));

        builder.setContent(rv);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        notificationManager.notify(NO_1, notification);

    }


    public void bindListener() {
        book_play_back.setOnClickListener(this);
        book_play_play.setOnClickListener(this);
        book_play_next.setOnClickListener(this);
        book_play_tuihou.setOnClickListener(this);

    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        RemoteViews contentViews = new RemoteViews(getPackageName(), R.layout.message_song_control);
        initNotification(contentViews);

        builder.setContent(contentViews);
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService
                (NOTIFICATION_SERVICE);
        notificationManager.notify(NO_1, notification);//调用notify方法后即可显示通知
    }

    private void initNotification(RemoteViews contentViews) {
        //NotificationManager的获取

        Intent mainIntent = new Intent(this, HomePageActivtiy.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(BookPlayingSceneActivity.this, 0,
                mainIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.message_layout, pendingIntent);
        //上一首图标添加点击监听
        Intent previousButtonIntent = new Intent(ACTION_PRE_SONG);
        PendingIntent pendPreviousButtonIntent = PendingIntent.getBroadcast(this, 0,
                previousButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.btn_pre, pendPreviousButtonIntent);
        //播放/暂停添加点击监听
        Intent playPauseButtonIntent = new Intent(ACTION_PLAY_AND_PAUSE);
        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(this, 0,
                playPauseButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.btn_play, playPausePendingIntent);
        //下一首图标添加监听
        Intent nextButtonIntent = new Intent(ACTION_NEXT_SONG);
        PendingIntent pendNextButtonIntent = PendingIntent.getBroadcast(this, 0,
                nextButtonIntent, 0);
        contentViews.setOnClickPendingIntent(R.id.btn_next, pendNextButtonIntent);
        //退出监听
        Intent exitButton = new Intent(ACTION_EXIT);
        PendingIntent pendingExitButtonIntent = PendingIntent.getBroadcast(this, 0, exitButton, 0);
        contentViews.setOnClickPendingIntent(R.id.btn_exit, pendingExitButtonIntent);

        if (MediaPlayControl.getInstance().isPlaying()) {
            contentViews.setImageViewResource(R.id.btn_play, R.drawable.book_play_play);
        } else {
            contentViews.setImageViewResource(R.id.btn_play, R.drawable.book_play_pause);
        }
    }

    private BroadcastReceiver playMusicReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ACTION_NEXT_SONG)) {
                MediaPlayControl.getInstance().setNext();
                book_play_play.setImageResource(R.drawable.book_play_pause);
                int pIndex = MediaPlayControl.getInstance().getPlayIndex();

                if (pIndex != MediaPlayControl.getInstance().getSongList().size() - 1) {
                    String tsgId1 = MediaPlayControl.getInstance().getSongDataList().get(pIndex)
                            .getTsgId();
                    HomeAPI.getBookContent(BookPlayingSceneActivity.this, detilUrl, tsgId1);
                }
                // 发布事件
                EventBusMessageModel event1 = new EventBusMessageModel();
                event1.setState(MediaPlayControl.getInstance().getPlayIndex());
                event1.setType(AppConstant.SELECT_ITEM);
                EventBus.getDefault().post(event1);
            } else if (action.equals(ACTION_PRE_SONG)) {
                MediaPlayControl.getInstance().setPrevious();
                book_play_play.setImageResource(R.drawable.book_play_pause);
                int index = MediaPlayControl.getInstance().getPlayIndex();
                String tsgId = MediaPlayControl.getInstance().getSongDataList().get(index)
                        .getTsgId();
                HomeAPI.getBookContent(BookPlayingSceneActivity.this, detilUrl,
                        tsgId);

                // 发布事件
                EventBusMessageModel event = new EventBusMessageModel();
                event.setState(MediaPlayControl.getInstance().getPlayIndex());
                event.setType(AppConstant.SELECT_ITEM);
                EventBus.getDefault().post(event);
            } else if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {//在播放器中点击home键时显示通知栏图标
                showNotification();
            } else if (action.equals(ACTION_EXIT)) {//通知栏点击退出图标
                finish();
            } else if (action.equals(ACTION_PLAY_AND_PAUSE)) {
                if (MediaPlayControl.getInstance().isPlaying()) {
                    book_play_play.setImageResource(R.drawable.book_play_play);
                    MediaPlayControl.getInstance().pause();
                } else {
                    book_play_play.setImageResource(R.drawable.book_play_pause);
                    MediaPlayControl.getInstance().continuePlay();
                }
            }
        }

    };


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            searchLayoutTop = rlayout.getBottom();//获取searchLayout的顶部位置

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_play_back:
                onBackPressed();
                finish();
                break;

            case R.id.book_play_play:
                if (MediaPlayControl.getInstance().isPlaying()) {
                    book_play_play.setImageResource(R.drawable.book_play_play);
                    MediaPlayControl.getInstance().pause();
                } else {
                    book_play_play.setImageResource(R.drawable.book_play_pause);
                    MediaPlayControl.getInstance().continuePlay();
                }
                break;

            case R.id.book_play_tuihou:
                MediaPlayControl.getInstance().setPrevious();
                book_play_play.setImageResource(R.drawable.book_play_pause);
                int index = MediaPlayControl.getInstance().getPlayIndex();
                String tsgId = MediaPlayControl.getInstance().getSongDataList().get(index)
                        .getTsgId();
                HomeAPI.getBookContent(this, detilUrl, tsgId);

                // 发布事件
                EventBusMessageModel event = new EventBusMessageModel();
                event.setState(MediaPlayControl.getInstance().getPlayIndex());
                event.setType(AppConstant.SELECT_ITEM);
                EventBus.getDefault().post(event);
                break;

            case R.id.book_play_next:

                int idex = MediaPlayControl.getInstance().getPlayIndex() + 1;
                if (!MediaPlayControl.getInstance().getBookState() && idex > 4) {
                    if (!isShowDialog) {

                        if (checkUserName()) {
                            showMakeSureDialog();
                        }
                    }
                    return;
                }
                MediaPlayControl.getInstance().setNext();
                book_play_play.setImageResource(R.drawable.book_play_pause);
                int pIndex = MediaPlayControl.getInstance().getPlayIndex();
                if (pIndex != MediaPlayControl.getInstance().getSongList().size()) {
                    String tsgId1 = MediaPlayControl.getInstance().getSongDataList().get(pIndex)
                            .getTsgId();
                    HomeAPI.getBookContent(this, detilUrl, tsgId1);
                } else {
                    ToastUtils.showShort("请欣赏其他章节");
                }
                // 发布事件
                EventBusMessageModel event1 = new EventBusMessageModel();
                event1.setState(MediaPlayControl.getInstance().getPlayIndex());
                event1.setType(AppConstant.SELECT_ITEM);
                EventBus.getDefault().post(event1);
                break;

            default:
                break;
        }

    }

    //监听滚动Y值变化，通过addView和removeView来实现悬停效果
    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= searchLayoutTop) {
            if (layout.getParent() != layout01) {
                layout02.removeView(layout);
                layout01.addView(layout);

                int l_h = layout02.getBottom() - searchLayoutTop;
                //book_content.setPadding(0, l_h, 0, 0);
                html_text.setPadding(0, l_h, 0, 0);
            }
        } else {
            if (layout.getParent() != layout02) {
                layout01.removeView(layout);
                layout02.addView(layout);
                //book_content.setPadding(0, 0, 0, 0);
                html_text.setPadding(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void onSuccess(int url_id, BaseBean response) {
        if (url_id == HomeAPI.NET_getBookContent) {
            BookContentBean bean = (BookContentBean) response;
            if (bean != null) {
                if (bean.getMessage().getView() != null) {

                    //标题
                    book_play_title.setText("" + bean.getMessage().getView().getNameSub());
                    //描述
                    book_play_introduce.setText("作者：" + bean.getMessage().getView().getAuthor() +
                            "  " +
                            "  朗读者：" + bean.getMessage().getView().getReader());

                    final String htmlContent = bean.getMessage().getView().getContent();

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String content = htmlContent;
                            content = insertStr(content, 0);

                            Logger.i(content);

                            RichText.fromHtml(content).into(html_text);

                        }
                    });
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

    public String insertStr(String content, int fromIndex){
        String backStr = content;
        String insertStr = "http://student.hebeijiaoyu.com.cn";

        int lastIndex = backStr.lastIndexOf("/glwz");
        if (lastIndex == -1){
            return backStr;
        }
        Logger.i("lastIndex = "+lastIndex);

        backStr = backStr.replaceAll("/glwz", insertStr+"/glwz");

        //backStr = backStr.replaceAll(" alt=\"\"","");

//        backStr = "<B>Start</B> \n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<img src='http://wx1.sinaimg.cn/mw690/eaaf2affly1fihvjpekzwj21el0qotfq.jpg' />\n" +
//                "<B>End</B>";
        return backStr;
    }


    public boolean checkUserName() {
        if (sharePreferenceUtil.getUserName().equals("")) {
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            return false;
        }
        return true;
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
        Button sure = layout.findViewById(R.id.cost_sure);
        TextView book_price = layout.findViewById(R.id.book_price);
        book_name.setText("书名：" + title_name);
        float pr = Integer.parseInt(price);
        book_price.setText("" + pr / 100);

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
                HomeAPI.createOrder(BookPlayingSceneActivity.this, "15833113069", "" + book_id);
            }
        });
    }

    /**
     * 更新进度条
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int position = MediaPlayControl.getInstance().getCurrentPostion();
            int mMax = MediaPlayControl.getInstance().getDuration();
            int sMax = book_play_seekbar.getMax();
            book_play_seekbar.setProgress(position * sMax / mMax);
            book_play_bar_time.setText(Utils.getTime_mm_ss(position));
            book_play_bar_total.setText(Utils.getTime_mm_ss(mMax));

            if (!MediaPlayControl.getInstance().isPlaying()) {
                book_play_play.setImageResource(R.drawable.book_play_play);
            } else {
                book_play_play.setImageResource(R.drawable.book_play_pause);
            }
        }
    };

    class upDateSeekBar implements Runnable {
        @Override
        public void run() {
            mHandler.sendMessage(Message.obtain());
            mHandler.postDelayed(update, 1000);
        }
    }

    private void weChatPay(GetPreOrderBean weChatBean) {
        IWXAPI payApi = WXAPIFactory.createWXAPI(this, weChatBean.getAppid(),
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
        payApi.sendReq(payReq);
    }
}

