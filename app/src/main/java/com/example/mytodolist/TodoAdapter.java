package com.example.mytodolist;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    public interface OnTodoItemLongClickListener {
        public void onItemLongClick(TodoAdapter.ViewHolder holder, View view, int position) ;
    }

    public interface OnTodoCheckClickListener {
        public void onCheckClick(TodoAdapter.ViewHolder holder, View view, int position) ;
    }

    public interface OnTodoTextClickListener {
        public void onTextClick(TodoAdapter.ViewHolder holder, View view, int position) ;
    }

    public interface OnTodoOutClickListener {
        public void onOutClick(TodoAdapter.ViewHolder holder, View view, int position) ;
    }

    ArrayList<Todo> items = new ArrayList<Todo>();
    OnTodoItemLongClickListener longListener;
    OnTodoCheckClickListener checkListener;
    OnTodoTextClickListener textListener;

    OnTodoOutClickListener outListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.todolist_item, parent, false);

        return new ViewHolder(itemView, longListener, checkListener, textListener, outListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo item = items.get(position);
        holder.setItem(item);
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

    public void setOnItemClickListener(OnTodoItemLongClickListener listener){
        this.longListener = listener;
    }

    public void setOnCheckClickListener(OnTodoCheckClickListener listener){
        this.checkListener = listener;
    }
    public void setOnTextClickListener(OnTodoTextClickListener listener){
        this.textListener = listener;
    }
    public void setOnOutClickListener(OnTodoOutClickListener listener){
        this.outListener = listener;
    }

//    @Override
//    public void onItemClick(ViewHolder holder, View view, int position) {
//        if (listener !=  null) {
//            listener.onItemClick(holder, view, position);
//        }
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoText;
        EditText todoEdit;
        ImageView todoStatus;
        LinearLayout todoParent;
        public ViewHolder(View itemView, final OnTodoItemLongClickListener longListener,
                          final OnTodoCheckClickListener checkListener,
                          final OnTodoTextClickListener textListener,
                          final OnTodoOutClickListener outListener) {
            super(itemView);

            todoText = itemView.findViewById(R.id.todoText);
            todoEdit = itemView.findViewById(R.id.todoEdit);
            todoStatus = itemView.findViewById(R.id.todoStatus);
            todoParent = itemView.findViewById(R.id.todoParent);

            todoEdit.setVisibility(View.GONE);
            todoStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (checkListener != null) {
                        checkListener.onCheckClick(ViewHolder.this, view, position);
                    }
                }
            });
            todoText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (textListener != null) {
                        textListener.onTextClick(ViewHolder.this, view, position);
                    }
                }
            });

            todoParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (outListener != null ) {
                        outListener.onOutClick(ViewHolder.this, view, position);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view){
                    int position = getAdapterPosition();

                    if (longListener != null) {
                        longListener.onItemLongClick(ViewHolder.this, view, position);
                    }
                    return true;
                }
            });
        }

        public void setItem(Todo item){
            todoText.setText(item.getText());
            String nowStatus = item.getStatus();
            switch (nowStatus) {
                case "NOT_STARTED":
                    todoStatus.setImageResource(R.drawable.status_not_started);
                    todoText.setPaintFlags(todoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    break;
                case "DONE":
                    todoStatus.setImageResource(R.drawable.status_done);
                    todoText.setPaintFlags(todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    break;

            }
        }
    }
}
