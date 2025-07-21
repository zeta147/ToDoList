package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ToDoAdapter extends BaseAdapter {
    private ArrayList<ToDo> _toDoList;
    private Context _context;
    private LayoutInflater _layoutInflater;
    public ToDoAdapter(Context context, ArrayList<ToDo> toDoList) {
        this._context = context;
        this. _layoutInflater = LayoutInflater.from(context);
        this._toDoList = toDoList;
    }

    @Override
    public int getCount() {
        return _toDoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = _layoutInflater.inflate(R.layout.todo_item, null);
        ToDo toDo = _toDoList.get(position);
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewDate = convertView.findViewById(R.id.textViewDate);
        TextView textViewDuration = convertView.findViewById(R.id.textViewDuration);
        TextView textViewDetail = convertView.findViewById(R.id.textViewDetail);

        textViewName.setText(toDo.getName());
        textViewDate.setText("Date: " + toDo.getDateForView());
        textViewDuration.setText("Duration (day): " + toDo.getDuration());
        textViewDetail.setText("Detail: " + toDo.getDescriptionForView());

        return convertView;
    }
}
