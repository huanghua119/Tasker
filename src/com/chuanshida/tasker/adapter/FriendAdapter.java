package com.chuanshida.tasker.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    public FriendAdapter(Context context, List<SortModel> list) {
        super(context, list);
        mMainThreadHandler = new Handler(context.getApplicationContext()
                .getMainLooper());
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.friend_item_view, null);
        }
        SortModel model = list.get(position);
        final User user = model.getUser();
        ImageView userPhoto = ViewHolder.get(view, R.id.user_photo);
        TextView userName = ViewHolder.get(view, R.id.user_name);
        userName.setText(user.getUsername());
        Button addFriend = ViewHolder.get(view, R.id.add_friend);
        addFriend.setText(R.string.add_friend);
        addFriend.setBackgroundResource(R.drawable.verify_btn__bg);
        addFriend.setTextColor(mContext.getResources().getColor(
                R.color.send_verify_color));
        addFriend.setVisibility(View.VISIBLE);
        Button assignedTa = ViewHolder.get(view, R.id.assigned_ta);
        assignedTa.setVisibility(View.GONE);
        setOnInViewClickListener(R.id.user_photo,
                new onInternalClickListener() {
                    @Override
                    public void OnClickListener(View parentV, View v,
                            Integer position, Object values) {
                        Intent intent = new Intent(mContext,
                                UserDetailActivity.class);
                        intent.putExtra("user", user);
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

}
