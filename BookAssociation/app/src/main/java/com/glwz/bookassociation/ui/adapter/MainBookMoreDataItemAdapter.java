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
import com.glwz.bookassociation.ui.Entity.BookTypeListBean;
import com.glwz.bookassociation.ui.utils.SharePreferenceUtil;

import java.util.ArrayList;

/**
 * 说明：
 * 更多图书页的adapter
 * Created by zy on 2018/4/25.
 */

public class MainBookMoreDataItemAdapter extends RecyclerView.Adapter<MainBookMoreDataItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BookTypeListBean.DataBean> mList; // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 构造函数
     * @param context
     * @param list
     */
    public MainBookMoreDataItemAdapter(Context context, ArrayList<BookTypeListBean.DataBean> list) {
        this.context = context;
        this.mList = list;
        sharePreferenceUtil = new SharePreferenceUtil(context, MyData.SAVE_USER);
    }

    @Override
    public MainBookMoreDataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_book_data, parent, false);
        return new MainBookMoreDataItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainBookMoreDataItemAdapter.ViewHolder holder, final int position) {
        //存储图片
        sharePreferenceUtil.setImgData(mList.get(position).getId(), mList.get(position).getImg());

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
