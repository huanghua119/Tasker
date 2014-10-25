package com.chuanshida.tasker.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.manager.UserManager.UserManagerListener;
import com.chuanshida.tasker.tencentlogin.TencentConstants;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.weibologin.AccessTokenKeeper;
import com.chuanshida.tasker.weibologin.WeiboConstants;
import com.chuanshida.tasker.weibologin.WeiboLoginButton;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class LoginChooseActivity extends BaseActivity implements
        OnClickListener {

    private Button mToLogin = null;
    private Button mToRegister = null;
    private User mCurrentUser = null;
    private WeiboLoginButton mWeiboLogin = null;
    private ImageView mQQLogin = null;
    /** 登陆认证对应的listener */
    private AuthListener mLoginListener = new AuthListener();
    /** 登出操作对应的listener */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();
    private Oauth2AccessToken mAccessToken = null;

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
        setContentView(R.layout.login_choose_view);
        init();
        initWeiboLogin();
    }

    private void initWeiboLogin() {
        // 创建授权认证信息
        mCurrentUser = userManager.getCurrentUser();
        if (mCurrentUser != null
                && mCurrentUser.getLogintype() == User.LOGIN_TYPE_WEIBO) {
            mAccessToken = AccessTokenKeeper.readAccessToken(this);
        }
        AuthInfo authInfo = new AuthInfo(this, WeiboConstants.APP_KEY,
                WeiboConstants.REDIRECT_URL, WeiboConstants.SCOPE);
        mWeiboLogin = (WeiboLoginButton) findViewById(R.id.weibo_login);
        mWeiboLogin.setWeiboAuthInfo(authInfo, mLoginListener);
        mWeiboLogin.setExternalOnClickListener(mButtonClickListener);
        mWeiboLogin.setVisibility(View.VISIBLE);
        mQQLogin = (ImageView) findViewById(R.id.qq_login);
        mQQLogin.setOnClickListener(this);
    }

    private void init() {
        mToLogin = (Button) findViewById(R.id.to_login);
        mToRegister = (Button) findViewById(R.id.to_register);
        mToLogin.setOnClickListener(this);
        mToRegister.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userManager.getCurrentUser() != null) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mToLogin) {
            Intent intent = new Intent();
            intent.setClass(this, UserLoginActivity.class);
            startAnimActivity(intent);
        } else if (v == mToRegister) {
            Intent intent = new Intent();
            intent.setClass(this, UserRegisterActivity.class);
            startAnimActivity(intent);
        }
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

    private Button mCurrentClickedButton;

    private OnClickListener mButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                mCurrentClickedButton = (Button) v;
            }
        }
    };

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    @SuppressWarnings("deprecation")
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            showLog(WeiboConstants.TAG, "AuthListener onComplete:" + values);
            Oauth2AccessToken accessToken = Oauth2AccessToken
                    .parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                showDialog(DIALOG_NEW_REGISTER);
                AccessTokenKeeper.writeAccessToken(getApplicationContext(),
                        accessToken);
                mAccessToken = accessToken;
                userManager.weiboLogin(mAccessToken, new UserManagerListener() {
                    @Override
                    public void onSuccess(User u) {
                        removeDialog(DIALOG_NEW_REGISTER);
                        finish();
                    }

                    @Override
                    public void onError(int arg0, String arg1) {
                        CommonUtils
                                .showLog(WeiboConstants.TAG,
                                        "singup onError arg0:" + arg0
                                                + " arg1:" + arg1);
                        String str = getString(R.string.other_login_fail);
                        ShowToast(str);
                        removeDialog(DIALOG_NEW_REGISTER);
                    }
                });
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            showLog(WeiboConstants.TAG,
                    "AuthListener onWeiboException:" + e.getMessage());
        }

        @Override
        public void onCancel() {
            showLog(WeiboConstants.TAG, "AuthListener onCancel");
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            showLog(WeiboConstants.TAG, "LogOutRequestListener response:"
                    + response);
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");
                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(LoginChooseActivity.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            userManager.logout();
            onBackPressed();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            showLog(WeiboConstants.TAG,
                    "LogOutRequestListener onWeiboException:" + e.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurrentClickedButton != null) {
            if (mCurrentClickedButton instanceof WeiboLoginButton) {
                ((WeiboLoginButton) mCurrentClickedButton).onActivityResult(
                        requestCode, resultCode, data);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void tencentLogin() {
        userManager.tencentLogin(this, new UserManagerListener() {
            @Override
            public void onSuccess(User u) {
                removeDialog(DIALOG_NEW_REGISTER);
                finish();
            }

            @Override
            public void onError(int arg0, String arg1) {
                showLog(TencentConstants.TAG, "user login failure arg0:" + arg0
                        + " arg1:" + arg0);
                String str = getString(R.string.no_conn_network);
                if (arg0 == 101 || arg0 == 0) {
                    str = getString(R.string.other_login_fail);
                }
                ShowToast(str);
            }
        }, mHandler);
    }

}
