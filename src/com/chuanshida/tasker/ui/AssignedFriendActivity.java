package com.chuanshida.tasker.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.adapter.FriendAdapter;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.TempData;
import com.chuanshida.tasker.view.sortlist.CharacterParser;
import com.chuanshida.tasker.view.sortlist.SideBar;
import com.chuanshida.tasker.view.sortlist.SideBar.OnTouchingLetterChangedListener;
import com.chuanshida.tasker.view.sortlist.SortModel;
import com.chuanshida.tasker.view.xlist.XListView;
import com.chuanshida.tasker.view.xlist.XListView.IXListViewListener;

public class AssignedFriendActivity extends BaseActivity implements
        OnClickListener, IXListViewListener, OnItemClickListener {

    private XListView mListFriend;
    private List<User> mList;
    private FriendAdapter mAdapter;
    private TextView mComplete;
    private SideBar mSideBar;
    private TextView mDialog;

    private CharacterParser mCharacterParser;
    private List<SortModel> mSourceDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigned_friend_view);
        init();
    }

    private void init() {
        mList = TempData.createTempMyFriend(this);
        mCharacterParser = CharacterParser.getInstance();
        mSourceDateList = filledData(mList);
        Collections.sort(mSourceDateList, new PinyinComparator());
        mListFriend = (XListView) findViewById(R.id.list_friend);
        mListFriend.setPullLoadEnable(false);
        mListFriend.setPullRefreshEnable(false);
        mListFriend.setXListViewListener(this);
        mListFriend.pullRefreshing();
        mAdapter = new FriendAdapter(this, mSourceDateList);
        mListFriend.setAdapter(mAdapter);
        mListFriend.setOnItemClickListener(this);
        mComplete = (TextView) findViewById(R.id.complete);
        mComplete.setOnClickListener(this);
        mSideBar = (SideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
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
            setResult(Activity.RESULT_OK, data);
            finish();
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
}
