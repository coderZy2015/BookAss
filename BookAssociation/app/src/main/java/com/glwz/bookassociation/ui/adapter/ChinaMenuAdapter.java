package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;
import com.glwz.bookassociation.ui.utils.MyViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/4.
 */

public class ChinaMenuAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX> groupList;
    private List<List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean>> childList = new ArrayList<>();
    private LayoutInflater mInflater = null;

    public ChinaMenuAdapter(Context context, List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX> _list)
    {
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        groupList = _list;

    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    //返回每个二级列表的个数
    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    //返回一级列表的单个item（返回的是对象）
    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }
    //返回二级列表中的单个item（返回的是对象）
    @Override
    public Object getChild(int i, int i1) {
        return childList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.readingbookmenu_group, null);
            holder = new MyViewHolder();

            holder.china_group_name = (TextView) convertView//menu章节
                    .findViewById(R.id.china_group_name);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.china_group_name.setText("123");
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.readingbookmenu_item, null);
            holder = new MyViewHolder();

            holder.reading_menu_content = (TextView) convertView//menu章节
                    .findViewById(R.id.reading_menu_content);
            holder.reading_menu_play = (ImageView) convertView//menu页数
                    .findViewById(R.id.reading_menu_play);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.reading_menu_content.setText("");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

