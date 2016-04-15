package com.example.ll.demo02.code.factory;

/**
 * Created by Administrator on 2016/4/15.
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String data = "";
        ExportFactory exportFactory = new ExportHtmlFactory();
        ExportFile ef = exportFactory.factory("financial");
        ef.export(data);
    }

}