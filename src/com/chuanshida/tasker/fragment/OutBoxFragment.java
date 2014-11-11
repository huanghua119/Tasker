package com.chuanshida.tasker.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.TaskListAdapter;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.manager.UserManager;
import com.chuanshida.tasker.ui.MainActivity;
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
    private TaskListAdapter mTaskListAdapter;

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
        mList = TempData.createTempTaskData(getActivity());
        mListTask = (XListView) findViewById(R.id.list_box);
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
        User user = mUserManager.getCurrentUser();
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

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
