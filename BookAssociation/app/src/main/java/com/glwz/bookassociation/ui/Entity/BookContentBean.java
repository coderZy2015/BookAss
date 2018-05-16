package com.glwz.bookassociation.ui.Entity;

import java.util.List;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/1.
 */

public class BookContentBean extends BaseBean {
    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        private String mark;
        private String message;
        private ViewBean view;

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ViewBean getView() {
            return view;
        }

        public void setView(ViewBean view) {
            this.view = view;
        }

        public static class ViewBean {
            /**
             * audioTime : 23'24
             * author : 周长青
             * child : []
             * content : 。
             * fileUrl : /resources/sxsb/lieyanzhuhun/02.mp3
             * id :
             * level :
             * max_introduce :
             * min_introduce :
             * name :
             * nameSub : 烈焰铸魂 第一章 2
             * parentId : 21404
             * reader : 小马哥
             * tsgId :
             * worksSrom : 冀版精品出版工程
             * yuanwen :
             * zhushi :
             */

            private String audioTime;
            private String author;
            private String content;
            private String fileUrl;
            private String id;
            private String level;
            private String max_introduce;
            private String min_introduce;
            private String name;
            private String nameSub;
            private String parentId;
            private String reader;
            private String tsgId;
            private String worksSrom;
            private String yuanwen;
            private String zhushi;
            private List<?> child;

            public String getAudioTime() {
                return audioTime;
            }

            public void setAudioTime(String audioTime) {
                this.audioTime = audioTime;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getFileUrl() {
                return fileUrl;
            }

            public void setFileUrl(String fileUrl) {
                this.fileUrl = fileUrl;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getMax_introduce() {
                return max_introduce;
            }

            public void setMax_introduce(String max_introduce) {
                this.max_introduce = max_introduce;
            }

            public String getMin_introduce() {
                return min_introduce;
            }

            public void setMin_introduce(String min_introduce) {
                this.min_introduce = min_introduce;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNameSub() {
                return nameSub;
            }

            public void setNameSub(String nameSub) {
                this.nameSub = nameSub;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getReader() {
                return reader;
            }

            public void setReader(String reader) {
                this.reader = reader;
            }

            public String getTsgId() {
                return tsgId;
            }

            public void setTsgId(String tsgId) {
                this.tsgId = tsgId;
            }

            public String getWorksSrom() {
                return worksSrom;
            }

            public void setWorksSrom(String worksSrom) {
                this.worksSrom = worksSrom;
            }

            public String getYuanwen() {
                return yuanwen;
            }

            public void setYuanwen(String yuanwen) {
                this.yuanwen = yuanwen;
            }

            public String getZhushi() {
                return zhushi;
            }

            public void setZhushi(String zhushi) {
                this.zhushi = zhushi;
            }

            public List<?> getChild() {
                return child;
            }

            public void setChild(List<?> child) {
                this.child = child;
            }
        }
    }
}
