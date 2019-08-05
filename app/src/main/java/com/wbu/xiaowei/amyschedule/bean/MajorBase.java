package com.wbu.xiaowei.amyschedule.bean;

import com.activeandroid.Model;

public class MajorBase {
    private String dm;
    private String dmmc;

    public Major toMajorModel() {
        return new Major(dmmc, dm);
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getDmmc() {
        return dmmc;
    }

    public void setDmmc(String dmmc) {
        this.dmmc = dmmc;
    }

    @Override
    public String toString() {
        return "MajorBase{" +
                "code='" + dm + '\'' +
                ", name='" + dmmc + '\'' +
                '}';
    }
}
