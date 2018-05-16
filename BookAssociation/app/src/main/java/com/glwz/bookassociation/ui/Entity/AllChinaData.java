package com.glwz.bookassociation.ui.Entity;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/7.
 */

public class AllChinaData {
    private static AllChinaData _data;
    private BookMenuChinaBean bookBean;
    public AllChinaData(){}

    public static AllChinaData getInstance(){
        if(_data == null){
            _data = new AllChinaData();
        }
        return _data;
    }

    public void setBookMenuChinaBean(BookMenuChinaBean bean){
        bookBean = bean;
    }

    public BookMenuChinaBean getBookMenuChinaBean(){
        return bookBean;
    }
}
