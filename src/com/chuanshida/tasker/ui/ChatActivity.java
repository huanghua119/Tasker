package com.chuanshida.tasker.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.NewMessage;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.TempData;

public class ChatActivity extends BaseActivity implements OnClickListener {

    private LayoutInflater mInFlater;
    private User mChatUser = null;
    private TextView mUserName = null;
    private TextView mSend = null;
    private EditText mContext = null;
    private ListView mChatList = null;
    private List<NewMessage> mAllMessage = null;
    private PopupWindow mAlertMessage = null;
    private TextView mNewMessage = null;
    private int mStatusHeight = 0;
    private int mWindowWidth = 0;

    public static final int HANDLER_MEG_REFRESHLIST = 1;
    public static final int HANDLER_MEG_FINISH = 2;

    private Runnable mDismissWindow = new Runnable() {
        @Override
        public void run() {
            if (mAlertMessage != null && mAlertMessage.isShowing()) {
                mAlertMessage.dismiss();
            }
        }
    };

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = mInFlater.inflate(R.layout.chat_list_item, null);
            View self = v.findViewById(R.id.self_chat);
            View it = v.findViewById(R.id.it_chat);
            NewMessage message = mAllMessage.get(position);
            User u = message.getUser();
            if (u.getPhoneNumber().equals(
                    userManager.getCurrentUser().getPhoneNumber())) {
                it.setVisibility(View.GONE);
                self.setVisibility(View.VISIBLE);
                TextView name = (TextView) self
                        .findViewById(R.id.self_chat_name);
                TextView date = (TextView) self
                        .findViewById(R.id.self_chat_time);
                TextView contextView = (TextView) self
                        .findViewById(R.id.self_chat_context);
                name.setText(message.getUser().getUsername());
                SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
                String time = sdf.format(message.getMessageDate());
                date.setText(time);
                contextView.setText(message.getContext());
            } else {
                self.setVisibility(View.GONE);
                it.setVisibility(View.VISIBLE);
                TextView name2 = (TextView) it.findViewById(R.id.it_chat_name);
                TextView date2 = (TextView) it.findViewById(R.id.it_chat_time);
                TextView contextView = (TextView) it
                        .findViewById(R.id.it_chat_context);
                name2.setText(message.getUser().getUsername());
                SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
                String time = sdf.format(message.getMessageDate());
                date2.setText(time);
                contextView.setText(message.getContext());
            }
            return v;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mAllMessage.get(position);
        }

        @Override
        public int getCount() {
            return mAllMessage.size();
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        mInFlater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAllMessage = new ArrayList<NewMessage>();
        mChatUser = (User) getIntent().getExtras().getSerializable("user");
        refreshList();
        init();
    }

    private void init() {
        mUserName = (TextView) findViewById(R.id.title_name);
        mUserName.setText(mChatUser.getUsername());
        mSend = (TextView) findViewById(R.id.send_context);
        mSend.setOnClickListener(this);
        mContext = (EditText) findViewById(R.id.chat_context);
        mChatList = (ListView) findViewById(R.id.chatList);
        mChatList.setAdapter(mAdapter);
        mChatList.setSelection(mAllMessage.size());
    }

    private void refreshList() {
        mAllMessage = TempData.createTempMessage(this, mChatUser);
    }

    @Override
    public void onClick(View v) {
        if (v == mSend) {
            String context = mContext.getText().toString();
            if (context != null && !"".equals(context)) {
                //refreshList();
                NewMessage message = new NewMessage();
                message.setMessageDate(new Date());
                message.setUser(mChatUser);
                message.setContext(context);
                mAllMessage.add(message);
                mAdapter.notifyDataSetInvalidated();
                mChatList.setSelection(mAllMessage.size());
                mContext.setText("");
            }
        } else if (v == mNewMessage) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChatList != null) {
            mChatList.setSelection(mAllMessage.size());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getWindowData() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        mStatusHeight = frame.top;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWindowWidth = dm.widthPixels;
    }

}
