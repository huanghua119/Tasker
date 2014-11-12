package com.chuanshida.tasker.bean;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    private User createUser;

    private User toUser;

    private String name;

    private String content;

    private Date createAt;

    private long remindAt;

    private Date finalAt;

    private String address;

    public static final int TASK_PERMISSIONS_ONLY_SELF = 1;
    public static final int TASK_PERMISSIONS_ONLY_FRIEND = 2;
    public static final int TASK_PERMISSIONS_PUBLIC = 3;

    private int permissions = TASK_PERMISSIONS_PUBLIC;

    private int status = TASK_STATUS_WAITING;

    public static final int TASK_STATUS_WAITING = 1;
    public static final int TASK_STATUS_FINISH = 2;
    public static final int TASK_STATUS_ACCEPT = 3;
    public static final int TASK_STATUS_NO_ACCEPT = 4;

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getFinalAt() {
        return finalAt;
    }

    public void setFinalAt(Date updateAt) {
        this.finalAt = updateAt;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(long remindAt) {
        this.remindAt = remindAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
