package com.wbu.xiaowei.amyschedule.net;

import android.util.Log;

import com.google.gson.Gson;
import com.wbu.xiaowei.amyschedule.bean.MajorBase;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Arrays;
import java.util.List;

import okhttp3.Response;

public abstract class MajorBeanListCallBack extends Callback<List<MajorBase>> {
    @Override
    public List<MajorBase> parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        MajorBase[] majorsArr = new Gson().fromJson(string, MajorBase[].class);
        return Arrays.asList(majorsArr);
    }
}
