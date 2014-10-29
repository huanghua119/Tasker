package com.chuanshida.tasker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.manager.UserManager;
import com.chuanshida.tasker.ui.MainActivity;

/***
 * 任务日历
 * 
 * @author huanghua
 * 
 */
public class NewTaskFragment extends FragmentBase implements OnClickListener {

    private UserManager mUserManager;
    private TextView mBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_new_task, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserManager = UserManager.getInstance(getActivity());
        init();
    }

    private void init() {
        mBack = (TextView) findViewById(R.id.btn_cancel);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = mUserManager.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            MainActivity activity = (MainActivity) getActivity();
            activity.exitNewTaskFragment();
        }
    }
}
