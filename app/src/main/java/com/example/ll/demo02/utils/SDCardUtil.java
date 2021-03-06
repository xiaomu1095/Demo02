package com.example.ll.demo02.utils;

import android.annotation.TargetApi;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class SDCardUtil {
    private SDCardUtil() {
        /** cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    @TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Deprecated
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = 0;
            // 获取单个数据块的大小（byte）
            long freeBlocks = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlocks = stat.getAvailableBlocksLong() - 4;
                freeBlocks = stat.getAvailableBlocksLong();
                return freeBlocks * availableBlocks;
            } else {
                availableBlocks = stat.getAvailableBlocks() - 4;
                freeBlocks = stat.getAvailableBlocks();
                return freeBlocks * availableBlocks;
            }

        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    @TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Deprecated
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableBlocks = stat.getAvailableBlocksLong() - 4;    //一、使用@TargetApi annotaion， 使高版本API的代码在低版本SDK不报错
            return stat.getBlockSizeLong() * availableBlocks;
        } else {
            availableBlocks = (long) stat.getAvailableBlocks() - 4;
            return stat.getBlockSize() * availableBlocks;
        }
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }
}
