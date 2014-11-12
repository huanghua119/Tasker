package com.chuanshida.tasker.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.manager.UserManager;

public class TempData {

    private static User user = new User();
    static {
        user.setUsername("Peter");
        user.setPhoneNumber("13036292929");
        user.setSex(true);
        user.setLabel("宅男");
        user.setSignature("阿西吧");
        user.setEmail("12345678@qq.com");
        user.setAddress("江西省萍乡市上栗县");
    }

    public static List<Task> createTempTaskData(Context context) {
        List<Task> result = new ArrayList<Task>();
        Task task1 = new Task();
        task1.setCreateAt(new Date());
        task1.setStatus(Task.TASK_STATUS_FINISH);
        task1.setPermissions(Task.TASK_PERMISSIONS_ONLY_SELF);
        task1.setName("天天加班，老子不干了");
        task1.setContent("从一些社会调查中我们可以看到，大部分中国家庭的独生子女在某些方面感到孤独。为什么独生子女会感到孤独呢？孤独是如此可怕的事吗？");
        task1.setAddress("公司");
        Calendar c = Calendar.getInstance();
        c.set(2015, 1, 22);
        task1.setFinalAt(c.getTime());
        task1.setToUser(UserManager.getInstance(context).getCurrentUser());
        task1.setCreateUser(user);
        result.add(task1);
        for (int i = 0; i < 14; i++) {
            Task task2 = new Task();
            c.set(2014, 10, 1 + i / 2);
            task2.setCreateAt(c.getTime());
            task2.setName("晚上回家吃饭");
            task2.setContent("11");
            task2.setRemindAt(30);
            Calendar c1 = Calendar.getInstance();
            c1.set(2034, 0, 19);
            task2.setFinalAt(c1.getTime());
            task2.setToUser(UserManager.getInstance(context).getCurrentUser());
            task2.setCreateUser(user);
            result.add(task2);
        }
        Task task3 = new Task();
        c.set(2014, 4, 13);
        task3.setCreateAt(c.getTime());
        task3.setStatus(Task.TASK_STATUS_FINISH);
        task3.setPermissions(Task.TASK_PERMISSIONS_ONLY_FRIEND);
        task3.setName("就是不回家吃饭");
        task3.setContent("11");
        task3.setRemindAt(60);
        task3.setAddress("金龙网吧");
        task3.setToUser(UserManager.getInstance(context).getCurrentUser());
        task3.setCreateUser(user);
        result.add(task3);
        return result;
    }

    public static List<Task> createTempDayTaskData(Context context) {
        List<Task> result = new ArrayList<Task>();
        for (int i = 0; i < 4; i++) {
            Task task1 = new Task();
            task1.setCreateAt(new Date());
            task1.setStatus(Task.TASK_STATUS_FINISH);
            task1.setPermissions(Task.TASK_PERMISSIONS_ONLY_SELF);
            task1.setName("天天加班，老子不干了");
            task1.setContent("11");
            task1.setToUser(UserManager.getInstance(context).getCurrentUser());
            task1.setCreateUser(user);
            result.add(task1);
            Task task3 = new Task();
            task3.setCreateAt(new Date());
            task3.setStatus(Task.TASK_STATUS_WAITING);
            task3.setPermissions(Task.TASK_PERMISSIONS_ONLY_FRIEND);
            task3.setName("就是不回家吃饭");
            task3.setContent("11");
            task3.setToUser(UserManager.getInstance(context).getCurrentUser());
            task3.setCreateUser(user);
            result.add(task3);
        }
        return result;
    }

    public static List<User> createTempMyFriend(Context context) {
        String name[] = new String[] { "Peter", "Peter", "Peter", "Peter",
                "Peter", "Abb", "黄品", "黄品", "黄品", "黄品", "渑顺", "软骨", "#1k",
                "Um", "有中", "估右", "估右", "林右", "估右" };
        List<User> result = new ArrayList<User>();
        for (int i = 0; i < name.length; i++) {
            User user = new User();
            user.setUsername(name[i]);
            user.setPhoneNumber("13036292929");
            user.setSex(true);
            user.setLabel("宅男");
            user.setSignature("阿西吧");
            user.setEmail("12345678@qq.com");
            user.setAddress("江西省萍乡市上栗县");
            result.add(user);
        }
        return result;
    }

    public static List<User> createTempNewFriend(Context context) {
        List<User> result = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("kkkwfnh");
            user.setPhoneNumber("123");
            user.setSex(true);
            user.setLabel("宅男");
            user.setSignature("阿西吧");
            user.setEmail("12345678@qq.com");
            user.setAddress("江西省萍乡市上栗县");
            result.add(user);
        }
        return result;
    }

    public static int[] createTempTaskImg() {
        return new int[] { R.drawable.sky3, R.drawable.sky1, R.drawable.ocean2,
                R.drawable.boat };
    }

    public static List<Task> createTempOutBoxData(Context context) {
        List<Task> result = new ArrayList<Task>();
        Task task1 = new Task();
        Calendar c = Calendar.getInstance();
        c.set(2013, 11, 11);
        task1.setCreateAt(c.getTime());
        task1.setStatus(Task.TASK_STATUS_FINISH);
        task1.setPermissions(Task.TASK_PERMISSIONS_ONLY_SELF);
        task1.setName("这样做真的好吗？");
        task1.setContent("从一些社会调查中我们可以看到，大部分中国家庭的独生子女在某些方面感到孤独。为什么独生子女会感到孤独呢？孤独是如此可怕的事吗？");
        task1.setAddress("宜山路附近");
        c.set(2015, 1, 22);
        task1.setFinalAt(c.getTime());
        task1.setCreateUser(UserManager.getInstance(context).getCurrentUser());
        task1.setToUser(user);
        result.add(task1);
        for (int i = 0; i < 14; i++) {
            Task task2 = new Task();
            c.set(2014, 10, 1 + i / 2);
            task2.setCreateAt(c.getTime());
            task2.setName("光棍节怎么过？");
            task2.setContent("111111111111111111111111111111111");
            task2.setRemindAt(30);
            c.set(2034, 0, 19);
            task2.setFinalAt(c.getTime());
            task2.setStatus((i % 2 == 0) ? Task.TASK_STATUS_ACCEPT
                    : Task.TASK_STATUS_NO_ACCEPT);
            task2.setCreateUser(UserManager.getInstance(context)
                    .getCurrentUser());
            task2.setToUser(user);
            result.add(task2);
        }
        Task task3 = new Task();
        c.set(2014, 1, 23);
        task3.setCreateAt(c.getTime());
        task3.setStatus(Task.TASK_STATUS_WAITING);
        task3.setPermissions(Task.TASK_PERMISSIONS_ONLY_FRIEND);
        task3.setName("去网吧打DOTA2");
        task3.setContent("22222222222222222222222222");
        task3.setRemindAt(60);
        task3.setAddress("金龙网吧");
        task3.setCreateUser(UserManager.getInstance(context).getCurrentUser());
        task3.setToUser(user);
        result.add(task3);
        return result;
    }
}
