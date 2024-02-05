package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TodoDatabase database;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    private void setPref() {
        pref = getSharedPreferences(getString(R.string.preference_file_key), Activity.MODE_PRIVATE);
        editor = pref.edit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Open Database
        if (database != null) {
            database.close();
            database = null;
        }

        database = TodoDatabase.getInstance(this);
        boolean isOpen = database.open();
        if (isOpen) {
            Log.d(TAG, "Todo database is opened.");
        } else {
            Log.d(TAG, "Todo database is not opened.");
        }

//        setPref();


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        TodoAdapter adapter = new TodoAdapter();

//        adapter.addItem(new Todo("간단한 TODO 리스트", "NOT_STARTED"));
//        adapter.addItem(new Todo("3가지 상태로 체크", "NOT_STARTED"));
//        adapter.addItem(new Todo("쉽게 활용해요!", "NOT_STARTED"));
        recyclerView.setAdapter(adapter);

        // 저장된 투두리스트 불러오기
        ArrayList<Todo> result = database.selectAll();
        adapter.setItems(result);

        // 추가 버튼 클릭시
        EditText editText = findViewById(R.id.editText);
        Button addBtn = findViewById(R.id.addBtn);
        Button addImgBtn = findViewById(R.id.addImgBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                long itemId = database.insertRecord(editText.getText().toString(), "NOT_STARTED");

                TodoText newTodoText = new TodoText(editText.getText().toString(), Status.NOT_STARTED);
                Todo newTodo = new Todo(0, ViewType.TEXT, newTodoText);
                adapter.addItem(newTodo);
                adapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo newTodo = new Todo(1, ViewType.IMAGE, null);
                adapter.addItem(newTodo);
                adapter.notifyDataSetChanged();
            }
        });
    }
}