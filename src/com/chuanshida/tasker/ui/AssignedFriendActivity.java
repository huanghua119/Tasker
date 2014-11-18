package com.chuanshida.tasker.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.FriendAdapter;
import com.chuanshida.tasker.adapter.FriendAdapter.OnCheckBoxClickListener;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.TempData;
import com.chuanshida.tasker.view.sortlist.CharacterParser;
import com.chuanshida.tasker.view.sortlist.SideBar;
import com.chuanshida.tasker.view.sortlist.SideBar.OnTouchingLetterChangedListener;
import com.chuanshida.tasker.view.sortlist.SortModel;
import com.chuanshida.tasker.view.xlist.XListView;
import com.chuanshida.tasker.view.xlist.XListView.IXListViewListener;

public class AssignedFriendActivity extends BaseActivity implements
        OnClickListener, IXListViewListener, OnItemClickListener,
        OnCheckBoxClickListener {

    private XListView mListFriend;
    private List<User> mList;
    private FriendAdapter mAdapter;
    private TextView mComplete;
    private SideBar mSideBar;
    private TextView mDialog;
    private LinearLayout mHeadGroup;
    private Map<String, User> mCheckUser;

    private CharacterParser mCharacterParser;
    private List<SortModel> mSourceDateList;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHeadGroup.removeAllViews();
            int i = 0;
            for (User user : mCheckUser.values()) {
                ImageView img = new ImageView(AssignedFriendActivity.this);
                img.setOnClickListener(AssignedFriendActivity.this);
                img.setTag(user);
                LayoutParams layout = new LayoutParams(100, 100);
                int left = 5;
                int right = 5;
                if (i == 0) {
                    left = 0;
                } else if (i == mCheckUser.size() - 1) {
                    right = 0;
                }
                layout.setMargins(left, 0, right, 0);
                img.setLayoutParams(layout);
                if (user.getAvatar() != null && !"".equals(user.getAvatar())) {
                } else {
                    img.setBackgroundResource(R.drawable.login_head);
                }
                mHeadGroup.addView(img);
                i++;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_friend_view);
        init();
    }

    private void init() {
        if (mCheckUser == null) {
            mCheckUser = new HashMap<String, User>();
        }
        mCheckUser.clear();
        mList = TempData.createTempMyFriend(this);
        mCharacterParser = CharacterParser.getInstance();
        mSourceDateList = filledData(mList);
        Collections.sort(mSourceDateList, new PinyinComparator());
        mListFriend = (XListView) findViewById(R.id.list_friend);
        mListFriend.setPullLoadEnable(false);
        mListFriend.setPullRefreshEnable(false);
        mListFriend.setXListViewListener(this);
        mListFriend.pullRefreshing();
        mAdapter = new FriendAdapter(this, mSourceDateList, this);
        mListFriend.setAdapter(mAdapter);
        mListFriend.setOnItemClickListener(this);
        mComplete = (TextView) findViewById(R.id.complete);
        mComplete.setOnClickListener(this);
        mSideBar = (SideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        mHeadGroup = (LinearLayout) findViewById(R.id.head_group);
        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListFriend.setSelection(position);
                }
            }
        });
        Bundle b = getIntent().getExtras();
        if (b != null) {
            int size = b.getInt("data_size");
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    User u = (User) b.getSerializable("data" + i);
                    mCheckUser.put(u.getPhoneNumber(), u);
                }
                mHandler.sendEmptyMessage(1);
                mAdapter.setCheckUserList(mCheckUser);
            }
        }
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
        if (v == mComplete) {
            Intent data = new Intent();
            Bundle b = new Bundle();
            b.putInt("data_size", mCheckUser.size());
            int i = 0;
            for (User user : mCheckUser.values()) {
                b.putSerializable("data" + i, user);
                i++;
            }
            data.putExtras(b);
            setResult(Activity.RESULT_OK, data);
            finish();
        } else {
            if (v instanceof ImageView) {
                User user = (User) v.getTag();
                mHeadGroup.removeView(v);
                mAdapter.checkItemForPosition(user, false);
            }
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        int count = mListFriend.getHeaderViewsCount();
        int key = arg2 - count;
        User user = ((SortModel) mAdapter.getItem(key)).getUser();
        boolean isChecked = mCheckUser.get(user.getPhoneNumber()) == null;
        mAdapter.checkItemForPosition(user, isChecked);
    }

    private List<SortModel> filledData(List<User> date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setUser(date.get(i));
            String pinyin = mCharacterParser.getSelling(date.get(i)
                    .getUsername());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;

    }

    class PinyinComparator implements Comparator<SortModel> {

        public int compare(SortModel o1, SortModel o2) {
            if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }

    }

    @Override
    public void onCheckBoxClickListener(Map<String, User> checkUser) {
        if (mCheckUser == null) {
            mCheckUser = new HashMap<String, User>();
        }
        mCheckUser.clear();
        for (Map.Entry<String, User> entry : checkUser.entrySet()) {
            String key = entry.getKey();
            User user = entry.getValue();
            mCheckUser.put(key, user);
        }
        mHandler.sendEmptyMessage(1);
    }
}
