package com.chuanshida.tasker.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.TempData;
import com.chuanshida.tasker.util.ViewHolder;
import com.chuanshida.tasker.view.xlist.XListView.IXListViewListener;

/***
 * 任务日历
 * 
 * @author huanghua
 * 
 */
public class FriendsFragment extends FragmentBase implements
        IXListViewListener, View.OnClickListener, OnItemClickListener {

    private ExpandableListView mAllFriendList;

    private List<List<User>> mFriendGroup = new ArrayList<List<User>>();
    private List<User> mMyList = new ArrayList<User>();
    private List<User> mNoAddList = new ArrayList<User>();

    private BaseExpandableListAdapter mAllFriendListAdapter = new BaseExpandableListAdapter() {

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.friend_group_view, null);
            }
            TextView groupName = ViewHolder.get(view, R.id.group_name);
            groupName.setText(groupPosition == 0 ? R.string.your_friend
                    : R.string.no_add_friend);
            return view;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return mFriendGroup.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mFriendGroup.get(groupPosition);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mFriendGroup.get(groupPosition).size();
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.friend_item_view, null);
            }
            User user = getChild(groupPosition, childPosition);
            TextView userName = ViewHolder.get(view, R.id.user_name);
            userName.setText(user.getUsername());
            userName.setTextColor(getResources().getColor(
                    R.color.task_name_color));
            Button addFriend = ViewHolder.get(view, R.id.add_friend);
            if (groupPosition == 1) {
                if (childPosition == getChildrenCount(groupPosition) - 1) {
                    addFriend.setText(R.string.invite_ta);
                    addFriend.setBackgroundResource(R.drawable.invite_btn_bg);
                    addFriend.setTextColor(getResources().getColor(
                            R.color.invite_friend_color));
                    userName.setTextColor(getResources().getColor(
                            R.color.invite_friend_color));
                } else {
                    addFriend.setText(R.string.add_friend);
                    addFriend.setBackgroundResource(R.drawable.verify_btn__bg);
                    addFriend.setTextColor(getResources().getColor(
                            R.color.send_verify_color));
                    userName.setTextColor(getResources().getColor(
                            R.color.task_name_color));
                }
                addFriend.setVisibility(View.VISIBLE);
            } else {
                addFriend.setVisibility(View.GONE);
            }
            return view;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public User getChild(int groupPosition, int childPosition) {
            return mFriendGroup.get(groupPosition).get(childPosition);
        }
    };

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
        mNoAddList = TempData.createTempNewFriend(getActivity());
        mFriendGroup.add(mMyList);
        mFriendGroup.add(mNoAddList);

        mAllFriendList = (ExpandableListView) findViewById(R.id.list_friends);
        mAllFriendList.setAdapter(mAllFriendListAdapter);
        mAllFriendList
                .setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent,
                            View v, int groupPosition, long id) {
                        return true;
                    }
                });
        for (int i = 0; i < mFriendGroup.size(); i++) {
            mAllFriendList.expandGroup(i);
        }
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
