package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BookSearchBean;

import java.util.ArrayList;

/**
 * 说明：
 * 搜索显示界面adapter
 * Created by zy on 2018/5/2.
 */

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BookSearchBean.DataBean> mList; // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    /**
     * 构造函数
     * @param context
     * @param list
     */
    public BookSearchAdapter(Context context, ArrayList<BookSearchBean.DataBean> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public BookSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_book_data, parent, false);
        return new BookSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookSearchAdapter.ViewHolder holder, final int position) {

        Glide.with(context).load(mList.get(position).getImg()).into(holder.imageInfo);
        holder.textInfo.setText(mList.get(position).getTitle());
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

        ImageView imageInfo;
        TextView textInfo;

        private OnItemClickListener mOnItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            imageInfo = (ImageView) itemView.findViewById(R.id.image_info);
            textInfo = (TextView) itemView.findViewById(R.id.text_info);
        }

    }


}