package com.example.mytodolist;


import android.app.Activity;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefHelper {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson gson;
    TodoList todoList;

    public PrefHelper(SharedPreferences pref) {
        this.pref = pref;
        this.editor = pref.edit();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Todo.class, new TodoDeserializer())
                .registerTypeAdapter(Todo.class, new TodoSerializer())
                .setPrettyPrinting()
                .create();
    }

    class TodoDeserializer implements JsonDeserializer<Todo> {
        @Override
        public Todo deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement viewType = jsonObject.get("viewType");
            if (viewType != null) {
                switch (viewType.getAsString()) {
                    case "TEXT":
                        return context.deserialize(jsonObject,
                                TodoText.class);
                    case "IMAGE":
                        return context.deserialize(jsonObject,
                                TodoImage.class);
                }
            }
            return null;
        }
    }

    class TodoSerializer implements JsonSerializer<Todo> {
        @Override
        public JsonElement serialize(Todo todoItem, Type typeOfT,
                                JsonSerializationContext context)
                throws JsonParseException {
            switch (todoItem.getViewType()) {
                case TEXT:
                    return context.serialize(todoItem,
                            TodoText.class);
                case IMAGE:
                    return context.serialize(todoItem,
                            TodoImage.class);
            }
            return null;
        }
    }

    public int getNextId() {
        return todoList.id_count+1;
    }

    public Todo insertPref(Todo newItem){
        ArrayList<Todo> items = todoList.items;
        items.add(newItem);
        todoList.id_count = newItem.getId();
        todoList.items = items;
//        Log.d("dd", todoList.items.toString());
        String prefTodolist = gson.toJson(todoList);
        editor.putString("todo", prefTodolist);
        editor.commit();
        return newItem;
    }

    public void updatePref(Todo newItem){
        ArrayList<Todo> items = todoList.items;
        items.replaceAll(item -> item.id==newItem.getId() ? newItem : item);
        todoList.items = items;
//        Log.d("dd", todoList.items.toString());
        String prefTodolist = gson.toJson(todoList);
        editor.putString("todo", prefTodolist);
        editor.commit();
    }

    public void deletePref(Todo item){
        ArrayList<Todo> items = todoList.items;
        items.remove(item);
        todoList.items = items;
//        Log.d("dd", todoList.items.toString());
        String prefTodolist = gson.toJson(todoList);
        editor.putString("todo", prefTodolist);
        editor.commit();
    }

    public TodoList selectAllPref(){
        String prefTodolist = pref.getString("todo", null);
        if (todoList != null){
//            Log.d("dd", todoList.items.toString());
            return todoList;
        }

        if (prefTodolist == null){
            todoList = new TodoList();
        } else {
            todoList = gson.fromJson(prefTodolist, TodoList.class);
        }
        return todoList;
    }

    public void initializePref(){
        editor.putString("todo", null);
        editor.commit();
    }
}
