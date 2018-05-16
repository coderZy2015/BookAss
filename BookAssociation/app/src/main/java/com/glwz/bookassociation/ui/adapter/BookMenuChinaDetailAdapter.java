package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BookMenuInfo;
import com.glwz.bookassociation.ui.utils.Utils;

import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/7.
 */

public class BookMenuChinaDetailAdapter extends RecyclerView.Adapter {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_CONTENT = 1;

    private Context context;
    private List<BookMenuInfo.MessageBean.CatalogBean> mList; // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    private int headViewNum = 1;
    private int selectItem = -1;
    private String pic_name;
    private String name;
    /**
     * 默认关闭图书权限
     */
    private boolean isOpenAllBook = false;

    public BookMenuChinaDetailAdapter(Context context, List<BookMenuInfo.MessageBean.CatalogBean> _mlist, String _name, String _pic_name) {
        this.context = context;
        mList = _mlist;
        name = _name;
        pic_name = _pic_name;
    }

    public void setSelectedItem(int item){
        selectItem = item;
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.header_menu_book_data,
                    parent, false);
            return new BookMenuListAdapter.HeadViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_book_data, parent, false);
        return new BookMenuListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof BookMenuListAdapter.HeadViewHolder){
            BookMenuListAdapter.HeadViewHolder hViewHolder = (BookMenuListAdapter.HeadViewHolder)holder;
            Glide.with(context).load(pic_name).into(hViewHolder.book);
            hViewHolder.book_name.setText(""+name);
        }

        if(holder instanceof BookMenuListAdapter.ViewHolder){
            BookMenuListAdapter.ViewHolder vHolder = (BookMenuListAdapter.ViewHolder)holder;
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

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public HeadViewHolder(View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.menu_book_data_header_bg);
            book = itemView.findViewById(R.id.menu_book_data_header_book);
            book_name = itemView.findViewById(R.id.menu_book_data_header_name);

            Bitmap bm = Utils.createBitmap(itemView.getContext(), R.drawable.top_bg);
            bm = getBlackImage(bm);
            Drawable drawable = new BitmapDrawable(bm);
            bg.setBackground(drawable);
        }

        public Bitmap getBlackImage(Bitmap bm){
            Bitmap bmp = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),Bitmap.Config.RGB_565);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Canvas canvas = new Canvas(bmp);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bm,0,0,paint);
            canvas.drawColor(Color.parseColor("#40000000"));
            return bmp;

        }
    }


}