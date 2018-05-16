package com.glwz.bookassociation.ui.Entity;

import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/10.
 */

public class BuyBookListBean extends BaseBean {


    private List<BookListBean> bookList;

    public List<BookListBean> getBookList() {
        return bookList;
    }

    public void setBookList(List<BookListBean> bookList) {
        this.bookList = bookList;
    }

    public static class BookListBean {
        /**
         * id : 15
         * keycode : http://student.hebeijiaoyu.com.cn/glwz/web/interface/catalog/?key=qianjiehoujie
         * qrcode : /upload/qrcode/15066646154538.jpg
         * title : 前街后街
         */

        private String id;
        private String keycode;
        private String qrcode;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeycode() {
            return keycode;
        }

        public void setKeycode(String keycode) {
            this.keycode = keycode;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
