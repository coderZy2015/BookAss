package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;
import com.glwz.bookassociation.ui.activity.BookMenuChinaActivity;
import com.glwz.bookassociation.ui.activity.LoginActivity;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;
import com.glwz.bookassociation.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 蒙学
 * Created by zy on 2018/4/24.
 */

public class BookMenuChinaAdapter extends RecyclerView.Adapter {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;

    private Context context;

    private OnItemClickListener mOnItemClickListener;
    private int headViewNum = 1;
    private String pic_name;
    private String name;
    private String price;
    private String bookId;
    private boolean isShowPrice = false;
    SharePreferenceUtil sharePreferenceUtil;

    private List<BookMenuChinaBean.MessageBean.CatalogBean> mList = new ArrayList<>();

    public BookMenuChinaAdapter(Context context, List<BookMenuChinaBean.MessageBean.CatalogBean>
            _mlist, String _name, String _pic_name, String _price, String _id) {
        this.context = context;
        mList = _mlist;
        name = _name;
        pic_name = _pic_name;
        price = _price;
        bookId = _id;
        sharePreferenceUtil = new SharePreferenceUtil(context, MyData.SAVE_USER);
    }

    public void setPriceAndButtonShow(boolean _show) {
        isShowPrice = _show;
    }

    /*
     * 检查登录名是否存在
     */
    public boolean checkUserName() {
        if (sharePreferenceUtil.getUserName().equals("")) {
            Intent intent = new Intent();
            intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.header_menu_book_data,
                    parent, false);
            return new HeadViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_china_menu_book_data,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeadViewHolder) {
            HeadViewHolder hViewHolder = (HeadViewHolder) holder;
            Glide.with(context).load(pic_name).into(hViewHolder.book);
            hViewHolder.book_name.setText("" + name);
            hViewHolder.btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkUserName()){
                        if (context instanceof BookMenuChinaActivity)
                            ((BookMenuChinaActivity) context).showMakeSureDialog();
                    }

                }
            });
            if (isShowPrice) {
                float pr = Integer.parseInt(price);
                hViewHolder.text_price.setText("￥ " + pr / 100);
                hViewHolder.btn_buy.setVisibility(View.VISIBLE);
            } else {
                hViewHolder.text_price.setText("");
                hViewHolder.btn_buy.setVisibility(View.GONE);
            }
        }

        if (holder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) holder;

            vHolder.textTitle.setText("" + mList.get(position - headViewNum).getName());
            vHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("zy_code", "layout  position = " + position);
                }
            });

            if (vHolder.recyclerView.getAdapter() == null) {
                BookMenuChinaItemAdapter adapter = new BookMenuChinaItemAdapter(context, mList
                        .get(position - headViewNum).getChild(), name, pic_name, price, bookId);
                vHolder.recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Log.i("zy_code", "adapter  position = " + position);
                    }

                    @Override
                    public void onLongClick(int position) {
                    }
                });
            } else {
                vHolder.recyclerView.getAdapter().notifyDataSetChanged();
            }

            /**
             * item监听
             */
            if (mOnItemClickListener != null) {
                vHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onClick(position - headViewNum);
                    }
                });
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == TYPE_HEADER)
            return TYPE_HEADER;
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mList.size() + headViewNum;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * static ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        LinearLayout layout;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.hide_show_menu_item);
            textTitle = itemView.findViewById(R.id.china_item_text);
            recyclerView = itemView.findViewById(R.id.book_menu_china_recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            recyclerView.setLayoutManager(layoutManager);

        }
    }

    static class HeadViewHolder extends RecyclerView.ViewHolder {
        ImageView bg;
        ImageView book;
        TextView book_name;
        Button btn_buy;
        TextView text_price;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public HeadViewHolder(final View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.menu_book_data_header_bg);
            book = itemView.findViewById(R.id.menu_book_data_header_book);
            book_name = itemView.findViewById(R.id.menu_book_data_header_name);
            text_price = itemView.findViewById(R.id.menu_book_data_header_price);
            btn_buy = itemView.findViewById(R.id.menu_book_data_header_buy);

            Bitmap bm = Utils.createBitmap(itemView.getContext(), R.drawable.top_bg);
            Drawable drawable = new BitmapDrawable(bm);
            bg.setBackground(drawable);
        }

    }


}
