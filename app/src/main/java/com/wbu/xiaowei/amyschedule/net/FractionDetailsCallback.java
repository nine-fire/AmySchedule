package com.wbu.xiaowei.amyschedule.net;

import com.wbu.xiaowei.amyschedule.bean.FractionDetails;
import com.zhy.http.okhttp.callback.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.Response;

public abstract class FractionDetailsCallback extends Callback<FractionDetails> {
    @Override
    public FractionDetails parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Document document = Jsoup.parse(string);
        Element table = document.getElementById("dataList");
        Elements tr = table.getElementsByTag("tr");
        Elements td = tr.get(1).getElementsByTag("td");

        float peacetimeScore = 0.0F;
        int peacetimeScoreProportion = 0;
        float midTermScore = 0.0F;
        int midTermScoreProportion = 0;
        float finalScore = 0.0F;
        int finalScoreProportion = 0;
        float totalScore = 0.0F;

        try {
            peacetimeScore = Float.parseFloat(td.get(1).text());
            peacetimeScoreProportion = Integer.parseInt(td.get(2).text().split("%")[0]);
            midTermScore = Float.parseFloat(td.get(3).text());
            midTermScoreProportion = Integer.parseInt(td.get(4).text().split("%")[0]);
            finalScore = Float.parseFloat(td.get(5).text());
            finalScoreProportion = Integer.parseInt(td.get(6).text().split("%")[0]);
            totalScore = Float.parseFloat(td.get(7).text());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new FractionDetails(peacetimeScore, peacetimeScoreProportion, midTermScore, midTermScoreProportion,
                finalScore, finalScoreProportion, totalScore);
    }
}
