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
import com.glwz.bookassociation.ui.Entity.ChildInfo;

import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/4/27.
 */

public class SeeBookAdapter extends RecyclerView.Adapter<SeeBookAdapter.ViewHolder> {

    private Context context;
    private List<ChildInfo> list; // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    /**
     * 显示全部数据
     */
    private boolean isShowAll = false;
    /**
     * 构造函数
     * @param context
     * @param list
     */
    public SeeBookAdapter(Context context, List<ChildInfo> list, boolean _isShowAll) {
        this.context = context;
        this.list = list;
        this.isShowAll = _isShowAll;
    }

    @Override
    public SeeBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_book_data, parent, false);
        return new SeeBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeeBookAdapter.ViewHolder holder, final int position) {
        ChildInfo info = list.get(position);
        holder.imageInfo.setImageResource(info.getIconImgID());
        holder.textInfo.setText(info.getMenuName());
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
        if (isShowAll){
            return list == null ? 0: list.size();
        }
        return 3;
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
