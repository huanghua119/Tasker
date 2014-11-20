package com.chuanshida.tasker.ui;

import java.util.Date;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.util.TempData;

public class NewTaskActivity extends BaseActivity {

    private EditText mTaskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_update_name);
        init();
    }

    private void init() {
        mTaskName = (EditText) findViewById(R.id.task_name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onComplete(View v) {
        String name = mTaskName.getText().toString();
        if (name == null || "".equals(name)) {
            return;
        } else if (TempData.mTempTaskList.get(name) != null) {
            ShowToast(R.string.task_name_same);
        } else {
            hideInputMethod();
            showCompleteDialog(name);
        }
    }

    private void showCompleteDialog(final String title) {
        Dialog dialog = CommonUtils.showCompleteDialog(this, title,
                getString(R.string.complete_create_task),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Task task = new Task();
                        task.setCreateUser(userManager.getCurrentUser());
                        task.setCreateAt(new Date());
                        task.setName(title);
                        // TempData.mTempTaskList.add(task);
                        Intent intent = new Intent(NewTaskActivity.this,
                                UpdateTaskActivity.class);
                        Bundle b = new Bundle();
                        Bundle bundle = getIntent().getExtras();
                        if (bundle != null) {
                            User user = (User) bundle.getSerializable("user");
                            if (user != null) {
                                b.putSerializable("user", user);
                            }
                        }
                        b.putSerializable("task", task);
                        intent.putExtras(b);
                        startAnimActivity(intent);
                        mRunFinishAnim = false;
                        finish();
                    }
                }, false);
        dialog.show();
    }
}
