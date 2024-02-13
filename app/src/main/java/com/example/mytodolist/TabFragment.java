package com.example.mytodolist;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TabFragment extends Fragment {
    private static final String TAG = "TabFragment";
    // SharedPreferences 쓰기 위함
    SharedPreferences pref;
    PrefHelper prefHelper;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    TodoAdapter adapter;

    // Factory method to create a new instance of this fragment using the provided parameters.
    // 팩토리 메서드란??
    public static TabFragment newInstance(String category) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString("category", category); // 카테고리를 변수로 보내주기
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 카테고리 arg에 따라 다른 화면 띄워주기
        String category = getArguments().getString("category", "");
        View view;

        // 1. SharedPreferences를 통해 데이터 가져오기
        pref = getContext().getSharedPreferences(getString(R.string.preference_file_key), Activity.MODE_PRIVATE);
        prefHelper = new PrefHelper(pref);
        TodoList todolist = prefHelper.selectAllPref();

//        Log.d(TAG, todolist.toString()); // 투두리스트 비어있는지 등을 확인하기 위한 디버깅 용도

        // 2. 데이터 등록
        // 단, 데이터가 없을 때는 빈 ArrayList 넣어주어야 오류가 안남


        // 2. 데이터 등록
        // 단, 데이터가 없을 때는 빈 ArrayList 넣어주어야 오류가 안남
        // 3. 화면 반환
        // 궁금한 점: 어떻게 코드 재사용을 줄일 수 있을 것인가? 중복 되는 코드가 보기 안좋음.
        switch (category) {
            case "Fragment 0":
                recyclerView = container.findViewById(R.id.recyclerViewAllFragment);
                // 궁금한 점: 리사이클러뷰는 리니어 레이아웃으로만 가능한가? 다른 레이아웃은 불가한지
                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new TodoAdapter(prefHelper);
                recyclerView.setAdapter(adapter);

                if (todolist == null) adapter.setItems(new ArrayList<Todo>());
                else adapter.setItems(todolist.items);

                view = inflater.inflate(R.layout.todoall_fragment, container, false);
                // 아래 작성된 클릭 이벤트 등록
                setListeners(category, view, adapter);

                return view;
            case "Fragment 1":
                recyclerView = container.findViewById(R.id.recyclerViewTextFragment);
                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new TodoAdapter(prefHelper);
                recyclerView.setAdapter(adapter);

                if (todolist == null) adapter.setItems(new ArrayList<Todo>());
                else adapter.setItems(todolist.items);

                view = inflater.inflate(R.layout.todotext_fragment, container, false);
                setListeners(category, view, adapter);

                return view;
            case "Fragment 2":
                recyclerView = container.findViewById(R.id.recyclerViewImgFragment);
                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new TodoAdapter(prefHelper);
                recyclerView.setAdapter(adapter);

                if (todolist == null) adapter.setItems(new ArrayList<Todo>());
                else adapter.setItems(todolist.items);

                view = inflater.inflate(R.layout.todoimg_fragment, container, false);
                setListeners(category, view, adapter);

                return view;
            default:
                // Should not come here
                Log.e("TabFragment: ", "category not exists or not match with tab names");
                // But anyway, Let's return todoall fragment.
                recyclerView = container.findViewById(R.id.recyclerViewAllFragment);
                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new TodoAdapter(prefHelper);
                recyclerView.setAdapter(adapter);

//                int cd = item.getViewType().getValue();
//                if (todolist == null) adapter.setItems(new ArrayList<Todo>());
//                else adapter.setItems(todolist.items.stream().filter(item -> item.getViewType().getValue().equals(ViewType.TEXT.getValue())));

                view = inflater.inflate(R.layout.todoall_fragment, container, false);
                setListeners(category, view, adapter);

                return view;
        }
    }

    // 클릭 이벤트 리스너 모음
    // 1. 텍스트 추가 버튼 클릭
    // 2. 이미지 추가 버튼 클릭
    private void setListeners(String category, View view, TodoAdapter adapter) {
        Button addBtn;
        Button addImgBtn;

        // 1. 텍스트 추가 버튼 클릭시
        View.OnClickListener textClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = view.findViewById(R.id.editText);
                TodoText newTodoText = new TodoText(prefHelper.getNextId(), ViewType.TEXT,
                        editText.getText().toString(), Status.NOT_STARTED);
                prefHelper.insertPref(newTodoText);
//                adapter.addItem(newTodo); // 왜 이 줄을 추가하면 두 번씩 추가되는지에 대한 분석 필요
                adapter.notifyDataSetChanged(); // 더 적합한 메소드가 있는지? 경고 뜸
                editText.setText("");
            }
        };

        // 2. 이미지 추가 버튼 클릭
        View.OnClickListener imgClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TodoImage newTodoImage = new TodoImage(prefHelper.getNextId(), ViewType.IMAGE);
                prefHelper.insertPref(newTodoImage);
//                adapter.addItem(newTodo); // 왜 이 줄을 추가하면 두 번씩 추가되는지에 대한 분석 필요
                adapter.notifyDataSetChanged(); // 더 적합한 메소드가 있는지? 경고 뜸
            }
        };

        switch (category) {
            case "Fragment 0":
                addBtn = view.findViewById(R.id.addBtn);
                addImgBtn = view.findViewById(R.id.addImgBtn);

                addBtn.setOnClickListener(textClickListener);
                addImgBtn.setOnClickListener(imgClickListener);
                break;
            case "Fragment 1":
                addBtn = view.findViewById(R.id.addBtn);

                addBtn.setOnClickListener(textClickListener);
                break;
            case "Fragment 2":
                addImgBtn = view.findViewById(R.id.addImgBtn);

                addImgBtn.setOnClickListener(imgClickListener);
                break;
            default:
                break;
        }
    }


    // 디버깅을 위해 빠르게 값을 추가하고 싶을 때 쓰는 함수
    public void addItems(){
        prefHelper.initializePref();
        for (int i=0; i<40; i++) {
            if (i%4==0){
                TodoImage newTodoImage = new TodoImage(prefHelper.getNextId(), ViewType.IMAGE);
                prefHelper.insertPref(newTodoImage);
            }else {
                TodoText newTodoText = new TodoText(prefHelper.getNextId(), ViewType.TEXT,
                        "happy cat...", Status.NOT_STARTED);
                prefHelper.insertPref(newTodoText);
            }
        }
    }
}
