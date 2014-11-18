package com.chuanshida.tasker.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.TaskToUser;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.TempData;

public class UpdateTaskActivity extends BaseActivity implements OnClickListener {

    private EditText mTaskName;
    private Task mCurrentTask;

    private View mAssignedView;
    private View mFinalDateView;
    private View mRemarkView;
    private View mRepeatView;
    private View mLocationView;

    private TextView mTitle;
    private TextView mFinalDate;
    private TextView mLocation;
    private TextView mRepeat;
    private TextView mRemark;
    private TextView mAssigned;
    private ImageButton mBottomRemark;
    private ImageButton mBottomPic;
    private ImageButton mBottomVoice;
    private ImageButton mBottomRepeat;
    private ImageButton mBottomLocation;
    private LinearLayout mHeadGroup;

    private Map<String, User> mCheckUser;
    private int mCurrentRepeat = Task.TASK_REPEAT_TYLE_NO;

    private static final int REQUEST_CODE_FOR_ASSIGNED = 1;
    private static final int REQUEST_CODE_FOR_REMARK = 2;
    private static final int REQUEST_CODE_FOR_REPEAT = 3;
    private static final int REQUEST_CODE_FOR_LOCATION = 4;

    private boolean mResume = true;

    private OnClickListener mHeadClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                User user = (User) v.getTag();
                Intent intent = new Intent(UpdateTaskActivity.this,
                        UserDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user);
                intent.putExtras(b);
                startAnimActivity(intent);
            }
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHeadGroup.removeAllViews();
            int i = 0;
            for (User user : mCheckUser.values()) {
                ImageView img = new ImageView(UpdateTaskActivity.this);
                img.setOnClickListener(mHeadClickListener);
                img.setTag(user);
                LayoutParams layout = new LayoutParams(70, 70);
                int left = 5;
                int right = 5;
                if (i == 0) {
                    left = 0;
                } else if (i == mCheckUser.size() - 1) {
                    right = 0;
                }
                layout.setMargins(left, 0, right, 0);
                img.setLayoutParams(layout);
                if (user.getAvatar() != null && !"".equals(user.getAvatar())) {
                } else {
                    img.setBackgroundResource(R.drawable.login_head);
                }
                mHeadGroup.addView(img);
                i++;
            }
            if (mCheckUser.size() == 0) {
                mAssigned.setHint(R.string.new_assigned);
            } else {
                mAssigned.setHint("");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_update_view);
        mCurrentTask = (Task) getIntent().getExtras().getSerializable("task");
        init();
    }

    private void init() {
        mTitle = (TextView) findViewById(R.id.title);
        mTaskName = (EditText) findViewById(R.id.task_name);
        mAssignedView = findViewById(R.id.new_assigned_view);
        mAssignedView.setOnClickListener(this);
        mAssigned = (TextView) findViewById(R.id.new_assigned);
        mAssigned.setText("");
        mFinalDateView = findViewById(R.id.final_date_view);
        mFinalDate = (TextView) findViewById(R.id.final_date);
        mRemarkView = findViewById(R.id.task_remark_view);
        mRemarkView.setOnClickListener(this);
        mRemark = (TextView) findViewById(R.id.task_remark);
        mRepeatView = findViewById(R.id.task_repeat_view);
        mRepeat = (TextView) findViewById(R.id.task_repeat);
        mRepeatView.setOnClickListener(this);
        mLocationView = findViewById(R.id.task_location_view);
        mLocation = (TextView) findViewById(R.id.task_location);
        mLocationView.setOnClickListener(this);
        mHeadGroup = (LinearLayout) findViewById(R.id.head_group);

        mBottomRemark = (ImageButton) findViewById(R.id.task_bottom_remark);
        mBottomPic = (ImageButton) findViewById(R.id.task_bottom_pic);
        mBottomVoice = (ImageButton) findViewById(R.id.task_bottom_voice);
        mBottomRepeat = (ImageButton) findViewById(R.id.task_bottom_repeat);
        mBottomLocation = (ImageButton) findViewById(R.id.task_bottom_location);
        mBottomRemark.setOnClickListener(this);
        mBottomPic.setOnClickListener(this);
        mBottomVoice.setOnClickListener(this);
        mBottomRepeat.setOnClickListener(this);
        mBottomLocation.setOnClickListener(this);
        User user = (User) getIntent().getExtras().getSerializable("user");
        if (user != null) {
            if (mCheckUser == null) {
                mCheckUser = new HashMap<String, User>();
            }
            mCheckUser.put(user.getPhoneNumber(), user);
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mResume) {
            updateUI();
        }
        mResume = true;
    }

    private void updateUI() {
        mTaskName.setText(mCurrentTask.getName());
        mTaskName.setEnabled(false);

        String location = mCurrentTask.getAddress();
        if (location == null || "".equals(location)) {
            mLocationView.setVisibility(View.GONE);
            mBottomLocation.setVisibility(View.VISIBLE);
        } else {
            mLocationView.setVisibility(View.VISIBLE);
            mLocation.setText(location);
            mBottomLocation.setVisibility(View.GONE);
        }

        String remark = mCurrentTask.getContent();
        if (remark == null || "".equals(remark)) {
            mRemarkView.setVisibility(View.GONE);
            mBottomRemark.setVisibility(View.VISIBLE);
        } else {
            mRemarkView.setVisibility(View.VISIBLE);
            mRemark.setText(remark);
            mBottomRemark.setVisibility(View.GONE);
        }

        mCurrentRepeat = mCurrentTask.getRepeat();
        if (mCurrentRepeat == Task.TASK_REPEAT_TYLE_NO) {
            mRepeatView.setVisibility(View.GONE);
            mBottomRepeat.setVisibility(View.VISIBLE);
        } else {
            mRepeatView.setVisibility(View.VISIBLE);
            updateRepeatView();
            mBottomRepeat.setVisibility(View.GONE);
        }

        List<TaskToUser> ttus = TempData.getTaskToUserForTask(mCurrentTask);
        if (ttus != null && ttus.size() > 0) {
            if (mCheckUser == null) {
                mCheckUser = new HashMap<String, User>();
            }
            for (TaskToUser ttu : ttus) {
                User user = ttu.getToUser();
                mCheckUser.put(user.getPhoneNumber(), user);
            }
            mHandler.sendEmptyMessage(1);
        }
        mTitle.setText(R.string.task_info);
    }

    public void onComplete(View v) {
        if (mCheckUser == null || mCheckUser.size() == 0) {
            return;
        }
        String address = mLocation.getText().toString();
        if (address != null && !"".equals(address)) {
            mCurrentTask.setAddress(address);
        }
        String remark = mRemark.getText().toString();
        if (remark != null && !"".equals(remark)) {
            mCurrentTask.setContent(remark);
        }
        mCurrentTask.setRepeat(mCurrentRepeat);
        int count = mCheckUser.size();
        for (User user : mCheckUser.values()) {
            TaskToUser taskToUser = new TaskToUser();
            taskToUser.setToUser(user);
            taskToUser.setTask(mCurrentTask);
            TempData.mTempTaskToUserList.add(taskToUser);
        }
        TempData.mTempTaskList.put(mCurrentTask.getName(), mCurrentTask);
        if (count > 0) {
            onBackPressed();
            ShowToast(R.string.task_new_success,
                    R.drawable.tenpay_toast_logo_success);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mAssignedView) {
            Intent intent = new Intent(this, AssignedFriendActivity.class);
            if (mCheckUser != null && mCheckUser.size() > 0) {
                Bundle b = new Bundle();
                b.putInt("data_size", mCheckUser.size());
                int i = 0;
                for (Map.Entry<String, User> entry : mCheckUser.entrySet()) {
                    User user = entry.getValue();
                    b.putSerializable("data" + i, user);
                    i++;
                }
                intent.putExtras(b);
            }
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_ASSIGNED);
        } else if (v == mRemarkView) {
            Intent intent = new Intent(this, EditFieldActivity.class);
            intent.putExtra("type", EditFieldActivity.EDIT_STYLE_REMARK);
            intent.putExtra("remark_old", mRemark.getText().toString());
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_REMARK);
        } else if (v == mRepeatView) {
            Intent intent = new Intent(this, EditFieldActivity.class);
            intent.putExtra("type", EditFieldActivity.EDIT_STYLE_REPEAT);
            intent.putExtra("repeat_old", mCurrentRepeat);
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_REPEAT);
        } else if (v == mLocationView) {
            Intent intent = new Intent(this, EditFieldActivity.class);
            intent.putExtra("type", EditFieldActivity.EDIT_STYLE_LOCATION);
            intent.putExtra("location_old", mLocation.getText().toString());
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_LOCATION);
        } else {
            Intent intent = new Intent(this, EditFieldActivity.class);
            int requestCode = 0;
            if (v == mBottomRemark) {
                intent.putExtra("type", EditFieldActivity.EDIT_STYLE_REMARK);
                requestCode = REQUEST_CODE_FOR_REMARK;
            } else if (v == mBottomRepeat) {
                intent.putExtra("type", EditFieldActivity.EDIT_STYLE_REPEAT);
                requestCode = REQUEST_CODE_FOR_REPEAT;
            } else if (v == mBottomLocation) {
                intent.putExtra("type", EditFieldActivity.EDIT_STYLE_LOCATION);
                requestCode = REQUEST_CODE_FOR_LOCATION;
            }
            startAnimActivityForResult(intent, requestCode);
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mResume = false;
            switch (requestCode) {
            case REQUEST_CODE_FOR_ASSIGNED:
                Bundle b = data.getExtras();
                int size = b.getInt("data_size");
                if (mCheckUser == null) {
                    mCheckUser = new HashMap<String, User>();
                }
                mCheckUser.clear();
                for (int i = 0; i < size; i++) {
                    User u = (User) b.getSerializable("data" + i);
                    mCheckUser.put(u.getPhoneNumber(), u);
                }
                String names = "";
                int i = 0;
                for (User user : mCheckUser.values()) {
                    names += user.getUsername();
                    if (i != mCheckUser.size() - 1) {
                        names += ",";
                    }
                    i++;
                }
                mHandler.sendEmptyMessage(1);
                break;
            case REQUEST_CODE_FOR_REMARK:
                String remark = data.getStringExtra("remark");
                if (remark == null || "".equals(remark)) {
                    mRemarkView.setVisibility(View.GONE);
                    mBottomRemark.setVisibility(View.VISIBLE);
                } else {
                    mRemarkView.setVisibility(View.VISIBLE);
                    mBottomRemark.setVisibility(View.GONE);
                }
                mRemark.setText(remark);
                break;
            case REQUEST_CODE_FOR_REPEAT:
                mCurrentRepeat = data.getIntExtra("repeat",
                        Task.TASK_REPEAT_TYLE_NO);
                if (mCurrentRepeat == Task.TASK_REPEAT_TYLE_NO) {
                    mRepeatView.setVisibility(View.GONE);
                    mBottomRepeat.setVisibility(View.VISIBLE);
                } else {
                    mRepeatView.setVisibility(View.VISIBLE);
                    mBottomRepeat.setVisibility(View.GONE);
                }
                updateRepeatView();
                break;
            case REQUEST_CODE_FOR_LOCATION:
                String location = data.getStringExtra("location");
                if (location == null || "".equals(location)) {
                    mLocationView.setVisibility(View.GONE);
                    mBottomLocation.setVisibility(View.VISIBLE);
                } else {
                    mLocationView.setVisibility(View.VISIBLE);
                    mBottomLocation.setVisibility(View.GONE);
                }
                mLocation.setText(location);
                break;
            }
        }
    }
}
