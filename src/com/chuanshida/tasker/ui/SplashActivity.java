package com.chuanshida.tasker.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.chuanshida.tasker.R;

/**
 * 引导页
 */
public class SplashActivity extends BaseActivity {

    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000);
        mRunFinishAnim = false;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case GO_HOME:
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
                break;
            case GO_LOGIN:
                startActivity(new Intent(SplashActivity.this, LoginChooseActivity.class));
                overridePendingTransition(0, R.anim.push_up_out);
                finish();
                break;
            }
        }
    };

}
