package com.glwz.bookassociation.ui.event;

import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;

import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/15.
 */

public class BookChinaEvent {
    String name;
    String pic_name;
    String price;
    String book_id;
    List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean.ChildBeanZ> object;

    public List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean.ChildBeanZ> getObject() {
        return object;
    }

    public void setObject(List<BookMenuChinaBean.MessageBean.CatalogBean.ChildBeanX.ChildBean.ChildBeanZ> object) {
        this.object = object;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getPic_name() {
        return pic_name;
    }

    public void setPic_name(String pic_name) {
        this.pic_name = pic_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
