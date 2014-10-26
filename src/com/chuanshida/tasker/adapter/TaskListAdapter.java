package com.chuanshida.tasker.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.util.ViewHolder;

public class TaskListAdapter extends BaseListAdapter<Task> {

    protected Handler mMainThreadHandler;

    public TaskListAdapter(Context context, List<Task> list) {
        super(context, list);
        mMainThreadHandler = new Handler(context.getApplicationContext()
                .getMainLooper());
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.task_item_view, null);
        }
        List<Task> list = getList();
        final Task task = list.get(position);
        ImageView userPhoto = ViewHolder.get(view, R.id.user_photo);
        TextView taskName = ViewHolder.get(view, R.id.task_name);
        TextView taskCreateTime = ViewHolder.get(view, R.id.task_create_time);
        TextView taskPermissions = ViewHolder.get(view, R.id.task_permissions);
        CheckBox taskStatus = ViewHolder.get(view, R.id.task_status);

        taskName.setText(task.getName());

        taskPermissions.setText(CommonUtils.getTaskPermission(
                mContext.getResources(), task.getPermissions()));
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String time = sdf.format(task.getCreateAt());
        taskCreateTime.setText("(" + time +")");
        taskStatus.setChecked(task.getStatus() == Task.TASK_STATUS_FINISH);
        if (task.getStatus() == Task.TASK_STATUS_PROGRESS) {
            taskStatus.setText(R.string.status_progress);
            taskStatus.setTextColor(mContext.getResources().getColor(
                    R.color.orange_color));
        } else if (task.getStatus() == Task.TASK_STATUS_FINISH) {
            taskStatus.setText(R.string.status_finish);
            taskStatus.setTextColor(mContext.getResources().getColor(
                    R.color.task_permissions_color));
        }
        return view;
    }

}
