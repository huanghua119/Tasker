package com.chuanshida.tasker.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.ui.BaseActivity;
import com.chuanshida.tasker.ui.UserDetailActivity;
import com.chuanshida.tasker.util.ViewHolder;

public class LikePeopleAdapter extends BaseListAdapter<User> {

    protected Handler mMainThreadHandler;

    public LikePeopleAdapter(Context context, List<User> list) {
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
        List<User> list = getList();
        final User user = list.get(position);
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
                        Bundle b = new Bundle();
                        b.putSerializable("user", user);
                        intent.putExtras(b);
                        ((BaseActivity) mContext).startAnimActivity(intent);
                    }
                });
        return view;
    }
}
