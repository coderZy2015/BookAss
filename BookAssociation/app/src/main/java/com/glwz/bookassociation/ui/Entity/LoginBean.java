package com.glwz.bookassociation.ui.Entity;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/2.
 */

public class LoginBean extends BaseBean {

    /**
     * mark : 1
     * message : 用户名密码错误！
     * userInfo : null
     */

    private String mark;
    private String message;
    private Object userInfo;

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

    public Object getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Object userInfo) {
        this.userInfo = userInfo;
    }
}
