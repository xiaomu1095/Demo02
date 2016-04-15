package com.example.ll.demo02.code.factory;

/**
 * Created by Administrator on 2016/4/15.
 */
public class ExportStandardPdfFile implements ExportFile {

    @Override
    public boolean export(String data) {
        // TODO Auto-generated method stub
        /**
         * 业务逻辑
         */
        System.out.println("导出标准PDF文件");
        return true;
    }

}
