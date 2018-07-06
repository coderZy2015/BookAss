package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glwz.bookassociation.R;

import java.util.ArrayList;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/30.
 */

public class MyCouponAdapter extends RecyclerView.Adapter<MyCouponAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> mList = new ArrayList<>();

    public MyCouponAdapter(Context cc, ArrayList<String> _list){
        context = cc;
        mList = _list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);
        return new MyCouponAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Glide.with(context).load(mList.get(position).getImg()).into(holder.imageInfo);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0: mList.size();
    }

    /**
     * static ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
