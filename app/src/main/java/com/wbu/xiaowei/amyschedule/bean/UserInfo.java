package com.wbu.xiaowei.amyschedule.bean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfo {
    public static List<String> SEMESTER;
    public static Map<String, String> DEPARTMENTS;
    public static List<String> GRADE;

    static {
        SEMESTER = Arrays.asList("2019-2020-1", "2018-2019-2", "2018-2019-1", "2017-2018-2", "2017-2018-1", "2016-2017-2", "2016-2017-1", "2015-2016-2", "2015-2016-1");

        DEPARTMENTS = new HashMap<String, String>();
        DEPARTMENTS.put("旅游与酒店管理学院", "03");
        DEPARTMENTS.put("信息工程学院", "04");
        DEPARTMENTS.put("艺术学院", "05");
        DEPARTMENTS.put("外国语学院", "06");
        DEPARTMENTS.put("机电工程与汽车服务学院", "07");
        DEPARTMENTS.put("商贸物流学院", "08");
        DEPARTMENTS.put("烹饪与食品工程学院", "09");
        DEPARTMENTS.put("体育学院·国际马术学院", "12");
        DEPARTMENTS.put("工商管理学院", "14");
        DEPARTMENTS.put("继续教育学院", "18");
        DEPARTMENTS.put("经济与金融学院", "32");
        DEPARTMENTS.put("国际教育学院", "35");

        GRADE = new ArrayList<>();
        for (int year = Calendar.getInstance().get(Calendar.YEAR); year >= 2015; year--) {
            GRADE.add(year + "级");
        }
    }

    public String username; // 学号
    public String password; // 密码
    public String semester; // 学年学期
    public int currentWeek = 1; // 当前周
    public String departments; // 上课院系
    public String grade; // 年级
    public String major; // 专业
    public String clazz; // 班级
    public String studentName; // 学生姓名
    public String gender; // 性别
    public boolean isLogin; // 是否登录教务系统

    public UserInfo(String username, String password, String semester, int currentWeek, String departments, String grade, String major, String clazz, String studentName, String gender, boolean isLogin) {
        this.username = username;
        this.password = password;
        this.semester = semester;
        this.currentWeek = currentWeek;
        this.departments = departments;
        this.grade = grade;
        this.major = major;
        this.clazz = clazz;
        this.studentName = studentName;
        this.gender = gender;
        this.isLogin = isLogin;
    }

    public UserInfo() {
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", semester='" + semester + '\'' +
                ", currentWeek=" + currentWeek +
                ", departments='" + departments + '\'' +
                ", grade='" + grade + '\'' +
                ", major='" + major + '\'' +
                ", clazz='" + clazz + '\'' +
                ", studentName='" + studentName + '\'' +
                ", gender='" + gender + '\'' +
                ", isLogin=" + isLogin +
                '}';
    }
}
