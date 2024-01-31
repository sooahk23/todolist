package com.example.mytodolist;

enum Status{DONE, DOING, NOT_STARTED}
public class Todo {
    String todo;
    Status status;

    public Todo(String todo, Status status) {
        this.todo = todo;
        this.status = status;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
