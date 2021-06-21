package com.example.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText notes;
    private String storageKey = "com.lukasz.ShoppingList.key1";
    private ListView listView;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    private Button nextBtn;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onPause() {
        super.onPause();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Set<String> foo = new HashSet<String>(stringArrayList);
        editor.putStringSet(storageKey,foo);
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Context context = MainActivity.this;
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        Set<String> sourceSet = sharedPref.getStringSet(storageKey, new HashSet<>());
        stringArrayList = new ArrayList<String>(sourceSet);
        notes=this.findViewById(R.id.notes);
        listView = this.findViewById(R.id.listView);
        nextBtn = this.findViewById(R.id.nextBtn);
        //stringArrayList = new ArrayList<String>();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArrayList);

        stringArrayAdapter.notifyDataSetChanged();
        listView.setAdapter(stringArrayAdapter);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        notes.requestFocus();



        stringArrayAdapter.notifyDataSetChanged();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShopListActivity();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)stringArrayAdapter.getItem(position);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle(getString(R.string.alertDialogTitle));
                alertDialog.setMessage(getString(R.string.alertDialogText));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.alertDialogPositive),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( "AlertDialog", "Positive" );
                                removeItemFromListView(item);

                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.alertDialogNegative),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( "AlertDialog", "Negative" );

                                dialog.dismiss();

                            }
                        });
                alertDialog.show();

                return false;
            }
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



    }

    public void saveList(View view)
    {
        populateList();
    }

    public void removeItemFromListView(String item)
    {
        stringArrayList.remove(item);
        stringArrayAdapter.notifyDataSetChanged();
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void resetApp(View view)
    {
        stringArrayList.clear();
        stringArrayAdapter.clear();
        stringArrayAdapter.notifyDataSetChanged();
    }
    public void populateList()
    {
        if (!notes.getText().toString().matches(""))
        {

            stringArrayList.add(0, notes.getText().toString());
            stringArrayAdapter.notifyDataSetChanged();

            notes.setText(null);

        }
    }
    public void openShopListActivity()
    {
        Intent intent = new Intent(this, SecondPageActivity.class);
        intent.putStringArrayListExtra("stringArrayList", stringArrayList);
        startActivity(intent);
    }



}