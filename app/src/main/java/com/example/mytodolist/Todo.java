package com.example.mytodolist;

enum Status{DONE, DOING, NOT_STARTED}
public class Todo {
    long id;
    String text;
    String status;

    public Todo(long id, String text, String status) {
        this.id = id;
        this.text = text;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
