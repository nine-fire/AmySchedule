package com.wbu.xiaowei.amyschedule.bean;

public class ScoreBase {
    private String scoreIndex; // 序号
    private String time; // 开课时间
    private String courseId; // 课程编号
    private String name; // 课程名称
    private String fraction; // 成绩
    private String credit; // 学分
    private String learningTime; // 总学时
    private String examinationMethods; // 考核方式
    private String courseAttributes; // 课程属性
    private String courseNature; // 课程性质
    private String fractionDetails; // 分数详情

    public Score toScoreModel() {
        return new Score(scoreIndex, time, courseId, name, fraction, credit, learningTime, examinationMethods, courseAttributes, courseNature, fractionDetails);
    }

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

    @Override
    public String toString() {
        return "ScoreBase{" +
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
