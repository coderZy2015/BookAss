package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 *
 * Created by zy on 2018/5/15.
 */

public class BookMenuChinaContentAdapter extends RecyclerView.Adapter<BookMenuChinaContentAdapter.ViewHolder> {

    private Context context;
    private List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean.ChildBeanZ> mList = new ArrayList<>(); // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    private int selectItem = -1;

    /**
     * 默认关闭图书权限
     */
    private boolean isOpenAllBook = false;

    /**
     * 构造函数
     * @param context
     * @param list
     */
    public BookMenuChinaContentAdapter(Context context, List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean.ChildBeanZ> list) {
        this.context = context;
        this.mList = list;
    }

    public void setSelectedItem(int item){
        selectItem = item;
        notifyDataSetChanged();
    }

    //显示播放和锁
    public void setOpenAllBook(boolean open){
        isOpenAllBook = open;
    }

    @Override
    public BookMenuChinaContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_china_book_data, parent, false);
        return new BookMenuChinaContentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookMenuChinaContentAdapter.ViewHolder holder, final int position) {

        holder.textTitle.setText(mList.get(position).getName());

        //isOpenAllBook 没有打开  关闭5条以后的收听
        if(position >4 && !isOpenAllBook){
            holder.btn_play.setImageResource(R.drawable.book_menu_close);
        }else{
            holder.btn_play.setImageResource(R.drawable.book_menu_play);
        }
        //vHolder.btn_play.setImageResource(R.drawable.book_menu_play);

        if(selectItem == position){
            holder.textTitle.setTextColor(context.getResources().getColor(R.color.seek_bar_select));
            holder.btn_play.setImageResource(R.drawable.book_menu_pause);
        }else{
            holder.textTitle.setTextColor(context.getResources().getColor(R.color.black));
        }

        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0: mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * static ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        ImageView btn_play;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.book_menu_item_title);
            btn_play = itemView.findViewById(R.id.book_menu_item_play);
        }
    }

}