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
import com.glwz.bookassociation.MyData;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.MainBookListBean;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;

import java.util.List;


/**
 * 内部的RecyclerView
 * 内容为：
 * 首页主界面list的二级adapter
 * Created by zt on 2018/4/25.
 */

public class MainDataChildItemAdapter extends RecyclerView.Adapter<MainDataChildItemAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private List<MainBookListBean.DataBean.BooksBean> list;
    private SharePreferenceUtil sp;
    /**
     * 构造函数
     * @param context
     * @param list
     */
    public MainDataChildItemAdapter(Context context, List<MainBookListBean.DataBean.BooksBean> list) {
        this.context = context;
        this.list = list;
        sp = new SharePreferenceUtil(context, MyData.SAVE_USER);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImg()).into(holder.imageInfo);
        //存储图片
        sp.setImgData(list.get(position).getId(), list.get(position).getImg());

        holder.textInfo.setText(list.get(position).getTitle());
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
        if (list == null)
            return 0;
        if (list.size() >3)
            return 3;

        return list.size();
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
