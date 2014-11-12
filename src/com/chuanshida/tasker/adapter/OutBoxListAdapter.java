package com.chuanshida.tasker.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.ui.BaseActivity;
import com.chuanshida.tasker.ui.UserDetailActivity;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.util.ViewHolder;
import com.chuanshida.tasker.view.DateTextView;

public class OutBoxListAdapter extends BaseListAdapter<Task> {

    protected Handler mMainThreadHandler;

    public OutBoxListAdapter(Context context, List<Task> list) {
        super(context, list);
        mMainThreadHandler = new Handler(context.getApplicationContext()
                .getMainLooper());
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.outbox_item_view, null);
        }
        List<Task> list = getList();
        final Task task = list.get(position);
        ImageView userPhoto = ViewHolder.get(view, R.id.user_photo);
        TextView taskName = ViewHolder.get(view, R.id.task_name);
        taskName.setText(task.getName());
        TextView status = ViewHolder.get(view, R.id.status);
        Button remindTa = ViewHolder.get(view, R.id.remind_ta);
        int taskStatus = task.getStatus();
        status.setText(CommonUtils.getTaskStatus(mContext.getResources(),
                taskStatus));
        status.setTextColor(CommonUtils.getTaskStatusColor(
                mContext.getResources(), taskStatus));
        if (taskStatus == Task.TASK_STATUS_FINISH) {
            status.getPaint().setFlags(
                    Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        } else {
            status.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        }
        remindTa.setVisibility(task.getStatus() == Task.TASK_STATUS_WAITING ? View.VISIBLE
                : View.GONE);

        DateTextView taskCreateTime = ViewHolder.get(view,
                R.id.task_create_time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(task.getCreateAt());
        taskCreateTime.setText("(" + time + ")");
        setOnInViewClickListener(R.id.user_photo,
                new onInternalClickListener() {
                    @Override
                    public void OnClickListener(View parentV, View v,
                            Integer position, Object values) {
                        Intent intent = new Intent(mContext,
                                UserDetailActivity.class);
                        intent.putExtra("user", task.getToUser());
                        ((BaseActivity) mContext).startAnimActivity(intent);
                    }
                });
        TextView titleName = ViewHolder.get(view, R.id.title_name);
        Date select = getSectionForPosition(position);
        int selectPosition = getPositionForSection(select);
        if (position == selectPosition) {
            Date date = new Date();
            if (select.getYear() != date.getYear()) {
                sdf = new SimpleDateFormat("yyyy年MM月dd日");
            } else {
                sdf = new SimpleDateFormat("MM月dd日");
            }
            titleName.setText(sdf.format(select));
            titleName.setVisibility(View.VISIBLE);
        } else {
            titleName.setVisibility(View.GONE);
        }
        return view;
    }

    public int getPositionForSection(Date section) {
        for (int i = 0; i < getCount(); i++) {
            Date date = list.get(i).getCreateAt();
            if (date.compareTo(section) == 0) {
                return i;
            }
        }
        return -1;
    }

    public Date getSectionForPosition(int position) {
        return list.get(position).getCreateAt();
    }

}
