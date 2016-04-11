package com.example.ll.demo02.mvp.view;

import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface MainView {

    void showProgress();

    void hideProgress();

    void setItems(List<String> items);

    void showMessage(String message);
}
