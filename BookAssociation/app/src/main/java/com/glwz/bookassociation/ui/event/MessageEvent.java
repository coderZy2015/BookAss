package com.glwz.bookassociation.ui.event;

import com.glwz.bookassociation.ui.Entity.BuyBookListBean;

import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/13.
 */

public class MessageEvent {
    private String message;
    private List<BuyBookListBean.BookListBean> bookList;

    public MessageEvent(String message, List<BuyBookListBean.BookListBean> list) {
        this.message = message;
        this.bookList = list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BuyBookListBean.BookListBean> getList(){
        return bookList;
    }

}
