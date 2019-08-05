package com.wbu.xiaowei.amyschedule.bean;

public class CourseBase {
    public static final int ALL_WEEK = 0; // 每周
    public static final int ODD_WEEK = 1; // 单周
    public static final int EVEN_WEEK = 2; // 双周

    private int courseId;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 教室
     */
    private String place;

    /**
     * 教师
     */
    private String teacherName;

    /**
     * 起始周
     */
    private int startWeek;

    /**
     * 结束周
     */
    private int endWeek;

    /**
     * 开始上课的节次
     */
    private int start;

    /**
     * 上课节数
     */
    private int step;

    /**
     * 周几上
     */
    private int day;

    /**
     * 单双周
     */
    private int parity = ALL_WEEK;

    public Course toCourseModel() {
        return new Course(courseId, courseName, place, teacherName, startWeek, endWeek, start, step, day, parity);
    }

    public CourseBase() {
    }

    public CourseBase(int courseId, String courseName, String place, String teacherName, int startWeek, int endWeek, int start, int step, int day, int parity) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseBase that = (CourseBase) o;

        if (startWeek != that.startWeek) return false;
        if (endWeek != that.endWeek) return false;
        if (day != that.day) return false;
        if (parity != that.parity) return false;
        if (courseName != null ? !courseName.equals(that.courseName) : that.courseName != null)
            return false;
        if (place != null ? !place.equals(that.place) : that.place != null) return false;
        return teacherName != null ? teacherName.equals(that.teacherName) : that.teacherName == null;
    }

    @Override
    public String toString() {
        return "CourseBase{" +
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
