package com.chuanshida.tasker.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.TaskToUser;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.CacheUtils;
import com.chuanshida.tasker.util.DocumentsUtil;
import com.chuanshida.tasker.util.TempData;

public class UpdateTaskActivity extends BaseActivity implements OnClickListener {

    private EditText mTaskName;
    private Task mCurrentTask;

    private View mAssignedView;
    private View mFinalDateView;
    private View mRemarkView;
    private View mRepeatView;
    private View mLocationView;

    private TextView mTitle;
    private TextView mFinalDate;
    private TextView mLocation;
    private TextView mRepeat;
    private TextView mRemark;
    private TextView mAssigned;
    private ImageButton mBottomRemark;
    private ImageButton mBottomPic;
    private ImageButton mBottomVoice;
    private ImageButton mBottomRepeat;
    private ImageButton mBottomLocation;
    private LinearLayout mHeadGroup;
    private LinearLayout mPicGroup;
    private View mPicGroupParent;

    private String mClickDataTime = "";
    public static Map<String, Bitmap> mTaskPic = new HashMap<String, Bitmap>();
    private Map<String, User> mCheckUser;
    private int mCurrentRepeat = Task.TASK_REPEAT_TYLE_NO;
    private Calendar mFinalDateCalendar = Calendar.getInstance();

    private static final int REQUEST_CODE_FOR_ASSIGNED = 1;
    private static final int REQUEST_CODE_FOR_REMARK = 2;
    private static final int REQUEST_CODE_FOR_REPEAT = 3;
    private static final int REQUEST_CODE_FOR_LOCATION = 4;
    private static final int REQUEST_CODE_FOR_PIC_CAMERA = 5;
    private static final int REQUEST_CODE_FOR_PIC_STORAGE = 6;

    private boolean mIsUpdate = false;
    private boolean mResume = true;

