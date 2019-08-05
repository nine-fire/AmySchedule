package com.wbu.xiaowei.amyschedule.net;

import com.wbu.xiaowei.amyschedule.bean.UserInfo;
import com.zhy.http.okhttp.callback.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

import okhttp3.Response;

public abstract class StudentInfoCallback extends Callback<UserInfo> {

    @Override
    public UserInfo parseNetworkResponse(Response response, int id) throws Exception {
        String html = response.body().string();
        Document document = Jsoup.parse(html);
        Element table = document.getElementById("xjkpTable");
        List<Element> tr = table.getElementsByTag("tr").subList(2, 4);

        // 第一行
        String departments = tr.get(0).getElementsByTag("td").get(0).text().substring(3).trim();
        String major = tr.get(0).getElementsByTag("td").get(1).text().substring(3).trim();
        String clazz = tr.get(0).getElementsByTag("td").get(3).text().substring(3).trim();
        String username = tr.get(0).getElementsByTag("td").get(4).text().substring(3).trim();

        // 第二行
        String studentName = tr.get(1).getElementsByTag("td").get(1).text().trim();
        String gender = tr.get(1).getElementsByTag("td").get(3).text().trim();

        String grade = "20" + username.substring(0, 2);

        return new UserInfo(username, "", "2019-2020-1", 1, departments, grade, major, clazz, studentName, gender, true);
    }
}
