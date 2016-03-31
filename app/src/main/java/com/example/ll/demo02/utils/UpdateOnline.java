package com.example.ll.demo02.utils;

import android.content.Context;

import com.example.ll.demo02.bean.VersionInfo;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/31.
 */
public class UpdateOnline {


    private void getAppVersion(final Context context) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                VersionInfo versionInfo = new Gson().fromJson(response.toString(), VersionInfo.class);
                if (versionInfo.getVersion() != String.valueOf(APPUtil.getVersionCode(context))){

                }
            }

        });
    }

}
