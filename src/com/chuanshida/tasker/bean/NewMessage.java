package com.chuanshida.tasker.bean;

import java.io.Serializable;
import java.util.Date;

public class NewMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date messageDate;

    private String context;

    private User user;

    private boolean isNew;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

}
