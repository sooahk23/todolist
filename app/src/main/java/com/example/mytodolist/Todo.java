package com.example.mytodolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

enum ViewType {TEXT(0), IMAGE(1);
    private final int value;
    ViewType(int value) {
        this.value= value;
    }
    public final int getValue() {
        return value;
    }
}

public class Todo {
    long id;
    ViewType viewType;

    @Nullable
    Object object;

    public Todo(long id, ViewType viewType, Object object) {
        this.id = id;
        this.viewType = viewType;
        this.object = object;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
