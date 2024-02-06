package com.example.mytodolist;

enum Status{NOT_STARTED, DONE}
public class TodoText extends Todo {
    String text;
    Status status;

    public TodoText(int id, ViewType viewType, String text, Status status) {
        super(id, viewType);
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
