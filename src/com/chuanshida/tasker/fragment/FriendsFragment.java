package com.chuanshida.tasker.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.FriendAdapter;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.TempData;
import com.chuanshida.tasker.view.xlist.XListView;
import com.chuanshida.tasker.view.xlist.XListView.IXListViewListener;

/***
 * 任务日历
 * 
 * @author huanghua
 * 
 */
public class FriendsFragment extends FragmentBase implements
        IXListViewListener, View.OnClickListener, OnItemClickListener {

    private XListView mMyFriendList;
    private XListView mNoAddFriend;
    private FriendAdapter mNoAddFriendAdapter;
    private FriendAdapter mFriendAdapter;
    private List<User> mMyList = new ArrayList<User>();
    private List<User> mNoAddList = new ArrayList<User>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_friends, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        mMyList = TempData.createTempMyFriend(getActivity());
        mMyFriendList = (XListView) findViewById(R.id.list_my_friend);
        mMyFriendList.setPullLoadEnable(false);
        mMyFriendList.setPullRefreshEnable(false);
        mMyFriendList.setXListViewListener(this);
        mFriendAdapter = new FriendAdapter(getActivity(), mMyList, true);
        mMyFriendList.setAdapter(mFriendAdapter);
        mMyFriendList.pullRefreshing();
        mMyFriendList.setOnItemClickListener(this);

        mNoAddList = TempData.createTempNewFriend(getActivity());
        mNoAddFriend = (XListView) findViewById(R.id.list_no_add_friend);
        mNoAddFriend.setPullLoadEnable(false);
        mNoAddFriend.setPullRefreshEnable(false);
        mNoAddFriend.setXListViewListener(this);
        mNoAddFriendAdapter = new FriendAdapter(getActivity(), mNoAddList,
                false);
        mNoAddFriend.setAdapter(mNoAddFriendAdapter);
        mNoAddFriend.pullRefreshing();
        mNoAddFriend.setOnItemClickListener(this);
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
