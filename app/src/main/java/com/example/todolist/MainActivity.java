package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listViewToDoList;
    private TextView _textViewEmptyMessage;
    private ProgressBar _progressBar;
    private ArrayList<ToDo> toDoList;
    private DatabaseHelper databaseHelper;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listViewToDoList = findViewById(R.id.listViewToDoList);
        _textViewEmptyMessage = findViewById(R.id.textViewEmptyMessage);
        _progressBar = findViewById(R.id.progressBar);
        context = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToDoList();
    }

    private void getToDoList(){
        Thread thread = new Thread(new getToDoListTask());
        thread.start();
        while (thread.isAlive()){}
        _progressBar.setVisibility(View.INVISIBLE);
        if(toDoList.isEmpty()){
            _textViewEmptyMessage.setVisibility(View.VISIBLE);
        }
        else{
            _textViewEmptyMessage.setVisibility(View.INVISIBLE);
        }
        ToDoAdapter toDoAdapter = new ToDoAdapter(context, toDoList);
        listViewToDoList.setAdapter(toDoAdapter);
        listViewToDoList.setOnItemClickListener((parent, view, position, id) -> {
           Intent intent = new Intent(context, DetailsActivity.class);
           intent.putExtra("id", toDoList.get(position).getId());
           intent.putExtra("name", toDoList.get(position).getName());
           intent.putExtra("date", toDoList.get(position).getDate());
           intent.putExtra("duration", toDoList.get(position).getDuration());
           intent.putExtra("description", toDoList.get(position).getDescription());
           startActivity(intent);
        });
        databaseHelper.close();
    }

    private class getToDoListTask implements Runnable {
        @Override
        public void run() {
            databaseHelper = new DatabaseHelper(context);
            toDoList = databaseHelper.getToDoList();
        }

    }

    public void onClickAddToDo(View view){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
}