package com.chuanshida.tasker.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.OutBoxListAdapter;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.manager.UserManager;
import com.chuanshida.tasker.ui.MainActivity;
import com.chuanshida.tasker.ui.UpdateTaskActivity;
import com.chuanshida.tasker.util.TempData;
import com.chuanshida.tasker.view.xlist.XListView;
import com.chuanshida.tasker.view.xlist.XListView.IXListViewListener;

/***
 * 发件箱
 * 
 * @author huanghua
 * 
 */
public class OutBoxFragment extends FragmentBase implements OnClickListener,
        IXListViewListener, OnItemClickListener {

    private UserManager mUserManager;
    private TextView mBack;
    private XListView mListTask;
    private List<Task> mList = new ArrayList<Task>();
    private OutBoxListAdapter mOutBoxListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_out_box, container, false);
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
        mList.clear();
        for (Task task : TempData.mTempTaskList.values()) {
            mList.add(task);
        }
        Collections.sort(mList, new DateComparator());
        mListTask = (XListView) findViewById(R.id.list_box);
        mListTask.setPullLoadEnable(false);
        mListTask.setPullRefreshEnable(false);
        mListTask.setXListViewListener(this);
        mListTask.pullRefreshing();
        mOutBoxListAdapter = new OutBoxListAdapter(getActivity(), mList);
        mListTask.setAdapter(mOutBoxListAdapter);
        mListTask.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mList.clear();
        for (Task task : TempData.mTempTaskList.values()) {
            mList.add(task);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            MainActivity activity = (MainActivity) getActivity();
            activity.exitFragment();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        int count = mListTask.getHeaderViewsCount();
        Task task = mList.get(arg2 - count);
        // Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        Intent intent = new Intent(getActivity(), UpdateTaskActivity.class);
        intent.putExtra("update", true);
        intent.putExtra("task", task);
        startAnimActivity(intent);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    class DateComparator implements Comparator<Task> {

        public int compare(Task o1, Task o2) {
            Date date1 = o1.getCreateAt();
            Date date2 = o2.getCreateAt();
            return date1.compareTo(date2);
        }
    }

}
