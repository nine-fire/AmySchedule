package com.wbu.xiaowei.amyschedule.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "major_table")
public class Major extends Model {
    @Column
    private String majorName;

    @Column
    private String majorCode;

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public Major(String majorName, String majorCode) {
        this.majorName = majorName;
        this.majorCode = majorCode;
    }

    public Major() {
    }

    @Override
    public String toString() {
        return "Major{" +
                "majorName='" + majorName + '\'' +
                ", majorCode='" + majorCode + '\'' +
                '}';
    }
}
