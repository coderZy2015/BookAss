package com.glwz.bookassociation.Net;

import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.glwz.bookassociation.Interface.HttpAPICallBack;
import com.glwz.bookassociation.ui.Entity.BookContentBean;
import com.glwz.bookassociation.ui.Entity.BookIsBuyBean;
import com.glwz.bookassociation.ui.Entity.BookMenuChinaBean;
import com.glwz.bookassociation.ui.Entity.BookMenuInfo;
import com.glwz.bookassociation.ui.Entity.BookSearchBean;
import com.glwz.bookassociation.ui.Entity.BookTypeListBean;
import com.glwz.bookassociation.ui.Entity.BuyBookListBean;
import com.glwz.bookassociation.ui.Entity.GetPreOrderBean;
import com.glwz.bookassociation.ui.Entity.LoginBean;
import com.glwz.bookassociation.ui.Entity.MainBookListBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

/**
 * Created by zy on 2018/4/23.
 */

public class HomeAPI {

    public static final int NET_Test = -1;
    public static final int NET_Login = 0;
    public static final int NET_sendMessage = 1;
    public static final int NET_register = 2;
    public static final int NET_changePassword = 3;
    public static final int NET_getBookInfo = 4;
    public static final int NET_getMainBookList = 5;
    public static final int NET_getBookTypeList = 6;
    public static final int NET_getBookContent = 7;
    public static final int NET_getSearchBook = 8;
    public static final int NET_getBookChinaInfo = 9;
    public static final int NET_GetPreOrder = 10;
    public static final int NET_BookIsBuy = 11;
    public static final int NET_BuyBookList = 12;

