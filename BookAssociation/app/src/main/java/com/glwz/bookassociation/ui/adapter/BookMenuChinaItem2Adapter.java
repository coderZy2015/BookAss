package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 三字经 栏
 * Created by zy on 2018/5/14.
 */

public class BookMenuChinaItem2Adapter extends RecyclerView.Adapter<BookMenuChinaItemAdapter.ViewHolder> {

    private Context context;
    private List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean> mList = new ArrayList<>(); // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    /**
     * 构造函数
     * @param context
     * @param list
     */
    public BookMenuChinaItem2Adapter(Context context, List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public BookMenuChinaItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_china_menu_book_data_3, parent, false);
        return new BookMenuChinaItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookMenuChinaItemAdapter.ViewHolder holder, final int position) {

        holder.textInfo.setText(""+mList.get(position).getName());

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

        TextView textInfo;
        LinearLayout layout;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.hide_show_menu_item);
            textInfo = (TextView) itemView.findViewById(R.id.item_select);
            recyclerView = itemView.findViewById(R.id.book_menu_china_recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            recyclerView.setLayoutManager(layoutManager);
        }
    }

}