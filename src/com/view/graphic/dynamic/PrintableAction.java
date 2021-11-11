package com.view.graphic.dynamic;

class PrintableAction {
    final String action;
    int displayTime;

    PrintableAction(String action) {
        this.action = action;
        this.displayTime = action.length() / 10 + 5;
    }
}
