package com.example.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleObserver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SecondPageActivity extends AppCompatActivity implements LifecycleObserver{

    static String storageKey = "com.lukasz.ShoppingList.tempArrayList";
    private ListView listView;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    private ArrayList<String> tempArrayList;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Button menuBtn;
    private String title;
    private String myTitle="current";
    private Toolbar toolbar;


    @Override
    protected void onResume() {
        super.onResume();

        Set<String> sourceSet = sharedPref.getStringSet(storageKey+title, new HashSet<>());
        tempArrayList = new ArrayList<>(sourceSet);
    }

    protected void onPause() {
        super.onPause();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Set<String> foo = new HashSet<String>(tempArrayList);
        editor.putStringSet(storageKey+title,foo);
        editor.putString(myTitle,title);
        editor.commit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        toolbar = this.findViewById(R.id.menuToolbar);
        String current = sharedPref.getString(myTitle,"");
        Intent intent = getIntent();
        if (intent.getStringExtra("title")!=null)
        {title = intent.getStringExtra("title");}
        else title = current;
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        menuBtn = this.findViewById(R.id.menuBtn);
        stringArrayList = intent.getStringArrayListExtra("stringArrayList");
        listView = this.findViewById(R.id.listView);

        tempArrayList = new ArrayList<String>();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArrayList);

        stringArrayAdapter.notifyDataSetChanged();

        listView.setAdapter(stringArrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMainMenu();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)stringArrayAdapter.getItem(position);
                AlertDialog alertDialog = new AlertDialog.Builder(SecondPageActivity.this).create();
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

                view.setBackgroundResource(R.color.white);
            }
            else
            {
                tempArrayList.add(item);
                view.setBackgroundResource(R.color.pressed_color);
            }

            view.setSelected(true);
            checkClickedItems();
            stringArrayAdapter.notifyDataSetChanged();
        });



    }



    public void removeItemFromListView(String item)
    {
        tempArrayList.remove(item);
        stringArrayList.remove(item);
        checkClickedItems();


    }

    public void checkClickedItems()
    {
        for (int i = 0; i<stringArrayList.size();i++)
        {
            View v = listView.getChildAt(i);
            String item = (String)stringArrayAdapter.getItem(i);
            boolean blnFound = tempArrayList.contains(item);

            if (blnFound==true)
            {       if (v!=null)
                    v.setBackgroundResource(R.color.pressed_color);
            }
            else
            {       if (v!=null)
                    v.setBackgroundResource(R.color.white);
            }
            stringArrayAdapter.notifyDataSetChanged();
        }
    }


    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void resetApp(View view)
    {
        stringArrayList.clear();
        tempArrayList.clear();
        stringArrayAdapter.clear();
        stringArrayAdapter.notifyDataSetChanged();

    }
    public void editShopListActivity(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putStringArrayListExtra("tempArrayList", tempArrayList);
        startActivity(intent);
    }
    public void displayMainMenu()
    {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }




}