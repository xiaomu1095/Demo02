package com.example.ll.demo02.mvp.presenter;

import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface FindItemsInteractor {

    interface OnFinishedListener {
        void onFinished(List<String> items);
    }

    void findItems(OnFinishedListener listener);
}
