package com.chuanshida.tasker.bean;

public class User {

    public static final int LOGIN_TYPE_WEIBO = 1;
    public static final int LOGIN_TYPE_TENCENT_QQ = 2;

    private String objectId;

    private String userName;

    private String passWord;

    private boolean sex;

    private String avatar;

    private String othername;

    private Integer logintype;

    public User() {

    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        User u = (User) o;
        if (u != null && u.getObjectId() != null && getObjectId() != null
                && u.getObjectId().equals(this.getObjectId())) {
            return true;
        } else {
            return false;
        }
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public Integer getLogintype() {
        if (logintype == null) {
            return 0;
        }
        return logintype;
    }

    public void setLogintype(Integer logintype) {
        this.logintype = logintype;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String setUsername() {
        if (logintype != null
                && (logintype == LOGIN_TYPE_WEIBO || logintype == LOGIN_TYPE_TENCENT_QQ)) {
            return this.othername;
        } else {
            return userName;
        }
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return passWord;
    }

    public void setPassword(String passWord) {
        this.passWord = passWord;
    }

}
