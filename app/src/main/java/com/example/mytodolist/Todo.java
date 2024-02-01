package com.example.mytodolist;

enum Status{DONE, DOING, NOT_STARTED}
public class Todo {
    String text;
    String status;

    public Todo(String text, String status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
