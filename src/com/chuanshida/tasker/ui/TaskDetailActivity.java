package com.chuanshida.tasker.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.util.TempData;

public class TaskDetailActivity extends BaseActivity implements OnClickListener {

    private Task mCurrentTask = null;
    private ImageView mUserPhoto;
    private TextView mTaskName;
    private TextView mTaskContent;
    private ImageView mTaskVoice;
    private TextView mTaskTime;
    private TextView mTaskRemind;
    private TextView mTaskAddress;
    private TextView mTaskPermissions;
    private ImageView mTaskPermissionsImg;
    private LinearLayout mTaskImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_detail_view);
        mCurrentTask = (Task) getIntent().getSerializableExtra("task");
        init();
    }

    private void init() {
        mUserPhoto = (ImageView) findViewById(R.id.user_photo);
        mTaskName = (TextView) findViewById(R.id.task_name);
        mTaskContent = (TextView) findViewById(R.id.task_content);
        mTaskVoice = (ImageView) findViewById(R.id.task_voice);
        mTaskTime = (TextView) findViewById(R.id.task_time);
        mTaskRemind = (TextView) findViewById(R.id.task_remind);
        mTaskAddress = (TextView) findViewById(R.id.task_address);
        mTaskPermissions = (TextView) findViewById(R.id.task_permissions);
        mTaskPermissionsImg = (ImageView) findViewById(R.id.task_permissions_img);
        mTaskImg = (LinearLayout) findViewById(R.id.task_img);
        mUserPhoto.setOnClickListener(this);
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
            mTaskContent.setText(mCurrentTask.getContent());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
            Date finalDate = mCurrentTask.getFinalAt();
            if (finalDate != null) {
                String time = sdf.format(finalDate);
                mTaskTime.setText(time);
            } else {
                mTaskTime.setText(R.string.task_no_final_date);
            }
            long remind = mCurrentTask.getRemindAt();
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
            mTaskPermissions.setText(CommonUtils.getTaskPermission(
                    getResources(), mCurrentTask.getPermissions()));
            mTaskPermissionsImg.setBackgroundResource(CommonUtils
                    .getTaskPermission(mCurrentTask.getPermissions()));
            int[] imgRes = TempData.createTempTaskImg();
            for (int i = 0; i < imgRes.length; i++) {
                ImageView img = new ImageView(this);
                LayoutParams layout = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
                mTaskImg.addView(img);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mUserPhoto) {
            Intent intent = new Intent(this,
                    UserDetailActivity.class);
            intent.putExtra("user", mCurrentTask.getCreateUser());
            startAnimActivity(intent);
        }
    }

}
