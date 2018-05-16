package com.glwz.bookassociation.ui.Entity;

import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/4/26.
 */

public class MainBookListBean extends BaseBean{

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String cat_id;
        private String cat_title;
        private String logoimg;
        private List<BooksBean> books;

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getCat_title() {
            return cat_title;
        }

        public void setCat_title(String cat_title) {
            this.cat_title = cat_title;
        }

        public String getLogoimg() {
            return logoimg;
        }

        public void setLogoimg(String logoimg) {
            this.logoimg = logoimg;
        }

        public List<BooksBean> getBooks() {
            return books;
        }

        public void setBooks(List<BooksBean> books) {
            this.books = books;
        }

        public static class BooksBean {
            /**
             * book_id : 27
             * cate_id : 7
             * id : 27
             * title : 王厨娘的烟火人生
             * img : https://glbook.hebeijiaoyu.com.cn/upload/2017-10-15/59e308078d120.jpg
             * topimg : 0,197
             * price : 1980
             * keycode : http://student.hebeijiaoyu.com.cn/glwz/web/interface/catalog/?key=wcndyhrs
             * describe :
             * salenum_online : 2
             * salenum_line : 1
             * act_code_num : 2000
             * qrcode : /upload/qrcode/15066684143326.jpg
             * ctime : 1506668414
             * cat_title : 冀版精品
             * cat_id : 7
             * logoimg : 0,295
             * sort : 0
             */

            private String book_id;
            private String cate_id;
            private String id;
            private String title;
            private String img;
            private String topimg;
            private String price;
            private String keycode;
            private String describe;
            private String salenum_online;
            private String salenum_line;
            private String act_code_num;
            private String qrcode;
            private String ctime;
            private String cat_title;
            private String cat_id;
            private String logoimg;
            private String sort;

            public String getBook_id() {
                return book_id;
            }

            public void setBook_id(String book_id) {
                this.book_id = book_id;
            }

            public String getCate_id() {
                return cate_id;
            }

            public void setCate_id(String cate_id) {
                this.cate_id = cate_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getTopimg() {
                return topimg;
            }

            public void setTopimg(String topimg) {
                this.topimg = topimg;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getKeycode() {
                return keycode;
            }

            public void setKeycode(String keycode) {
                this.keycode = keycode;
            }

            public String getDescribe() {
                return describe;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }

            public String getSalenum_online() {
                return salenum_online;
            }

            public void setSalenum_online(String salenum_online) {
                this.salenum_online = salenum_online;
            }

            public String getSalenum_line() {
                return salenum_line;
            }

            public void setSalenum_line(String salenum_line) {
                this.salenum_line = salenum_line;
            }

            public String getAct_code_num() {
                return act_code_num;
            }

            public void setAct_code_num(String act_code_num) {
                this.act_code_num = act_code_num;
            }

            public String getQrcode() {
                return qrcode;
            }

            public void setQrcode(String qrcode) {
                this.qrcode = qrcode;
            }

            public String getCtime() {
                return ctime;
            }

            public void setCtime(String ctime) {
                this.ctime = ctime;
            }

            public String getCat_title() {
                return cat_title;
            }

            public void setCat_title(String cat_title) {
                this.cat_title = cat_title;
            }

            public String getCat_id() {
                return cat_id;
            }

            public void setCat_id(String cat_id) {
                this.cat_id = cat_id;
            }

            public String getLogoimg() {
                return logoimg;
            }

            public void setLogoimg(String logoimg) {
                this.logoimg = logoimg;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }
        }
    }

}
