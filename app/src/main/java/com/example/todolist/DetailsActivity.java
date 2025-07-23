package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity {
    private Context context;
    private DatabaseHelper _databaseHelper;
    private ToDo _toDo;
    private int _id;
    private EditText _editTextName;
    private DatePicker _datePickerDate;
    private EditText _editTextDuration;
    private EditText _editTextDescription;
    private TextView _textViewNameErrorMessage,
            _textViewDateErrorMessage,
            _textViewDurationErrorMessage,
            _textViewDescriptionErrorMessage;
    private Button _buttonEdit,
                    _buttonCancel,
                    _buttonSave,
                    _buttonDelete;
    private String _name, _date, _duration, _description;;
    private Calendar _calendar;
    private int _year;
    private int _month;
    private int _day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = this;

        getWidget();
        getErrorMessageWidgets();
        disableInput();
        getDetailsToDo();
        setWidgetInputValues();
        _toDo = new ToDo(_id, _name, _date, _duration, _description);
        getButtonWidgets();
        _buttonSave.setVisibility(View.INVISIBLE);
        _buttonCancel.setVisibility(View.INVISIBLE);
    }

    private void getWidget(){
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

    private void getButtonWidgets(){
        _buttonEdit = findViewById(R.id.buttonEdit);
        _buttonCancel = findViewById(R.id.buttonCancel);
        _buttonSave = findViewById(R.id.buttonSave);
        _buttonDelete = findViewById(R.id.buttonDelete);
    }

    private void disableInput(){
        _editTextName.setEnabled(false);
        _datePickerDate.setEnabled(false);
        _editTextDuration.setEnabled(false);
        _editTextDescription.setEnabled(false);
    }

    private void getDetailsToDo(){
        _id = getIntent().getIntExtra("id", 0);
        _name = getIntent().getStringExtra("name");
        _date = getIntent().getStringExtra("date");
        _duration = getIntent().getStringExtra("duration");
        _description = getIntent().getStringExtra("description");
    }

    private void setWidgetInputValues(){
        _editTextName.setText(_name);
        String[] date = _date.split("-");
        _year = Integer.parseInt(date[0]);
        _month = Integer.parseInt(date[1]);
        _day = Integer.parseInt(date[2]);
        _datePickerDate.updateDate(_year, _month, _day);
        _editTextDuration.setText(_duration);
        _editTextDescription.setText(_description);
    }

    public void onClickEditToDo(View view) {
        _editTextName.setEnabled(true);
        _datePickerDate.setEnabled(true);
        _editTextDuration.setEnabled(true);
        _editTextDescription.setEnabled(true);
        setButtonVisibilityEditMode();
    }

    public void onClickCancelToDo(View view) {

        _editTextName.setEnabled(false);
        _datePickerDate.setEnabled(false);
        _editTextDuration.setEnabled(false);
        _editTextDescription.setEnabled(false);
        setButtonVisibilityViewMode();
    }

    private void setButtonVisibilityEditMode(){
        _buttonEdit.setVisibility(View.INVISIBLE);
        _buttonCancel.setVisibility(View.VISIBLE);
        _buttonSave.setVisibility(View.VISIBLE);
        _buttonDelete.setVisibility(View.INVISIBLE);
    }

    private void setButtonVisibilityViewMode(){
        _buttonEdit.setVisibility(View.VISIBLE);
        _buttonCancel.setVisibility(View.INVISIBLE);
        _buttonSave.setVisibility(View.INVISIBLE);
        _buttonDelete.setVisibility(View.VISIBLE);
    }

    public void onClickSaveToDo(View view){
        boolean canSave;
        canSave = checkValidName() && checkValidDate() && checkValidDuration() && checkValidDescription();
        if(!canSave){
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Save To Do");
        alertDialogBuilder.setMessage("Are you sure you want to save?");
        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {});
        alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
            Thread thread = new Thread(new saveToDoTask());
            thread.start();
            while(thread.isAlive()){}
            Toast.makeText(this, "ToDo saved", Toast.LENGTH_SHORT).show();
            _databaseHelper.close();
            finish();
        });
        alertDialogBuilder.create().show();
    }

    private class saveToDoTask implements Runnable {
        @Override
        public void run() {
            _databaseHelper = new DatabaseHelper(context);
            _toDo = new ToDo(_id, _name, _date, _duration, _description);
            _databaseHelper.updateToDo(_toDo);

        }
    }

    public void onClickDeleteToDo(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Delete To Do");
        alertDialog.setMessage("Are you sure you want to delete?");
        alertDialog.setNegativeButton("No", (dialog, which) -> {});
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Thread thread = new Thread(new deleteToDoTask());
            thread.start();
            while (thread.isAlive()) {}
            Toast.makeText(this, "ToDo deleted", Toast.LENGTH_SHORT).show();
            _databaseHelper.close();
            finish();
        });
        alertDialog.create().show();
    }

    private class deleteToDoTask implements Runnable {
        @Override
        public void run() {
            _databaseHelper = new DatabaseHelper(context);
            _databaseHelper.deleteToDo(_toDo);
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
        int currentMonth = _calendar.get(Calendar.MONTH);
        int currentDay = _calendar.get(Calendar.DAY_OF_MONTH);

        if (_datePickerDate.getYear() < currentYear) {
            setErrorMessageVisible(_textViewDateErrorMessage, "Please select a future year");
            return false;
        }
        if(_datePickerDate.getMonth() < currentMonth){
            setErrorMessageVisible(_textViewDateErrorMessage, "Please select a future month");
            return false;
        }
        if(_datePickerDate.getDayOfMonth() < currentDay){
            setErrorMessageVisible(_textViewDateErrorMessage, "Please select a future day");
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