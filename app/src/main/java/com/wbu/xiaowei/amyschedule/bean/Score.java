package com.wbu.xiaowei.amyschedule.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "score_table")
public class Score extends Model {
    @Column
    private String scoreIndex; // 序号
    @Column
    private String time; // 开课时间
    @Column
    private String courseId; // 课程编号
    @Column
    private String name; // 课程名称
    @Column
    private String fraction; // 成绩
    @Column
    private String credit; // 学分
    @Column
    private String learningTime; // 总学时
    @Column
    private String examinationMethods; // 考核方式
    @Column
    private String courseAttributes; // 课程属性
    @Column
    private String courseNature; // 课程性质
    @Column
    private String fractionDetails; // 分数详情

    public String getScoreIndex() {
        return scoreIndex;
    }

    public void setScoreIndex(String scoreIndex) {
        this.scoreIndex = scoreIndex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getLearningTime() {
        return learningTime;
    }

    public void setLearningTime(String learningTime) {
        this.learningTime = learningTime;
    }

    public String getExaminationMethods() {
        return examinationMethods;
    }

    public void setExaminationMethods(String examinationMethods) {
        this.examinationMethods = examinationMethods;
    }

    public String getCourseAttributes() {
        return courseAttributes;
    }

    public void setCourseAttributes(String courseAttributes) {
        this.courseAttributes = courseAttributes;
    }

    public String getCourseNature() {
        return courseNature;
    }

    public void setCourseNature(String courseNature) {
        this.courseNature = courseNature;
    }

    public String getFractionDetails() {
        return fractionDetails;
    }

    public void setFractionDetails(String fractionDetails) {
        this.fractionDetails = fractionDetails;
    }

    public Score(String scoreIndex, String time, String courseId, String name, String fraction, String credit, String learningTime, String examinationMethods, String courseAttributes, String courseNature, String fractionDetails) {
        this.scoreIndex = scoreIndex;
        this.time = time;
        this.courseId = courseId;
        this.name = name;
        this.fraction = fraction;
        this.credit = credit;
        this.learningTime = learningTime;
        this.examinationMethods = examinationMethods;
        this.courseAttributes = courseAttributes;
        this.courseNature = courseNature;
        this.fractionDetails = fractionDetails;
    }

    public Score() {
    }

    @Override
    public String toString() {
        return "Score{" +
                "scoreIndex='" + scoreIndex + '\'' +
                ", time='" + time + '\'' +
                ", courseId='" + courseId + '\'' +
                ", name='" + name + '\'' +
                ", fraction='" + fraction + '\'' +
                ", credit='" + credit + '\'' +
                ", learningTime='" + learningTime + '\'' +
                ", examinationMethods='" + examinationMethods + '\'' +
                ", courseAttributes='" + courseAttributes + '\'' +
                ", courseNature='" + courseNature + '\'' +
                ", fractionDetails='" + fractionDetails + '\'' +
                '}';
    }
}
