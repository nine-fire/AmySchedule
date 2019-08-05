package com.wbu.xiaowei.amyschedule.bean;

/**
 * 分数详情
 */
public class FractionDetails {
    private float peacetimeScore; // 平时成绩
    private int peacetimeScoreProportion; // 平时成绩比例
    private float midTermScore; // 期中成绩
    private int midTermScoreProportion; // 期中成绩比例
    private float finalScore; // 期末成绩
    private int finalScoreProportion; // 期末成绩比例
    private float totalScore; // 总成绩

    public float getPeacetimeScore() {
        return peacetimeScore;
    }

    public void setPeacetimeScore(float peacetimeScore) {
        this.peacetimeScore = peacetimeScore;
    }

    public int getPeacetimeScoreProportion() {
        return peacetimeScoreProportion;
    }

    public void setPeacetimeScoreProportion(int peacetimeScoreProportion) {
        this.peacetimeScoreProportion = peacetimeScoreProportion;
    }

    public float getMidTermScore() {
        return midTermScore;
    }

    public void setMidTermScore(float midTermScore) {
        this.midTermScore = midTermScore;
    }

    public int getMidTermScoreProportion() {
        return midTermScoreProportion;
    }

    public void setMidTermScoreProportion(int midTermScoreProportion) {
        this.midTermScoreProportion = midTermScoreProportion;
    }

    public float getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(float finalScore) {
        this.finalScore = finalScore;
    }

    public int getFinalScoreProportion() {
        return finalScoreProportion;
    }

    public void setFinalScoreProportion(int finalScoreProportion) {
        this.finalScoreProportion = finalScoreProportion;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public FractionDetails(float peacetimeScore, int peacetimeScoreProportion, float midTermScore, int midTermScoreProportion, float finalScore, int finalScoreProportion, float totalScore) {
        this.peacetimeScore = peacetimeScore;
        this.peacetimeScoreProportion = peacetimeScoreProportion;
        this.midTermScore = midTermScore;
        this.midTermScoreProportion = midTermScoreProportion;
        this.finalScore = finalScore;
        this.finalScoreProportion = finalScoreProportion;
        this.totalScore = totalScore;
    }

    public FractionDetails() {
    }

    @Override
    public String toString() {
        return "FractionDetails{" +
                "peacetimeScore=" + peacetimeScore +
                ", peacetimeScoreProportion=" + peacetimeScoreProportion +
                ", midTermScore=" + midTermScore +
                ", midTermScoreProportion=" + midTermScoreProportion +
                ", finalScore=" + finalScore +
                ", finalScoreProportion=" + finalScoreProportion +
                ", totalScore=" + totalScore +
                '}';
    }
}
