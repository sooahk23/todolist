package com.example.mytodolist;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    String TAG = "TodoAdapter";
    public interface OnTodoItemClickListener {
        public void onItemLongClick(TodoAdapter.ViewHolder holder, View view, int position) ;
        public void onCheckClick(TodoAdapter.ViewHolder holder, View view, int position) ;
        public void onTextClick(TodoAdapter.ViewHolder holder, View view, int position) ;
        public void onOutClick(TodoAdapter.ViewHolder holder, View view, int position) ;
    }

    ArrayList<Todo> items = new ArrayList<Todo>();
    PrefHelper prefHelper;

    public TodoAdapter(PrefHelper prefHelper) {
        this.prefHelper = prefHelper;
    }

    OnTodoItemClickListener listener = new OnTodoItemClickListener() {
        @Override
        public void onItemLongClick(ViewHolder holder, View view, int position) {
            //TODO: 삭제하기 팝업 띄우기
        }

        // 아이콘 클릭시 상태 변경
        @Override
        public void onCheckClick(ViewHolder holder, View view, int position) {
            Todo item = getItem(position);
            if (item.getViewType() == ViewType.TEXT){
                TodoText todotext = (TodoText) item;
                switch (todotext.status) {
                    case NOT_STARTED:
                        todotext.status = Status.DONE;
                        break;
                    case DONE:
                        todotext.status = Status.NOT_STARTED;
                        break;
                }
                setItem(position, item);
                notifyDataSetChanged();
//                database.updateRecord(item.getId(), item.text, item.status);
                prefHelper.updatePref(item);
            }
        }

        @Override
        public void onTextClick(ViewHolder holder, View view, int position) {
            Todo item = getItem(position);
            TodoText todotext = (TodoText) item;
            if (todotext.status == Status.DONE) {
                return;
            }
            // EditText로 변경
            holder.todoText.setVisibility(View.GONE);
            holder.todoEdit.setText(todotext.text);
            holder.todoEdit.setVisibility(View.VISIBLE);
//                holder.todoStatus.setClickable(false);

            holder.todoEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    todotext.text = s.toString();
                    setItem(position, item);
                    notifyDataSetChanged();
                    prefHelper.updatePref(item);
//                    database.updateRecord(item.getId(), item.text, item.status);
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

        @Override
        public void onOutClick(ViewHolder holder, View view, int position) {
//            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            manager.hideSoftInputFromWindow(holder.todoEdit.getWindowToken(), 0);
            String str = holder.todoEdit.getText().toString().isEmpty() ? "" : holder.todoEdit.getText().toString();
            holder.todoText.setVisibility(View.VISIBLE);
            holder.todoText.setText(str);
            holder.todoEdit.setVisibility(View.GONE);

        }
    };

    @Override
    public int getItemViewType(int position) {
        Todo item = items.get(position);
        return item.getViewType().getValue();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        int text_val = ViewType.TEXT.getValue();
        int img_val = ViewType.IMAGE.getValue();

        if(viewType == ViewType.TEXT.getValue()){
            itemView = inflater.inflate(R.layout.todotext_item, parent, false);
        }else if (viewType == ViewType.IMAGE.getValue()){
            itemView = inflater.inflate(R.layout.todoimg_item, parent, false);
        }else {
            itemView = inflater.inflate(R.layout.todoimg_item, parent, false);
        }
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo item = items.get(position);
        holder.setItem(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Todo item) {
        items.add(item);
    }

    public void setItems(ArrayList<Todo> items){
        this.items = items;
    }

    public Todo getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Todo item){
        items.set(position, item);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoText;
        EditText todoEdit;
        ImageView todoStatus;
        LinearLayout todoParent;
        public ViewHolder(View itemView, final OnTodoItemClickListener listener) {
            super(itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view){
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemLongClick(ViewHolder.this, view, position);
                    }
                    return true;
                }
            });
        }

        public void setItem(Todo item, final OnTodoItemClickListener listener){
            switch (item.getViewType()) {
                case TEXT:
                    if(this.getItemViewType() == ViewType.TEXT.getValue()){
                        todoText = itemView.findViewById(R.id.todoText);
                        todoEdit = itemView.findViewById(R.id.todoEdit);
                        todoStatus = itemView.findViewById(R.id.todoStatus);
                        todoParent = itemView.findViewById(R.id.todoParent);

                        todoEdit.setVisibility(View.GONE);
                        todoStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = getAdapterPosition();

                                if (listener != null) {
                                    listener.onCheckClick(ViewHolder.this, view, position);
                                }
                            }
                        });
                        todoText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = getAdapterPosition();
                                if (listener != null) {
                                    listener.onTextClick(ViewHolder.this, view, position);
                                }
                            }
                        });

                        todoParent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = getAdapterPosition();
                                if (listener != null ) {
                                    listener.onOutClick(ViewHolder.this, view, position);
                                }
                            }
                        });
                    }

                    TodoText todotextItem = (TodoText) item;
                    Log.d("Adapterrrrr", String.valueOf(todotextItem.getId()));
                    todoText.setText(todotextItem.getText());
                    Status nowStatus = todotextItem.getStatus();

                    switch (nowStatus) {
                        case NOT_STARTED:
                            todoStatus.setImageResource(R.drawable.status_not_started);
                            todoText.setPaintFlags(todoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            break;
                        case DONE:
                            todoStatus.setImageResource(R.drawable.status_done);
                            todoText.setPaintFlags(todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            break;
                    }
                    break;
                case IMAGE:
                    break;

            }
        }
    }
}
