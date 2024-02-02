package com.example.mytodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
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

//        adapter.addItem(new Todo("간단한 TODO 리스트", "NOT_STARTED"));
//        adapter.addItem(new Todo("3가지 상태로 체크", "NOT_STARTED"));
//        adapter.addItem(new Todo("쉽게 활용해요!", "NOT_STARTED"));

        recyclerView.setAdapter(adapter);

        // 저장된 투두리스트 불러오기
        ArrayList<Todo> result = database.selectAll();
        adapter.setItems(result);

        // List 목록 클릭시 이벤트
        // 길게 누르면 삭제하기 팝업 띄우기
        adapter.setOnItemClickListener(new TodoAdapter.OnTodoItemLongClickListener() {
            @Override
            public void onItemLongClick(TodoAdapter.ViewHolder holder, View view, int position) {
                Todo item = adapter.getItem(position);
                //TODO: 삭제하기 팝업 띄우기
                Toast.makeText(getApplicationContext(), "Click! " + item.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 아이콘 클릭시 상태 변경
        adapter.setOnCheckClickListener(new TodoAdapter.OnTodoCheckClickListener() {
            @Override
            public void onCheckClick(TodoAdapter.ViewHolder holder, View view, int position) {
                Todo item = adapter.getItem(position);
                switch (item.status) {
                    case "NOT_STARTED":
                        item.status = "DONE";
                        break;
                    case "DONE":
                        item.status = "NOT_STARTED";
                        break;
                }
                adapter.setItem(position, item);
                adapter.notifyDataSetChanged();
                database.updateRecord(item.getId(), item.text, item.status);
            }
        });

        // 텍스트 부분 클릭시 텍스트 수정
        adapter.setOnTextClickListener(new TodoAdapter.OnTodoTextClickListener() {
            @Override
            public void onTextClick(TodoAdapter.ViewHolder holder, View view, int position) {
                Todo item = adapter.getItem(position);
//                Log.d(TAG, "HELLO!!!!!! " + item.id + item.status + item.text);
                if (item.status.equals("DONE")) {
//                    Log.d(TAG, "HELLO!!!!!! " + item.id + item.status + item.text);
                    return;
                }
                // EditText로 변경
                holder.todoText.setVisibility(View.GONE);
                holder.todoEdit.setText(item.text);
                holder.todoEdit.setVisibility(View.VISIBLE);
//                holder.todoStatus.setClickable(false);

                //
                holder.todoEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        item.text = s.toString();
                        adapter.setItem(position, item);
                        adapter.notifyDataSetChanged();
                        database.updateRecord(item.getId(), item.text, item.status);
                    }
                });

//                holder.todoParent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d(TAG, "here4");
//                        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                        manager.hideSoftInputFromWindow(holder.todoEdit.getWindowToken(), 0);
//                        String str = holder.todoEdit.getText().toString().isEmpty() ? "" : holder.todoEdit.getText().toString();
//                        holder.todoText.setVisibility(View.VISIBLE);
//                        holder.todoText.setText(str);
//                        holder.todoEdit.setVisibility(View.GONE);
//                    }
//                });
            }
        });

        adapter.setOnOutClickListener(new TodoAdapter.OnTodoOutClickListener() {
            @Override
            public void onOutClick(TodoAdapter.ViewHolder holder, View view, int position) {
                Log.d(TAG, "here4");
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(holder.todoEdit.getWindowToken(), 0);
                String str = holder.todoEdit.getText().toString().isEmpty() ? "" : holder.todoEdit.getText().toString();
                holder.todoText.setVisibility(View.VISIBLE);
                holder.todoText.setText(str);
                holder.todoEdit.setVisibility(View.GONE);
            }
        });

        // TODO List 추가 버튼 클릭시
        // Input 입력 되고 텍스트 초기화
        // TODO 데이터베이스에 추가
        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long itemId = database.insertRecord(editText.getText().toString(), "NOT_STARTED");
                Todo newItem = new Todo(itemId, editText.getText().toString(), "NOT_STARTED");
//                Log.d(TAG, "printing item.." + newItem.id+ newItem.text + newItem.status);
                adapter.addItem(newItem);
                adapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
    }
}