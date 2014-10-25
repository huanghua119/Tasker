package com.chuanshida.tasker.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.CommonUtils;

public class UserRegisterActivity extends BaseActivity implements
        OnClickListener {

    private Button mCommit;
    private EditText mPhoneNumber;
    private EditText mPass;
    private TextView mTitle;
    private boolean mResetPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_view);
        mResetPass = getIntent().getBooleanExtra("reset_pass", false);
        init();
    }

    private void init() {
        mCommit = (Button) findViewById(R.id.commit_register);
        mCommit.setOnClickListener(this);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mPass = (EditText) findViewById(R.id.pass_word);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(!mResetPass ? R.string.user_register : R.string.reset_password);
        mPass.setHint(!mResetPass ? R.string.input_password : R.string.input_new_password);
        mCommit.setText(!mResetPass ? R.string.now_register : R.string.ok);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == mCommit) {
            startRegister();
        }
    }

    private void startRegister() {
        String phoneNumber = mPhoneNumber.getText().toString();
        String password = mPass.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            ShowToast(R.string.namenotnull);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ShowToast(R.string.passnotnull);
            return;
        } else if (password.length() < 6) {
            ShowToast(R.string.pass_so_easy);
            return;
        }

        boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
        if (!isNetConnected) {
            ShowToast(R.string.no_conn_network);
            return;
        }

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.beingRegister));
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        User bu = new User();
        bu.setPhoneNumber(phoneNumber);
        bu.setPassword(password);
    }
}
