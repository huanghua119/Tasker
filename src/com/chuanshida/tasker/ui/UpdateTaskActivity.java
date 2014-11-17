package com.chuanshida.tasker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.User;

public class UpdateTaskActivity extends BaseActivity implements OnClickListener {

    private EditText mTaskName;
    private Task mCurrentTask;

    private View mAssignedView;
    private View mFinalDateView;

    private TextView mAssigned;
    private ImageButton mBottomRemark;
    private ImageButton mBottomPic;
    private ImageButton mBottomVoice;
    private ImageButton mBottomRepeat;
    private ImageButton mBottomLocation;

    private SparseArray<User> mCheckUser;

    private static final int REQUEST_CODE_FOR_ASSIGNED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_update_view);
        mCurrentTask = (Task) getIntent().getExtras().getSerializable("task");
        init();
    }

    private void init() {
        mTaskName = (EditText) findViewById(R.id.task_name);
        mAssignedView = findViewById(R.id.new_assigned_view);
        mAssignedView.setOnClickListener(this);
        mAssigned = (TextView) findViewById(R.id.new_assigned);
        mAssigned.setText("");
        mFinalDateView = findViewById(R.id.final_date_view);

        mBottomRemark = (ImageButton) findViewById(R.id.task_bottom_remark);
        mBottomPic = (ImageButton) findViewById(R.id.task_bottom_pic);
        mBottomVoice = (ImageButton) findViewById(R.id.task_bottom_voice);
        mBottomRepeat = (ImageButton) findViewById(R.id.task_bottom_repeat);
        mBottomLocation = (ImageButton) findViewById(R.id.task_bottom_location);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        mTaskName.setText(mCurrentTask.getName());
        mTaskName.setEnabled(false);
    }

    public void onComplete(View v) {
    }

    @Override
    public void onClick(View v) {
        if (v == mAssignedView) {
            Intent intent = new Intent(this, AssignedFriendActivity.class);
            if (mCheckUser != null && mCheckUser.size() > 0) {
                Bundle b = new Bundle();
                b.putInt("data_size", mCheckUser.size());
                for (int i = 0; i < mCheckUser.size(); i++) {
                    int key = mCheckUser.keyAt(i);
                    User user = mCheckUser.get(key);
                    b.putSerializable("data" + i, user);
                }
                intent.putExtras(b);
            }
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_ASSIGNED);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case REQUEST_CODE_FOR_ASSIGNED:
            if (resultCode == Activity.RESULT_OK) {
                Bundle b = data.getExtras();
                int size = b.getInt("data_size");
                if (mCheckUser == null) {
                    mCheckUser = new SparseArray<User>();
                }
                mCheckUser.clear();
                for (int i = 0; i < size; i++) {
                    User u = (User) b.getSerializable("data" + i);
                    mCheckUser.put(i, u);
                }
                String names = "";
                for (int i = 0; i < mCheckUser.size(); i++) {
                    int key = mCheckUser.keyAt(i);
                    User user = mCheckUser.get(key);
                    names += user.getUsername();
                    if (i != mCheckUser.size() - 1) {
                        names += ",";
                    }
                }
                mAssigned.setText(names);
            }
            break;
        }
    }
}
