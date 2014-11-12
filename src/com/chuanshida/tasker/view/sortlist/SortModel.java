package com.chuanshida.tasker.view.sortlist;

import com.chuanshida.tasker.bean.User;

public class SortModel {

    private User user;
    private String sortLetters; // 显示数据拼音的首字母

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
