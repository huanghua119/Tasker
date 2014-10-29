package com.chuanshida.tasker.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.ViewHolder;

public class FriendAdapter extends BaseListAdapter<User> {

    protected Handler mMainThreadHandler;
    private boolean mMyFriend = false;

    public FriendAdapter(Context context, List<User> list, boolean isMy) {
        super(context, list);
        mMainThreadHandler = new Handler(context.getApplicationContext()
                .getMainLooper());
        mMyFriend = isMy;
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
        if (position == getCount() - 1) {
            addFriend.setText(R.string.invite_ta);
            addFriend.setBackgroundResource(R.drawable.invite_btn_bg);
            addFriend.setTextColor(mContext.getResources().getColor(
                    R.color.invite_friend_color));
            userName.setTextColor(mContext.getResources().getColor(
                    R.color.invite_friend_color));
        } else {
            addFriend.setText(R.string.add_friend);
            addFriend.setBackgroundResource(R.drawable.verify_btn__bg);
            addFriend.setTextColor(mContext.getResources().getColor(
                    R.color.send_verify_color));
            userName.setTextColor(mContext.getResources().getColor(
                    R.color.task_name_color));
        }
        if (!mMyFriend) {
            addFriend.setVisibility(View.VISIBLE);
        } else {
            addFriend.setVisibility(View.GONE);
        }
        return view;
    }
}
