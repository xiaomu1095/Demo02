package com.example.ll.demo02.code.strategy;



public interface Strategy {

    //策略模式的核心思想就是把算法提取出来放到一个独立的对象中。

    String getSQL(String[] usernames);

}
