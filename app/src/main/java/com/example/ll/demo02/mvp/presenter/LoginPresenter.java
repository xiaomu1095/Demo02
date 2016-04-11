package com.example.ll.demo02.mvp.presenter;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface LoginPresenter {
    void validateCredentials(String username, String password);

    void onDestroy();
}
