package com.wbu.xiaowei.amyschedule.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.activity.MainActivity;
import com.wbu.xiaowei.amyschedule.adapter.TodayCourseListAdapter;
import com.wbu.xiaowei.amyschedule.bean.Course;
import com.wbu.xiaowei.amyschedule.other.SimpleDividerItemDecoration;
import com.wbu.xiaowei.amyschedule.util.ScheduleUtils;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private View view;
    private SwipeRefreshLayout refreshLayout; // 下拉刷新
    private RecyclerView todayCourseRecyclerView; // 今日课程
    private TextView timeTv;
    private TextView noneCourseTodayView; // 今日无课提示

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.view = view;
        initView(); // 初始化基本控件
        initTodayCourseView(); // 初始化今日课表
        return view;
    }

    /**
     * 初始化基本控件
     */
    private void initView() {
        noneCourseTodayView = view.findViewById(R.id.none_course_today_text_view);

        refreshLayout = view.findViewById(R.id.swipe_refresh_in_home_page);
        refreshLayout.setOnRefreshListener(mRefreshListener);
    }

    /**
     * 初始化今日课程
     */
    private void initTodayCourseView() {
        String[] weekStr = {"星期六", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五"};
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        timeTv = view.findViewById(R.id.time_on_today_course_card);
        timeTv.setText(year + "/" + month + "/" + day + "  " + weekStr[dayOfWeek]);

        if (MainActivity.scheduleList == null || MainActivity.scheduleList.isEmpty()) {
            // 从数据库读取课程列表
            List<Course> courses = new Select().from(Course.class).execute();
            MainActivity.scheduleList = ScheduleSupport.transform(courses);
        }
        if (MainActivity.scheduleList.isEmpty()) {
            // 没有课表，等待重新初始化
            noneCourseTodayView.setText("还未导入课表");
            noneCourseTodayView.setVisibility(View.VISIBLE);
            isInit = true;
            return;
        }

        // 获取今日课程
        List<Schedule> schedules = ScheduleSupport.getHaveSubjectsWithDay(MainActivity.scheduleList, ScheduleUtils.userInfo.currentWeek, Calendar.DAY_OF_WEEK - 1);
        if (schedules.isEmpty()) {
            // 今天确实没课
            noneCourseTodayView.setText("今天没有课  好好放松一下吧");
            noneCourseTodayView.setVisibility(View.VISIBLE);
            isInit = false;
            return;
        }
        // 今天有课
        noneCourseTodayView.setVisibility(View.GONE);

        // 获取今日课程控件
        todayCourseRecyclerView = view.findViewById(R.id.recycler_view_taday_course);
        // 创建今日课程数据适配器
        TodayCourseListAdapter todayCourseListAdapter = new TodayCourseListAdapter(getActivity(), schedules);
        // 创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 设置布局管理器
        todayCourseRecyclerView.setLayoutManager(layoutManager);
        // 设置为垂直布局
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        // 设置 Adapter
        todayCourseRecyclerView.setAdapter(todayCourseListAdapter);
        // 设置分割线
        todayCourseRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        // 设置增加或删除条目的动画
        todayCourseRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Log.d(TAG, "initTodayCourseView: 初始化完毕...");
        isInit = false;
    }

    // 是否进行首次初始化
    private boolean isInit = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isInit && getUserVisibleHint()) {
            initTodayCourseView();
        }
    }

    /**
     * 下拉刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hiddenLoadingAnim();
                }
            }, 4000);
        }
    };

    private void showLoadingAnim() {
        refreshLayout.setRefreshing(true);
    }

    private void hiddenLoadingAnim() {
        refreshLayout.setRefreshing(false);
    }

}
