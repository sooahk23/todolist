package com.example.mytodolist;

enum Status{NOT_STARTED, DONE}
public class TodoText {
    String text;
    Status status;

    public TodoText(String text, Status status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
