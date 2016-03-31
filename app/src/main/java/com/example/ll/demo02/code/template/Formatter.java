package com.example.ll.demo02.code.template;

/**
 * Created by Administrator on 2016/3/31.
 */
public abstract class Formatter {

    //模板方法： 定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，模板方法使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。

    public String formatBook(Book book, int format) {
        beforeFormat();
        String result = formating(book);
        afterFormat();
        return result;
    }

    protected void beforeFormat() {
        System.out.println("format begins");
    }

    protected abstract String formating(Book book);

    protected void afterFormat() {
        System.out.println("format finished");
    }

}