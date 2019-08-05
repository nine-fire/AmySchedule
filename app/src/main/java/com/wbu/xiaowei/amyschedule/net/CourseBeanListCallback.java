package com.wbu.xiaowei.amyschedule.net;

import com.wbu.xiaowei.amyschedule.bean.Course;
import com.wbu.xiaowei.amyschedule.bean.CourseBase;
import com.zhy.http.okhttp.callback.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

public abstract class CourseBeanListCallback extends Callback<Map<String, List<CourseBase>>> {
    @Override
    public Map<String, List<CourseBase>> parseNetworkResponse(Response response, int id) throws Exception {
        String html = response.body().string();
        Map<String, List<CourseBase>> courseMap = new HashMap<>();
        List<CourseBase> courseList = null;

        // 课表所有行
        Elements trs = null;
        // 课表某一行
        Element tr = null;
        // 某一行所有单元格
        Elements tds = null;
        // 某个单元格内课程
        Elements content = null;
        // 课表每个单元格的字符串
        String[][] courseStrs = null;

        Document document = Jsoup.parse(html);
        Element courseTable = document.getElementById("kbtable");

        trs = courseTable.getElementsByTag("tr");
        courseStrs = new String[trs.size() - 2][50];
        for (int i = 2; i < trs.size(); i++) { // 遍历行
            tr = trs.get(i); // 行
            tds = tr.getElementsByTag("td"); // 该行所有单元格
            for (int j = 0; j < tds.size(); j++) { // 遍历单元格
                content = tds.get(j).getElementsByTag("nobr");
                if (content.size() > 0) {
                    if (content.get(0).text().trim().contains("&nbsp;")) {
                        courseStrs[i - 2][j] = null;
                    } else if (content.get(0).getElementsByTag("div").size() > 0) {
                        courseStrs[i - 2][j] = content.get(0).toString();
                    } else {
                        courseStrs[i - 2][j] = content.get(0).text();
                    }
                }
            }
        }

        Document cell = null; // 单元格
        Elements divs = null; // 单元格内容
        CourseBase prevCourse = null; // 上一次遍历的课程
        CourseBase curCourse = null; // 正在遍历的课程

        String courseName = null; // 课程名称
        String place = null; // 上课地点
        String teacherName = null; // 教师姓名
        int startWeek = -1; // 起始周
        int endWeek = -1; // 结束周
        int start = -1; // 第几节开始上课
        int step = -1; // 上几节
        int day = -1; // 周几
        int parity = Course.ALL_WEEK; // 单双周

        // 将课程表二维数组转换为 Map，键为班级，值为班级课表组成的 List
        int[] starts = {1, 3, 5, 7, 9, 11, 13};
        for (int i = 0; i < courseStrs.length; i++) {
            courseList = new ArrayList<CourseBase>();
            for (int j = 0; j < courseStrs[0].length; j++) {
                cell = Jsoup.parse(courseStrs[i][j]);
                divs = cell.getElementsByTag("div");
                for (Element div : divs) {
                    String[] texts = div.text().split(" ");
                    courseName = texts[0];
                    place = texts[3];
                    teacherName = texts[1];

                    Pattern pattern = Pattern.compile("[^0-9|-]");
                    Matcher matcher = pattern.matcher(texts[2]);
                    String week = matcher.replaceAll("");
                    try {
                        startWeek = Integer.parseInt(week.split("-")[0]);
                        endWeek = Integer.parseInt(week.split("-")[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    start = starts[(j - 1) % 7];
                    step = 2;
                    day = (int) Math.ceil(j / 7.0);
                    parity = texts[2].contains("双周") ? Course.EVEN_WEEK : Course.ALL_WEEK;

                    curCourse = new CourseBase(Integer.parseInt((i + 1) + "" + (j + 1)), courseName, place, teacherName, startWeek, endWeek, start, step, day, parity);
                    if (curCourse.equals(prevCourse)) { // 判断当前课程是否和上节课相同
                        courseList.remove(courseList.size() - 1); // 若相同，说明是同一们课连上
                        prevCourse.setStep(prevCourse.getStep() + 2); // 那么不应当增加重复的课程
                        courseList.add(prevCourse);
                    } else {
                        // 是没有上过的全新版本，直接记录下并添加到集合
                        prevCourse = curCourse;
                        courseList.add(prevCourse);
                    }
                }
            }
            // 一行遍历结束，即完成了一个班级课表
            courseMap.put(courseStrs[i][0], courseList);
        }

        return courseMap;
    }
}
