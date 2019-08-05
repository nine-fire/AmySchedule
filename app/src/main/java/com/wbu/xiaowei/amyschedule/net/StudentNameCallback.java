package com.wbu.xiaowei.amyschedule.net;

import com.zhy.http.okhttp.callback.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.Response;

public abstract class StudentNameCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        String html = response.body().string();
        Document document = Jsoup.parse(html);
        String name = document.getElementById("welcomeMsg").text().trim();
        return name.split("：")[1];
    }
}
