package com.example.ll.demo02.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    /**
     * 获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }


    /**
     * 获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getSDCardPath() + File.separator + folderName + File.separator);
        if (!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    /**
     * 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建,如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
