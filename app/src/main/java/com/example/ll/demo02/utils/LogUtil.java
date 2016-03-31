package com.example.ll.demo02.utils;

/**
 * Created by Administrator on 2016/bg_btn_pressed/30.
 */
public class LogUtil {

    private static LogUtil sLogUtil;

    private LogUtil(){

    }


    //双重锁定(Double-Check Locking)单列模式
    public static LogUtil getInstance() {
        if (sLogUtil == null) {
            synchronized (LogUtil.class) {
                if (sLogUtil == null) {
                    sLogUtil = new LogUtil();
                }
            }
        }
        return sLogUtil;
    }

}
