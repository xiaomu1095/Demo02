package com.example.ll.demo02;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.example.ll.demo02.base.BaseApplication;
import com.example.ll.demo02.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

public class AppContext extends BaseApplication {

    private static Drawable cachedWallpaper;
    private static int selectedColor;
    private static boolean isCustomTheme;
//    private static final Object sync = new Object();

    public static volatile Context applicationContext;
    public static volatile Handler applicationHandler;
    private static volatile boolean applicationInited = false;

    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;

    public static boolean isCustomTheme() {
        return isCustomTheme;
    }

    public static int getSelectedColor() {
        return selectedColor;
    }



    private static AppContext instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationContext = getApplicationContext();
        applicationHandler = new Handler(applicationContext.getMainLooper());


        //这里可以设置自签名证书
//        OkHttpUtils.getInstance().setCertificates(new InputStream[]{
//                new Buffer()
//                        .writeUtf8(CER_12306)
//                        .inputStream()});
        OkHttpUtils.getInstance().debug("OkHttpUtils").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
        //使用https，但是默认信任全部证书
        OkHttpUtils.getInstance().setCertificates();


        //使用这种方式，设置多个OkHttpClient参数
//        OkHttpUtils.getInstance(new OkHttpClient.Builder().build());

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
