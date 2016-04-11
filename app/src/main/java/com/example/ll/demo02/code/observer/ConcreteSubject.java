package com.example.ll.demo02.code.observer;

public class ConcreteSubject extends Subject {

    private String state = "ConcreteSubject原來狀態";

    public String getState() {
        System.out.println(state);
        return state;
    }

    public void change(String newState) {
        state = newState;
        System.out.println("主题状态为：" + state);
        //状态发生改变，通知各个观察者
        this.nodifyObservers(state);
    }
}
