package com.chuanshida.tasker.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.manager.UserManager;

public class TempData {

    public static List<Task> createTempTaskData(Context context) {
        List<Task> result = new ArrayList<Task>();
        Task task1 = new Task();
        task1.setCreateAt(new Date());
        task1.setStatus(Task.TASK_STATUS_FINISH);
        task1.setPermissions(Task.TASK_PERMISSIONS_ONLY_SELF);
        task1.setName("天天加班，老子不干了");
        task1.setContent("11");
        task1.setCreateUser(UserManager.getInstance(context).getCurrentUser());
        result.add(task1);
        for (int i = 0; i < 14; i++) {
            Task task2 = new Task();
            task2.setCreateAt(new Date());
            task2.setName("晚上回家吃饭");
            task2.setContent("11");
            task2.setCreateUser(UserManager.getInstance(context)
                    .getCurrentUser());
            result.add(task2);
        }
        Task task3 = new Task();
        task3.setCreateAt(new Date());
        task3.setStatus(Task.TASK_STATUS_FINISH);
        task3.setPermissions(Task.TASK_PERMISSIONS_ONLY_FRIEND);
        task3.setName("就是不回家吃饭");
        task3.setContent("11");
        task3.setCreateUser(UserManager.getInstance(context).getCurrentUser());
        result.add(task3);
        return result;
    }

    public static List<Task> createTempDayTaskData(Context context) {
        List<Task> result = new ArrayList<Task>();
        Task task1 = new Task();
        task1.setCreateAt(new Date());
        task1.setStatus(Task.TASK_STATUS_FINISH);
        task1.setPermissions(Task.TASK_PERMISSIONS_ONLY_SELF);
        task1.setName("天天加班，老子不干了");
        task1.setContent("11");
        task1.setCreateUser(UserManager.getInstance(context).getCurrentUser());
        result.add(task1);
        Task task3 = new Task();
        task3.setCreateAt(new Date());
        task3.setStatus(Task.TASK_STATUS_PROGRESS);
        task3.setPermissions(Task.TASK_PERMISSIONS_ONLY_FRIEND);
        task3.setName("就是不回家吃饭");
        task3.setContent("11");
        task3.setCreateUser(UserManager.getInstance(context).getCurrentUser());
        result.add(task3);
        return result;
    }

    public static List<User> createTempMyFriend(Context context) {
        List<User> result = new ArrayList<User>();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setUsername("小华仔" + i);
            user.setPhoneNumber("123");
            result.add(user);
        }
        return result;
    }

    public static List<User> createTempNewFriend(Context context) {
        List<User> result = new ArrayList<User>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setUsername("大华仔" + i);
            user.setPhoneNumber("123");
            result.add(user);
        }
        return result;
    }
}
