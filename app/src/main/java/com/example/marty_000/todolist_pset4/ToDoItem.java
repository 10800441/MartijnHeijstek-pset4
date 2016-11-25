package com.example.marty_000.todolist_pset4;

/** App: ToDoList
 *  25-11-2016
 *  Martijn Heijstek, 1000441
 *
 *  This class makes a toDoItem with an id and text.
 */

// One toDoItem
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