package com.example.todolist;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class CreateActivity extends AppCompatActivity {
    private DatabaseHelper _databaseHelper;
    private ToDo _toDo;
    private EditText _editTextName;
    private DatePicker _datePickerDate;
    private EditText _editTextDuration;
    private EditText _editTextDescription;
    private TextView _textViewNameErrorMessage;
    private TextView _textViewDateErrorMessage;
    private TextView _textViewDurationErrorMessage;
    private TextView _textViewDescriptionErrorMessage;
    private String _name;
    private String _date;
    private String _duration;
    private String _description;
    private Calendar _calendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWidgets();
        getErrorMessageWidgets();
    }

    private void getWidgets(){
        _editTextName = findViewById(R.id.editTextText);
        _datePickerDate = findViewById(R.id.datePicker);
        _editTextDuration = findViewById(R.id.editTextDuration);
        _editTextDescription = findViewById(R.id.editTextDescription);
    }

    private void getErrorMessageWidgets(){
        _textViewNameErrorMessage = findViewById(R.id.textViewNameErrorMessage);
        _textViewDateErrorMessage = findViewById(R.id.textViewDateErrorMessage);
        _textViewDurationErrorMessage = findViewById(R.id.textViewDurationErrorMessage);
        _textViewDescriptionErrorMessage = findViewById(R.id.textViewDescriptionErrorMessage);
    }

    public void onClickAddToDo(View view){
        boolean canAdd;
        canAdd = checkValidName() && checkValidDate() && checkValidDuration() && checkValidDescription();
        if(!canAdd){
            return;
        }
        Thread thread = new Thread(new addToDoTask());
        thread.start();
        while(thread.isAlive()){}
        Toast.makeText(this, "ToDo added", Toast.LENGTH_SHORT).show();
    }

    private class addToDoTask implements Runnable {
        @Override
        public void run() {
            _databaseHelper = new DatabaseHelper(CreateActivity.this);
            _toDo = new ToDo(_name, _date, _duration, _description);
            _databaseHelper.insertToDo(_toDo);
        }
    }

    private boolean checkValidName(){
        String nameTemp = _editTextName.getText().toString();
        if (nameTemp.isEmpty()){
            setErrorMessageVisible(_textViewNameErrorMessage, "Please enter a name");
            return false;
        }
        if(nameTemp.length() > 255){
            setErrorMessageVisible(_textViewNameErrorMessage, "Please enter a name less than 255 characters");
            return false;
        }
        setErrorMessageInvisible(_textViewNameErrorMessage);
        _name = nameTemp;
        return true;
    }

    private boolean checkValidDate() {
        _calendar = Calendar.getInstance();
        int currentYear = _calendar.get(Calendar.YEAR);

        if (_datePickerDate.getYear() < currentYear) {
            setErrorMessageVisible(_textViewDateErrorMessage, "Please select a future year");
            return false;
        }
        _date = _datePickerDate.getYear() + "-" + _datePickerDate.getMonth() + "-" + _datePickerDate.getDayOfMonth();
        setErrorMessageInvisible(_textViewDateErrorMessage);
        return true;
    }

    private boolean checkValidDuration(){
        String durationTemp = _editTextDuration.getText().toString();
        if(durationTemp.isEmpty()){
            setErrorMessageVisible(_textViewDurationErrorMessage, "Please enter a duration (number)");
            return false;
        }
        _duration = durationTemp;
        return true;
    }

    private boolean checkValidDescription(){
        String descriptionTemp = _editTextDescription.getText().toString();
        if(descriptionTemp.length() > 255){
            setErrorMessageVisible(_textViewDescriptionErrorMessage, "Please enter a description less than 255 characters");
            return false;
        }
        _description = descriptionTemp;
        return true;
    }

    private void setErrorMessageVisible(TextView textView, String errorMessage){
        textView.setText(errorMessage);
        textView.setVisibility(View.VISIBLE);
    }

    private void setErrorMessageInvisible(TextView textView){
        textView.setVisibility(View.INVISIBLE);
    }
}