package com.glwz.bookassociation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.glwz.bookassociation.Interface.OnItemClickListener;
import com.glwz.bookassociation.Net.HttpUrl;
import com.glwz.bookassociation.R;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;
import com.glwz.bookassociation.ui.Entity.BookMenuInfo;
import com.glwz.bookassociation.ui.activity.BookChinaMenuListActivity;
import com.glwz.bookassociation.ui.activity.BookPlayingSceneActivity;
import com.glwz.bookassociation.ui.event.BookChinaEvent;
import com.glwz.bookassociation.ui.utils.MediaPlayControl;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：
 * 精品诵读 名典举要 栏
 * Created by zy on 2018/5/14.
 */

public class BookMenuChinaItemAdapter extends RecyclerView.Adapter<BookMenuChinaItemAdapter
        .ViewHolder> {

    private Context context;
    private List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX> mList = new ArrayList<>();
    // List 集合（里面是image+text）
    private OnItemClickListener mOnItemClickListener;
    private boolean showItem = false;
    private String name = "";
    private String pic_name = "";
    private String book_id = "";
    private String price = "";

    //存入音频管理器的数据格式
    private ArrayList<BookMenuInfo.MessageBean.CatalogBean> dataList = new ArrayList<>();
    /**
     * 构造函数
     *
     * @param context
     * @param list
     */
    public BookMenuChinaItemAdapter(Context context, List<BookMenuChinaBean.MessageBean
            .CatalogBean.ChildBeanX> list, String _name, String _pic_name, String _price, String _id) {
        this.context = context;
        this.mList = list;
        name = _name;
        pic_name =_pic_name;
        price = _price;
        book_id = _id;
    }

    @Override
    public BookMenuChinaItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_china_menu_book_data_2,
                parent, false);
        return new BookMenuChinaItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookMenuChinaItemAdapter.ViewHolder holder, final int
            position) {

        holder.textInfo.setText("" + mList.get(position).getName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showItem) {
                    showItem = false;
                    holder.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    showItem = true;
                    holder.recyclerView.setVisibility(View.GONE);
                }
            }
        });

        if (holder.recyclerView.getAdapter() == null) {
            BookMenuChinaItem2Adapter adapter = new BookMenuChinaItem2Adapter(context, mList.get
                    (position).getChild());
            holder.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(int po) {
                    //检测包含子类   不包含直接进播放
                    if (mList.get(position).getChild().get(po).getChild().size() == 0) {
                        Intent intent = new Intent();
                        intent.setClass(context, BookPlayingSceneActivity.class);
                        intent.putExtra("tsgId", mList.get(position).getChild().get(po).getTsgId());
                        intent.putExtra("title_name", name);
                        intent.putExtra("book_id", book_id);
                        intent.putExtra("price", price);

                        dataList.clear();
                        //转换数据
                        BookMenuInfo.MessageBean.CatalogBean bean = new BookMenuInfo.MessageBean.CatalogBean();
                        bean.setFileUrl(mList.get(position).getChild().get(po).getFileUrl());
                        bean.setAuthor(mList.get(position).getChild().get(po).getAuthor());
                        bean.setContent(mList.get(position).getChild().get(po).getContent());
                        bean.setId(mList.get(position).getChild().get(po).getId());
                        bean.setAudioTime(mList.get(position).getChild().get(po).getAudioTime());
                        bean.setName(mList.get(position).getChild().get(po).getName());
                        bean.setNameSub(mList.get(position).getChild().get(po).getNameSub());
                        bean.setReader(mList.get(position).getChild().get(po).getReader());
                        bean.setTsgId(mList.get(position).getChild().get(po).getTsgId());

                        dataList.add(bean);
                        setMediaPlayData();

                        //同一首音乐  继续播放
                        String song_url = HttpUrl.RES_URL + mList.get(position).getChild().get(po).getFileUrl();
                        if (song_url.equals(MediaPlayControl.getInstance().play_url)) {
                        } else {
                            MediaPlayControl.getInstance().setPlayIndex(position);
                        }

                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(context, BookChinaMenuListActivity.class);

                        // 发布粘性事件
                        BookChinaEvent event = new BookChinaEvent();
                        event.setObject(mList.get(position).getChild().get(po).getChild());
                        event.setBook_id(book_id);
                        event.setPrice(price);
                        event.setPic_name(pic_name);
                        event.setName(name);
                        EventBus.getDefault().postSticky(event);

                        context.startActivity(intent);
                    }
                }

                @Override
                public void onLongClick(int position) {
                }
            });
        } else {
            holder.recyclerView.getAdapter().notifyDataSetChanged();
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    /*
     * 设置mediaPlay数据
     */
    public void setMediaPlayData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            list.add(HttpUrl.RES_URL + dataList.get(i).getFileUrl());
        }
        MediaPlayControl.getInstance().setSongList(list);
        MediaPlayControl.getInstance().setSongDataList(dataList);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : 1;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
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