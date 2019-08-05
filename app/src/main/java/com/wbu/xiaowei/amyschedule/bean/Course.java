package com.wbu.xiaowei.amyschedule.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleEnable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Table(name = "course_table")
public class Course extends Model implements ScheduleEnable {
    public static final int ALL_WEEK = 0; // 每周
    public static final int ODD_WEEK = 1; // 单周
    public static final int EVEN_WEEK = 2; // 双周

    @Column(name = "course_id")
    private int courseId;

    /**
     * 课程名
     */
    @Column(name = "course_name")
    private String courseName;

    /**
     * 教室
     */
    @Column(name = "place")
    private String place;

    /**
     * 教师
     */
    @Column(name = "teacher_name")
    private String teacherName;

    /**
     * 起始周
     */
    @Column(name = "start_week")
    private int startWeek;

    /**
     * 结束周
     */
    @Column(name = "end_week")
    private int endWeek;

    /**
     * 开始上课的节次
     */
    @Column(name = "start")
    private int start;

    /**
     * 上课节数
     */
    @Column(name = "step")
    private int step;

    /**
     * 周几上
     */
    @Column(name = "day")
    private int day;

    /**
     * 单双周
     */
    @Column(name = "parity")
    private int parity = ALL_WEEK;

    @Override
    public Schedule getSchedule() {
        return new Schedule(courseName, place, teacherName, getWeekList(), start, step, day, getColor());
    }

    /**
     * 计算出哪些周需要上课，考虑单双周
     *
     * @return
     */
    private List<Integer> getWeekList() {
        List<Integer> list = new ArrayList<>();

        switch (parity) {
            case ALL_WEEK:
                for (int i = startWeek; i <= endWeek; i++) {
                    list.add(i);
                }
                break;
            case ODD_WEEK:
                for (int i = startWeek; i <= endWeek; i++) {
                    if (i % 2 != 0) {
                        list.add(i);
                    }
                }
                break;
            case EVEN_WEEK:
                for (int i = startWeek; i <= endWeek; i++) {
                    if (i == 2) {
                        list.add(i);
                        continue;
                    }
                    if (i % 2 == 0) {
                        list.add(i);
                    }
                }
                break;
        }

        return list;
    }

    private int getColor() {
//        MessageDigest md5 = null;
//        String str = null;
//        try {
//            md5 = MessageDigest.getInstance("MD5");
//            md5.update(this.courseName.getBytes());
//            byte[] bytes = md5.digest();
//            str = new String(bytes, "utf-8").substring(0, 6);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Integer.parseInt(str);
        return new Random().nextInt();
    }

    public Course(int courseId, String courseName, String place, String teacherName, int startWeek, int endWeek, int start, int step, int day, int parity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.place = place;
        this.teacherName = teacherName;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.start = start;
        this.step = step;
        this.day = day;
        this.parity = parity;
    }

    public Course() {
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", place='" + place + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", start=" + start +
                ", step=" + step +
                ", day=" + day +
                ", parity=" + parity +
                '}';
    }
}
