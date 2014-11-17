package com.chuanshida.tasker.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.ui.BaseActivity;
import com.chuanshida.tasker.ui.UserDetailActivity;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.util.ViewHolder;
import com.chuanshida.tasker.view.DateTextView;

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
        DateTextView taskCreateTime = ViewHolder.get(view, R.id.task_create_time);
        ImageView taskPermissions = ViewHolder.get(view, R.id.task_permissions);
        CheckBox taskStatus = ViewHolder.get(view, R.id.task_status);

        taskName.setText(task.getName());

        taskPermissions.setBackgroundResource(CommonUtils
                .getTaskPermission(task.getPermissions()));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(task.getCreateAt());
        taskCreateTime.setText("(" + time + ")");
        
        taskStatus.setChecked(task.getStatus() == Task.TASK_STATUS_FINISH);
        setOnInViewClickListener(R.id.user_photo,
                new onInternalClickListener() {
                    @Override
                    public void OnClickListener(View parentV, View v,
                            Integer position, Object values) {
                        Intent intent = new Intent(mContext,
                                UserDetailActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("user", task.getCreateUser());
                        intent.putExtras(b);
                        ((BaseActivity)mContext).startAnimActivity(intent);
                    }
                });
        return view;
    }

}
