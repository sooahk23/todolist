package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TodoDatabase database;

    EditText editText;

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

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editText);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        TodoAdapter adapter = new TodoAdapter();

        adapter.addItem(new Todo("간단한 TODO 리스트", "NOT_STARTED"));
        adapter.addItem(new Todo("3가지 상태로 체크", "NOT_STARTED"));
        adapter.addItem(new Todo("쉽게 활용해요!", "NOT_STARTED"));

        recyclerView.setAdapter(adapter);

        // 저장된 투두리스트 불러오기
        ArrayList<Todo> result = database.selectAll();
        adapter.setItems(result);

        // TODO List 목록 클릭시 이벤트
        // 텍스트 부분 클릭시 텍스트 수정
        // 아이콘 클릭시 상태 변경
        adapter.setOnItemClickListener(new OnTodoItemClickListener() {
            @Override
            public void onItemClick(TodoAdapter.ViewHolder holder, View view, int position) {
                Todo item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "Click! " + item.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // TODO List 추가 버튼 클릭시
        // Input 입력 되고 텍스트 초기화
        // TODO 데이터베이스에 추가
        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo newItem = new Todo(editText.getText().toString(), "NOT_STARTED");
                adapter.addItem(newItem);
                adapter.notifyDataSetChanged();
                database.insertRecord(newItem.getText(), newItem.getStatus());
                editText.setText("");

            }
        });
    }
}