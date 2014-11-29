package com.chuanshida.tasker.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.TaskToUser;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.util.TempData;

public class TaskDetailActivity extends BaseActivity implements OnClickListener {

    private Task mCurrentTask = null;
    private ImageView mUserPhoto;
    private TextView mTaskName;
    private TextView mRepeat;
    private TextView mTaskCreateTime;
    private TextView mTaskFinalTime;
    private TextView mTaskRemind;
    private TextView mTaskRemark;
    private TextView mTaskAddress;
    private Button mTaskAccept;
    private Button mTaskNoAccept;
    private TextView mTaskStatus;
    private LinearLayout mPicGroup;
    private View mPicGroupParent;
    private TaskToUser mCurrentTaskToUser;
    private List<TaskToUser> mTaskAllToUsers;

    private View mRepeatView;

    private int mCurrentRepeat = Task.TASK_REPEAT_TYLE_NO;

    private boolean mSelfCreate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail_view);
        mCurrentTask = (Task) getIntent().getSerializableExtra("task");
        mTaskAllToUsers = CommonUtils.getTaskToUserForTask(mCurrentTask);
        mCurrentTaskToUser = mTaskAllToUsers.get(0);
        User createUser = mCurrentTask.getCreateUser();
        if (createUser.getPhoneNumber().equals(
                userManager.getCurrentUser().getPhoneNumber())) {
            mSelfCreate = true;
        }
        init();
    }

    private void init() {
        mUserPhoto = (ImageView) findViewById(R.id.user_photo);
        mTaskName = (TextView) findViewById(R.id.task_name);
        mTaskCreateTime = (TextView) findViewById(R.id.task_crate_time);
        mTaskFinalTime = (TextView) findViewById(R.id.final_date);
        mTaskRemind = (TextView) findViewById(R.id.task_remind);
        mTaskRemark = (TextView) findViewById(R.id.task_remark);
        mTaskAddress = (TextView) findViewById(R.id.task_location);
        mTaskAccept = (Button) findViewById(R.id.task_accept);
        mTaskNoAccept = (Button) findViewById(R.id.task_no_accept);
        mTaskStatus = (TextView) findViewById(R.id.task_status);
        mPicGroup = (LinearLayout) findViewById(R.id.task_img);
        mPicGroupParent = findViewById(R.id.task_img_scroll);
        mRepeatView = findViewById(R.id.task_repeat_view);
        mRepeat = (TextView) findViewById(R.id.task_repeat);
        mUserPhoto.setOnClickListener(this);
    }

    private void updateRepeatView() {
        switch (mCurrentRepeat) {
        case Task.TASK_REPEAT_TYLE_NO:
            mRepeat.setText(R.string.task_no_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_WEEK:
            mRepeat.setText(R.string.task_week_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_DAY:
            mRepeat.setText(R.string.task_day_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_MONTH:
            mRepeat.setText(R.string.task_month_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_YEAR:
            mRepeat.setText(R.string.task_year_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_DIY:
            mRepeat.setText(R.string.diy);
            break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateInfoUI();
    }

    private void updateInfoUI() {
        if (mCurrentTask != null) {
            mTaskName.setText(mCurrentTask.getName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
            Date finalDate = mCurrentTask.getFinalAt();
            if (finalDate != null) {
                String time = sdf.format(finalDate);
                mTaskFinalTime.setText(time);
            } else {
                mTaskFinalTime.setText(R.string.task_no_final_date);
            }
            mTaskRemark.setText(mCurrentTask.getContent());
            long remind = mCurrentTaskToUser.getRemindAt();
            if (remind == 0) {
                mTaskRemind.setText(getString(R.string.advance_time));
            } else if (remind < 60) {
                mTaskRemind
                        .setText(getString(R.string.advance_time_sec, remind));
            } else {
                mTaskRemind.setText(getString(R.string.advance_time_hour,
                        remind / 60));
            }
            String address = mCurrentTask.getAddress();
            mTaskAddress
                    .setText((address == null || address.equals("")) ? getString(R.string.user_no_address)
                            : address);

            SimpleDateFormat sdf2 = null;
            Date createDate = mCurrentTask.getCreateAt();
            Calendar calendar = Calendar.getInstance();
            if (createDate.getYear() == calendar.get(Calendar.YEAR)) {
                if (createDate.getMonth() == calendar.get(Calendar.MARCH)) {
                    sdf2 = new SimpleDateFormat("HH:mm");
                } else {
                    sdf2 = new SimpleDateFormat("MM月dd HH:mm");
                }
            } else {
                sdf2 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            }
            sdf2 = new SimpleDateFormat("HH:mm");
            mTaskCreateTime.setText(sdf2.format(createDate));
            int[] imgRes = TempData.createTempTaskImg();
            for (int i = 0; i < imgRes.length; i++) {
                ImageView img = new ImageView(this);
                LayoutParams layout = new LayoutParams(150, 200);
                int left = 5;
                int right = 5;
                if (i == 0) {
                    left = 0;
                } else if (i == imgRes.length - 1) {
                    right = 0;
                }
                layout.setMargins(left, 0, right, 0);
                img.setLayoutParams(layout);
                img.setBackgroundResource(imgRes[i]);
                mPicGroup.addView(img);
            }
            if (imgRes.length > 0) {
                mPicGroupParent.setVisibility(View.VISIBLE);
            } else {
                mPicGroupParent.setVisibility(View.GONE);
            }

            mCurrentRepeat = mCurrentTask.getRepeat();
            if (mCurrentRepeat == Task.TASK_REPEAT_TYLE_NO) {
                mRepeatView.setVisibility(View.GONE);
            } else {
                mRepeatView.setVisibility(View.VISIBLE);
                updateRepeatView();
            }

            int status = mCurrentTaskToUser.getStatus();
            if (status == TaskToUser.TASK_STATUS_WAITING) {
                mTaskStatus.setVisibility(View.GONE);
                mTaskAccept.setVisibility(View.VISIBLE);
                if (mSelfCreate) {
                    mTaskAccept.setText(R.string.remind_ta);
                    mTaskNoAccept.setVisibility(View.GONE);
                } else {
                    mTaskAccept.setText(R.string.task_accept);
                    mTaskNoAccept.setVisibility(View.VISIBLE);
                }
            } else {
                mTaskAccept.setVisibility(View.GONE);
                mTaskNoAccept.setVisibility(View.GONE);
                mTaskStatus.setVisibility(View.VISIBLE);
                mTaskStatus.setText(CommonUtils.getTaskStatus(getResources(),
                        status));
                mTaskStatus.setTextColor(CommonUtils.getTaskStatusColor(
                        getResources(), status));
                if (status == TaskToUser.TASK_STATUS_FINISH) {
                    mTaskStatus.getPaint()
                            .setFlags(
                                    Paint.STRIKE_THRU_TEXT_FLAG
                                            | Paint.ANTI_ALIAS_FLAG);
                    mTaskStatus.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mUserPhoto) {
            Intent intent = new Intent(this, UserDetailActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(
                    "user",
                    mSelfCreate ? mCurrentTaskToUser.getToUser() : mCurrentTask
                            .getCreateUser());
            intent.putExtras(b);
            startAnimActivity(intent);
        }
    }

}
