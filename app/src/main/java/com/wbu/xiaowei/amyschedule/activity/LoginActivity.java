package com.wbu.xiaowei.amyschedule.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.bean.Course;
import com.wbu.xiaowei.amyschedule.bean.CourseBase;
import com.wbu.xiaowei.amyschedule.bean.MajorBase;
import com.wbu.xiaowei.amyschedule.bean.UserInfo;
import com.wbu.xiaowei.amyschedule.net.CourseBeanListCallback;
import com.wbu.xiaowei.amyschedule.net.MajorBeanListCallBack;
import com.wbu.xiaowei.amyschedule.net.StudentInfoCallback;
import com.wbu.xiaowei.amyschedule.other.LoadingAnimationDialog;
import com.wbu.xiaowei.amyschedule.util.ScheduleUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    // 控件
    private ImageButton backBtn;
    private EditText usernameEt;
    private EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView(); // 初始化
    }

    /**
     * 初始化
     */
    private void initView() {
        backBtn = findViewById(R.id.back_btn_in_login_page);
        backBtn.setOnClickListener(this);

        Button loginBtn = findViewById(R.id.login_btn_in_login_page);
        loginBtn.setOnClickListener(this);

        // 设置 logo 字体
        TextView logoText = findViewById(R.id.logo_in_login_page);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.library_3_am);
        logoText.setTypeface(typeface);

        usernameEt = findViewById(R.id.username_edit_text);
        passwordEt = findViewById(R.id.password_edit_text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn_in_login_page: // 返回
                finish();
                break;

            case R.id.login_btn_in_login_page: // 登录
                closeKeyboard(); // 关闭软键盘
                login(); // 尝试登录
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        // 获取用户输入
        String username = usernameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        // 简单验证用户输入
        boolean flag = verifyUsername(username, password);

        if (flag) {
            showLoadingAnim(); // 显示加载动画
            // 在发送登录请求之前先保存学号和密码，以防登录网络问题
            ScheduleUtils.userInfo = new UserInfo(username, password, "", 1, "", "", "", "", "", "", false);
            // ScheduleUtils.getInstance(this).saveUserInfo(); // 保存用户信息
            int status = ScheduleUtils.login(loginCallback); // 发送登录请求

            switch (status) {
                case ScheduleUtils.NOT_LOGIN:
                    // 没有用户数据，发生的几率很小
                    break;
                case ScheduleUtils.INCOMPLETE_USER_INFORMATION:
                    // 用户信息不完整，此处发生的概率几乎为零
                    break;
            }
        }
    }

    /**
     * 登录的回调
     */
    private Callback loginCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            Log.d(TAG, "loginCallback: onError: 登录失败...");
            ScheduleUtils.userInfo.isLogin = false;
        }

        @Override
        public void onResponse(String response, int id) {
            if (response.contains("学生个人中心")) {
                Log.d(TAG, "loginCallback: onResponse: response.contains(\"学生个人中心\"): 登录成功...");

                // 更新用户信息
                ScheduleUtils.userInfo.isLogin = true;
                ScheduleUtils.getInstance(LoginActivity.this).saveUserInfo();

                // 发送登录成功的消息，进行更多操作
                Message msg = Message.obtain();
                msg.what = ScheduleUtils.LOGIN_OK;
                handler.sendMessage(msg);
            } else if (response.contains("用户名或者密码有误")) {
                Log.d(TAG, "loginCallback: onResponse: response.contains(\"用户名或者密码有误\"): 用户名或者密码有误: 登录失败...");
                hiddenLoadingAnim();

                // 更新用户信息
                ScheduleUtils.userInfo.isLogin = false;
                showSnackbar("学号或密码错误  仔细检查吧");
            } else if (response.contains("验证码")) {
                Log.d(TAG, "loginCallback: onResponse: response.contains(\"验证码\"): 需要输入验证码: 登录失败...");

                hiddenLoadingAnim();
                showSnackbar("由于登录频繁  现在登录需要输入图形验证码\n需要去智慧校园手动登录一次即可解决");
            } else {
                Log.d(TAG, "loginCallback: onResponse: 响应体不包含“学生个人中心”: 登录失败...");
                hiddenLoadingAnim();

                // 更新用户信息
                ScheduleUtils.userInfo.isLogin = false;
                showSnackbar("发生了未知错误  请稍后重试吧");
            }
        }
    };

    /**
     * 消息处理器
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ScheduleUtils.LOGIN_OK:
                    getMajors();
                    break;
                case ScheduleUtils.MAJORS_INFO_OK:
                    getStudentInfo();
                    break;
                case ScheduleUtils.STUDENT_INFO_OK:
                    getCourse();
                    break;
                case ScheduleUtils.COURSE_LIST_OK:
                    hiddenLoadingAnim();
                    finish();
                    break;
            }
            return false;
        }
    });

    /**
     * 获取专业列表
     */
    private void getMajors() {
        ScheduleUtils.getMajors(new MajorBeanListCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(final List<MajorBase> majorBaseList, int id) {
                if (majorBaseList != null && !majorBaseList.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (MajorBase majorBase : majorBaseList) {
                                majorBase.toMajorModel().save();
                            }
                            Message msg = Message.obtain();
                            msg.what = ScheduleUtils.MAJORS_INFO_OK;
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
        });
    }

    /**
     * 获取学生信息
     */
    private void getStudentInfo() {
        int status = ScheduleUtils.getStudentInfo(new StudentInfoCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(UserInfo rUserInfo, int id) {
                if (rUserInfo != null && ScheduleUtils.userInfo.username.equals(rUserInfo.username)) {

                    rUserInfo.password = ScheduleUtils.userInfo.password;
                    ScheduleUtils.userInfo = rUserInfo;
                    ScheduleUtils.getInstance(LoginActivity.this).saveUserInfo();

                    // 发送学生信息 OK
                    Message msg = Message.obtain();
                    msg.what = ScheduleUtils.STUDENT_INFO_OK;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 获取课表
     */
    private void getCourse() {
        Log.d(TAG, "getCourse: 开始获取课表...");
        int status = ScheduleUtils.getCourse(new CourseBeanListCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "getCourse: 获取课表失败...");
            }

            @Override
            public void onResponse(Map<String, List<CourseBase>> courseMap, int id) {
                Log.d(TAG, "getCourse: " + courseMap);
                if (courseMap != null && !courseMap.isEmpty()) {

                    final List<CourseBase> courseBaseList = courseMap.get(ScheduleUtils.userInfo.clazz);
                    if (courseBaseList != null && !courseBaseList.isEmpty()) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new Delete().from(Course.class).execute(); // 删除所有旧课程
                                for (CourseBase courseBase : courseBaseList) {
                                    courseBase.toCourseModel().save();
                                }
                                Message msg = Message.obtain();
                                msg.what = ScheduleUtils.COURSE_LIST_OK;
                                handler.sendMessage(msg);
                                Log.d(TAG, "getCourse: 获取课表成功...");
                            }
                        }).start();
                    }
                }
            }
        });
    }

    /**
     * 简单验证学号和密码
     */
    private boolean verifyUsername(String username, String password) {
        if (username == null || "".equals(username)) {
            showSnackbar("学号填一下呗");
            return false;
        }
        if (password == null || "".equals(password)) {
            showSnackbar("密码写一下吧");
            return false;
        }
        if (username.length() < 6) {
            showSnackbar("你这学号恐怕不对吧");
            return false;
        }
        if (password.length() < 4) {
            showSnackbar("有这样儿的密码？");
            return false;
        }
        return true;
    }

    private LoadingAnimationDialog loadingAnim;

    private void showLoadingAnim() {
        loadingAnim = new LoadingAnimationDialog(LoginActivity.this, "登录中");
        loadingAnim.show();
    }

    private void hiddenLoadingAnim() {
        loadingAnim.dismiss();
    }

    /**
     * 展示一个 Snackbar
     *
     * @param text 需要显示的信息
     */
    private void showSnackbar(String text) {
        Snackbar.make(backBtn, text, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 关闭软键盘
     */
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