    private static final int HANDLER_SEND_UPDATE_HEADGROUP = 1;
    private static final int HANDLER_SEND_UPDATE_PIC_GROUP = 2;

    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
        }
    };
    private OnClickListener mHeadClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                User user = (User) v.getTag();
                Intent intent = new Intent(UpdateTaskActivity.this,
                        UserDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user);
                intent.putExtras(b);
                startAnimActivity(intent);
            }
        }
    };

    private OnClickListener mPicClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                if (v.getTag() == null) {
                    showSelectPicDialog();
                } else {
                    String pic = (String) v.getTag();
                    Intent intent = new Intent(UpdateTaskActivity.this,
                            PhotoViewActivity.class);
                    intent.putExtra("photo_bit", pic);
                    startActivity(intent);
                    // overridePendingTransition(R.anim.slide_up_in,
                    // R.anim.push_up_out);
                }
            }
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = 0;
            switch (msg.what) {
            case HANDLER_SEND_UPDATE_HEADGROUP:
                mHeadGroup.removeAllViews();
                for (User user : mCheckUser.values()) {
                    ImageView img = new ImageView(UpdateTaskActivity.this);
                    img.setOnClickListener(mHeadClickListener);
                    img.setTag(user);
                    LayoutParams layout = new LayoutParams(70, 70);
                    int left = 5;
                    int right = 5;
                    if (i == 0) {
                        left = 0;
                    } else if (i == mCheckUser.size() - 1) {
                        right = 0;
                    }
                    layout.setMargins(left, 0, right, 0);
                    img.setLayoutParams(layout);
                    if (user.getAvatar() != null
                            && !"".equals(user.getAvatar())) {
                    } else {
                        img.setBackgroundResource(R.drawable.login_head);
                    }
                    mHeadGroup.addView(img);
                    i++;
                }
                if (mCheckUser.size() == 0) {
                    mAssigned.setHint(R.string.new_assigned);
                } else {
                    mAssigned.setHint("");
                }
                break;
            case HANDLER_SEND_UPDATE_PIC_GROUP:
                mPicGroup.removeAllViews();
                for (Map.Entry<String, Bitmap> entry : mTaskPic.entrySet()) {
                    ImageView img = new ImageView(UpdateTaskActivity.this);
                    img.setOnClickListener(mPicClickListener);
                    img.setTag(entry.getKey());
                    LayoutParams layout = new LayoutParams(150, 200);
                    int left = 5;
                    int right = 5;
                    if (i == 0) {
                        left = 0;
                    }
                    layout.setMargins(left, 0, right, 0);
                    img.setLayoutParams(layout);
                    img.setBackground(new BitmapDrawable(getResources(), entry
                            .getValue()));
                    mPicGroup.addView(img);
                    i++;
                }
                if (mTaskPic.size() == 0) {
                    mPicGroupParent.setVisibility(View.GONE);
                    mBottomPic.setVisibility(View.VISIBLE);
                } else {
                    ImageView img = new ImageView(UpdateTaskActivity.this);
                    img.setOnClickListener(mPicClickListener);
                    img.setTag(null);
                    LayoutParams layout = new LayoutParams(150, 200);
                    layout.setMargins(5, 0, 0, 0);
                    img.setLayoutParams(layout);
                    img.setBackgroundResource(R.drawable.repeat_day);
                    mPicGroup.addView(img);
                    mPicGroupParent.setVisibility(View.VISIBLE);
                    mBottomPic.setVisibility(View.GONE);
                }
                break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_update_view);
        init();
    }

    private void init() {
        mTitle = (TextView) findViewById(R.id.title);
        mTaskName = (EditText) findViewById(R.id.task_name);
        mAssignedView = findViewById(R.id.new_assigned_view);
        mAssignedView.setOnClickListener(this);
        mAssigned = (TextView) findViewById(R.id.new_assigned);
        mAssigned.setText("");
        mFinalDateView = findViewById(R.id.final_date_view);
        mFinalDateView.setOnClickListener(this);
        mFinalDate = (TextView) findViewById(R.id.final_date);
        mRemarkView = findViewById(R.id.task_remark_view);
        mRemarkView.setOnClickListener(this);
        mRemark = (TextView) findViewById(R.id.task_remark);
        mRepeatView = findViewById(R.id.task_repeat_view);
        mRepeat = (TextView) findViewById(R.id.task_repeat);
        mRepeatView.setOnClickListener(this);
        mLocationView = findViewById(R.id.task_location_view);
        mLocation = (TextView) findViewById(R.id.task_location);
        mLocationView.setOnClickListener(this);
        mHeadGroup = (LinearLayout) findViewById(R.id.head_group);
        mPicGroup = (LinearLayout) findViewById(R.id.task_img);
        mPicGroupParent = findViewById(R.id.task_img_scroll);

        mBottomRemark = (ImageButton) findViewById(R.id.task_bottom_remark);
        mBottomPic = (ImageButton) findViewById(R.id.task_bottom_pic);
        mBottomVoice = (ImageButton) findViewById(R.id.task_bottom_voice);
        mBottomRepeat = (ImageButton) findViewById(R.id.task_bottom_repeat);
        mBottomLocation = (ImageButton) findViewById(R.id.task_bottom_location);
        mBottomRemark.setOnClickListener(this);
        mBottomPic.setOnClickListener(this);
        mBottomVoice.setOnClickListener(this);
        mBottomRepeat.setOnClickListener(this);
        mBottomLocation.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mResume) {
            updateUI();
        }
        mResume = true;
    }

    private void updateUI() {
        mCurrentTask = (Task) getIntent().getExtras().getSerializable("task");
        mIsUpdate = getIntent().getBooleanExtra("update", false);
        User user = (User) getIntent().getExtras().getSerializable("user");
        if (user != null) {
            if (mCheckUser == null) {
                mCheckUser = new HashMap<String, User>();
            }
            mCheckUser.put(user.getPhoneNumber(), user);
            mHandler.sendEmptyMessage(HANDLER_SEND_UPDATE_HEADGROUP);
        }
        mTaskName.setText(mCurrentTask.getName());
        mTaskName.setEnabled(false);

        String location = mCurrentTask.getAddress();
        if (location == null || "".equals(location)) {
            mLocationView.setVisibility(View.GONE);
            mBottomLocation.setVisibility(View.VISIBLE);
        } else {
            mLocationView.setVisibility(View.VISIBLE);
            mLocation.setText(location);
            mBottomLocation.setVisibility(View.GONE);
        }

        String remark = mCurrentTask.getContent();
        if (remark == null || "".equals(remark)) {
            mRemarkView.setVisibility(View.GONE);
            mBottomRemark.setVisibility(View.VISIBLE);
        } else {
            mRemarkView.setVisibility(View.VISIBLE);
            mRemark.setText(remark);
            mBottomRemark.setVisibility(View.GONE);
        }

        mCurrentRepeat = mCurrentTask.getRepeat();
        if (mCurrentRepeat == Task.TASK_REPEAT_TYLE_NO) {
            mRepeatView.setVisibility(View.GONE);
            mBottomRepeat.setVisibility(View.VISIBLE);
        } else {
            mRepeatView.setVisibility(View.VISIBLE);
            updateRepeatView();
            mBottomRepeat.setVisibility(View.GONE);
        }

        List<TaskToUser> ttus = TempData.getTaskToUserForTask(mCurrentTask);
        if (ttus != null && ttus.size() > 0) {
            if (mCheckUser == null) {
                mCheckUser = new HashMap<String, User>();
            }
            for (TaskToUser ttu : ttus) {
                User u = ttu.getToUser();
                mCheckUser.put(u.getPhoneNumber(), u);
            }
            mHandler.sendEmptyMessage(HANDLER_SEND_UPDATE_HEADGROUP);
        }
        mTitle.setText(mIsUpdate ? R.string.task_info : R.string.new_task);
        mHandler.sendEmptyMessage(HANDLER_SEND_UPDATE_PIC_GROUP);
    }

    public void onComplete(View v) {
        if (mCheckUser == null || mCheckUser.size() == 0) {
            return;
        }
        String address = mLocation.getText().toString();
        if (address != null && !"".equals(address)) {
            mCurrentTask.setAddress(address);
        }
        String remark = mRemark.getText().toString();
        if (remark != null && !"".equals(remark)) {
            mCurrentTask.setContent(remark);
        }
        mCurrentTask.setRepeat(mCurrentRepeat);
        int count = mCheckUser.size();
        for (User user : mCheckUser.values()) {
            TaskToUser taskToUser = new TaskToUser();
            taskToUser.setToUser(user);
            taskToUser.setTask(mCurrentTask);
            TempData.mTempTaskToUserList.add(taskToUser);
        }
        TempData.mTempTaskList.put(mCurrentTask.getName(), mCurrentTask);
        if (count > 0) {
            onBackPressed();
            ShowToast(R.string.task_new_success,
                    R.drawable.tenpay_toast_logo_success);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mAssignedView) {
            Intent intent = new Intent(this, AssignedFriendActivity.class);
            if (mCheckUser != null && mCheckUser.size() > 0) {
                Bundle b = new Bundle();
                b.putInt("data_size", mCheckUser.size());
                int i = 0;
                for (Map.Entry<String, User> entry : mCheckUser.entrySet()) {
                    User user = entry.getValue();
                    b.putSerializable("data" + i, user);
                    i++;
                }
                intent.putExtras(b);
            }
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_ASSIGNED);
        } else if (v == mRemarkView) {
            Intent intent = new Intent(this, EditFieldActivity.class);
            intent.putExtra("type", EditFieldActivity.EDIT_STYLE_REMARK);
            intent.putExtra("remark_old", mRemark.getText().toString());
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_REMARK);
        } else if (v == mRepeatView) {
            Intent intent = new Intent(this, EditFieldActivity.class);
            intent.putExtra("type", EditFieldActivity.EDIT_STYLE_REPEAT);
            intent.putExtra("repeat_old", mCurrentRepeat);
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_REPEAT);
        } else if (v == mLocationView) {
            Intent intent = new Intent(this, EditFieldActivity.class);
            intent.putExtra("type", EditFieldActivity.EDIT_STYLE_LOCATION);
            intent.putExtra("location_old", mLocation.getText().toString());
            startAnimActivityForResult(intent, REQUEST_CODE_FOR_LOCATION);
        } else if (v == mBottomPic) {
            showSelectPicDialog();
        } else if (v == mFinalDateView) {
            showSelectDateDialog();
        } else {
            Intent intent = new Intent(this, EditFieldActivity.class);
            int requestCode = 0;
            if (v == mBottomRemark) {
                intent.putExtra("type", EditFieldActivity.EDIT_STYLE_REMARK);
                requestCode = REQUEST_CODE_FOR_REMARK;
            } else if (v == mBottomRepeat) {
                intent.putExtra("type", EditFieldActivity.EDIT_STYLE_REPEAT);
                requestCode = REQUEST_CODE_FOR_REPEAT;
            } else if (v == mBottomLocation) {
                intent.putExtra("type", EditFieldActivity.EDIT_STYLE_LOCATION);
                requestCode = REQUEST_CODE_FOR_LOCATION;
            }
            startAnimActivityForResult(intent, requestCode);
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

    private void showSelectDateDialog() {
        Calendar today = Calendar.getInstance();
        final Calendar tempCalendar = Calendar.getInstance();
        LinearLayout dateTimeLayout = (LinearLayout) mInFlater.inflate(
                R.layout.select_date_time, null);
        DatePicker datePicker = (DatePicker) dateTimeLayout
                .findViewById(R.id.date_picker);
        TimePicker timePicker = (TimePicker) dateTimeLayout
                .findViewById(R.id.time_picker);

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MARCH),
                today.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year,
                            int monthOfYear, int dayOfMonth) {
                        tempCalendar.set(Calendar.YEAR, year);
                        tempCalendar.set(Calendar.MARCH, monthOfYear);
                        tempCalendar.set(Calendar.DAY_OF_YEAR, dayOfMonth);
                    }
                });
        timePicker.setCurrentHour(today.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(today.get(Calendar.MINUTE));
        timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                tempCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                tempCalendar.set(Calendar.MINUTE, minute);
            }
        });

        AlertDialog.Builder build = new AlertDialog.Builder(this)
                .setTitle(R.string.new_final_date)
                .setView(dateTimeLayout)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                mFinalDateCalendar.set(Calendar.YEAR,
                                        tempCalendar.get(Calendar.YEAR));
                                mFinalDateCalendar.set(Calendar.MARCH,
                                        tempCalendar.get(Calendar.MARCH));
                                mFinalDateCalendar.set(Calendar.DAY_OF_YEAR,
                                        tempCalendar.get(Calendar.DAY_OF_YEAR));
                                mFinalDateCalendar.set(Calendar.HOUR_OF_DAY,
                                        tempCalendar.get(Calendar.HOUR_OF_DAY));
                                mFinalDateCalendar.set(Calendar.MINUTE,
                                        tempCalendar.get(Calendar.MINUTE));
                                Date date = mFinalDateCalendar.getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm");
                                String time = sdf.format(date);
                                mFinalDate.setText(time);
                            }
                        });

        build.show();
    }

    private void showSelectPicDialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(this).setTitle(
                R.string.add_pic).setNegativeButton(android.R.string.cancel,
                null);

        build.setItems(R.array.add_pic_array,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case 0:
                            openCamera();
                            break;
                        case 1:
                            Intent intent = new Intent(
                                    Intent.ACTION_GET_CONTENT);
                            intent.setDataAndType(
                                    MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                                    "image/*");
                            startActivityForResult(intent,
                                    REQUEST_CODE_FOR_PIC_STORAGE);
                            break;
                        }
                    }
                });
        build.show();
    }

    private void openCamera() {
        Date date = new Date(System.currentTimeMillis());
        mClickDataTime = date.getTime() + "";
        File f = new File(CacheUtils.getCacheDirectory(this, true, "pic")
                + mClickDataTime);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(f);
        Log.e("uri", uri + "");

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(camera, REQUEST_CODE_FOR_PIC_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mResume = false;
            switch (requestCode) {
            case REQUEST_CODE_FOR_ASSIGNED:
                Bundle b = data.getExtras();
                int size = b.getInt("data_size");
                if (mCheckUser == null) {
                    mCheckUser = new HashMap<String, User>();
                }
                mCheckUser.clear();
                for (int i = 0; i < size; i++) {
                    User u = (User) b.getSerializable("data" + i);
                    mCheckUser.put(u.getPhoneNumber(), u);
                }
                String names = "";
                int i = 0;
                for (User user : mCheckUser.values()) {
                    names += user.getUsername();
                    if (i != mCheckUser.size() - 1) {
                        names += ",";
                    }
                    i++;
                }
                mHandler.sendEmptyMessage(HANDLER_SEND_UPDATE_HEADGROUP);
                break;
            case REQUEST_CODE_FOR_REMARK:
                String remark = data.getStringExtra("remark");
                if (remark == null || "".equals(remark)) {
                    mRemarkView.setVisibility(View.GONE);
                    mBottomRemark.setVisibility(View.VISIBLE);
                } else {
                    mRemarkView.setVisibility(View.VISIBLE);
                    mBottomRemark.setVisibility(View.GONE);
                }
                mRemark.setText(remark);
                break;
            case REQUEST_CODE_FOR_REPEAT:
                mCurrentRepeat = data.getIntExtra("repeat",
                        Task.TASK_REPEAT_TYLE_NO);
                if (mCurrentRepeat == Task.TASK_REPEAT_TYLE_NO) {
                    mRepeatView.setVisibility(View.GONE);
                    mBottomRepeat.setVisibility(View.VISIBLE);
                } else {
                    mRepeatView.setVisibility(View.VISIBLE);
                    mBottomRepeat.setVisibility(View.GONE);
                }
                updateRepeatView();
                break;
            case REQUEST_CODE_FOR_LOCATION:
                String location = data.getStringExtra("location");
                if (location == null || "".equals(location)) {
                    mLocationView.setVisibility(View.GONE);
                    mBottomLocation.setVisibility(View.VISIBLE);
                } else {
                    mLocationView.setVisibility(View.VISIBLE);
                    mBottomLocation.setVisibility(View.GONE);
                }
                mLocation.setText(location);
                break;
            case REQUEST_CODE_FOR_PIC_STORAGE:
                Uri imageUri = data.getData();
                int sdk_int = Build.VERSION.SDK_INT;
                if (sdk_int < 19) {
                    setPicForUri(imageUri);
                } else {
                    String picturePath = DocumentsUtil.getPath(this, imageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    updatePicUI(picturePath, bitmap);
                }
                break;
            case REQUEST_CODE_FOR_PIC_CAMERA:
                String files = CacheUtils.getCacheDirectory(this, true, "pic")
                        + mClickDataTime;
                File file = new File(files);
                if (file.exists()) {
                    Bitmap bitmap = compressImageFromFile(files);
                    String picturePath = saveToSdCard(bitmap);
                    updatePicUI(picturePath, bitmap);
                }
                break;
            }
        }
    }

    private void updatePicUI(String path, Bitmap bitmap) {
        Bitmap bit = mTaskPic.get(path);
        if (bit != null) {
            ShowToastOld(R.string.pic_added);
        } else {
            mTaskPic.put(path, bitmap);
            mHandler.sendEmptyMessage(HANDLER_SEND_UPDATE_PIC_GROUP);
        }
    }

    private String saveToSdCard(Bitmap bitmap) {
        String files = CacheUtils.getCacheDirectory(this, true, "pic")
                + mClickDataTime + "_11";
        File file = new File(files);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    private void setPicForUri(Uri imageUri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(imageUri, filePathColumn,
                null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        updatePicUI(picturePath, bitmap);
    }
}
