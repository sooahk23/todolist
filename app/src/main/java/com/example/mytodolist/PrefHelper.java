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

//    class TodoSerializer implements JsonSerializer<Todo> {
//        @Override
//        public Todo serialize(JsonElement json, Type typeOfT,
//                                JsonDeserializationContext context)
//                throws JsonParseException {
//            JsonObject jsonObject = json.getAsJsonObject();
//            JsonElement viewType = jsonObject.get("viewType");
//            if (viewType != null) {
//                switch (viewType.getAsString()) {
//                    case "TEXT":
//                        return context.deserialize(jsonObject,
//                                TodoText.class);
//                    case "IMAGE":
//                        return context.deserialize(jsonObject,
//                                TodoImage.class);
//                }
//            }
//            return null;
//        }
//    }

    public int getNextId() {
        return todoList.id_count+1;
    }

    public Todo insertPref(Todo newItem){
        ArrayList<Todo> items = todoList.items;
        items.add(newItem);
        todoList.id_count = newItem.getId();
        todoList.items = items;
        String prefTodolist = gson.toJson(todoList);
        Log.d("PrefHelper adding", prefTodolist);
        editor.putString("todo", prefTodolist);
        editor.apply();
        return newItem;
    }

    public void updatePref(Todo newItem){
        ArrayList<Todo> items = todoList.items;
        items.replaceAll(item -> item.id==newItem.getId() ? newItem : item);
        String prefTodolist = gson.toJson(todoList);
        editor.putString("todo", prefTodolist);
        editor.apply();
    }

    public void deletePref(Todo item){
        ArrayList<Todo> items = todoList.items;
        items.remove(item);
        String prefTodolist = gson.toJson(todoList);
        editor.putString("todo", prefTodolist);
        editor.apply();
    }

    public TodoList selectAllPref(){
        String prefTodolist = pref.getString("todo", null);
        if (prefTodolist == null){
            todoList = new TodoList();
        } else {
            todoList = gson.fromJson(prefTodolist, TodoList.class);
            Log.d("here!!!!!!", todoList.toString());
        }
        return todoList;
    }
}