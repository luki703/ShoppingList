package com.example.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static String notesKey = "com.lukasz.ShoppingList.noteskey";
    private SharedPreferences sharedPref;
    private EditText notes;
    private SharedPreferences.Editor editor;
    private ListView listView;
    private boolean dialogExitStatus = false;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    private ArrayList<String> tempArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = MainActivity.this;
       // sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        notes=this.findViewById(R.id.notes);
        listView = this.findViewById(R.id.listView);
        stringArrayList = new ArrayList<String>();
        tempArrayList = new ArrayList<String>();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArrayList);
        stringArrayAdapter.clear();

        listView.setAdapter(stringArrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);
        notes.requestFocus();
        for (int i =0; i<=10;i++){
            stringArrayList.add(0,"item"+i);
            tempArrayList.add("item"+i);
            stringArrayAdapter.notifyDataSetChanged();
        }
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
        listView.setOnItemClickListener((parent, view, position, id) -> {


            String item = (String)stringArrayAdapter.getItem(position);
            boolean blnFound = tempArrayList.contains(item);
            if (blnFound==true)
            {
                tempArrayList.remove(item);

                view.setBackgroundResource(R.color.pressed_color);
            }
            else
            {
                tempArrayList.add(item);
                view.setBackgroundResource(R.color.white);
            }

            view.setSelected(true);

            stringArrayAdapter.notifyDataSetChanged();
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

    public void removeItemFromListView(String item)
    {
        tempArrayList.remove(item);
        stringArrayList.remove(item);
        stringArrayAdapter.notifyDataSetChanged();
        checkClickedItems();//podejrzane

    }

    public void checkClickedItems()
    {
        //hideSoftKeyboard(findViewById(android.R.id.content));
        for (int i = 0; i<stringArrayList.size();i++)
        {
            View v = listView.getChildAt(i);
        String item = (String)stringArrayAdapter.getItem(i);
        boolean blnFound = tempArrayList.contains(item);

        if (blnFound==true)
        {
            if (v!=null)
            v.setBackgroundResource(R.color.white);
        }
        else
        {
            if (v!=null)
            v.setBackgroundResource(R.color.pressed_color);
        }
        }

    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void resetApp(View view)//działa
    {
        stringArrayList.clear();
        tempArrayList.clear();
        stringArrayAdapter.clear();
        stringArrayAdapter.notifyDataSetChanged();

    }
    public void populateList()//działa ale zle zaznacza po dodaniu
    {
        if (!notes.getText().toString().matches(""))
        {

            stringArrayList.add(0,notes.getText().toString());
            tempArrayList.add(notes.getText().toString());
            checkClickedItems();
            stringArrayAdapter.notifyDataSetChanged();


            notes.setText(null);


        }



    }

}