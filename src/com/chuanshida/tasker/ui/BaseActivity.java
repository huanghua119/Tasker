package com.chuanshida.tasker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanshida.tasker.CustomApplcation;
import com.chuanshida.tasker.R;
import com.chuanshida.tasker.util.CommonUtils;

public class BaseActivity extends Activity {

    CustomApplcation mApplication;

    protected int mScreenWidth;
    protected int mScreenHeight;
    public LayoutInflater mInFlater;
    public UserManager userManager;
    public boolean mRunFinishAnim = true;
    public boolean mSwitchTheme = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = CustomApplcation.getInstance();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mInFlater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSwitchTheme = false;
    }

    Toast mToast;
    Toast mToast2;

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = new Toast(BaseActivity.this);
                        mToast.setDuration(Toast.LENGTH_SHORT);
                        mToast.setView(mInFlater.inflate(R.layout.toast_view,
                                null));
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                    }
                    View toast = mToast.getView();
                    TextView m = (TextView) toast.findViewById(R.id.toast_msg);
                    m.setText(text);
                    mToast.show();
                }
            });

        }
    }

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast == null) {
                    mToast = new Toast(BaseActivity.this);
                    mToast.setDuration(Toast.LENGTH_SHORT);
                    mToast.setView(mInFlater.inflate(R.layout.toast_view, null));
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                }
                View toast = mToast.getView();
                TextView m = (TextView) toast.findViewById(R.id.toast_msg);
                m.setText(resId);
                mToast.show();
            }
        });
    }

    public void ShowToast(final String text, final int drawableId) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = new Toast(BaseActivity.this);
                        mToast.setDuration(Toast.LENGTH_SHORT);
                        mToast.setView(mInFlater.inflate(R.layout.toast_view,
                                null));
                        mToast.setGravity(Gravity.CENTER, 0, 0);
                    }
                    View toast = mToast.getView();
                    TextView m = (TextView) toast.findViewById(R.id.toast_msg);
                    m.setText(text);
                    ImageView image = (ImageView) toast
                            .findViewById(R.id.toast_image);
                    image.setBackgroundResource(drawableId);
                    mToast.show();
                }
            });

        }
    }

    public void ShowToast(final int resId, final int drawableId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast == null) {
                    mToast = new Toast(BaseActivity.this);
                    mToast.setDuration(Toast.LENGTH_SHORT);
                    mToast.setView(mInFlater.inflate(R.layout.toast_view, null));
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                }
                View toast = mToast.getView();
                TextView m = (TextView) toast.findViewById(R.id.toast_msg);
                m.setText(resId);
                ImageView image = (ImageView) toast
                        .findViewById(R.id.toast_image);
                image.setBackgroundResource(drawableId);
                mToast.show();
            }
        });
    }

    public void ShowToastOld(String text) {
        if (mToast2 == null) {
            mToast2 = Toast.makeText(BaseActivity.this, text,
                    Toast.LENGTH_SHORT);
        }
        mToast2.setText(text);
        mToast2.show();
    }

    public void ShowToastOld(int text) {
        if (mToast2 == null) {
            mToast2 = Toast.makeText(BaseActivity.this, text,
                    Toast.LENGTH_SHORT);
        }
        mToast2.setText(text);
        mToast2.show();
    }

    public void startAnimActivity(Class<?> cla) {
        this.startActivity(new Intent(this, cla));
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public void startAnimActivity(Intent intent) {
        this.startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public void showLog(String msg) {
        if (CustomApplcation.DEBUG) {
            Log.i(CustomApplcation.TAG, msg);
        }
    }

    public void showLog(String tag, String msg) {
        if (CustomApplcation.DEBUG) {
            Log.i(tag, msg);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (mRunFinishAnim) {
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }
    }

    public boolean checkNetwork() {
        if (!CommonUtils.isNetworkAvailable(this)) {
            ShowToast(R.string.no_conn_network);
            return false;
        }
        return true;
    }

    public void onBack(View view) {
        onBackPressed();
    }

    public boolean checkUserLogin() {
        if (!checkNetwork()) {
            return true;
        }
        return false;
    }


}
