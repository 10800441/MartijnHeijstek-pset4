package com.example.marty_000.todolist_pset4;

/** App: ToDoList
 *  25-11-2016
 *  Martijn Heijstek, 1000441
 *
 *  This class shows the toDoList to the user and transmits
 *  actions of the user (such as adding, deleting and updating)
 *  to the DBHelper class.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DBHelper helper;
    private ListView listView;
    private EditText editText;
    private SharedPreferences prefs;
    private ArrayAdapter<ToDoItem> adapter;
    private ArrayList<ToDoItem> toDoList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.toDoListView);
        helper = new DBHelper(this);

        // Load three toDoItems on first initialization
        prefs = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        if (!prefs.contains("toDoList")) {

            helper.create(new ToDoItem("Welcome user!"));
            helper.create(new ToDoItem("Add your to-do items"));
            helper.create(new ToDoItem("Tap to edit and remove them by longpress"));

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("toDoList", "toDoList");
            editor.apply();
        }
        updateAdapter();
    }

    // This function will update the listview
    private void updateAdapter(){
        toDoList = helper.read();

        // Standard adapter
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, toDoList);
        listView.setAdapter(adapter);

        // Long press to delete an item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                helper.delete(adapter.getItem(pos));
                updateAdapter();
                return true;
            }
        });

        // Tap to edit an item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                ToDoItem editableItem = adapter.getItem(pos);

                if (editableItem != null) {
                    // Send the user an alert dialog
                    popUp(editableItem.id_number, editableItem.text);
                    updateAdapter();

                } else {
                    Toast.makeText(context, "item is not editable", Toast.LENGTH_SHORT).show();
                    updateAdapter();
                }
            }
        });
    }

    // Add a new item to the listView
    public void add(View view) {
        String todo_string = editText.getText().toString();
        if(todo_string.length() !=0) {
            ToDoItem item = new ToDoItem(todo_string);
            helper.create(item);
            editText.getText().clear();
            updateAdapter();

        } else {
            Toast.makeText(context, "Please enter text!", Toast.LENGTH_SHORT).show();
        }
    }

    // Pop up screen that gives the user the ability to change an item
    public void popUp(final int id,final String popUpText){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Update ToDo");
        alert.setMessage("Change the text of your to do item");

        // Open key board
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setText(popUpText);
        alert.setView(input);

        // Set the cursor to the right of the text i the editText
        input.setSelection(input.getText().length());

        // Change the ToDoItem
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(input.getText().length() != 0){
                    helper.update(new ToDoItem(id, input.getText().toString()));
                    updateAdapter();
                } else {
                    Toast.makeText(context, "Please enter text!", Toast.LENGTH_SHORT).show();
                    popUp(id, popUpText);
                }
            }
        });

        // Cancel popUp
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}