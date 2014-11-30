package com.chuanshida.tasker.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.chuanshida.tasker.bean.TaskToUser;
import com.chuanshida.tasker.ui.BaseActivity;
import com.chuanshida.tasker.ui.UserDetailActivity;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.util.ViewHolder;
import com.chuanshida.tasker.view.DateTextView;

public class TaskListAdapter extends BaseListAdapter<TaskToUser> {

    protected Handler mMainThreadHandler;

    public TaskListAdapter(Context context, List<TaskToUser> list) {
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
        List<TaskToUser> list = getList();
        final TaskToUser ttu = list.get(position);
        final Task task = ttu.getTask();
        ImageView userPhoto = ViewHolder.get(view, R.id.user_photo);
        TextView taskName = ViewHolder.get(view, R.id.task_name);
        DateTextView taskCreateTime = ViewHolder.get(view,
                R.id.task_create_time);
        TextView taskPermissions = ViewHolder.get(view, R.id.task_permissions);
        CheckBox taskStatus = ViewHolder.get(view, R.id.task_status);

        taskName.setText(task.getName());

        taskPermissions.setText(CommonUtils.getTaskPermission(
                mContext.getResources(), task.getPermissions()));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(task.getCreateAt());
        taskCreateTime.setText("(" + time + ")");

        taskStatus.setChecked(ttu.getStatus() == TaskToUser.TASK_STATUS_FINISH);
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
                        ((BaseActivity) mContext).startAnimActivity(intent);
                    }
                });
        View timeView = ViewHolder.get(view, R.id.title_time_view);
        Date section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            timeView.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
            TextView titleTime = ViewHolder.get(view, R.id.title_time);
            titleTime.setText(sdf2.format(task.getCreateAt()));
        } else {
            timeView.setVisibility(View.GONE);
        }
        return view;
    }

    public int getPositionForSection(Date section) {
        for (int i = 0; i < getCount(); i++) {
            Date sortStr = list.get(i).getTask().getCreateAt();
            if (sortStr.compareTo(section) == 0) {
                return i;
            }
        }
        return -1;
    }

    public Date getSectionForPosition(int position) {
        return list.get(position).getTask().getCreateAt();
    }

}
