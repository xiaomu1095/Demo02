package com.example.ll.demo02;

import com.example.ll.demo02.base.BaseApplication;

/**
 * Created by Administrator on 2016/3/31.
 */
public class AppContext extends BaseApplication {

    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }


}
