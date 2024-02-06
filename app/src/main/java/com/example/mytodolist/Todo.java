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
    int id;
    ViewType viewType;

    public Todo(int id, ViewType viewType) {
        this.id = id;
        this.viewType = viewType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }


}
