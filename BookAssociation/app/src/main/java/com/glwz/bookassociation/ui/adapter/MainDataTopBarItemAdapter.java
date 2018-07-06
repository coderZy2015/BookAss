package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.MainBookListBean;
import com.glwz.bookassociation.ui.activity.EnterBookActivity;
import com.glwz.bookassociation.ui.activity.MainBookMoreActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *  首页横向标题
 * Created by zy on 2018/4/25.
 */

public class MainDataTopBarItemAdapter extends RecyclerView.Adapter<MainDataTopBarItemAdapter.ViewHolder> {
    private List<MainBookListBean.DataBean> mList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;

   public MainDataTopBarItemAdapter(Context context, List<MainBookListBean.DataBean> _list){
       mContext = context;
       mList.addAll(_list);
       mLayoutInflater = LayoutInflater.from(context);
       updateData();
   }

   public void updateData(){
       MainBookListBean.DataBean bookBean = new MainBookListBean.DataBean();
       bookBean.setCat_title("冠林悦听会");
       bookBean.setCat_id("---");
       mList.add(0, bookBean);
       notifyDataSetChanged();
   }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_main_layout_data_topbar_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(""+mList.get(position).getCat_title());
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(mContext, MainBookMoreActivity.class);
                if (mList.get(position).getCat_id().equals("---")){
                    intent.setClass(mContext, EnterBookActivity.class);
                }
                intent.putExtra("cate_id", mList.get(position).getCat_id());
                intent.putExtra("title_name", mList.get(position).getCat_title());


                mContext.startActivity(intent);
            }
        });
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
