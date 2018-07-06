package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BannerData;
import com.glwz.bookassociation.ui.Entity.MainBookListBean;
import com.glwz.bookassociation.ui.activity.BookMenuActivity;
import com.glwz.bookassociation.ui.activity.BookMenuChinaActivity;
import com.glwz.bookassociation.ui.activity.MainBookMoreActivity;
import com.glwz.bookassociation.ui.activity.SearchFinishActivity;
import com.glwz.bookassociation.ui.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 首页recyclerView的adapter，包含多个type的子布局
 * Created by zy on 2018/4/24.
 */

public class MainDataItemAdapter extends RecyclerView.Adapter {

    public static final int TYPE_SELECT = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_HORIZONTAL = 2;
    public static final int TYPE_NORMAL = 3;

    private Context context;
    private List<MainBookListBean.DataBean> list = new ArrayList<>();//父层列表 （里面是 text +
    // 子List（子list是image+text））
    private int headerNum = 3;
    private ArrayList<MainBookListBean.DataBean> menuList = new ArrayList<>();

    public MainDataItemAdapter(Context context, List<MainBookListBean.DataBean> _list) {
        this.context = context;
        list = _list;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_SELECT;
        if (position == 1)
            return TYPE_HEADER;
        if (position == 2)
            return TYPE_HORIZONTAL;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SELECT) {
            View view = LayoutInflater.from(context).inflate(R.layout.select_main_layout_data,
                    null);
            return new SearchViewHolder(view);
        }
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.header_main_layout_data,
                    null);
            return new HeaderViewHolder(view);
        }
        if (viewType == TYPE_HORIZONTAL) {
            View view = LayoutInflater.from(context).inflate(R.layout.topbar_main_layout_data,
                    parent, false);
            return new HorizontalViewHolder(view);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_main_layout_data,
                parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MainDataItemAdapter.SearchViewHolder) {//搜索
            final SearchViewHolder sHolder = (SearchViewHolder) holder;
            sHolder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    Intent intent = new Intent();
                    intent.setClass(context, SearchFinishActivity.class);
                    intent.putExtra("title", "" + sHolder.editText.getText().toString());

                    Log.i("zy_code", "title == " + sHolder.editText.getText().toString());

                    context.startActivity(intent);
                    return false;
                }
            });
        }
        if (holder instanceof MainDataItemAdapter.HeaderViewHolder) {//轮播图

        } else if (holder instanceof MainDataItemAdapter.HorizontalViewHolder) {//上方滑动栏

            MainDataItemAdapter.HorizontalViewHolder vewHolder = (MainDataItemAdapter
                    .HorizontalViewHolder) holder;
            if (vewHolder.mRecyclerView.getAdapter() == null) {
                final MainDataTopBarItemAdapter adapter = new MainDataTopBarItemAdapter(context, list);
                vewHolder.mRecyclerView.setAdapter(adapter);
            } else {
                vewHolder.mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } else if (holder instanceof MainDataItemAdapter.ViewHolder) {//item
            if (list == null)
                return;
            MainDataItemAdapter.ViewHolder mViewHolder = (MainDataItemAdapter.ViewHolder) holder;

            mViewHolder.mTitle.setText(list.get(position - headerNum).getCat_title());

            //把内层的RecyclerView 绑定在外层的onBindViewHolder
            // 先判断一下是不是已经设置了Adapter
            if (mViewHolder.mRecyclerView.getAdapter() == null) {
                MainDataChildItemAdapter adapter = new MainDataChildItemAdapter(context, list.get
                        (position - headerNum).getBooks());
                mViewHolder.mRecyclerView.setAdapter(adapter);

                final int typePostion = position;

                //点击更多的跳转
                mViewHolder.loadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(context, MainBookMoreActivity.class);
                        intent.putExtra("cate_id", list.get(position - headerNum).getCat_id());
                        intent.putExtra("title_name", list.get(position - headerNum).getCat_title
                                ());

                        context.startActivity(intent);
                    }
                });

                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        //经典中国
                        String title = list.get(typePostion - headerNum).getBooks
                                ().get(position).getTitle();
                        if (title.equals("经典中国")) {
                            Intent intent = new Intent();
                            intent.setClass(context, BookMenuChinaActivity.class);
                            intent.putExtra("keycode", list.get(typePostion - headerNum).getBooks()
                                    .get(position).getKeycode());
                            intent.putExtra("title_name", list.get(typePostion - headerNum).getBooks
                                    ().get(position).getTitle());
                            intent.putExtra("pic_name", list.get(typePostion - headerNum).getBooks
                                    ().get(position).getImg());
                            intent.putExtra("book_id", list.get(typePostion - headerNum).getBooks
                                    ().get(position).getBook_id());
                            intent.putExtra("price", list.get(typePostion - headerNum).getBooks
                                    ().get(position).getPrice());

                            context.startActivity(intent);
                            return;
                        }
                        Intent intent = new Intent();
                        intent.setClass(context, BookMenuActivity.class);
                        intent.putExtra("keycode", list.get(typePostion - headerNum).getBooks()
                                .get(position).getKeycode());
                        intent.putExtra("title_name", list.get(typePostion - headerNum).getBooks
                                ().get(position).getTitle());
                        intent.putExtra("pic_name", list.get(typePostion - headerNum).getBooks
                                ().get(position).getImg());
                        intent.putExtra("book_id", list.get(typePostion - headerNum).getBooks
                                ().get(position).getBook_id());
                        intent.putExtra("price", list.get(typePostion - headerNum).getBooks
                                ().get(position).getPrice());
                        context.startActivity(intent);
                    }

                    @Override
                    public void onLongClick(int position) {
                    }
                });
            } else {
                mViewHolder.mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return headerNum;
        }
        return list.size() + headerNum;
    }

    /**
     * 顶部搜索框
     */
    static class SearchViewHolder extends RecyclerView.ViewHolder {

        EditText editText;
        RelativeLayout edit_layout;

        public SearchViewHolder(final View itemView) {
            super(itemView);
            edit_layout = itemView.findViewById(R.id.edit_layout);
            editText = itemView.findViewById(R.id.main_select_edit);
        }
    }

    /**
     * 顶部轮播图
     */
    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        Banner mBanner;
        ArrayList<String> imageList;

        ArrayList<Integer> list;

        public HeaderViewHolder(final View itemView) {
            super(itemView);
            mBanner = itemView.findViewById(R.id.banner);
            imageList = new ArrayList<>();
            list = new ArrayList<>();

            //设置banner的高度为手机屏幕的四分之一
            mBanner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .MATCH_PARENT, ScreenUtils.getScreenHeight() / 4));
            imageList.add("http://img.zcool" +
                    ".cn/community/0142135541fe180000019ae9b8cf86.jpg@1280w_1l_2o_100sh.png");
            imageList.add("http://img.zcool" +
                    ".cn/community/01bef15568672b0000012716336dd4.jpg@1280w_1l_2o_100sh.jpg");
            imageList.add("http://img.zcool" +
                    ".cn/community/010a1b554c01d1000001bf72a68b37.jpg@1280w_1l_2o_100sh.png");
            list.add(R.drawable.zizhitongjian);
            list.add(R.drawable.qianjiehoujie);
            list.add(R.drawable.mingjiapianduan);
            mBanner.setImages(list)
                    .setDelayTime(2000)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            BannerData data = new BannerData();
                            Intent intent = new Intent();
                            intent.setClass(itemView.getContext(), BookMenuActivity.class);

                            String keycode = "";
                            String title = "";
                            String pic_name = "";
                            String book_id = "";
                            String price = "";
                            switch (position) {
                                case 0:
                                    pic_name = data.getZZ().img;
                                    keycode = data.getZZ().keycode;
                                    title = data.getZZ().title;
                                    book_id = data.getZZ().book_id;
                                    price = data.getZZ().price;
                                    break;
                                case 1:
                                    pic_name = data.getQj().img;
                                    keycode = data.getQj().keycode;
                                    title = data.getQj().title;
                                    book_id = data.getQj().book_id;
                                    price = data.getQj().price;
                                    break;
                                case 2:
                                    pic_name = data.getPj().img;
                                    keycode = data.getPj().keycode;
                                    title = data.getPj().title;
                                    book_id = data.getPj().book_id;
                                    price = data.getPj().price;
                                    break;
                                default:
                                    break;
                            }

                            intent.putExtra("keycode", keycode);
                            intent.putExtra("title_name", title);
                            intent.putExtra("pic_name", pic_name);
                            intent.putExtra("book_id", book_id);
                            intent.putExtra("price", price);
                            itemView.getContext().startActivity(intent);
                        }
                    })
                    .start();
        }
    }

    /**
     * 顶部横向栏目图
     */
    static class HorizontalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView mRecyclerView;

        public HorizontalViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.main_list_top_list);
            LinearLayoutManager manager = new LinearLayoutManager(itemView.getContext());
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView.setLayoutManager(manager);
        }
    }

    /**
     * 具体内容holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;//标题
        TextView loadMore;//标题
        RecyclerView mRecyclerView; // 父层的 RecyclerView

        public ViewHolder(final View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.main_list_title);
            loadMore = itemView.findViewById(R.id.main_list_more);

            mRecyclerView = itemView.findViewById(R.id.menu_info_recyclerview);

            RecyclerView.LayoutManager manager = new GridLayoutManager(itemView.getContext(), 3);
            // 需要注意的是GridLayoutManager要设置setAutoMeasureEnabled(true)成自适应高度
            manager.setAutoMeasureEnabled(true);
            mRecyclerView.setLayoutManager(manager);
        }
    }
}