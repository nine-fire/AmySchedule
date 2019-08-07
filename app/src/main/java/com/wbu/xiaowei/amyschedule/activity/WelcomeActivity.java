package com.wbu.xiaowei.amyschedule.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.util.ScheduleUtils;
import com.wbu.xiaowei.amyschedule.util.TimeUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Date;

import okhttp3.Call;

public class WelcomeActivity extends BaseActivity {
    public static final String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 显示版本号
        TextView versionTv = findViewById(R.id.version_text_view_in_welcome);
        versionTv.setText(getVersionName(this));

        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (isFirstTimeRun()) { // 安装 APP 后首次打开
            doFirstTimeRun();
        } else { // 安装后非首次打开
            doNotFirstTimeRun();
        }
    }

    /**
     * 判断是否为当天首次打开 APP
     */
    private boolean isFirstTimeRunOfDay() {
        SharedPreferences sp = getSharedPreferences("welcome", MODE_PRIVATE);
        long lastTimeRun = sp.getLong("lastTimeRun", new Date().getTime());
        long nowTime = new Date().getTime();

        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("lastTimeRun", nowTime);
        editor.apply();

        int dayOffset = TimeUtil.calcDayOffset(lastTimeRun, nowTime);

        return dayOffset > 0;

    }

    /**
     * 判断是否为当周首次打开 APP
     */
    private boolean isFirstTimeRunOfWeek() {
        SharedPreferences sp = getSharedPreferences("welcome", MODE_PRIVATE);
        long lastTimeRun = sp.getLong("lastTimeRun", new Date().getTime());
        long nowTime = new Date().getTime();

        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("lastTimeRun", nowTime);
        editor.apply();

        int weekOffset = TimeUtil.calcWeekOffset(lastTimeRun, nowTime);

        if (weekOffset > 0) {
            ScheduleUtils.userInfo.currentWeek += weekOffset;
            ScheduleUtils.getInstance(this).saveUserInfo();
            return true;
        }

        return false;
    }

    /**
     * 判断是否首次打开 APP
     */
    private boolean isFirstTimeRun() {
        SharedPreferences sp = getSharedPreferences("welcome", MODE_PRIVATE);
        boolean isFirstTimeRun = sp.getBoolean("isFirstTimeRun", true);
        if (isFirstTimeRun) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isFirstTimeRun", false);
            editor.apply();
        }
        return isFirstTimeRun;
    }

    /**
     * 判断是否为新版的 APP
     */
    private boolean isNewVersionApp() {
        SharedPreferences sp = getSharedPreferences("welcome", MODE_PRIVATE);

        float oldVersionCode = sp.getFloat("oldVersionCode", 0);
        float nowVersionCode = getVersionCode(this);

        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("oldVersionCode", nowVersionCode);
        editor.apply();

        return nowVersionCode > oldVersionCode;
    }

    /**
     * 获取软件版本号
     */
    private float getVersionCode(Context context) {
        float versionCode = 0;
        try {
            // 获取软件版本号，对应 AndroidManifest.xml 下 android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取软件版本名称
     */
    private String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 应用首次启动
     */
    private void doFirstTimeRun() {
        Log.d(TAG, "doFirstTimeRun: " + "应用首次启动");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAppToHome();
            }
        }, 3000);
    }

    /**
     * 应用非首次启动
     */
    private void doNotFirstTimeRun() {
        Log.d(TAG, "doNotFirstTimeRun: " + "应用非首次启动，正在尝试登录...");

        // 读取用户信息
        ScheduleUtils.getInstance(this).readUserInfo();

        if (ScheduleUtils.userInfo != null && isFirstTimeRunOfWeek()) {
            Log.d(TAG, "当周首次打开");
        } else {
            Log.d(TAG, "非当周首次打开");
        }

        // 尝试登录教务系统
        ScheduleUtils.login(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ScheduleUtils.userInfo.isLogin = false;
                ScheduleUtils.getInstance(WelcomeActivity.this).saveUserInfo();
            }

            @Override
            public void onResponse(String response, int id) {
                if (response.contains("学生个人中心")) {
                    Log.d(TAG, "登录成功");
                    ScheduleUtils.userInfo.isLogin = true;
                } else {
                    ScheduleUtils.userInfo.isLogin = false;
                }
                ScheduleUtils.getInstance(WelcomeActivity.this).saveUserInfo();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startAppToHome();
            }
        }, 1000);
    }

    /**
     * 跳转至首页
     */
    private void startAppToHome() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
