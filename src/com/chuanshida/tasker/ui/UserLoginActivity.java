package com.chuanshida.tasker.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.manager.UserManager.UserManagerListener;
import com.chuanshida.tasker.util.CommonUtils;

public class UserLoginActivity extends BaseActivity implements OnClickListener {

    private EditText mUserPhone = null;
    private EditText mUserPass = null;
    private Button mLogin = null;
    private TextView mNowRegiser;
    private TextView mForgotPass;

    private User mCurrentUser = null;
    public static final int DIALOG_NEW_REGISTER = 1;
    public static final int OTHEN_LOGIN_WEIBO = 1;

    @SuppressWarnings("deprecation")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                showDialog(DIALOG_NEW_REGISTER);
                break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_view);
        init();
    }

    private void init() {
        mNowRegiser = (TextView) findViewById(R.id.now_regiser);
        mForgotPass = (TextView) findViewById(R.id.forgot_pass);
        mForgotPass.getPaint().setFlags(
                Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mNowRegiser.getPaint().setFlags(
                Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mForgotPass.setOnClickListener(this);
        mNowRegiser.setOnClickListener(this);
        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(this);
        mUserPhone = (EditText) findViewById(R.id.user_phone);
        mUserPass = (EditText) findViewById(R.id.user_pass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = getUserInfo();
        String phoneNumber = mCurrentUser.getPhoneNumber();
        String password = mCurrentUser.getPassword();
        if (phoneNumber != null && !"".equals(phoneNumber)) {
            mUserPhone.setText(phoneNumber);
        }
        if (password != null && !"".equals(password)) {
            mUserPass.setText(password);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mNowRegiser) {
            Intent intent = new Intent();
            intent.setClass(this, UserRegisterActivity.class);
            startAnimActivity(intent);
        } else if (v == mLogin) {
            login();
        } else if (v == mForgotPass) {
            Intent intent = new Intent();
            intent.putExtra("reset_pass", true);
            intent.setClass(this, UserRegisterActivity.class);
            startAnimActivity(intent);
        }
    }

    @SuppressWarnings("deprecation")
    private void login() {
        String phoneNumber = mUserPhone.getText().toString();
        String password = mUserPass.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            ShowToast(R.string.notid);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.notpass);
            return;
        }
        saveUserInfo(phoneNumber, password);
        userManager.login(phoneNumber, password, new UserManagerListener() {
            @Override
            public void onSuccess(User u) {
                removeDialog(DIALOG_NEW_REGISTER);
                startAnimActivity(new Intent(UserLoginActivity.this,
                        MainActivity.class));
                finish();
            }

            @Override
            public void onError(int arg0, String arg1) {
                showLog("user login failure:" + arg0);
                String str = getString(R.string.no_conn_network);
                if (arg0 == 101) {
                    str = getString(R.string.login_fail);
                }
                removeDialog(DIALOG_NEW_REGISTER);
                ShowToast(str);
            }
        });

        showDialog(DIALOG_NEW_REGISTER);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
        case DIALOG_NEW_REGISTER:
            dialog = CommonUtils.createLoadingDialog(this,
                    getString(R.string.login_now));
            break;
        }
        return dialog;
    }

    private User getUserInfo() {
        SharedPreferences mSharedPreferences = getSharedPreferences("tasker",
                Context.MODE_PRIVATE);
        String phoneNumber = mSharedPreferences.getString("phone_number", "");
        String password = mSharedPreferences.getString("pass_word", "");
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        return user;
    }

    private void saveUserInfo(String phoneNumber, String password) {
        SharedPreferences mSharedPreferences = getSharedPreferences("tasker",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("phone_number", phoneNumber);
        editor.putString("pass_word", password);
        editor.commit();
    }
}
