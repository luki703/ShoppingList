package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    static String notesKey = "com.lukasz.ShoppingList.noteskey";
    private SharedPreferences sharedPref;
    private EditText notes;
    private String noteString;
    private SharedPreferences.Editor editor;
    private ListView listView;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = MainActivity.this;
       // sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        notes=this.findViewById(R.id.notes);
        listView = this.findViewById(R.id.listView);
        stringArrayList = new ArrayList<String>();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArrayList);
        stringArrayAdapter.clear();

       /* SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();*/
        listView.setAdapter(stringArrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);
        notes.requestFocus();
        for (int i =0; i<=10;i++){
            stringArrayList.add("item"+i);
            stringArrayAdapter.notifyDataSetChanged();
        }



        listView.setOnItemClickListener((parent, view, position, id) -> {
            String value = (String)stringArrayAdapter.getItem(position);
            //notes.setText(value);

            view.setSelected(true);

            //stringArrayAdapter.notifyDataSetChanged();
        });

        notes.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER)
                {populateList();
                return true;}

                return false;
            }
        });

        //String notesString = sharedPref.getString(notesKey,"");
        //notes.setText(notesString);
        //noteData = (NoteData) getIntent().getExtras().getSerializable(SecondPageActivity.ADDITIONAL_DATA);
    }

    public void saveList(View view)
    {
        populateList();

    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void populateList()
    {
        if (!notes.getText().toString().matches(""))
        {

            stringArrayList.add(notes.getText().toString());
            stringArrayAdapter.notifyDataSetChanged();
            //hideSoftKeyboard(findViewById(android.R.id.content));
            notes.setText(null);
            notes.callOnClick();


        }



    }

}