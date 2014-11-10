package com.chuanshida.tasker.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.User;

public class UserDetailActivity extends BaseActivity implements OnClickListener {

    private User mCurrentUser = null;
    private TextView mUserName;
    private ImageView mUserPhoto;
    private TextView mUserSix;
    private TextView mUserAddress;
    private TextView mUserSignature;
    private TextView mUserPhone;
    private TextView mUserEmail;
    private TextView mUserLabel;
    private Button mAddLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_view);
        init();
    }

    private void init() {
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserPhoto = (ImageView) findViewById(R.id.user_photo);
        mUserSix = (TextView) findViewById(R.id.user_six);
        mUserAddress = (TextView) findViewById(R.id.user_address);
        mUserSignature = (TextView) findViewById(R.id.user_signature);
        mUserPhone = (TextView) findViewById(R.id.user_phone);
        mUserEmail = (TextView) findViewById(R.id.user_email);
        mUserLabel = (TextView) findViewById(R.id.user_label);
        mAddLabel = (Button) findViewById(R.id.add_label);
        mAddLabel.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateInfoUI();
    }

    private void updateInfoUI() {
        mCurrentUser = userManager.getCurrentUser();
        if (mCurrentUser != null) {
            mUserName.setText(mCurrentUser.getUsername());
            mUserSix.setText(mCurrentUser.isSex() ? R.string.sex_man
                    : R.string.sex_woman);
            String address = mCurrentUser.getAddress();
            mUserAddress
                    .setText((address == null || address.equals("")) ? getString(R.string.user_no_address)
                            : address);
            String signature = mCurrentUser.getSignature();
            mUserSignature
                    .setText((signature == null || signature.equals("")) ? getString(R.string.user_no_signature)
                            : signature);
            String email = mCurrentUser.getEmail();
            mUserEmail
                    .setText((email == null || email.equals("")) ? getString(R.string.user_no_email)
                            : email);
            String phone = mCurrentUser.getPhoneNumber();
            mUserPhone
                    .setText((phone == null || phone.equals("")) ? getString(R.string.user_no_phone)
                            : phone);
            mUserLabel.setText(mCurrentUser.getLabel());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mAddLabel) {

        }
    }

}
