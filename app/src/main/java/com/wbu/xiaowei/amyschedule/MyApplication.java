package com.wbu.xiaowei.amyschedule;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化 ActivityAndroid
        ActiveAndroid.initialize(MyApplication.this);

        // Cookie 持久化
        CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());

        // 实例化 OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .build();

        // 初始化 OkHttpUtils
        OkHttpUtils.initClient(okHttpClient);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // 清理 ActivityAndroid
        ActiveAndroid.dispose();
    }
}
