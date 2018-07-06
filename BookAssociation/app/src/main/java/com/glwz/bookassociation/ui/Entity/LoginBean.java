package com.glwz.bookassociation.ui.Entity;

/**
 * 说明：
 * 首页主界面
 * Created by zy on 2018/5/2.
 */

public class LoginBean extends BaseBean {


    /**
     * mark : 0
     * message : 登录成功！
     * userInfo : {"username":"15833113069","grade":null,"email":null,"phone":"15833113069",
     * "nickName":null,"id":"651526"}
     */

    private String mark;
    private String message;
    private UserInfoBean userInfo;

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

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        /**
         * username : 15833113069
         * grade : null
         * email : null
         * phone : 15833113069
         * nickName : null
         * id : 651526
         */

        private String username;
        private Object grade;
        private Object email;
        private String phone;
        private Object nickName;
        private String id;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Object getGrade() {
            return grade;
        }

        public void setGrade(Object grade) {
            this.grade = grade;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getNickName() {
            return nickName;
        }

        public void setNickName(Object nickName) {
            this.nickName = nickName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
