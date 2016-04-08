package com.example.ll.demo02.code.observer;

public interface Observer {
    /**
     * 更新接口
     *
     * @param state 更新的状态
     */
    void update(String state);
}
