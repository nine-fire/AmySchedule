package com.wbu.xiaowei.amyschedule.net;


import com.wbu.xiaowei.amyschedule.bean.LoginFormData;
import com.zhy.http.okhttp.callback.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import okhttp3.Response;

public abstract class LoginFormDataCallback extends Callback<LoginFormData> {
    @Override
    public LoginFormData parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Document document = Jsoup.parse(string);

        Elements ltInput = document.getElementsByAttributeValue("name", "lt");
        Elements executionInput = document.getElementsByAttributeValue("name", "execution");
        Elements _eventIdInput = document.getElementsByAttributeValue("name", "_eventId");
        Elements rmShownInput = document.getElementsByAttributeValue("name", "rmShown");

        String lt = ltInput.val();
        String execution = executionInput.val();
        String _eventId = _eventIdInput.val();
        String rmShown = rmShownInput.val();

        return new LoginFormData(null, null, lt, execution, _eventId, rmShown);
    }
}
