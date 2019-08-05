package com.wbu.xiaowei.amyschedule.net;

import com.google.gson.Gson;
import com.wbu.xiaowei.amyschedule.bean.ScoreBase;
import com.zhy.http.okhttp.callback.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

import okhttp3.Response;

public abstract class ScoreBeanListCallback extends Callback<List<ScoreBase>> {
    @Override
    public List<ScoreBase> parseNetworkResponse(Response response, int id) throws Exception {
        String html = response.body().string();

        Document document = Jsoup.parse(html);
        Element table = document.getElementById("dataList");
        Elements tr = table.getElementsByTag("tr");

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("["); // [

        for (int i = 1; i < tr.size(); i++) {
            Elements td = tr.get(i).getElementsByTag("td");
            stringBuffer.append("{"); // {
            stringBuffer.append("\"scoreIndex\" : \"")
                    .append(td.get(0).text())
                    .append("\",  \"time\" : \"")
                    .append(td.get(1).text())
                    .append("\",  \"courseId\" : \"")
                    .append(td.get(2).text())
                    .append("\",  \"name\" : \"")
                    .append(td.get(3).text())
                    .append("\",  \"fraction\" : \"")
                    .append(td.get(4).text())
                    .append("\",  \"credit\" : \"")
                    .append(td.get(5).text())
                    .append("\",  \"learningTime\" : \"")
                    .append(td.get(6).text())
                    .append("\",  \"examinationMethods\" : \"")
                    .append(td.get(7).text())
                    .append("\", \"courseAttributes\" : \"")
                    .append(td.get(8).text())
                    .append("\",  \"courseNature\" : \"")
                    .append(td.get(9).text())
                    .append("\",  \"fractionDetails\" : \"")
                    .append(td.get(4).getElementsByAttribute("href").get(0).attr("href").split("[?]")[1].split("'")[0])
                    .append("\"");
            // "key":"value",
            stringBuffer.append((i < tr.size() - 1) ? "},\n" : "}\n"); // },
        }

        stringBuffer.append("]"); // ]

        ScoreBase[] myScores = new Gson().fromJson(stringBuffer.toString(), ScoreBase[].class);
        return Arrays.asList(myScores);
    }
}
