package com.wbu.xiaowei.amyschedule.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.wbu.xiaowei.amyschedule.bean.LoginFormData;
import com.wbu.xiaowei.amyschedule.bean.Major;
import com.wbu.xiaowei.amyschedule.bean.MajorBase;
import com.wbu.xiaowei.amyschedule.bean.UserInfo;
import com.wbu.xiaowei.amyschedule.net.CourseBeanListCallback;
import com.wbu.xiaowei.amyschedule.net.LoginFormDataCallback;
import com.wbu.xiaowei.amyschedule.net.MajorBeanListCallBack;
import com.wbu.xiaowei.amyschedule.net.ScoreBeanListCallback;
import com.wbu.xiaowei.amyschedule.net.StudentInfoCallback;
import com.wbu.xiaowei.amyschedule.net.URL;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.internal.StringUtil;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import okhttp3.Call;

public class ScheduleUtils {
    public static final int PERFECT = 0; // 成功
    public static final int NOT_LOGIN = 1; // 没有登录
    public static final int LOGIN_OK = 2; // 登录成功
    public static final int LOGIN_FORM_DATA_OK = 3; // 获取登录必要信息成功
    public static final int INCOMPLETE_USER_INFORMATION = 4; // 用户信息不完整
    public static final int NETWORK_UNAVAILABLE = 5; // 网络不可用
    public static final int STUDENT_INFO_OK = 6; // 学生信息已到手
    public static final int MAJORS_INFO_OK = 7; // 专业信息已到手
    public static final int COURSE_LIST_OK = 8; // 课程列表已到手
    public static final int SCORE_LIST_OK = 9; // 课程列表已到手

    public static UserInfo userInfo; // 用户信息

    private static ScheduleUtils instance;
    private static Callback callback;
    private Context context;

    private ScheduleUtils() {
        // cannot be instantiated without context
        throw new UnsupportedOperationException("cannot be instantiated without context");
    }

    private ScheduleUtils(Context context) {
        this.context = context;
    }

