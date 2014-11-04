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

/***
 * 任务日历
 * 
 * @author huanghua
 * 
 */
public class MeFragment extends FragmentBase implements OnClickListener {

    private UserManager mUserManager;
    private TextView mUserName;
    private TextView mUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_me, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mUserManager = UserManager.getInstance(getActivity());
        init();
    }

    private void init() {
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserId = (TextView) findViewById(R.id.user_id);
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = mUserManager.getCurrentUser();
        if (user != null) {
            mUserName.setText(user.getUsername());
            mUserId.setText(user.getPhoneNumber());
        }
    }

    @Override
    public void onClick(View v) {

    }
}
