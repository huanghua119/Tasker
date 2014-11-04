package com.chuanshida.tasker.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.TaskListAdapter;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.timessquare.CalendarViewPagerAdapter;
import com.chuanshida.tasker.timessquare.CalendarViewPagerAdapter.FluentInitializer;
import com.chuanshida.tasker.timessquare.CalendarViewPagerAdapter.SelectionMode;
import com.chuanshida.tasker.util.TempData;
import com.chuanshida.tasker.view.xlist.XListView;
import com.chuanshida.tasker.view.xlist.XListView.IXListViewListener;

/***
 * 任务日历
 * 
 * @author huanghua
 * 
 */
public class CalendarTaskFragment extends FragmentBase implements
        IXListViewListener, View.OnClickListener, OnItemClickListener {

    public static final int MAX_YEAR = 2050;
    public static final int MIX_YEAR = 2000;
    private ViewPager mPager;
    private CalendarViewPagerAdapter mPagerAdapter;
    private FluentInitializer mPageFluent;
    private TextView mCalendarMonth;
    private View mCalendarHead;
    private View mCalendarBottom;
    private XListView mDayTask;
    private TaskListAdapter mTaskListAdapter;
    private List<Task> mList = new ArrayList<Task>();
    private DatePickerDialog mSelectDateDialog = null;
    private boolean mDelayAnim = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            Date date = new Date();
            Time time = new Time();
            time.set(dayOfMonth, monthOfYear, year);
            date.setTime(time.toMillis(true));
            mDelayAnim = true;
            mPageFluent.withSelectedDate(date);
        }
    };

    private CalendarViewPagerAdapter.OnDateSelectedListener mDateSelectedListener = new CalendarViewPagerAdapter.OnDateSelectedListener() {

        @Override
        public void onDateUnselected(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            ShowToastOld(" onDateUnselected:" + c.get(Calendar.MONTH)
                    + c.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onDateSelected(Date date) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            ShowToastOld(" onDateSelected:" + c.get(Calendar.MONTH)
                    + c.get(Calendar.DAY_OF_MONTH));
        }
    };

    // 月份显示切换事件
    private class SimplePageChangeListener extends
            ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            mCalendarMonth.setText(mPagerAdapter.getCurrentDateLabel());
            int delay = mDelayAnim ? 300 : 0;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int bottom = mPagerAdapter.getMonthViewBottom();
                    if (bottom != 0) {
                        final float fromY =  mCalendarBottom.getY();
                        final float toY = bottom + mCalendarMonth.getHeight()
                                + mCalendarHead.getHeight();
                        ValueAnimator animation = ValueAnimator.ofFloat(fromY, toY);
                        animation.setDuration(200);
                        animation.addUpdateListener(new AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (Float)animation.getAnimatedValue();
                                mCalendarBottom.setY(value);
                            }
                        });
                        animation.start();
                    }
                    mDelayAnim = false;
                }
            }, delay);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_calendar, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        mCalendarMonth = (TextView) findViewById(R.id.calendar_month);
        mCalendarHead = findViewById(R.id.calendar_head_view);
        mCalendarBottom = findViewById(R.id.calendar_bottom_view);
        mPager = (ViewPager) findViewById(R.id.pager);
        try {
            mPagerAdapter = new CalendarViewPagerAdapter(
                    getChildFragmentManager(), getActivity());
        } catch (NoSuchMethodError e) {
            mPagerAdapter = new CalendarViewPagerAdapter(getFragmentManager(),
                    getActivity());
        }
        final Calendar lastYear = Calendar.getInstance();
        int last = MIX_YEAR - lastYear.get(Calendar.YEAR);
        lastYear.add(Calendar.YEAR, last);
        final Calendar nextYear = Calendar.getInstance();
        int next = MAX_YEAR - lastYear.get(Calendar.YEAR);
        nextYear.add(Calendar.YEAR, next);
        mPageFluent = mPagerAdapter
                .init(lastYear.getTime(), nextYear.getTime(), mPager)
                .inMode(SelectionMode.SINGLE).withSelectedDate(new Date());
        mCalendarMonth.setText(mPagerAdapter.getCurrentDateLabel());
        mPagerAdapter.setOnDateSelectedListener(mDateSelectedListener);
        mPager.setOnPageChangeListener(new SimplePageChangeListener());

        mList = TempData.createTempDayTaskData(getActivity());
        mDayTask = (XListView) findViewById(R.id.list_task);
        mDayTask.setPullLoadEnable(false);
        mDayTask.setPullRefreshEnable(false);
        mDayTask.setXListViewListener(this);
        mTaskListAdapter = new TaskListAdapter(getActivity(), mList);
        mDayTask.setAdapter(mTaskListAdapter);
        mDayTask.pullRefreshing();
        mDayTask.setOnItemClickListener(this);
        mCalendarMonth.setOnClickListener(this);

    }

    private void showSelectDateDialog() {
        Calendar today = Calendar.getInstance();
        today.setTime(mPagerAdapter.getCurrentDate());
        mSelectDateDialog = new DatePickerDialog(getActivity(), mDateListener,
                today.get(Calendar.YEAR), today.get(Calendar.MARCH),
                today.get(Calendar.DAY_OF_MONTH));
        mSelectDateDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == mCalendarMonth) {
            showSelectDateDialog();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

}
