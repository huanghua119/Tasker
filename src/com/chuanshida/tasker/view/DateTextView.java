package com.chuanshida.tasker.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.chuanshida.tasker.CustomApplcation;
import com.chuanshida.tasker.R;

public class DateTextView extends TextView {

    private TimeChangedReceiver mIntentReceiver = null;
    private Context mContext;
    private String mInitDate = "";

    private class TimeChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateText();
        }
    };

    public DateTextView(Context context) {
        this(context, null);
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mIntentReceiver == null) {
            mIntentReceiver = new TimeChangedReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_LOCALE_CHANGED);
            mContext.registerReceiver(mIntentReceiver, filter);
            updateText();
        }
    }

    public void updateText() {
        try {
            Calendar mCalendar = Calendar.getInstance(TimeZone.getDefault());
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            Date nowDate = mCalendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date initDate = sdf.parse(mInitDate);

            long day = Math
                    .abs(((nowDate.getTime() - initDate.getTime()) / (1000 * 60)));
            if (day < 60) {
                long second = Math
                        .abs((nowDate.getTime() - initDate.getTime()) / (1000));
                if (second < 60) {
                    setText(mContext.getString(R.string.date_now));
                } else {
                    setText(day + mContext.getString(R.string.date_min_befor));
                }
            } else if (day >= 60 && day < (60 * 24)) {
                setText(day / 60 + mContext.getString(R.string.date_hour_befor));
            } else if (day >= (60 * 24) && day < (60 * 24 * 30)) {
                setText(day / (60 * 24)
                        + mContext.getString(R.string.date_day_befor));
            } else if (day >= (60 * 24 * 30) && day < (60 * 24 * 30 * 12)) {
                setText(day / (60 * 24 * 30)
                        + mContext.getString(R.string.date_month_befor));
            } else {
                setText(mInitDate);
            }
            //showLog("nowDate: " + nowDate + " initDate:" + initDate + " day:"
            //        + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mIntentReceiver != null) {
            mContext.unregisterReceiver(mIntentReceiver);
        }
    }

    public void setInitDate(String createdAt) {
        mInitDate = createdAt;
        updateText();
    }

    public void showLog(String msg) {
        if (CustomApplcation.DEBUG) {
            Log.i(CustomApplcation.TAG, msg);
        }
    }
}
