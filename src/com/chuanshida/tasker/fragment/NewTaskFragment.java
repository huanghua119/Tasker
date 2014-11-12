package com.chuanshida.tasker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.manager.UserManager;
import com.chuanshida.tasker.ui.AssignedFriendActivity;
import com.chuanshida.tasker.ui.MainActivity;

/***
 * 新建任务
 * 
 * @author huanghua
 * 
 */
public class NewTaskFragment extends FragmentBase implements OnClickListener {

    private UserManager mUserManager;
    private TextView mBack;
    private User mToUser;
    private TextView mAssigned;
    private SparseArray<User> mCheckUser;

    private static final int REQUEST_CODE_FOR_ASSIGNED = 1;

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
        mAssigned = (TextView) findViewById(R.id.new_assigned);
        mAssigned.setOnClickListener(this);
        mAssigned.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            mToUser = (User) getArguments().getSerializable("user");
        }
        if (mToUser != null) {
            mAssigned.setText(mToUser.getUsername());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mToUser = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            MainActivity activity = (MainActivity) getActivity();
            activity.exitFragment();
        } else if (v == mAssigned) {
            Intent intent = new Intent(getActivity(),
                    AssignedFriendActivity.class);
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_ASSIGNED);
        }
    }
}
