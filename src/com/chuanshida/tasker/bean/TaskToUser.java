package com.chuanshida.tasker.bean;

import java.io.Serializable;

public class TaskToUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private long remindAt;

    private Task task;

    private User toUser;

    private int status = TASK_STATUS_WAITING;

    public static final int TASK_STATUS_WAITING = 1;
    public static final int TASK_STATUS_FINISH = 2;
    public static final int TASK_STATUS_ACCEPT = 3;
    public static final int TASK_STATUS_NO_ACCEPT = 4;

    public long getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(long remindAt) {
        this.remindAt = remindAt;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
