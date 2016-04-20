package com.example.ll.demo02.mvp.presenter;

import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.ll.demo02.okhttp.OkHttpUtils;
import com.example.ll.demo02.okhttp.callback.Callback;
import com.example.ll.demo02.okhttp.callback.StringCallback;
import com.example.ll.demo02.utils.FileLog;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

public class LoginInteractorImpl implements LoginInteractor {

    @Override
    public void login(final String username, final String password, final OnLoginFinishedListener listener) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        OkHttpUtils.postString()
                .url("http://www.baidu.com")
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content("123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        listener.onSuccess();
                        FileLog.d("test", response);
                    }
                });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                boolean error = false;
//                if (TextUtils.isEmpty(username)) {
//                    listener.onUsernameError();
//                    error = true;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    listener.onPasswordError();
//                    error = true;
//                }
//                if (!error) {
//                    listener.onSuccess();
//                }
//            }
//        }, 2000);
    }
}
