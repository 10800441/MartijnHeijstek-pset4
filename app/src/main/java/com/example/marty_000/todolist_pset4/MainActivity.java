package com.example.marty_000.todolist_pset4;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DBHelper helper;
    ListView listView;
    EditText editText;
    SharedPreferences prefs;
    ArrayAdapter<ToDoItem> adapter;
    ArrayList<ToDoItem> toDoList;
    private String editedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.toDoListView);
        helper = new DBHelper(this);

        prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        if (!prefs.contains("toDoList")) {

            helper.create(new ToDoItem("Welcome user!"));

            helper.create(new ToDoItem("Add your to-do items"));

            helper.create(new ToDoItem("And remove them by longpress"));

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("toDoList", "toDoList");
            editor.apply();
        }
        updateAdapter();
    }
        private void updateAdapter(){
        toDoList = helper.read();

        // standard adapter
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, toDoList);
        listView.setAdapter(adapter);

        // Long press to delete an item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                delete(pos);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Context context = getApplicationContext();
                ToDoItem editableItem = adapter.getItem(pos);

                if (editableItem != null) {
                    final String updatedText = popUp(editableItem.text);
                    helper.update(new ToDoItem(editableItem.id_number, updatedText));
                    adapter.insert(new ToDoItem(editableItem.id_number, updatedText), pos);
                    updateAdapter();

                } else {
                    Toast.makeText(context, "item is not editable", Toast.LENGTH_SHORT).show();
                    updateAdapter();
                }
            }
        });
    }

    public void delete(int pos) {
        helper.delete(adapter.getItem(pos));
        updateAdapter();
    }
    public void add(View view) {
        String todo_string = editText.getText().toString();

        ToDoItem item = new ToDoItem(todo_string);
        helper.create(item);
        editText.getText().clear();
        updateAdapter();
    }

    public String popUp(String popUpText){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Update ToDo");
        alert.setMessage("Change the text of your to do item");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(popUpText);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MainActivity.this.editedText = input.getText().toString();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
        return editedText;
    }
}