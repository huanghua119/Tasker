package com.chuanshida.tasker.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.ui.BaseActivity;
import com.chuanshida.tasker.ui.UserDetailActivity;
import com.chuanshida.tasker.util.ViewHolder;
import com.chuanshida.tasker.view.sortlist.SortModel;

public class FriendAdapter extends BaseListAdapter<SortModel> implements
        SectionIndexer {

    protected Handler mMainThreadHandler;
    private Map<String, User> mCheckUser = new HashMap<String, User>();
    private OnCheckBoxClickListener mOnCheckBoxClickListener;

    public interface OnCheckBoxClickListener {
        public void onCheckBoxClickListener(Map<String, User> checkUser);
    }

    public FriendAdapter(Context context, List<SortModel> list,
            OnCheckBoxClickListener onCheckBoxClickListener) {
        super(context, list);
        mOnCheckBoxClickListener = onCheckBoxClickListener;
        mMainThreadHandler = new Handler(context.getApplicationContext()
                .getMainLooper());
    }

    @Override
    public View bindView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.assigned_friend_item_view, null);
        }
        SortModel model = list.get(position);
        final User user = model.getUser();
        ImageView userPhoto = ViewHolder.get(view, R.id.user_photo);
        TextView userName = ViewHolder.get(view, R.id.user_name);
        userName.setText(user.getUsername());
        setOnInViewClickListener(R.id.user_photo,
                new onInternalClickListener() {
                    @Override
                    public void OnClickListener(View parentV, View v,
                            Integer position, Object values) {
                        Intent intent = new Intent(mContext,
                                UserDetailActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("user", user);
                        intent.putExtras(b);
                        ((BaseActivity) mContext).startAnimActivity(intent);
                    }
                });
        TextView title = ViewHolder.get(view, R.id.title_name);
        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            title.setVisibility(View.VISIBLE);
            title.setText(model.getSortLetters());
        } else {
            title.setVisibility(View.GONE);
        }
        CheckBox checkBox = ViewHolder.get(view, R.id.check_box);
        checkBox.setChecked(mCheckUser.get(user.getPhoneNumber()) != null);
        setOnInViewClickListener(R.id.check_box, new onInternalClickListener() {
            @Override
            public void OnClickListener(View parentV, View v, Integer position,
                    Object values) {
                CheckBox box = (CheckBox) v;
                if (box.isChecked()) {
                    mCheckUser.put(user.getPhoneNumber(), user);
                } else {
                    mCheckUser.remove(user.getPhoneNumber());
                }
                if (mOnCheckBoxClickListener != null) {
                    mOnCheckBoxClickListener
                            .onCheckBoxClickListener(mCheckUser);
                }
            }
        });

        return view;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public void checkItemForPosition(User user, boolean checked) {
        if (checked) {
            mCheckUser.put(user.getPhoneNumber(), user);
        } else {
            mCheckUser.remove(user.getPhoneNumber());
        }
        notifyDataSetChanged();
        if (mOnCheckBoxClickListener != null) {
            mOnCheckBoxClickListener.onCheckBoxClickListener(mCheckUser);
        }
    }

    public void setCheckUserList(Map<String, User> list) {
        if (mCheckUser == null) {
            mCheckUser = new HashMap<String, User>();
        }
        mCheckUser.clear();
        mCheckUser.putAll(list);
    }
}
