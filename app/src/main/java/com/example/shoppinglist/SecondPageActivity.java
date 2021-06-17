package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class SecondPageActivity extends AppCompatActivity {

    private EditText notes;
    private ListView listView;
    private ArrayList<String> stringArrayList;
    //private String[] stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);


        Context context = SecondPageActivity.this;
        notes=this.findViewById(R.id.notes);
        /*for (int i =0; i<=10;i++){
            stringArrayList.add("item"+i);
            stringArrayAdapter.notifyDataSetChanged();
        }*/
        listView=this.findViewById(R.id.listView_data);
        stringArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice,stringArrayList);
        listView.setAdapter(stringArrayAdapter);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.item_done)
        {
            String itemSelected = "Selected items: \n";
            for (int i=0;i<listView.getCount();i++)
            {
                if (listView.isItemChecked(i))
                {
                    itemSelected += listView.getItemAtPosition(i) +"\n";
                }
            }
            Toast.makeText(this,itemSelected,Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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