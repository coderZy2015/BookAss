package com.glwz.bookassociation.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/3.
 */

public class SharePreferenceUtil {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharePreferenceUtil(Context context, String file) {
        sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 清除SharePreferenceUtil数据
     */
    public void clear() {
        editor.clear().commit();
    }

    public void setImgData(String id, String img){
        editor.putString(id, img);
        editor.commit();
    }

    public String getImgData(String id){
        return sp.getString(id, "");
    }

    // 登录信息
    public void setUserName(String userName) {
        editor.putString("userName", userName);
        editor.commit();
    }

    public String getUserName() {
        return sp.getString("userName", "");
    }

    // 登录信息
    public void setPassword(String password) {
        editor.putString("password", password);
        editor.commit();
    }

    public String getPassword() {
        return sp.getString("password", "");
    }
}
