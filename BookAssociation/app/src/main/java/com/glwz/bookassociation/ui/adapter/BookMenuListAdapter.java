package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BookMenuInfo;
import com.glwz.bookassociation.ui.activity.BookMenuActivity;
import com.glwz.bookassociation.ui.utils.Utils;

import java.util.List;

/**
 * 菜单页面 标题内容adapter
 * Created by zy on 2018/4/24.
 */

public class BookMenuListAdapter extends RecyclerView.Adapter {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;

    private Context context;
    private List<BookMenuInfo.MessageBean.CatalogBean> mList; // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    private int headViewNum = 1;
    private int selectItem = -1;
    private String pic_name;
    private String name;
    private String price;
    private boolean isShowPrice = false;

    /**
     * 默认关闭图书权限
     */
    private boolean isOpenAllBook = false;

    public BookMenuListAdapter(Context context, List<BookMenuInfo.MessageBean.CatalogBean> _mlist,String _name, String _pic_name, String _price) {
        this.context = context;
        mList = _mlist;
        name = _name;
        pic_name = _pic_name;
        price = _price;
    }

    public void setSelectedItem(int item){
        selectItem = item;
        notifyDataSetChanged();
    }

    public void setPriceAndButtonShow(boolean _show){
        isShowPrice = _show;
    }

    //显示播放和锁
    public void setOpenAllBook(boolean open){
        isOpenAllBook = open;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.header_menu_book_data,
                    parent, false);
            return new HeadViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_book_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof HeadViewHolder){
            HeadViewHolder hViewHolder = (HeadViewHolder)holder;
            Glide.with(context).load(pic_name).into(hViewHolder.book);
            hViewHolder.book_name.setText(""+name);
            hViewHolder.btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof BookMenuActivity)
                    ((BookMenuActivity)context).showMakeSureDialog();
                }
            });
            if (isShowPrice){
                float pr = Integer.parseInt(price);
                hViewHolder.text_price.setText("￥ "+pr/100);
                hViewHolder.btn_buy.setVisibility(View.VISIBLE);
            }else{
                hViewHolder.text_price.setText("");
                hViewHolder.btn_buy.setVisibility(View.GONE);
            }
        }

        if(holder instanceof ViewHolder){
            ViewHolder vHolder = (ViewHolder)holder;
            vHolder.textTitle.setText(mList.get(position - headViewNum).getName());

            //隐藏其他item的menu
            if(position>1){
                vHolder.layout.setVisibility(View.GONE);
            }else{
                vHolder.layout.setVisibility(View.VISIBLE);
            }

            //isOpenAllBook 没有打开  关闭5条以后的收听
            if(position >5 && !isOpenAllBook){
                vHolder.btn_play.setImageResource(R.drawable.book_menu_close);
            }else{
                vHolder.btn_play.setImageResource(R.drawable.book_menu_play);
            }
            //vHolder.btn_play.setImageResource(R.drawable.book_menu_play);

            if(selectItem == position - 1){
                vHolder.textTitle.setTextColor(context.getResources().getColor(R.color.seek_bar_select));
                vHolder.btn_play.setImageResource(R.drawable.book_menu_pause);
            }else{
                vHolder.textTitle.setTextColor(context.getResources().getColor(R.color.black));
            }

            /**
             * item监听
             */
            if( mOnItemClickListener!= null){
                vHolder.itemView.setOnClickListener( new View.OnClickListener() {
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
        if(position == TYPE_HEADER) return TYPE_HEADER;
        return TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mList.size()+headViewNum;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * static ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        LinearLayout layout;
        ImageView btn_play;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.hide_show_menu_item);
            textTitle = itemView.findViewById(R.id.book_menu_item_title);
            btn_play = itemView.findViewById(R.id.book_menu_item_play);
        }
    }

    static class HeadViewHolder extends RecyclerView.ViewHolder{
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
