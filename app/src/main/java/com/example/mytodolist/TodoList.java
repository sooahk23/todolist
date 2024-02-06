package com.example.mytodolist;

import java.util.ArrayList;

public class TodoList {
    int id_count;
    ArrayList<Todo> items;

    public TodoList() {
        id_count = 0;
        items = new ArrayList<Todo>();
    }

    public int getId_count() {
        return id_count;
    }

    public void setId_count(int id_count) {
        this.id_count = id_count;
    }

    public void addItem(Todo item) {
        items.add(item);
    }

    public void updateItem(Todo preItem, Todo newItem) {
        int index = items.indexOf(preItem);
        items.set(index, newItem);
    }

    public void deleteItem(Todo item) {
        int index = items.indexOf(item);
        items.remove(item);
    }

}
