package com.glwz.bookassociation.Interface;

import com.glwz.bookassociation.ui.Entity.BaseBean;

/**
 * Created by zy on 2018/4/23.
 */

public interface HttpAPICallBack {
    void onSuccess(int url_id, BaseBean response);
    void onError(BaseBean response);
}