    public static ScheduleUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ScheduleUtils(context.getApplicationContext());
        }
        return instance;
    }

    // 消息处理器
    private static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == LOGIN_FORM_DATA_OK) {
                LoginFormData loginFormData = (LoginFormData) msg.obj;
                login(loginFormData);
            }
            return false;
        }
    });

    /**
     * 供外部调用的登录方法
     */
    public static int login(Callback cb) {
        callback = cb;
        return getLoginFormData();
    }

    /**
     * 获取课表
     */
    public static int getCourse(CourseBeanListCallback callback) {
        if (userInfo == null) {
            // 没有登录
            Log.d("ScheduleUtils", "获取课表: 没有登录: userInfo == null");
            return NOT_LOGIN;
        }

        // 存放查询课表所需的表单数据
        String semester = null;
        String departments = null;
        String grade = null;
        String major = null;
        String clazz = null;

        // 准备表单数据
        semester = userInfo.semester;
        grade = userInfo.grade;
        clazz = userInfo.clazz;
        departments = UserInfo.DEPARTMENTS.get(userInfo.departments);
        List<Major> majorModel = new Select().from(Major.class).where("majorName=?", userInfo.major).execute();
        if (majorModel != null && majorModel.size() > 0) {
            major = majorModel.get(0).getMajorCode();
        }

        if (TextUtils.isEmpty(semester) || TextUtils.isEmpty(departments) || TextUtils.isEmpty(grade)
                || TextUtils.isEmpty(major) || TextUtils.isEmpty(clazz)) {
            // 用户信息不完整
            return INCOMPLETE_USER_INFORMATION;
        }

        // 发送请求获取课表
        OkHttpUtils.get()
                .url(URL.GET_COURSE_TABLE_URL)
                .addParams("xnxqh", semester)
                .addParams("skyx", departments)
                .addParams("sknj", grade)
                .addParams("skzy", major)
                .build()
                .execute(callback);

        // 完美返回
        return PERFECT;
    }

    /**
     * 获取成绩
     */
    public static int getScore(ScoreBeanListCallback callback) {
        if (userInfo == null) {
            // 没有登录
            Log.d("ScheduleUtils", "getScore: 获取成绩: 没有登录: userInfo == null");
            return NOT_LOGIN;
        }

        // 发送请求获取成绩
        OkHttpUtils.get().url(URL.GET_SCORE_URL).build().execute(callback);

        return PERFECT;
    }

    /**
     * 获取学生信息
     */
    public static int getStudentInfo(StudentInfoCallback callback) {
        if (userInfo == null || !userInfo.isLogin) {
            return NOT_LOGIN;
        }
        OkHttpUtils.get().url(URL.GET_USER_INFO_URL).build().execute(callback);
        return PERFECT;
    }

    /**
     * 获取专业列表
     */
    public static void getMajors(MajorBeanListCallBack callback) {
        OkHttpUtils.get().url(URL.GET_MAJOR_MAP_URL).build().execute(callback);
    }

    /**
     * 获取专业列表
     */
    public static void getMajors() {
        OkHttpUtils.get().url(URL.GET_MAJOR_MAP_URL).build().execute(new MajorBeanListCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(final List<MajorBase> majorBaseList, int id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Delete().from(Major.class).execute();
                        if (!majorBaseList.isEmpty()) {
                            for (MajorBase major : majorBaseList) {
                                major.toMajorModel().save();
                            }
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 从教务系统注销
     */
    public static void logout() {
        OkHttpUtils.get().url(URL.LOGOUT_URL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // do nothing
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("ScheduleUtils", "logout: onResponse: 注销成功...");
            }
        });
    }

    /**
     * 获取登录所必须的表单数据
     */
    private static int getLoginFormData() {
        if (userInfo == null) {
            Log.d("ScheduleUtils", "getLoginFormData: 获取登录所必须的表单数据: 没有用户数据: ScheduleUtils.userInfo == null");
            return NOT_LOGIN;
        }

        final String username = userInfo.username;
        final String password = userInfo.password;

        if (StringUtil.isBlank(username) || StringUtil.isBlank(password)) {
            Log.d("ScheduleUtils", "getLoginFormData: 获取登录所必须的表单数据: 用户信息不完整: username: " + username + " password: " + password);
            return INCOMPLETE_USER_INFORMATION;
        }

        OkHttpUtils.get().url(URL.LOGIN_URL).build().execute(new LoginFormDataCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                // do nothing
            }

            @Override
            public void onResponse(LoginFormData loginFormData, int id) {
                loginFormData.setUsername(username);
                loginFormData.setPassword(password);
                Message msg = Message.obtain();
                msg.what = LOGIN_FORM_DATA_OK;
                msg.obj = loginFormData;
                handler.sendMessage(msg);
            }
        });

        return LOGIN_FORM_DATA_OK;
    }

    /**
     * 发送 post 请求登录
     *
     * @param loginFormData 表单数据
     */
    private static void login(LoginFormData loginFormData) {
        if (callback != null) {
            OkHttpUtils.post().url(URL.LOGIN_URL).params(loginFormData.toRequestParameters()).build().execute(callback);
        }
    }

    /**
     * 读取用户信息
     */
    public void readUserInfo() {
        Log.d("ScheduleUtils", "开始读取用户信息...");
        FileInputStream in = null;
        Properties properties = new Properties();
        try {
            in = context.openFileInput("user_info.xml");
            properties.loadFromXML(in);
            String username = properties.getProperty("username", "");
            String password = properties.getProperty("password", "");
            String semester = properties.getProperty("semester", "");
            String currentWeek = properties.getProperty("currentWeek", "1");
            String departments = properties.getProperty("departments", "");
            String grade = properties.getProperty("grade", "");
            String major = properties.getProperty("major", "");
            String clazz = properties.getProperty("clazz", "");
            String studentName = properties.getProperty("studentName", "");
            String gender = properties.getProperty("gender", "");
            String isLogin = properties.getProperty("isLogin", "false");
            userInfo = new UserInfo(username, password, semester, Integer.parseInt(currentWeek), departments, grade, major, clazz, studentName, gender, Boolean.parseBoolean(isLogin));
            Log.d("ScheduleUtils", "用户信息读取成功: " + userInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存用户信息
     */
    public void saveUserInfo() {
        Log.d("ScheduleUtils", "saveUserInfo: new Thread: userInfo=" + userInfo.toString() + " 开始在子线程中保存用户信息...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Properties properties = new Properties();
                properties.setProperty("username", userInfo.username);
                properties.setProperty("password", userInfo.password);
                properties.setProperty("semester", userInfo.semester);
                properties.setProperty("currentWeek", userInfo.currentWeek + "");
                properties.setProperty("departments", userInfo.departments);
                properties.setProperty("grade", userInfo.grade);
                properties.setProperty("major", userInfo.major);
                properties.setProperty("clazz", userInfo.clazz);
                properties.setProperty("studentName", userInfo.studentName);
                properties.setProperty("gender", userInfo.gender);
                properties.setProperty("isLogin", String.valueOf(userInfo.isLogin));
                FileOutputStream out = null;
                try {
                    out = context.openFileOutput("user_info.xml", Context.MODE_PRIVATE);
                    properties.storeToXML(out, null, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
        Log.d("ScheduleUtils", "saveUserInfo: new Thread: 在子线程中保存用户信息成功...");
    }
}
