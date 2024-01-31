package com.example.mytodolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>  implements OnTodoItemClickListener {

    ArrayList<Todo> items = new ArrayList<Todo>();
    OnTodoItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.todolist_item, parent, false);

        return new ViewHolder(itemView, this);
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

    public void setOnItemClickListener(OnTodoItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener !=  null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText todoText;
        ImageView todoStatus;
        public ViewHolder(View itemView, final OnTodoItemClickListener listener) {
            super(itemView);

            todoText = itemView.findViewById(R.id.todoText);
            todoStatus = itemView.findViewById(R.id.todoStatus);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Todo item){
            todoText.setText(item.getTodo());
            Status statusHoleder = item.getStatus();
//            todoStatus.setImageIcon();
        }
    }
}
