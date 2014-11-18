package com.chuanshida.tasker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;

public class EditFieldActivity extends BaseActivity implements OnClickListener {

    public static final int EDIT_STYLE_REMARK = 1;
    public static final int EDIT_STYLE_REPEAT = 2;
    public static final int EDIT_STYLE_LOCATION = 3;
    private int mCurrentType = 0;

    private EditText mEditText;
    private TextView mTitle;

    private View mRepeatView;
    private TextView mRepeat;
    private int mCurrentRepeat = Task.TASK_REPEAT_TYLE_NO;

    private View mLocatioinView;
    private TextView mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_field_view);
        mCurrentType = getIntent().getIntExtra("type", 0);
        init();
    }

    private void init() {
        mEditText = (EditText) findViewById(R.id.edit_text);
        mTitle = (TextView) findViewById(R.id.title);
        mRepeatView = findViewById(R.id.repeat_view);
        mRepeat = (TextView) findViewById(R.id.repeat);
        mLocatioinView = findViewById(R.id.location_view);
        mLocation = (TextView) findViewById(R.id.location);
        mLocatioinView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (mCurrentType) {
        case EDIT_STYLE_REMARK:
            mTitle.setText(R.string.task_remark);
            String remark = getIntent().getStringExtra("remark_old");
            mEditText.setText(remark);
            mEditText.setSelection(mEditText.getText().length());
            mEditText.setVisibility(View.VISIBLE);
            mRepeatView.setVisibility(View.GONE);
            mLocatioinView.setVisibility(View.GONE);
            break;
        case EDIT_STYLE_LOCATION:
            String location = getIntent().getStringExtra("location_old");
            mTitle.setText(R.string.task_location);
            mLocation.setText(location);
            mLocatioinView.setVisibility(View.VISIBLE);
            mRepeatView.setVisibility(View.GONE);
            mEditText.setVisibility(View.GONE);
            break;
        case EDIT_STYLE_REPEAT:
            mCurrentRepeat = getIntent().getIntExtra("repeat_old",
                    Task.TASK_REPEAT_TYLE_NO);
            mTitle.setText(R.string.task_repeat);
            updateRepeatView();
            mEditText.setVisibility(View.GONE);
            mRepeatView.setVisibility(View.VISIBLE);
            mLocatioinView.setVisibility(View.GONE);
            break;
        default:
            finish();
            break;
        }
    }

    public void onRepeatClick(View v) {
        switch (v.getId()) {
        case R.id.task_no_repeat:
            mRepeat.setText(R.string.task_no_repeat);
            mCurrentRepeat = Task.TASK_REPEAT_TYLE_NO;
            break;
        case R.id.task_week_repeat:
            mRepeat.setText(R.string.task_week_repeat);
            mCurrentRepeat = Task.TASK_REPEAT_TYLE_WEEK;
            break;
        case R.id.task_day_repeat:
            mRepeat.setText(R.string.task_day_repeat);
            mCurrentRepeat = Task.TASK_REPEAT_TYLE_DAY;
            break;
        case R.id.task_month_repeat:
            mRepeat.setText(R.string.task_month_repeat);
            mCurrentRepeat = Task.TASK_REPEAT_TYLE_MONTH;
            break;
        case R.id.task_year_repeat:
            mRepeat.setText(R.string.task_year_repeat);
            mCurrentRepeat = Task.TASK_REPEAT_TYLE_YEAR;
            break;
        case R.id.task_diy_repeat:
            mRepeat.setText(R.string.diy);
            mCurrentRepeat = Task.TASK_REPEAT_TYLE_DIY;
            break;
        }
    }

    private void updateRepeatView() {
        switch (mCurrentRepeat) {
        case Task.TASK_REPEAT_TYLE_NO:
            mRepeat.setText(R.string.task_no_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_WEEK:
            mRepeat.setText(R.string.task_week_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_DAY:
            mRepeat.setText(R.string.task_day_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_MONTH:
            mRepeat.setText(R.string.task_month_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_YEAR:
            mRepeat.setText(R.string.task_year_repeat);
            break;
        case Task.TASK_REPEAT_TYLE_DIY:
            mRepeat.setText(R.string.diy);
            break;
        }
    }

    public void onComplete(View v) {
        Intent data = new Intent();
        switch (mCurrentType) {
        case EDIT_STYLE_REMARK:
            String remark = mEditText.getText().toString();
            if (remark == null) {
                remark = "";
            }
            data.putExtra("remark", remark);
            break;
        case EDIT_STYLE_REPEAT:
            data.putExtra("repeat", mCurrentRepeat);
            setResult(Activity.RESULT_OK, data);
            break;
        case EDIT_STYLE_LOCATION:
            data.putExtra("location", mLocation.getText().toString());
            setResult(Activity.RESULT_OK, data);
            break;

        default:
            break;
        }
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mLocatioinView) {
            mLocation.setText("暂时写北京");
        }
    }

}
