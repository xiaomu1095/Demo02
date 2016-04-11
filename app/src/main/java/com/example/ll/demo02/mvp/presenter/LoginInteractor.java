package com.example.ll.demo02.mvp.presenter;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface LoginInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onSuccess();
    }

    void login(String username, String password, OnLoginFinishedListener listener);

}
