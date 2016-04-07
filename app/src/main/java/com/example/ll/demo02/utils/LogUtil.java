package com.example.ll.demo02.utils;

public class LogUtil {

    private static LogUtil sLogUtil;

    private LogUtil(){

    }

    //双重锁定(Double-Check Locking)单列模式
    public static LogUtil getInstance() {
        LogUtil mLogUtil = sLogUtil;
        if (mLogUtil == null) {
            synchronized (LogUtil.class) {
                mLogUtil = sLogUtil;
                if (mLogUtil == null) {
                    sLogUtil = mLogUtil = new LogUtil();
                }
            }
        }
        return mLogUtil;
    }

}
