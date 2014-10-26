package com.chuanshida.tasker.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.TaskListAdapter;
import com.chuanshida.tasker.bean.Task;
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

    private XListView mDayTask;
    private TaskListAdapter mTaskListAdapter;
    private List<Task> mList = new ArrayList<Task>();

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
        mList = TempData.createTempDayTaskData(getActivity());
        mDayTask = (XListView) findViewById(R.id.list_task);
        mDayTask.setPullLoadEnable(false);
        mDayTask.setPullRefreshEnable(false);
        mDayTask.setXListViewListener(this);
        mTaskListAdapter = new TaskListAdapter(getActivity(), mList);
        mDayTask.setAdapter(mTaskListAdapter);
        mDayTask.pullRefreshing();
        mDayTask.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

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
