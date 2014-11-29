package com.chuanshida.tasker.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.TaskListAdapter;
import com.chuanshida.tasker.bean.TaskToUser;
import com.chuanshida.tasker.ui.TaskDetailActivity;
import com.chuanshida.tasker.ui.UpdateTaskActivity;
import com.chuanshida.tasker.util.TempData;
import com.chuanshida.tasker.view.xlist.XListView;
import com.chuanshida.tasker.view.xlist.XListView.IXListViewListener;

/***
 * 列表任务日历
 * 
 * @author huanghua
 * 
 */
public class ListTaskFragment extends FragmentBase implements
        IXListViewListener, View.OnClickListener, OnItemClickListener {

    private XListView mListTask;
    private TaskListAdapter mTaskListAdapter;
    private List<TaskToUser> mList = new ArrayList<TaskToUser>();
    private View mLoadView = null;
    private ImageView mLoadImage = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        TempData.createTempTaskData(getActivity());
        mList = TempData.mTempTaskToUserList;
        mListTask = (XListView) findViewById(R.id.list_task);
        mListTask.setPullLoadEnable(false);
        mListTask.setPullRefreshEnable(false);
        mListTask.setXListViewListener(this);
        mListTask.pullRefreshing();
        mTaskListAdapter = new TaskListAdapter(getActivity(), mList);
        mListTask.setAdapter(mTaskListAdapter);
        mListTask.setOnItemClickListener(this);
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
        int count = mListTask.getHeaderViewsCount();
        TaskToUser task = mList.get(arg2 - count);
        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        intent.putExtra("task", task.getTask());
        startAnimActivity(intent);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

}
