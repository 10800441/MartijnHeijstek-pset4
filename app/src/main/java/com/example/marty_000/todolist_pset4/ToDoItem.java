package com.example.marty_000.todolist_pset4;

/**
 * Created by marty_000 on 22-11-2016.
 */

public class ToDoItem{
    public int id_number;
    public String text;

    public ToDoItem(int id_number, String text){
        this.id_number = id_number;
        this.text = text;
    }
    public ToDoItem(String text){
        this.text = text;
    }

    @Override
    public String toString() {
         return text;
    }
}
