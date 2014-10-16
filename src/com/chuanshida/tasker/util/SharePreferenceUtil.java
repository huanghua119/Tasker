package com.chuanshida.tasker.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 首选项管理
 */
@SuppressLint("CommitPrefEdits")
public class SharePreferenceUtil {
    private SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    public SharePreferenceUtil(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    private String SHARED_KEY_NOTIFY = "shared_key_notify";
    private String SHARED_KEY_VOICE = "shared_key_sound";
    private String SHARED_KEY_VIBRATE = "shared_key_vibrate";
    private String SHARED_KEY_NEW_MESSAGE = "shared_key_new_message";
    private String SHARED_KEY_INSTALLATION_OBJECTID = "shared_key_installation_objectid";

    // 是否允许推送通知
    public boolean isAllowPushNotify() {
        return mSharedPreferences.getBoolean(SHARED_KEY_NOTIFY, true);
    }

    public void setPushNotifyEnable(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_NOTIFY, isChecked);
        editor.commit();
    }

    // 允许声音
    public boolean isAllowVoice() {
        return mSharedPreferences.getBoolean(SHARED_KEY_VOICE, true);
    }

    public void setAllowVoiceEnable(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_VOICE, isChecked);
        editor.commit();
    }

    // 允许震动
    public boolean isAllowVibrate() {
        return mSharedPreferences.getBoolean(SHARED_KEY_VIBRATE, true);
    }

    public void setAllowVibrateEnable(boolean isChecked) {
        editor.putBoolean(SHARED_KEY_VIBRATE, isChecked);
        editor.commit();
    }

    public boolean hasNewMessage() {
        return mSharedPreferences.getBoolean(SHARED_KEY_NEW_MESSAGE, false);
    }

    public void setNewMessage(boolean hasNew) {
        editor.putBoolean(SHARED_KEY_NEW_MESSAGE, hasNew);
        editor.commit();
    }

    public void setInstallationObjectId(String obejctId) {
        editor.putString(SHARED_KEY_INSTALLATION_OBJECTID, obejctId);
        editor.commit();
    }

    public String getInstallationObjectId() {
        return mSharedPreferences.getString(SHARED_KEY_INSTALLATION_OBJECTID,
                "");
    }
}
