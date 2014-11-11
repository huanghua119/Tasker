package com.chuanshida.tasker.ui;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.LikePeopleAdapter;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.TempData;
import com.chuanshida.tasker.view.xlist.XListView;
import com.chuanshida.tasker.view.xlist.XListView.IXListViewListener;

public class AddFriendActivity extends BaseActivity implements OnClickListener,
        IXListViewListener, OnItemClickListener {

    private XListView mListTask;
    private List<User> mList;
    private LikePeopleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends);
        init();
    }

    private void init() {
        mList = TempData.createTempMyFriend(this);
        mListTask = (XListView) findViewById(R.id.list_like_people);
        mListTask.setPullLoadEnable(false);
        mListTask.setPullRefreshEnable(false);
        mListTask.setXListViewListener(this);
        mListTask.pullRefreshing();
        mAdapter = new LikePeopleAdapter(this, mList);
        mListTask.setAdapter(mAdapter);
        mListTask.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateInfoUI();
    }

    private void updateInfoUI() {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

}
