package com.chuanshida.tasker.util;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuanshida.tasker.CustomApplcation;
import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.TaskToUser;

public class CommonUtils {
    /** 检查是否有网络 */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    /** 检查是否是WIFI */
    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    /** 检查是否是移动网络 */
    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /** 检查SD卡是否存在 */
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

        // loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }

    public static boolean isBackgroundRunning(Context context) {
        String processName = context.getPackageName();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);

        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processList) {
            if (process.processName.startsWith(processName)) {
                boolean isBackground = process.importance != IMPORTANCE_FOREGROUND
                        && process.importance != IMPORTANCE_VISIBLE;
                boolean isLockedState = keyguardManager
                        .inKeyguardRestrictedInputMode();
                if (isBackground || isLockedState) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static void showLog(String msg) {
        if (CustomApplcation.DEBUG) {
            Log.i(CustomApplcation.TAG, msg);
        }
    }

    public static void showLog(String tag, String msg) {
        if (CustomApplcation.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static String getTaskPermission(Resources res, int permission) {
        switch (permission) {
        case Task.TASK_PERMISSIONS_PUBLIC:
            return "(" + res.getString(R.string.task_public) + ")";
        case Task.TASK_PERMISSIONS_ONLY_FRIEND:
            return "(" +  res.getString(R.string.task_only_friend) + ")";
        case Task.TASK_PERMISSIONS_ONLY_SELF:
            return "(" + res.getString(R.string.task_only_self) + ")";
        }
        return "";
    }

    public static int getTaskPermission(int permission) {
        switch (permission) {
        case Task.TASK_PERMISSIONS_PUBLIC:
            return R.drawable.permission_public;
        case Task.TASK_PERMISSIONS_ONLY_FRIEND:
            return R.drawable.permission_friend;
        case Task.TASK_PERMISSIONS_ONLY_SELF:
            return R.drawable.permission_only_self;
        }
        return 0;
    }

    public static String getTaskStatus(Resources res, int status) {
        switch (status) {
        case TaskToUser.TASK_STATUS_FINISH:
            return res.getString(R.string.has_final);
        case TaskToUser.TASK_STATUS_WAITING:
            return res.getString(R.string.waiting);
        case TaskToUser.TASK_STATUS_ACCEPT:
            return res.getString(R.string.has_accept);
        case TaskToUser.TASK_STATUS_NO_ACCEPT:
            return res.getString(R.string.has_no_accept);
        }
        return "";
    }

    public static int getTaskStatusColor(Resources res, int status) {
        switch (status) {
        case TaskToUser.TASK_STATUS_FINISH:
        case TaskToUser.TASK_STATUS_WAITING:
            return res.getColor(R.color.assigned_text_color);
        case TaskToUser.TASK_STATUS_ACCEPT:
            return res.getColor(R.color.orange_color);
        case TaskToUser.TASK_STATUS_NO_ACCEPT:
            return res.getColor(R.color.send_verify_color);
        }
        return res.getColor(R.color.assigned_text_color);
    }

    public static List<TaskToUser> getTaskToUserForTask(Task mCurrentTask) {
        List<TaskToUser> result = new ArrayList<TaskToUser>();
        List<TaskToUser> list = TempData.mTempTaskToUserList;
        for (TaskToUser ttu : list) {
            if (ttu.getTask().getName().equals(mCurrentTask.getName())) {
                result.add(ttu);
            }
        }
        return result;
    }

    public static TaskToUser getTaskFirstUser(List<TaskToUser> users) {
        TaskToUser accept = null;
        TaskToUser noAccept = null;
        TaskToUser finish = null;
        for (TaskToUser ttu : users) {
            int status = ttu.getStatus();
            if (status == TaskToUser.TASK_STATUS_WAITING) {
                return ttu;
            } else if (status == TaskToUser.TASK_STATUS_ACCEPT) {
                accept = ttu;
            } else if (status == TaskToUser.TASK_STATUS_NO_ACCEPT) {
                noAccept = ttu;
            } else {
                finish = ttu;
            }
        }
        if (accept != null) {
            return accept;
        } else if (noAccept != null) {
            return noAccept;
        } else {
            return finish;
        }
    }

    public static int stringToInt(String number) {
        return Integer.parseInt(number);
    }

    public static Dialog showCompleteDialog(Context content, String title,
            String message, OnClickListener onClickListener, boolean cancelable) {
        Dialog dialog = new AlertDialog.Builder(content).setTitle(title)
                .setMessage(message).setCancelable(cancelable)
                .setPositiveButton(R.string.ok, onClickListener)
                .setNegativeButton(R.string.cancle, null).create();
        return dialog;
    }

    public static Dialog showCompleteDialog(Context content, int title,
            int message, OnClickListener onClickListener, boolean cancelable) {
        Dialog dialog = new AlertDialog.Builder(content).setTitle(title)
                .setMessage(message).setCancelable(cancelable)
                .setPositiveButton(R.string.ok, onClickListener)
                .setNegativeButton(R.string.cancle, null).create();
        return dialog;
    }
}
