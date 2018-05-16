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
import com.glwz.bookassociation.ui.Entity.MainBookListBean;

import java.util.List;

/**
 *  首页横向标题
 * Created by zy on 2018/4/25.
 */

public class MainDataTopBarItemAdapter extends RecyclerView.Adapter<MainDataTopBarItemAdapter.ViewHolder> {
    private List<MainBookListBean.DataBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;

   public MainDataTopBarItemAdapter(Context context, List<MainBookListBean.DataBean> _list){
       mContext = context;
       mList = _list;
       mLayoutInflater = LayoutInflater.from(context);
   }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_main_layout_data_topbar_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(""+mList.get(position).getCat_title());
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
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.main_list_top_title);
            mImageView = view.findViewById(R.id.main_list_top_image);
        }

    }
}
