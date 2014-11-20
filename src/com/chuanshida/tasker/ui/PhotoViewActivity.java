package com.chuanshida.tasker.ui;

import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.bean.Task;
import com.chuanshida.tasker.bean.User;
import com.chuanshida.tasker.util.CommonUtils;
import com.chuanshida.tasker.util.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * PhotoView
 * 
 * @author huanghua
 * 
 */
public class PhotoViewActivity extends BaseActivity {

    private ImageView mImageView;
    private ImageView mDeletePic;
    private PhotoViewAttacher mAttacher;
    private boolean mHasTouchPhoto = false;
    private boolean mFirstRun = true;
    private String mSelectPicKey = "";
    private View mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);
        mTitleBar = findViewById(R.id.title_bar);
        mImageView = (ImageView) findViewById(R.id.iv_photo);
        mDeletePic = (ImageView) findViewById(R.id.delete);
        mSelectPicKey = (String) getIntent().getStringExtra("photo_bit");
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnMatrixChangeListener(mOatrixChangedListener);
        mAttacher.setOnPhotoTapListener(mOnPhotoTapListener);
        mAttacher.setOnViewTapListener(mViewTapListener);
        if (mSelectPicKey == null) {
            String uri = getIntent().getStringExtra("photo_uri");
            ImageLoader.getInstance().displayImage(uri, mImageView,
                    ImageLoadOptions.getOptions(),
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri,
                                View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            int imageWidth = loadedImage.getWidth();
                            int width = 800;
                            showLog("pic_view", "imageWidth:" + imageWidth
                                    + " scale:" + width / imageWidth);
                            // mAttacher.setScale(width / imageWidth);
                        }
                    });
        } else {
            mImageView.setImageBitmap(UpdateTaskActivity.mTaskPic
                    .get(mSelectPicKey));
            mDeletePic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCompleteDialog();
                }
            });
        }
        // mRunFinishAnim = false;
        mFirstRun = true;
    }

    private void showCompleteDialog() {
        Dialog dialog = CommonUtils.showCompleteDialog(this, R.string.alert,
                R.string.sure_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateTaskActivity.mTaskPic.remove(mSelectPicKey);
                        finish();
                    }
                }, false);

        dialog.show();
    }

    @Override
    public void finish() {
        super.finish();
        // overridePendingTransition(0, R.anim.slide_down_out);d
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAttacher.cleanup();
    }

    private OnMatrixChangedListener mOatrixChangedListener = new OnMatrixChangedListener() {
        @Override
        public void onMatrixChanged(RectF arg0) {
            if (!mFirstRun) {
                mHasTouchPhoto = true;
                mOnPhotoTapListener.onPhotoTap(null, 0, 0);
            } else {
                mFirstRun = false;
            }
        }
    };

    private OnViewTapListener mViewTapListener = new OnViewTapListener() {
        @Override
        public void onViewTap(View arg0, float arg1, float arg2) {
            finish();
        }
    };

    private OnPhotoTapListener mOnPhotoTapListener = new OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View arg0, float arg1, float arg2) {
            if (!mHasTouchPhoto) {
                // finish();
                mTitleBar
                        .setVisibility(mTitleBar.getVisibility() == View.VISIBLE ? View.INVISIBLE
                                : View.VISIBLE);
            }
            mHasTouchPhoto = false;
        }
    };
}
