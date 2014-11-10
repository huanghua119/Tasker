package com.chuanshida.tasker.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.User;

public class TaskDetailActivity extends BaseActivity implements OnClickListener {

    private User mCurrentUser = null;
    private Task mCurrentTask = null;
    private ImageView mUserPhoto;
    private TextView mTaskName;
    private TextView mTaskContent;
    private ImageView mTaskVoice;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = userManager.getCurrentUser();
        updateInfoUI();
    }

    private void updateInfoUI() {
        if (mCurrentTask != null) {
            mTaskName.setText(mCurrentTask.getName());
            mTaskContent.setText(mCurrentTask.getContent());
        }
    }

    @Override
    public void onClick(View v) {
    }

}
