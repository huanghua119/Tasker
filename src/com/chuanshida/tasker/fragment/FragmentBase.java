package com.chuanshida.tasker.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanshida.tasker.CustomApplcation;
import com.chuanshida.tasker.R;

/**
 * Fragmenet 基类
 */
public abstract class FragmentBase extends Fragment {

    protected View contentView;

    public LayoutInflater mInflater;

    Toast mToast;
    Toast mToast2;

    private Handler handler = new Handler();

    public void runOnWorkThread(Runnable action) {
        new Thread(action).start();
    }

    public void runOnUiThread(Runnable action) {
        handler.post(action);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);
        mApplication = CustomApplcation.getInstance();
        mInflater = LayoutInflater.from(getActivity());
    }

    public FragmentBase() {

    }

    public void ShowToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (getActivity() == null) {
                        return;
                    }
                    if (mToast == null) {
                        mToast = new Toast(getActivity());
                        mToast.setDuration(Toast.LENGTH_SHORT);
                        mToast.setView(mInflater.inflate(R.layout.toast_view,
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

    public void ShowToast(final String text, final int drawableId) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (getActivity() == null) {
                        return;
                    }
                    if (mToast == null) {
                        mToast = new Toast(getActivity());
                        mToast.setDuration(Toast.LENGTH_SHORT);
                        mToast.setView(mInflater.inflate(R.layout.toast_view,
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
                if (getActivity() == null) {
                    return;
                }
                if (mToast == null) {
                    mToast = new Toast(getActivity());
                    mToast.setDuration(Toast.LENGTH_SHORT);
                    mToast.setView(mInflater.inflate(R.layout.toast_view, null));
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

    public void ShowToast(final int resId) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (getActivity() == null) {
                    return;
                }
                if (mToast == null) {
                    mToast = new Toast(getActivity());
                    mToast.setDuration(Toast.LENGTH_SHORT);
                    mToast.setView(mInflater.inflate(R.layout.toast_view, null));
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                }
                View toast = mToast.getView();
                TextView m = (TextView) toast.findViewById(R.id.toast_msg);
                m.setText(resId);
                mToast.show();
            }
        });
    }

    public void ShowToastOld(String text) {
        if (getActivity() == null) {
            return;
        }
        if (mToast2 == null) {
            mToast2 = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        }
        mToast2.setText(text);
        mToast2.show();
    }

    public void ShowToastOld(int text) {
        if (getActivity() == null) {
            return;
        }
        if (mToast2 == null) {
            mToast2 = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        }
        mToast2.setText(text);
        mToast2.show();
    }

    public View findViewById(int paramInt) {
        return getView().findViewById(paramInt);
    }

    public CustomApplcation mApplication;

    /**
     * 动画启动页面 startAnimActivity
     * 
     * @throws
     */
    public void startAnimActivity(Intent intent) {
        this.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_in,
                R.anim.right_out);
    }

    public void startAnimActivity(Class<?> cla) {
        getActivity().startActivity(new Intent(getActivity(), cla));
        getActivity().overridePendingTransition(R.anim.right_in,
                R.anim.right_out);
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
}
