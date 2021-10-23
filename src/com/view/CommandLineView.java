package com.view;

public class CommandLineView implements Observer {
    public void update(String message) {
        System.out.println(message);
    }

}
