package com.glwz.bookassociation.Net;

/**
 * Created by zy on 2018/4/23.
 */

public class HttpUrl {

    /**
     * 登录平台地址  拼接
     */
    public static final String Login_Url = "http://student.hebeijiaoyu.com.cn/glwz/user/weixin/userCheck/";
    /**
     * 注册平台地址  传参
     */
    public static final String Register_Url = "http://student.hebeijiaoyu.com" +
            ".cn/glwz/user/weixin/interface/quickRegister";
    /**
     * 修改密码地址  传参
     */
    public static final String ChangePassword_Url = "http://student.hebeijiaoyu.com" +
            ".cn/glwz/user/weixin/interface/changePassword";
    /**
     * 发送验证码地址  拼接
     */
    public static final String SendMessage_Url = "http://student.hebeijiaoyu.com" +
            ".cn/glwz/user/weixin/interface/sendMessage/";

    /**
     * 资源服务器地址
     */
    public static final String RES_URL = "http://resources.hebeijiaoyu.com.cn";

    /**
     * 获取首页图书列表  from小程序
     */
    public static final String MainBookList_Url = "https://glbook.hebeijiaoyu.com" +
            ".cn/Home/Index/getbooks.shtml";

    /**
     * 获取具体图书的列表  from小程序
     */
    public static final String BookTypeList_Url = "https://glbook.hebeijiaoyu.com" +
            ".cn/Home/Index/getbooklist.shtml";

    /**
     * 获取具体图书内容
     */
    public static final String BookContent_Url = "http://student.hebeijiaoyu.com" +
            ".cn/glwz/web/interface/view/2/";
    /**
     * 搜索书籍接口
     */
    public static final String BookSearch_Url = "https://glbook.hebeijiaoyu.com" +
            ".cn/Home/Index/getbooklist" +
            ".shtml?cate_id=seach&page=1&keytxt=";
    /**
     * 获取预支付订单
     */
    public static final String CreateOrder_Url = "http://student.hebeijiaoyu.com.cn/glwz/gltsApp/createOrder";

    /**
     * 根据用户名查询已经购买，或者已经过期的图书列表
     */
    public static final String GetBuyBookList_Url = "http://student.hebeijiaoyu.com.cn/glwz/gltsApp/bookList";

    /**
     * 查询资源是否购买
     */
    public static final String GetBookIsBuy_Url = "http://student.hebeijiaoyu.com.cn/glwz/gltsApp/isBuy";

    /**
     * 有优惠卷的个数
     */
    public static final String IsHaveCoupon = "http://student.hebeijiaoyu.com.cn/glwz/gltsApp/cardNum";


}
