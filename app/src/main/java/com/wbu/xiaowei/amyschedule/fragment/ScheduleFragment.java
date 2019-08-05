package com.wbu.xiaowei.amyschedule.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.activity.LoginActivity;
import com.wbu.xiaowei.amyschedule.activity.MainActivity;
import com.wbu.xiaowei.amyschedule.bean.Course;
import com.wbu.xiaowei.amyschedule.bean.CourseBase;
import com.wbu.xiaowei.amyschedule.net.CourseBeanListCallback;
import com.wbu.xiaowei.amyschedule.other.WheelViewDialog;
import com.wbu.xiaowei.amyschedule.util.DrawableTintUtil;
import com.wbu.xiaowei.amyschedule.util.NetWorkUtils;
import com.wbu.xiaowei.amyschedule.util.ScheduleUtils;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.listener.OnItemBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleSupport;
import com.zhuangfei.timetable.view.WeekView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class ScheduleFragment extends Fragment {
    private View view;

    // 课表控件
    private WeekView weekView;
    private TimetableView timetableView;

    // 其他控件
    private TextView selectWeekBtn; // 周次选择
    private ImageButton addBtn; // 右上角 + 号
    private PopupWindow popupMenu; // 右上角菜单
    private TextView promptTv; // 提示文本
    private View translucentMask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        this.view = view;
        initView(); // 初始化基本控件
        return view;
    }

    /**
     * 初始化基本控件
     */
    private void initView() {
        addBtn = view.findViewById(R.id.add_btn_in_schedule_page);
        addBtn.setColorFilter(getResources().getColor(R.color.white));
        addBtn.setOnClickListener(titleOnClickListener);

        promptTv = view.findViewById(R.id.prompt_text_view_in_schedule_page);
        translucentMask = view.findViewById(R.id.translucent_mask_in_schedule_page);
    }

    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            importCourse();
        }
    };

    // 是否进行首次初始化
    private boolean isInit = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 提高性能，课表页可见时再进行初始化
        if (isInit && getUserVisibleHint()) {
            hiddenNoneScheduleInfo();
            showLoadingAnim();
            initPopMenu(); // 初始化下拉菜单
            initTimetableView(); // 初始化课表控件
        }
    }

    /**
     * 初始化右上角菜单
     */
    private void initPopMenu() {
        // 获取自定义的菜单布局文件
        View menu_view = getLayoutInflater().inflate(R.layout.menu_schedule_add, null, false);

        // 创建 PopupWindow 实例，设置菜单宽度和高度为包裹其自身内容
        popupMenu = new PopupWindow(menu_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 隐藏半透明背景
                translucentMask.setVisibility(View.INVISIBLE);
            }
        });

        // 获取菜单并添加监听
        LinearLayout refreshBtn = menu_view.findViewById(R.id.refresh_schedule);
        LinearLayout importBtn = menu_view.findViewById(R.id.import_schedule);
        refreshBtn.setOnClickListener(menuOnclickListener);
        importBtn.setOnClickListener(menuOnclickListener);
    }

    /**
     * 初始化课表控件
     */
    private void initTimetableView() {
        Log.d("ScheduleFragment", "initTimetableView: 开始初始化课表控件...");

        // 展示加载动画
        showLoadingAnim();

        if (MainActivity.scheduleList == null || MainActivity.scheduleList.isEmpty()) {
            // 从数据库读取课程列表
            List<Course> courses = new Select().from(Course.class).execute();
            MainActivity.scheduleList = ScheduleSupport.transform(courses);
        }

        if (MainActivity.scheduleList.isEmpty()) {
            // 没有课表，等待重新初始化
            showNoneScheduleInfo();
            Log.d("ScheduleFragment", "initTimetableView: 没有课程列表，停止初始化，等待下次初始化...");
            return;
        }

        // 初始化周次选择下拉
        initSelectWeekDropDown();
        // 当前周
        int curWeek = ScheduleUtils.userInfo.currentWeek;
        selectWeekBtn.setText("第" + curWeek + "周");
        selectWeekBtn.setCompoundDrawables(null, null, iconDropDown, null);
        Log.d("ScheduleFragment", "initTimetableView: 获取当前周: curWeek: " + curWeek);

        // 获取周次选择控件
        weekView = view.findViewById(R.id.id_weekview);
        // 设置周次选择控件
        weekView.curWeek(curWeek)
                .data(MainActivity.scheduleList)
                .callback(onWeekLeftClickedListener) // 点击左侧
                .callback(new IWeekView.OnWeekItemClickedListener() { // 点击周
                    @Override
                    public void onWeekClicked(int week) {
                        int cur = timetableView.curWeek();
                        // 更新切换后的日期，从当前周 cur -> 切换的周 week
                        timetableView.onDateBuildListener().onUpdateDate(cur, week);
                        timetableView.changeWeekOnly(week);
                    }
                })
                .isShow(false); // 设置隐藏，默认显示

        // 获取课表控件
        timetableView = view.findViewById(R.id.id_timetableView);
        // 为课表控件添加数据
        timetableView.data(MainActivity.scheduleList);
        // 当前学期
        String curTerm = ScheduleUtils.userInfo.semester;
        // 设置课表控件
        timetableView.curWeek(curWeek)
                .curTerm(curTerm)
                .isShowFlaglayout(false)
                .isShowWeekends(!ScheduleSupport.getHaveSubjectsWithDay(MainActivity.scheduleList, curWeek, 6).isEmpty())
                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, List<Schedule> scheduleList) { // 点击课程
                        display(scheduleList);
                    }
                })
                .callback(new ISchedule.OnItemLongClickListener() { // 长按课程
                    @Override
                    public void onLongClick(View v, int day, int start) {
                        Toast.makeText(getActivity(), "你压我干嘛", Toast.LENGTH_SHORT).show();
                    }
                })
                .callback(new ISchedule.OnWeekChangedListener() {
                    @Override
                    public void onWeekChanged(int curWeek) {
                        selectWeekBtn.setText("第" + curWeek + "周");
                    }
                })
                .callback(new ISchedule.OnScrollViewBuildListener() {
                    @Override
                    public View getScrollView(LayoutInflater mInflate) {
                        return mInflate.inflate(R.layout.custom_myscrollview, null, false);
                    }
                })
                .callback(new OnItemBuildAdapter() {
                    @Override
                    public void onItemUpdate(FrameLayout layout, TextView textView, TextView countTextView, Schedule schedule, GradientDrawable gd) {
                        super.onItemUpdate(layout, textView, countTextView, schedule, gd);
                        // TODO: 2019/7/22 给课程位置添加广告等
                    }
                });

        // 初始化完成 显示课表
        showSchedule();
        isInit = false;
        Log.d("ScheduleFragment", "initTimetableView: 初始化课表控件完成...");
    }

    private Drawable iconDropDown; // 向下三角
    private Drawable iconDropUp; // 向上三角

    /**
     * 初始化周次选择下拉
     */
    private void initSelectWeekDropDown() {
        iconDropDown = getActivity().getResources().getDrawable(R.drawable.ic_drop_down);
        iconDropDown.setBounds(0, 0, iconDropDown.getMinimumWidth(), iconDropDown.getMinimumHeight());
        iconDropDown = DrawableTintUtil.tintDrawable(iconDropDown, getActivity().getResources().getColor(R.color.white));

        iconDropUp = getActivity().getResources().getDrawable(R.drawable.ic_drop_up);
        iconDropUp.setBounds(0, 0, iconDropUp.getMinimumWidth(), iconDropUp.getMinimumHeight());
        iconDropUp = DrawableTintUtil.tintDrawable(iconDropUp, getActivity().getResources().getColor(R.color.white));

        selectWeekBtn = view.findViewById(R.id.select_week_btn);
        selectWeekBtn.setCompoundDrawables(null, null, iconDropDown, null);
        // 设置周次选择按钮点击事件监听
        selectWeekBtn.setOnClickListener(titleOnClickListener);
    }

    /**
     * 标题栏点击事件
     */
    View.OnClickListener titleOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.select_week_btn:
                    // 如果周次选择已经显示了，那么将它隐藏，更新课程、日期。否则，显示
                    if (weekView.isShowing()) {
                        hideWeekView();
                    } else {
                        showWeekView();
                    }
                    break;

                case R.id.add_btn_in_schedule_page:
                    // 弹出右上角菜单
                    popupMenu.showAsDropDown(addBtn, 0, 0);
                    translucentMask.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    /**
     * 右上角菜单点击事件
     */
    View.OnClickListener menuOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.refresh_schedule:
                    popupMenu.dismiss();
                    List<Course> courses = new Select().from(Course.class).execute();
                    if (courses.isEmpty()) {
                        return;
                    }
                    timetableView.source(courses);
                    timetableView.updateView();
                    break;
                case R.id.import_schedule:
                    popupMenu.dismiss();
                    showLoadingAnim();
                    importCourse();
                    break;
            }
        }
    };

    /**
     * 从教务系统导入课表，由于课程数量屈指可数，因此没有使用多线程<br/>
     * 后期可以考虑在子线程获取课表以提高性能，无非突破一下 500 行代码
     */
    private void importCourse() {
        int flag = ScheduleUtils.getCourse(new CourseBeanListCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("ScheduleFragment", "ScheduleUtils.getCourse(): onError(): 获取课表失败...");
                checkNetork(); // 检查网络
            }

            @Override
            public void onResponse(Map<String, List<CourseBase>> courseBaseMap, int id) {
                if (courseBaseMap == null || courseBaseMap.isEmpty()) {
                    // 教务系统没有课表
                    Log.d("获取课表：", "教务系统没有课表...");
                    return;
                }

                // 按照班级获取课程列表
                List<CourseBase> courseBaseList = courseBaseMap.get(ScheduleUtils.userInfo.clazz);
                if (courseBaseList == null || courseBaseList.isEmpty()) {
                    // 获取的课表没有该班级
                    Log.d("获取课表：", "获取的课表没有该班级...");
                    return;
                }

                // 将课程列表转换为 Schedule 实现类对象集合
                final List<Course> courses = new ArrayList<>();
                for (CourseBase courseBase : courseBaseList) {
                    courses.add(courseBase.toCourseModel());
                }

                if (isInit) {
                    // 初始化课表
                    MainActivity.scheduleList = ScheduleSupport.transform(courses);
                    initTimetableView();
                } else {
                    // 刷新课表
                    timetableView.source(courses);
                    timetableView.updateView();

                    weekView.source(courses);
                    weekView.updateView();

                    promptTv.setVisibility(View.INVISIBLE);
                    hiddenLoadingAnim();
                }

                // 在子线程中将课表保存到数据库
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ScheduleFragment", "importCourse: 保存课表: 正在子线程保存课表...");
                        new Delete().from(Course.class).execute(); // 删除所有旧课程
                        for (Course course : courses) {
                            course.save();
                        }
                        Log.d("ScheduleFragment", "importCourse: 保存课表: 子线程保存课表成功...");
                    }
                }).start();
            }
        });

        switch (flag) {
            case ScheduleUtils.NOT_LOGIN:
                Log.d("ScheduleFragment", "importCourse: 获取课表: case ScheduleUtils.NOT_LOGIN: 用户没有登录...");
                hiddenLoadingAnim();
                showToLoginSnackbar(); // 底部提示去登录
                break;
            case ScheduleUtils.INCOMPLETE_USER_INFORMATION:
                Log.d("ScheduleFragment", "importCourse: 获取课表: case ScheduleUtils.INCOMPLETE_USER_INFORMATION: 用户信息不完整...");
                hiddenLoadingAnim();
                showToCompleteInfoSnackbar(); // 底部提示去完善资料
                break;
        }
    }

    /**
     * 检查网络
     */
    private void checkNetork() {
        if (!NetWorkUtils.isNetWorkAvailable(getActivity())) {
            hiddenLoadingAnim();
            showNetworkUnavailableSnackbar();
            return;
        }

        int status = ScheduleUtils.login(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // 不在校园网络环境内
                showNotOnSchoolNetworkSnackbar();
            }

            @Override
            public void onResponse(String response, int id) {
                if (response.contains("学生个人中心")) {
                    // 重试
                    ScheduleUtils.userInfo.isLogin = true;
                } else {
                    // 未知错误
                }
            }
        });

        switch (status) {
            case ScheduleUtils.NOT_LOGIN:
                showToLoginSnackbar();
                break;
            case ScheduleUtils.INCOMPLETE_USER_INFORMATION:
                showToCompleteInfoSnackbar();
                break;
        }
    }

    /**
     * 显示周次选择控件
     */
    private void showWeekView() {
        weekView.setVisibility(View.VISIBLE);
        weekView.isShow(true);
        // 三角形向上
        selectWeekBtn.setCompoundDrawables(null, null, iconDropUp, null);
    }

    /**
     * 隐藏周次选择，此时需要将课表的日期恢复到本周并将课表切换到当前周
     */
    private void hideWeekView() {
        weekView.isShow(false);
        int cur = timetableView.curWeek();
        timetableView.onDateBuildListener().onUpdateDate(cur, cur);
        timetableView.changeWeekOnly(cur);
        // 三角形向下
        selectWeekBtn.setCompoundDrawables(null, null, iconDropDown, null);
    }

    /**
     * 点击课程显示内容
     *
     * @param scheduleList 课程列表
     */
    private void display(List<Schedule> scheduleList) {
        StringBuilder str = new StringBuilder("显示类容：");
        for (Schedule schedule : scheduleList) {
            str.append(schedule.getName()).append("\n");
        }
        Toast.makeText(getActivity(), str.toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 周次选择布局的左侧被点击时回调<br/>
     * 对话框修改当前周次
     */
    private IWeekView.OnWeekLeftClickedListener onWeekLeftClickedListener = new IWeekView.OnWeekLeftClickedListener() { // 点击左侧
        @Override
        public void onWeekLeftClicked() {
            List<String> items = new ArrayList<>();
            int itemCount = weekView.itemCount();

            for (int i = 0; i < itemCount; i++) {
                items.add("第" + (i + 1) + "周");
            }

            WheelViewDialog dialog = new WheelViewDialog(getActivity(), items);
            dialog.setTitleText("设置当前周");
            dialog.setPositiveButtonText("设为当前周");
            dialog.setInitPosition(ScheduleUtils.userInfo.currentWeek - 1);
            dialog.setNoNegativeButton(true);
            dialog.setOnSelectedListener(new WheelViewDialog.OnSelectedListener() {
                @Override
                public void onSelected(int target) {
                    if (target != -1) {
                        Log.d("ScheduleFragment", "onWeekLeftLayoutClicked: 设置当前周: curWeek: " + (target + 1));
                        hideWeekView();
                        weekView.curWeek(target + 1).updateView();
                        timetableView.changeWeekForce(target + 1);
                        ScheduleUtils.userInfo.currentWeek = target + 1;
                        ScheduleUtils.getInstance(getActivity()).saveUserInfo();
                    }
                }
            });
            dialog.show();
        }
    };

    /**
     * 隐藏周次和课表
     */
    private void hiddenSchedule() {
        weekView.setVisibility(View.INVISIBLE);
        timetableView.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示周次和课表
     */
    private void showSchedule() {
        hiddenLoadingAnim();
        hiddenNoneScheduleInfo();
        weekView.setVisibility(View.VISIBLE);
        weekView.showView();
        timetableView.setVisibility(View.VISIBLE);
        timetableView.showView();
    }

    /**
     * NO_LOGIN
     * 去登录的底部提示
     */
    private void showToLoginSnackbar() {
        Snackbar.make(addBtn, "你还未登录，登录了还有救", Snackbar.LENGTH_INDEFINITE).setAction("去登录", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }).show();
    }

    /**
     * INCOMPLETE_USER_INFORMATION
     * 去完善信息的底部提示
     */
    private void showToCompleteInfoSnackbar() {
        Snackbar.make(addBtn, "你的信息不完整", Snackbar.LENGTH_LONG).setAction("去填写", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        }).show();
    }

    /**
     * 不在商院网络环境内的底部提示
     */
    private void showNotOnSchoolNetworkSnackbar() {
        Snackbar.make(addBtn, "你不在商院网络环境内", Snackbar.LENGTH_LONG).setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).show();
    }

    /**
     * 网络不可用的底部提示
     */
    private void showNetworkUnavailableSnackbar() {
        Snackbar.make(addBtn, "你好像没有连接互联网", Snackbar.LENGTH_INDEFINITE)
                .setAction("起开!", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // do nothing
                    }
                })
                .show();
    }

    /**
     * 隐藏提示信息
     */
    private void hiddenNoneScheduleInfo() {
        hiddenLoadingAnim();
        promptTv.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示提示信息
     */
    private void showNoneScheduleInfo() {
        hiddenLoadingAnim();
        promptTv.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏加载动画
     */
    private void hiddenLoadingAnim() {
        //refreshLayout.setRefreshing(false);
    }

    /**
     * 显示加载动画
     */
    private void showLoadingAnim() {
        //refreshLayout.setRefreshing(true);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
