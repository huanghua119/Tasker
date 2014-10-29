package com.chuanshida.tasker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.ui.MainActivity;

/***
 * 任务日历
 * 
 * @author huanghua
 * 
 */
public class TaskFragment extends FragmentBase implements OnClickListener {

    private ViewPager mPager;
    private RadioGroup mTitleRadioGroup = null;
    private ImageButton mNewTask = null;
    private ViewPagerAdapter mPagerAdapter;
    private ListTaskFragment mListTaskFragment;
    private CalendarTaskFragment mCalendarTaskFragment;
    private static final int TAB_CALENDAR_TASK = 0;
    private static final int TAB_LIST_TASK = 1;
    private static final int TAB_COUNT = 2;

    private RadioGroup.OnCheckedChangeListener mRadioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
            case R.id.radio_calendar:
                mPager.setCurrentItem(TAB_CALENDAR_TASK);
                break;
            case R.id.radio_list:
                mPager.setCurrentItem(TAB_LIST_TASK);
                break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_calendar, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mCalendarTaskFragment = new CalendarTaskFragment();
        mListTaskFragment = new ListTaskFragment();
        try {
            mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        } catch (NoSuchMethodError e) {
            mPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        }
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new PageChangeListener());

        mTitleRadioGroup = (RadioGroup) findViewById(R.id.calendar_radio);
        mTitleRadioGroup.setVisibility(View.VISIBLE);
        mTitleRadioGroup.setOnCheckedChangeListener(mRadioListener);
        mNewTask = (ImageButton) findViewById(R.id.add_task);
        mNewTask.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        int checkId = mTitleRadioGroup.getCheckedRadioButtonId();
        switch (checkId) {
        case R.id.radio_calendar:
            mPager.setCurrentItem(TAB_CALENDAR_TASK, false);
            break;
        case R.id.radio_list:
        default:
            mPager.setCurrentItem(TAB_LIST_TASK, false);
            break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mNewTask) {
            MainActivity activity = (MainActivity) getActivity();
            activity.toNewTaskFragment();
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
            case TAB_CALENDAR_TASK:
                return mCalendarTaskFragment;
            case TAB_LIST_TASK:
                return mListTaskFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    public class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            checkRadio(arg0);
        }

    }

    private void checkRadio(int itemId) {
        switch (itemId) {
        case TAB_CALENDAR_TASK:
            mTitleRadioGroup.check(R.id.radio_calendar);
            break;
        case TAB_LIST_TASK:
            mTitleRadioGroup.check(R.id.radio_list);
            break;
        }
    }

}
