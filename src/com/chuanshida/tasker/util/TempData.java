package com.chuanshida.tasker.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.NewMessage;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.TaskToUser;
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

    public static List<Task> mTempTaskList = new ArrayList<Task>();
    public static List<TaskToUser> mTempTaskToUserList = new ArrayList<TaskToUser>();

    public static List<Task> createTempTaskData(Context context) {
        List<Task> result = new ArrayList<Task>();
        Task task1 = new Task();
        task1.setCreateAt(new Date());
        task1.setPermissions(Task.TASK_PERMISSIONS_ONLY_SELF);
        task1.setName("天天加班，老子不干了");
        task1.setContent("从一些社会调查中我们可以看到，大部分中国家庭的独生子女在某些方面感到孤独。为什么独生子女会感到孤独呢？孤独是如此可怕的事吗？");
        task1.setAddress("公司");
        Calendar c = Calendar.getInstance();
        c.set(2015, 1, 22);
        task1.setFinalAt(c.getTime());
        TaskToUser toUser = new TaskToUser();
        toUser.setStatus(TaskToUser.TASK_STATUS_FINISH);
        toUser.setTask(task1);
        toUser.setToUser(UserManager.getInstance(context).getCurrentUser());
        mTempTaskToUserList.add(toUser);
        task1.setCreateUser(user);
        result.add(task1);
        for (int i = 0; i < 14; i++) {
            Task task2 = new Task();
            c.set(2014, 10, 1 + i / 2);
            task2.setCreateAt(c.getTime());
            task2.setName("晚上回家吃饭");
            task2.setContent("11");
            Calendar c1 = Calendar.getInstance();
            c1.set(2034, 0, 19);
            task2.setFinalAt(c1.getTime());
            TaskToUser toUser2 = new TaskToUser();
            toUser2.setTask(task2);
            toUser2.setRemindAt(30);
            toUser2.setToUser(UserManager.getInstance(context).getCurrentUser());
            mTempTaskToUserList.add(toUser2);
            task2.setCreateUser(user);
            result.add(task2);
        }
        Task task3 = new Task();
        c.set(2014, 4, 13);
        task3.setCreateAt(c.getTime());
        task3.setPermissions(Task.TASK_PERMISSIONS_ONLY_FRIEND);
        task3.setName("就是不回家吃饭");
        task3.setContent("11");
        TaskToUser toUse3 = new TaskToUser();
        toUse3.setStatus(TaskToUser.TASK_STATUS_FINISH);
        toUse3.setTask(task3);
        toUse3.setRemindAt(60);
        toUse3.setToUser(UserManager.getInstance(context).getCurrentUser());
        mTempTaskToUserList.add(toUse3);
        task3.setAddress("金龙网吧");
        task3.setCreateUser(user);
        result.add(task3);
        return result;
    }

    public static List<Task> createTempDayTaskData(Context context) {
        List<Task> result = new ArrayList<Task>();
        for (int i = 0; i < 4; i++) {
            Task task1 = new Task();
            task1.setCreateAt(new Date());
            task1.setPermissions(Task.TASK_PERMISSIONS_ONLY_SELF);
            task1.setName("天天加班，老子不干了");
            task1.setContent("11");
            TaskToUser toUser = new TaskToUser();
            toUser.setStatus(TaskToUser.TASK_STATUS_FINISH);
            toUser.setTask(task1);
            toUser.setRemindAt(60);
            toUser.setToUser(UserManager.getInstance(context).getCurrentUser());
            mTempTaskToUserList.add(toUser);
            task1.setCreateUser(user);
            result.add(task1);
            Task task3 = new Task();
            task3.setCreateAt(new Date());
            task3.setPermissions(Task.TASK_PERMISSIONS_ONLY_FRIEND);
            task3.setName("就是不回家吃饭");
            task3.setContent("11");
            TaskToUser toUser1 = new TaskToUser();
            toUser1.setStatus(TaskToUser.TASK_STATUS_WAITING);
            toUser1.setTask(task3);
            toUser1.setToUser(UserManager.getInstance(context).getCurrentUser());
            mTempTaskToUserList.add(toUser1);
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
            user.setPhoneNumber("1303629292" + i);
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
            user.setPhoneNumber("123" + i);
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
        task1.setPermissions(Task.TASK_PERMISSIONS_ONLY_SELF);
        task1.setName("这样做真的好吗？");
        task1.setContent("从一些社会调查中我们可以看到，大部分中国家庭的独生子女在某些方面感到孤独。为什么独生子女会感到孤独呢？孤独是如此可怕的事吗？");
        task1.setAddress("宜山路附近");
        c.set(2015, 1, 22);
        task1.setFinalAt(c.getTime());
        task1.setCreateUser(UserManager.getInstance(context).getCurrentUser());
        TaskToUser toUser1 = new TaskToUser();
        toUser1.setStatus(TaskToUser.TASK_STATUS_FINISH);
        toUser1.setTask(task1);
        toUser1.setToUser(user);
        mTempTaskToUserList.add(toUser1);
        result.add(task1);
        for (int i = 0; i < 14; i++) {
            Task task2 = new Task();
            c.set(2014, 10, 1 + i / 2);
            task2.setCreateAt(c.getTime());
            task2.setName("光棍节怎么过？");
            task2.setContent("111111111111111111111111111111111");
            TaskToUser toUser2 = new TaskToUser();
            toUser2.setTask(task2);
            toUser2.setToUser(user);
            toUser2.setRemindAt(30);
            toUser2.setStatus((i % 2 == 0) ? TaskToUser.TASK_STATUS_ACCEPT
                    : TaskToUser.TASK_STATUS_NO_ACCEPT);
            mTempTaskToUserList.add(toUser2);
            c.set(2034, 0, 19);
            task2.setFinalAt(c.getTime());
            task2.setCreateUser(UserManager.getInstance(context)
                    .getCurrentUser());
            result.add(task2);
        }
        Task task3 = new Task();
        c.set(2014, 1, 23);
        task3.setCreateAt(c.getTime());
        task3.setPermissions(Task.TASK_PERMISSIONS_ONLY_FRIEND);
        task3.setName("去网吧打DOTA2");
        task3.setContent("22222222222222222222222222");

        TaskToUser toUser3 = new TaskToUser();
        toUser3.setStatus(TaskToUser.TASK_STATUS_WAITING);
        toUser3.setTask(task3);
        toUser3.setToUser(user);
        toUser3.setRemindAt(60);
        mTempTaskToUserList.add(toUser3);
        task3.setAddress("金龙网吧");
        task3.setCreateUser(UserManager.getInstance(context).getCurrentUser());
        result.add(task3);
        return result;
    }

    public static List<NewMessage> createTempMessage(Context context,
            User mChatUser) {
        List<NewMessage> result = new ArrayList<NewMessage>();
        NewMessage message = new NewMessage();
        Calendar c = Calendar.getInstance();
        c.set(2013, 4, 11);
        message.setMessageDate(c.getTime());
        message.setUser(mChatUser);
        message.setContext("你好吗？");
        result.add(message);
        boolean isMe = false;
        for (int i = 0; i < 6; i++) {
            NewMessage message1 = new NewMessage();
            Date date = new Date();
            c.set(2013, 10, 11, date.getHours(), date.getMinutes(),
                    date.getSeconds());
            message1.setMessageDate(c.getTime());
            message1.setUser(isMe ? mChatUser : UserManager
                    .getInstance(context).getCurrentUser());
            message1.setContext(isMe ? "你好吗？" : "不好！");
            isMe = !isMe;
            result.add(message1);
        }
        NewMessage message2 = new NewMessage();
        c.set(2013, 5, 11);
        message2.setMessageDate(c.getTime());
        message2.setUser(mChatUser);
        message2.setContext("Parcelable实现要点：需要实现三个东西1）writeToParcel 方法。该方法将类的数据写入外部提供的Parcel中.声明如下：writeToParcel (Parcel dest, int flags) 具体参数含义见javadoc2）describeContents方法。没搞懂有什么用，反正直接返回0也可以3）静态的Parcelable.Creator接口，本接口有两个方法：createFromParcel(Parcel in) 实现从in中创建出类的实例的功能newArray(int size) 创建一个类型为T，长度为size的数组，仅一句话（return new T[size])即可。估计本方法是供外部类反序列化本类数组使用。");
        result.add(message2);
        return result;
    }
}