    /**
     * 登录接口   密码需要使用md5加密拼接
     */
    public static void Login(final HttpAPICallBack callBack, String url, String name, String
            password) {

        String sUrl = url + name + "/" + MD5Util.encrypt(password);
        OkGo.<LoginBean>get(sUrl)
                .execute(new JsonCallback<LoginBean>(LoginBean.class) {
                    @Override
                    public void onError(Response<LoginBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<LoginBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_Login, response.body());
                    }
                });

    }


    public static void Test(final HttpAPICallBack callBack) {

        String sUrl = "http://student.hebeijiaoyu.com" +
                ".cn/glwz/gltsApp/orderNotify?payTime=20130307212800&notifyTime" +
                "=20130307212803&resultCode=1&cm=4444&msisdn=13777572818&productId=192647" +
                "&type=2&sign=123456";
        OkGo.<LoginBean>get(sUrl)
                .execute(new JsonCallback<LoginBean>(LoginBean.class) {
                    @Override
                    public void onError(Response<LoginBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<LoginBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_Test, response.body());
                    }
                });

    }


    /**
     * 发送短信验证码接口
     */
    public static void sendMessage(final HttpAPICallBack callBack, String phone) {
        String sUrl = HttpUrl.SendMessage_Url + phone;
        OkGo.<LoginBean>get(sUrl)
                .execute(new JsonCallback<LoginBean>(LoginBean.class) {
                    @Override
                    public void onError(Response<LoginBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<LoginBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_sendMessage, response.body());
                    }
                });

    }

    /**
     * 注册接口
     */
    public static void register(final HttpAPICallBack callBack, String phone, String password1,
                                String yzm) {

        OkGo.<LoginBean>get(HttpUrl.Register_Url)
                .params("phone", phone)
                .params("password1", password1)
                .params("yzm", yzm)
                .execute(new JsonCallback<LoginBean>(LoginBean.class) {
                    @Override
                    public void onError(Response<LoginBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<LoginBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_register, response.body());
                    }
                });

    }

    /**
     * 修改密码接口
     */
    public static void changePassword(final HttpAPICallBack callBack, String username, String
            password, String yzm) {

        OkGo.<LoginBean>post(HttpUrl.ChangePassword_Url)
                .params("username", username)
                .params("yzm", yzm)
                .params("password1", password)
                .params("password2", password)
                .isMultipart(true)
                .execute(new JsonCallback<LoginBean>(LoginBean.class) {
                    @Override
                    public void onError(Response<LoginBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<LoginBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_changePassword, response.body());
                    }
                });
    }

    /**
     * 获取书籍信息
     */
    public static void getBookInfo(final HttpAPICallBack callBack, String url) {
        OkGo.<BookMenuInfo>get(url)
                .execute(new JsonCallback<BookMenuInfo>(BookMenuInfo.class) {
                    @Override
                    public void onError(Response<BookMenuInfo> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<BookMenuInfo> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_getBookInfo, response.body());
                    }
                });

    }

    /**
     * 获取经典中国书籍信息
     */
    public static void getBookChinaInfo(final HttpAPICallBack callBack, String url) {
        OkGo.<BookMenuChinaBean>get(url)
                .execute(new JsonCallback<BookMenuChinaBean>(BookMenuChinaBean.class) {
                    @Override
                    public void onError(Response<BookMenuChinaBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<BookMenuChinaBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_getBookChinaInfo, response.body());
                    }
                });

    }

    /**
     * 获取首页书籍列表
     */
    public static void getMainBookList(final HttpAPICallBack callBack, String url) {
        OkGo.<MainBookListBean>get(url)
                .execute(new JsonCallback<MainBookListBean>(MainBookListBean.class) {
                    @Override
                    public void onError(Response<MainBookListBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<MainBookListBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_getMainBookList, response.body());
                    }
                });
    }

    /**
     * 获取具体分类的书籍列表
     *
     * @param cate_id 分类ID
     */
    public static void getBookTypeList(final HttpAPICallBack callBack, String url, String cate_id) {
        OkGo.<BookTypeListBean>get(url)
                .params("cate_id", cate_id)
                .params("page", "1")
                .execute(new JsonCallback<BookTypeListBean>(BookTypeListBean.class) {
                    @Override
                    public void onError(Response<BookTypeListBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<BookTypeListBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_getBookTypeList, response.body());
                    }
                });
    }

    /**
     * 获取书籍具体内容
     *
     * @param tsgId 分类ID
     */
    public static void getBookContent(final HttpAPICallBack callBack, String url, String tsgId) {
        url = url + tsgId;
        Log.i("zy_code", "url = " + url);
        OkGo.<BookContentBean>get(url)
                .execute(new JsonCallback<BookContentBean>(BookContentBean.class) {
                    @Override
                    public void onError(Response<BookContentBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<BookContentBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_getBookContent, response.body());
                    }
                });
    }


    /**
     * 获取搜索书籍列表
     */
    public static void getSearchBook(final HttpAPICallBack callBack, String url, String name) {
        String Surl = url + name;
        OkGo.<BookSearchBean>get(Surl)
                .execute(new JsonCallback<BookSearchBean>(BookSearchBean.class) {
                    @Override
                    public void onError(Response<BookSearchBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<BookSearchBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_getSearchBook, response.body());
                    }
                });
    }

    /**
     * 获取微信支付 pre_id接口
     */
    public static void createOrder(final HttpAPICallBack callBack, String username, String id) {
        String url = HttpUrl.CreateOrder_Url;
        HttpParams params = new HttpParams();
        params.put("username", username);
        params.put("id", id);
        params.put("spbill_create_ip", "" + NetworkUtils.getIPAddress(true));


        OkGo.<GetPreOrderBean>post(url)
                .params(params)
                .isMultipart(true)
                .execute(new JsonCallback<GetPreOrderBean>(GetPreOrderBean.class) {
                    @Override
                    public void onError(Response<GetPreOrderBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<GetPreOrderBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_GetPreOrder, response.body());
                    }
                });
    }

    /**
     * 获取购买书籍列表
     */
    public static void GetBuyBookList(final HttpAPICallBack callBack, String username) {

        OkGo.<BuyBookListBean>get(HttpUrl.GetBuyBookList_Url)
                .params("username",username)
                .params("state", "1")
                .execute(new JsonCallback<BuyBookListBean>(BuyBookListBean.class) {
                    @Override
                    public void onError(Response<BuyBookListBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<BuyBookListBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_BuyBookList, response.body());
                    }
                });
    }

    /**
     * 获取书籍是否购买
     */
    public static void GetBookIsBuy(final HttpAPICallBack callBack,String id, String username) {

        OkGo.<BookIsBuyBean>get(HttpUrl.GetBookIsBuy_Url)
                .params("resourceId", id)
                .params("username",username)
                .execute(new JsonCallback<BookIsBuyBean>(BookIsBuyBean.class) {
                    @Override
                    public void onError(Response<BookIsBuyBean> response) {
                        super.onError(response);
                        callBack.onError(response.body());
                    }

                    @Override
                    public void onSuccess(Response<BookIsBuyBean> response) {
                        super.onSuccess(response);
                        callBack.onSuccess(NET_BookIsBuy, response.body());
                    }
                });
    }

}
