package com.view.graphic.dynamic;

class PrintableAction {
    final String text;
    int displayTime;

    PrintableAction(String text) {
        this.text = text;
        this.displayTime = text.length() / 10 + 5;
    }
}
