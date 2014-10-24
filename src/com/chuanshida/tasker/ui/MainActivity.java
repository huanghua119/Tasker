package com.chuanshida.tasker.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chuanshida.tasker.CustomApplcation;
import com.chuanshida.tasker.R;
import com.chuanshida.tasker.fragment.CalendarFragment;
import com.chuanshida.tasker.fragment.FindFragment;
import com.chuanshida.tasker.fragment.FriendsFragment;
import com.chuanshida.tasker.fragment.MeFragment;
import com.chuanshida.tasker.util.SharePreferenceUtil;

public class MainActivity extends BaseActivity {

    private Button[] mTabs;
    private Fragment[] fragments;
    private int mIndex;
    private int mCurrentTabIndex;

    private CalendarFragment mCalendarFrament;
    private FriendsFragment mFriendsFrament;
    private FindFragment mFindFrament;
    private MeFragment mMeFragment;
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
        mCalendarFrament = new CalendarFragment();
        mFriendsFrament = new FriendsFragment();
        mFindFrament = new FindFragment();
        mMeFragment = new MeFragment();
        fragments = new Fragment[] { mCalendarFrament, mFriendsFrament,
                mFindFrament, mMeFragment };
        FragmentTransaction trx = getFragmentManager().beginTransaction();
        trx.add(R.id.fragment_container, mCalendarFrament);
        trx.add(R.id.fragment_container, mFriendsFrament);
        trx.add(R.id.fragment_container, mFindFrament);
        trx.add(R.id.fragment_container, mMeFragment);
        trx.commit();
        getFragmentManager().beginTransaction().hide(mFriendsFrament)
                .hide(mMeFragment).hide(mFindFrament).show(mCalendarFrament)
                .commit();
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
            trx.hide(fragments[mCurrentTabIndex]);
            if (!fragments[mIndex].isAdded()) {
                trx.add(R.id.fragment_container, fragments[mIndex]);
            }
            trx.show(fragments[mIndex]).commit();
        }
        mTabs[mCurrentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[mIndex].setSelected(true);
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

}
