package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SecondPageActivity extends AppCompatActivity {

    public static final String ADDITIONAL_DATA = "additionalData";
    private Button button;
    private EditText secondNote;
    private ListView listView;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        button = (Button) findViewById(R.id.secondPageButton);
        secondNote = this.findViewById(R.id.secondNotes);
        listView = this.findViewById(R.id.listView);
        //stringArrayList = new ArrayList<String>();

        
        //stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringArrayList);
        //listView.setAdapter(stringArrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //stringArrayList.add(secondNote.getText().toString());
                //stringArrayAdapter.notifyDataSetChanged();
                /*Intent intent = new Intent(SecondPageActivity.this, MainActivity.class);
                intent.putExtra(ADDITIONAL_DATA, new NoteData(secondNote.getText().toString(), new Date()));
                startActivity(intent);*/

            }
        });
    }




}
