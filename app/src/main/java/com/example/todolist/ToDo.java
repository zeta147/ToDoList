package com.example.todolist;

public class ToDo {
    private int _id;
    private String _name;
    private String _date;
    private String _duration;
    private String _description;


    ToDo(String name, String date, String duration, String detail) {
        this._name = name;
        this._date = date;
        this._duration = duration;
        this._description = detail;
    }

    ToDo(int id, String name, String date, String duration, String description) {
        this._id = id;
        this._name = name;
        this._date = date;
        this._description = description;
        this._duration = duration;
    }

    public int getId() {return _id;}
    public String getName() {return _name;}
    public String getDate() {return _date;}
    public String getDescription() {return _description;}
    public String getDuration() {return _duration;}

    public String getDateForView(){
        String[] date = _date.split("-");
        String year = date[0];
        String month = date[1];
        String day = date[2];
        return day + "/" + month + "/" + year;
    }

    public String getDescriptionForView(){
        String shortDescription;
        if(_description.length() <= 15){
            return _description;
        }
        else {
            shortDescription = _description.substring(0, 15)+"...";
        }
        return shortDescription;
    }
}
