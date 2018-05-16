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
import com.glwz.bookassociation.ui.Entity.BuyBookListBean;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/4/27.
 */

public class BuyBookAdapter extends RecyclerView.Adapter<BuyBookAdapter.ViewHolder> {

    private Context context;
    private List<BuyBookListBean.BookListBean> mList  = new ArrayList<>(); // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    /**
     * 显示全部数据
     */
    private boolean isShowAll = false;

    SharePreferenceUtil sharePreferenceUtil;
    /**
     * 构造函数
     * @param context
     */
    public BuyBookAdapter(Context context, List<BuyBookListBean.BookListBean> bookList , boolean _isShowAll) {
        this.context = context;
        this.mList = bookList;
        this.isShowAll = _isShowAll;
        sharePreferenceUtil = new SharePreferenceUtil(context, MyData.SAVE_USER);
    }

    @Override
    public BuyBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_book_data, parent, false);
        return new BuyBookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BuyBookAdapter.ViewHolder holder, final int position) {

        Glide.with(context).load(sharePreferenceUtil.getImgData(mList.get(position).getId())).into( holder.imageInfo);
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
        if (isShowAll){
            return mList == null ? 0: mList.size();
        }

        if (mList.size() >= 3){
            return 3;
        }
        return mList.size();
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
