package com.chuanshida.tasker.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.fragment.CalendarTaskFragment;

public class CalendarGestureView extends RelativeLayout {

    private CalendarTaskFragment mCalendarTaskFragment;
    private View mBottomView;
    private ViewPager mPager;
    private RelativeLayout.LayoutParams mBottomLayout;

    public CalendarGestureView(Context context) {
        super(context);
    }

    public CalendarGestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarGestureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFragment(CalendarTaskFragment fragment) {
        mCalendarTaskFragment = fragment;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPager = (ViewPager) findViewById(R.id.pager);
        mBottomView = findViewById(R.id.calendar_bottom_view);
        mBottomLayout = (RelativeLayout.LayoutParams) mBottomView
                .getLayoutParams();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    private int mDowntopMargin;
    private int mDownY;
    private int mMoveY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int y = (int) event.getRawY();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            mDowntopMargin = mBottomLayout.topMargin;
            mDownY = (int) event.getRawY();
            break;
        case MotionEvent.ACTION_MOVE:
            mMoveY = y - mDownY;
            int newTopMargin = mDowntopMargin + mMoveY;
            mBottomLayout.setMargins(0, newTopMargin, 0, 0);
            mBottomView.setLayoutParams(mBottomLayout);
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            mMoveY = 0;
            mDownY = 0;
            break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}
