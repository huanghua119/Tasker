package com.chuanshida.tasker.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.chuanshida.tasker.calendar.CalendarPagerAdapter;
import com.chuanshida.tasker.calendar.LunarCalendar;
import com.chuanshida.tasker.timessquare.CalendarPickerView;
import com.chuanshida.tasker.timessquare.CalendarPickerView.SelectionMode;
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

    private ViewPager mPager;
    private CalendarPickerView calendar;
    private CalendarPagerAdapter mPagerAdapter;
    private TextView mCalendarMonth;
    private XListView mDayTask;
    private TaskListAdapter mTaskListAdapter;
    private List<Task> mList = new ArrayList<Task>();
    private DatePickerDialog mSelectDateDialog = null;

    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            int offset = (year - LunarCalendar.getMinYear()) * 12 + monthOfYear;
            LunarCalendar.DATE_SELECT_OFFSET = offset + dayOfMonth;
            mPager.setCurrentItem(offset, true);
        }
    };

    // 月份显示切换事件
    private class SimplePageChangeListener extends
            ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            // set title year month
            StringBuilder title = new StringBuilder();
            title.append(LunarCalendar.getMinYear() + (position / 12));
            title.append(getResources().getString(R.string.calendar_year));
            int month = (position % 12) + 1;
            if (month < 10) {
                title.append('0');
            }
            title.append(month
                    + getResources().getString(R.string.calendar_month));
            mCalendarMonth.setText(title);
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
/*        mPager = (ViewPager) findViewById(R.id.pager);
        try {
            mPagerAdapter = new CalendarPagerAdapter(getChildFragmentManager());
        } catch (NoSuchMethodError e) {
            mPagerAdapter = new CalendarPagerAdapter(getFragmentManager());
        }
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new SimplePageChangeListener());
        mPager.setCurrentItem(getTodayMonthIndex(), false);*/

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

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(SelectionMode.SINGLE) //
                .withSelectedDate(new Date());
    }

    private int getTodayMonthIndex() {
        Calendar today = Calendar.getInstance();
        int offset = (today.get(Calendar.YEAR) - LunarCalendar.getMinYear())
                * 12 + today.get(Calendar.MONTH);
        return offset;
    }

    private void showSelectDateDialog() {
        Calendar today = Calendar.getInstance();
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
