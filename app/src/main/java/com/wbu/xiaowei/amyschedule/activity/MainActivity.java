package com.wbu.xiaowei.amyschedule.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.activeandroid.query.Select;
import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.adapter.MainFragmentAdapter;
import com.wbu.xiaowei.amyschedule.bean.Course;
import com.wbu.xiaowei.amyschedule.fragment.HomeFragment;
import com.wbu.xiaowei.amyschedule.fragment.PersonalCenterFragment;
import com.wbu.xiaowei.amyschedule.fragment.ScheduleFragment;
import com.wbu.xiaowei.amyschedule.net.URL;
import com.wbu.xiaowei.amyschedule.util.ScheduleUtils;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView navView; // 底部导航按钮
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(); // 初始化
        checkNetwork(); // 检查网络
    }

    /**
     * 初始化
     */
    private void initView() {
        navView = findViewById(R.id.nav_view);
        // 设置底部导航栏按钮第一个选中
        navView.setSelectedItemId(R.id.nav_home);
        // 为底部导航栏按钮添加监听
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 为 FragmentList 添加数据
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ScheduleFragment());
        fragmentList.add(new PersonalCenterFragment());

        viewPager = findViewById(R.id.view_pager);
        // 创建适配器
        MainFragmentAdapter mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(3); // 设置
        viewPager.setAdapter(mainFragmentAdapter); // 设置数据适配器
        viewPager.setCurrentItem(0); // 设置默认显示的页面
        // ViewPager 监听事件
        viewPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    /**
     * 底部导航按钮点击事件
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    viewPager.setCurrentItem(0);
                    return true;

                case R.id.nav_schedule:
                    viewPager.setCurrentItem(1);
                    return true;

                case R.id.nav_personal_center:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    /**
     * view pager 切换事件
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int position) {

            // 设置底部导航栏选中状态跟随页面
            switch (position) {
                case 0:
                    navView.setSelectedItemId(R.id.nav_home);
                    break;

                case 1:
                    navView.setSelectedItemId(R.id.nav_schedule);
                    break;

                case 2:
                    navView.setSelectedItemId(R.id.nav_personal_center);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    /**
     * 在子线程进行网络检查<br/>
     * 1. 是否连接互联网<br/>
     * 2. 是否连接校园网
     */
    private void checkNetwork() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url(URL.LOGIN_URL).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showNetworkUnavailableSnackbar();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!response.contains("学生个人中心")) {
                            showNotOnSchoolNetworkSnackbar();
                        }
                    }
                });
            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ScheduleUtils.getInstance(this).readUserInfo();
        if (ScheduleUtils.userInfo != null) {
            Log.d(TAG, "onResume: " + ScheduleUtils.userInfo.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }

    /**
     * 网络不可用的底部提示
     */
    private void showNetworkUnavailableSnackbar() {
        Snackbar.make(navView, "你好像没有连接互联网", Snackbar.LENGTH_INDEFINITE)
                .setAction("朕已阅", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // do nothing
                    }
                })
                .show();
    }

    /**
     * 不在商院网络环境内的底部提示
     */
    private void showNotOnSchoolNetworkSnackbar() {
        Snackbar.make(navView, "你不在商院网络环境内\n部分功能将受到限制", Snackbar.LENGTH_INDEFINITE)
                .setAction("本宫知道", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // do nothing
                    }
                })
                .show();
    }

    /**
     * 退出教务系统
     */
    private void logout() {
        Log.d(TAG, "onDestroy: 正在注销...");
        if (ScheduleUtils.userInfo != null) {
            ScheduleUtils.userInfo.isLogin = false;
            ScheduleUtils.getInstance(this).saveUserInfo();
            ScheduleUtils.logout(); // 注销
        }
    }

    public static List<Schedule> scheduleList;

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Course> courses = new Select().from(Course.class).execute();
                scheduleList = ScheduleSupport.transform(courses);
            }
        }).start();
    }
}
