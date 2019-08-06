package com.wbu.xiaowei.amyschedule.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.bean.Course;
import com.wbu.xiaowei.amyschedule.bean.Major;
import com.wbu.xiaowei.amyschedule.bean.UserInfo;
import com.wbu.xiaowei.amyschedule.other.WheelViewDialog;
import com.wbu.xiaowei.amyschedule.util.ScheduleUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = PersonalInformationActivity.class.getSimpleName();

    private ImageButton backBtnInUserInfoPage;
    private TextView logoutBtn;
    private TextView usernameTvInUserInfoPage;
    private TextView gradeTvInUserInfoPage;
    private TextView departmentsTvInUserInfoPage;
    private TextView majorTvInUserInfoPage;
    private TextView clazzTvInUserInfoPage;
    private TextView semesterTvInUserInfoPage;
    private Button saveBtnInLoginPage;
    private ProgressBar progressBar;

    private String grade;
    private String departments;
    private String major;
    private String clazz;
    private String semester;

    private List<String> departmentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        backBtnInUserInfoPage = findViewById(R.id.back_btn_in_user_info_page);
        backBtnInUserInfoPage.setColorFilter(getResources().getColor(R.color.white));
        backBtnInUserInfoPage.setOnClickListener(this);

        logoutBtn = findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(this);

        usernameTvInUserInfoPage = findViewById(R.id.username_tv_in_user_info_page);
        usernameTvInUserInfoPage.setText(ScheduleUtils.userInfo.username);

        gradeTvInUserInfoPage = findViewById(R.id.grade_tv_in_user_info_page);
        gradeTvInUserInfoPage.setText(ScheduleUtils.userInfo.grade);
        gradeTvInUserInfoPage.setOnClickListener(this);

        departmentsTvInUserInfoPage = findViewById(R.id.departments_tv_in_user_info_page);
        departmentsTvInUserInfoPage.setText(ScheduleUtils.userInfo.departments);
        departmentsTvInUserInfoPage.setOnClickListener(this);

        majorTvInUserInfoPage = findViewById(R.id.major_tv_in_user_info_page);
        majorTvInUserInfoPage.setText(ScheduleUtils.userInfo.major);
        majorTvInUserInfoPage.setOnClickListener(this);

        clazzTvInUserInfoPage = findViewById(R.id.clazz_tv_in_user_info_page);
        clazzTvInUserInfoPage.setText(ScheduleUtils.userInfo.clazz);
        clazzTvInUserInfoPage.setOnClickListener(this);

        semesterTvInUserInfoPage = findViewById(R.id.semester_tv_in_user_info_page);
        semesterTvInUserInfoPage.setText(ScheduleUtils.userInfo.semester);
        semesterTvInUserInfoPage.setOnClickListener(this);

        saveBtnInLoginPage = findViewById(R.id.save_btn_in_login_page);
        saveBtnInLoginPage.setOnClickListener(this);

        progressBar = findViewById(R.id.progress_bar_in_info_page);
        departmentsList = new ArrayList<>(UserInfo.DEPARTMENTS.keySet());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn_in_user_info_page: // 返回
                finish();
                break;
            case R.id.logout_btn: // 注销
                logout();
                break;
            case R.id.save_btn_in_login_page: // 保存
                // TODO: 2019/8/5 保存用户信息
                //saveUserInfo();
                break;
            default:
                showSelectDialog(view.getId());
                break;
        }
    }

    /**
     * 保存用户信息
     */
    private void saveUserInfo() {
        progressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(grade)) {
            ScheduleUtils.userInfo.grade = grade;
        }
        if (!TextUtils.isEmpty(departments)) {
            ScheduleUtils.userInfo.departments = departments;
        }
        if (!TextUtils.isEmpty(major)) {
            ScheduleUtils.userInfo.major = major;
        }
        if (!TextUtils.isEmpty(clazz)) {
            ScheduleUtils.userInfo.clazz = clazz;
        }
        if (!TextUtils.isEmpty(semester)) {
            ScheduleUtils.userInfo.semester = semester;
        }
        ScheduleUtils.getInstance(this).saveUserInfo();
        progressBar.setVisibility(View.INVISIBLE);
        Snackbar.make(backBtnInUserInfoPage, "保存成功", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    /**
     * 显示单选对话框
     */
    private void showSelectDialog(int id) {
        WheelViewDialog dialog = new WheelViewDialog(PersonalInformationActivity.this);
        dialog.setNoNegativeButton(true);
        dialog.setLoop(false);
        switch (id) {
            case R.id.grade_tv_in_user_info_page: // 年级
                dialog.setItems(UserInfo.GRADE);
                dialog.setTitleText("请选择年级");
                dialog.setOnSelectedListener(new WheelViewDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int position) {
                        grade = UserInfo.GRADE.get(position).substring(0, 4);
                        gradeTvInUserInfoPage.setText(grade);
                    }
                });
                break;

            case R.id.departments_tv_in_user_info_page: // 学院
                dialog.setItems(departmentsList);
                dialog.setTitleText("请选择学院");
                dialog.setInitPosition(departmentsList.indexOf(ScheduleUtils.userInfo.departments));
                dialog.setOnSelectedListener(new WheelViewDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int position) {
                        departments = UserInfo.DEPARTMENTS.get(departmentsList.get(position));
                        departmentsTvInUserInfoPage.setText(departmentsList.get(position));
                    }
                });

                break;
            case R.id.major_tv_in_user_info_page: // 专业
                dialog.setItems(null);
                dialog.setTitleText("请选择专业");
                dialog.setOnSelectedListener(new WheelViewDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int position) {
                        majorTvInUserInfoPage.setText(major);
                    }
                });
                break;

            case R.id.clazz_tv_in_user_info_page: // 班级
                dialog.setItems(null);
                dialog.setTitleText("请选择班级");
                dialog.setOnSelectedListener(new WheelViewDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int position) {
                        clazzTvInUserInfoPage.setText(clazz);
                    }
                });
                break;

            case R.id.semester_tv_in_user_info_page: // 学期
                dialog.setItems(UserInfo.SEMESTER);
                dialog.setTitleText("请选择学期");
                dialog.setOnSelectedListener(new WheelViewDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int position) {
                        semester = UserInfo.SEMESTER.get(position);
                        semesterTvInUserInfoPage.setText(semester);
                    }
                });
                break;
        }
        dialog.show();
    }

    /**
     * 删除账户信息并退出教务系统
     */
    private void logout() {
        ScheduleUtils.logout();
        ScheduleUtils.userInfo = null;
        boolean success = deleteFile("user_info.xml");
        Log.d(TAG, "logout: user_info.xml删除: " + success);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Delete().from(Course.class).execute();
                new Delete().from(Major.class).execute();
                resetOkHttpUtils();
            }
        }).start();
        finish();
    }

    /**
     * 重置 OKHttpUtils
     */
    private void resetOkHttpUtils() {
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

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
