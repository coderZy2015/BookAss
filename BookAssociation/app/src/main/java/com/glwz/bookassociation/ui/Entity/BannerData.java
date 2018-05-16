package com.glwz.bookassociation.ui.Entity;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/9.
 */

public class BannerData {
    public pingju pj;
    public qianjie qj;
    public Zizhi zz;
    public BannerData(){
        pj = new pingju();
        qj = new qianjie();
        zz = new Zizhi();
    }
    public Zizhi getZZ(){
        return zz;
    }
    public pingju getPj(){
        return pj;
    }
    public qianjie getQj(){
        return qj;
    }

    public class pingju{
        public String book_id = "70";
        public String title = "评剧名家名段";
        public String img = "https://glbook.hebeijiaoyu.com.cn/upload/2017-10-15/59e30ca744975.jpg";
        public String keycode = "http://student.hebeijiaoyu.com.cn/glwz/web/interface/catalog/?key=pingju";
        public String price = "1980";
        public String cat_title = "曲艺";
    }

    public class qianjie{
        public String book_id = "15";
        public String title = "前街后街";
        public String img = "https://glbook.hebeijiaoyu.com.cn/upload/2017-10-15/59e2f7e2f040a.jpg";
        public String keycode = "http://student.hebeijiaoyu.com.cn/glwz/web/interface/catalog/?key=qianjiehoujie";
        public String price = "1980";
        public String cat_title = "小说";
    }

    public class Zizhi{
        public String book_id = "32";
        public String title = "资治通鉴";
        public String img = "https://glbook.hebeijiaoyu.com.cn/upload/2017-10-15/59e3089ac279e.jpg";
        public String keycode = "http://student.hebeijiaoyu.com.cn/glwz/web/interface/catalog/2";
        public String price = "39800";
        public String cat_title = "国学";

    }
}
