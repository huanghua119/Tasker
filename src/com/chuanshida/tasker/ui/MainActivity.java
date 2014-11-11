package com.chuanshida.tasker.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import com.chuanshida.tasker.CustomApplcation;
import com.chuanshida.tasker.R;
import com.chuanshida.tasker.fragment.FindFragment;
import com.chuanshida.tasker.fragment.FriendsFragment;
import com.chuanshida.tasker.fragment.MeFragment;
import com.chuanshida.tasker.fragment.NewTaskFragment;
import com.chuanshida.tasker.fragment.OutBoxFragment;
import com.chuanshida.tasker.fragment.TaskFragment;
import com.chuanshida.tasker.util.SharePreferenceUtil;

public class MainActivity extends BaseActivity implements OnFocusChangeListener {

    private Button[] mTabs;
    private Fragment[] fragments;
    private int mIndex;
    private int mCurrentTabIndex;

    private TaskFragment mCalendarFrament;
    private FriendsFragment mFriendsFrament;
    private FindFragment mFindFrament;
    private MeFragment mMeFragment;
    private NewTaskFragment mNewTaskFragment;
    private OutBoxFragment mOutBoxFragment;
    private SharePreferenceUtil mSp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTab();
        if (mSp == null) {
            mSp = CustomApplcation.getInstance().getSpUtil();
        }
    }

    private void initView() {
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_calendar);
        mTabs[1] = (Button) findViewById(R.id.btn_friends);
        mTabs[2] = (Button) findViewById(R.id.btn_find);
        mTabs[3] = (Button) findViewById(R.id.btn_me);
    }

    private void initTab() {
        mCalendarFrament = new TaskFragment();
        mFriendsFrament = new FriendsFragment();
        mFindFrament = new FindFragment();
        mMeFragment = new MeFragment();
        mNewTaskFragment = new NewTaskFragment();
        mOutBoxFragment = new OutBoxFragment();
        fragments = new Fragment[] { mCalendarFrament, mFriendsFrament,
                mFindFrament, mMeFragment, mNewTaskFragment, mOutBoxFragment };
        FragmentTransaction trx = getFragmentManager().beginTransaction();
        trx.add(R.id.fragment_container, mCalendarFrament);
        trx.add(R.id.fragment_container, mFriendsFrament);
        trx.add(R.id.fragment_container, mFindFrament);
        trx.add(R.id.fragment_container, mMeFragment);
        trx.add(R.id.fragment_container, mNewTaskFragment);
        trx.add(R.id.fragment_container, mOutBoxFragment);
        trx.commit();
        getFragmentManager().beginTransaction().hide(mFriendsFrament)
                .hide(mMeFragment).hide(mFindFrament).hide(mNewTaskFragment)
                .hide(mOutBoxFragment).show(mCalendarFrament).commit();
        mTabs[0].setSelected(true);
        mCurrentTabIndex = 0;
    }

    /**
     * button点击事件
     * 
     * @param view
     */
    public void onTabSelect(View view) {
        switch (view.getId()) {
        case R.id.btn_calendar:
            mIndex = 0;
            break;
        case R.id.btn_friends:
            mIndex = 1;
            break;
        case R.id.btn_find:
            mIndex = 2;
            break;
        case R.id.btn_me:
            mIndex = 3;
            break;
        }
        if (mCurrentTabIndex != mIndex) {
            FragmentTransaction trx = getFragmentManager().beginTransaction();
            /*
             * if (mCurrentTabIndex == 4 && mIndex == 0 ) {
             * trx.setCustomAnimations(R.anim.fragment_left_out,
             * R.anim.fragment_left_in); }
             */
            trx.hide(fragments[mCurrentTabIndex]);
            if (!fragments[mIndex].isAdded()) {
                trx.add(R.id.fragment_container, fragments[mIndex]);
            }
            trx.show(fragments[mIndex]).commit();
        }
        if (mCurrentTabIndex == 4 || mCurrentTabIndex == 5) {
            mCurrentTabIndex = 0;
        }
        if (mCurrentTabIndex < 4) {
            mTabs[mCurrentTabIndex].setSelected(false);
            mTabs[mIndex].setSelected(true);
        }
        mCurrentTabIndex = mIndex;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        mRunFinishAnim = false;
        super.finish();
    }

    private static long firstTime;

    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {
        if (firstTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            ShowToastOld(R.string.pass_exit);
        }
        firstTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onLogout(View v) {
        userManager.logout();
        startActivity(new Intent(this, LoginChooseActivity.class));
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    public void toNewTaskFragment() {
        FragmentTransaction trx = getFragmentManager().beginTransaction();
        /*
         * trx.setCustomAnimations(R.anim.fragment_left_in,
         * R.anim.fragment_left_out);
         */
        mIndex = 4;
        trx.hide(fragments[mCurrentTabIndex]);
        if (!fragments[mIndex].isAdded()) {
            trx.add(R.id.fragment_container, fragments[mIndex]);
        }
        trx.show(fragments[mIndex]).commit();
        mCurrentTabIndex = mIndex;
    }

    public void exitFragment() {
        FragmentTransaction trx = getFragmentManager().beginTransaction();
        /*
         * trx.setCustomAnimations(R.anim.fragment_left_out,
         * R.anim.fragment_left_in); }
         */
        mIndex = 0;
        trx.hide(fragments[mCurrentTabIndex]);
        if (!fragments[mIndex].isAdded()) {
            trx.add(R.id.fragment_container, fragments[mIndex]);
        }
        trx.show(fragments[mIndex]).commit();
        mCurrentTabIndex = mIndex;
    }

    public void toOutBoxFragment() {
        FragmentTransaction trx = getFragmentManager().beginTransaction();
        mIndex = 5;
        trx.hide(fragments[mCurrentTabIndex]);
        if (!fragments[mIndex].isAdded()) {
            trx.add(R.id.fragment_container, fragments[mIndex]);
        }
        trx.show(fragments[mIndex]).commit();
        mCurrentTabIndex = mIndex;
    }

    public void onCellClick(View v) {

    }
}
