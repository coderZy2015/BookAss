package com.glwz.bookassociation.ui.event;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/3.
 */

public class EventBusMessageModel {
    private String type;
    private int state;
    private Object passValue;

    public EventBusMessageModel(){
    }

    public EventBusMessageModel(String type, Object passValue) {
        this.type = type;
        this.passValue = passValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getPassValue() {
        return passValue;
    }

    public void setPassValue(Object passValue) {
        this.passValue = passValue;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
