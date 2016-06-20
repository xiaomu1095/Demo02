package com.example.ll.demo02.jni;

public class NDKJniUtils {
    public native String getCLanguageString();

    static {
        System.loadLibrary("XiaomuJniLibName");	//defaultConfig.ndk.moduleName
    }
}
