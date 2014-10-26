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
        Button button = ViewHolder.get(view, R.id.btn);
        button.setText(mMyFriend ? R.string.assigned_ta : R.string.add_ta);
        return view;
    }

}
