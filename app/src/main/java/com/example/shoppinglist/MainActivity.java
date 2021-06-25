package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText notes;
    private String storageKey = "com.lukasz.ShoppingList.";
    private String myTitle="current";

    private ListView listView;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String title;
    private Toolbar toolbar;
    private boolean isDeleteMode = false;

    public MainActivity() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Set<String> foo = new HashSet<String>(stringArrayList);
        editor.putStringSet(storageKey+title,foo);
        editor.putString(myTitle,title);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateData();


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        menu.findItem(R.id.menuIconBtn).setVisible(false);
        menu.findItem(R.id.editIconBtn).setVisible(false);
        if (!isDeleteMode){

            menu.findItem(R.id.deleteBtn).setVisible(true);
            menu.findItem(R.id.clearAllIconBtn).setVisible(false);
            menu.findItem(R.id.backIconBtn).setVisible(true);
            menu.findItem(R.id.checkIconBtn).setVisible(true);
            menu.findItem(R.id.hiddenDoneBtn).setVisible(false);


        }else if(isDeleteMode){
            menu.findItem(R.id.clearAllIconBtn).setVisible(true);
            menu.findItem(R.id.hiddenDoneBtn).setVisible(true);
            menu.findItem(R.id.deleteBtn).setVisible(false);
            menu.findItem(R.id.backIconBtn).setVisible(false);
            menu.findItem(R.id.checkIconBtn).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.deleteBtn:
                toolbar.setBackgroundResource(R.color.red);
                isDeleteMode=true;
                toolbar.setTitle("Kasowanie");
                invalidateOptionsMenu();
                return true;
            case R.id.hiddenDoneBtn:
                toolbar.setBackgroundResource(R.color.light_blue);

                isDeleteMode=false;
                toolbar.setTitle(title);
                invalidateOptionsMenu();
                return true;
            case R.id.backIconBtn:
                displayMainMenu();
                return true;
            case R.id.checkIconBtn:
                openShopListActivity(title);
                return true;
            case R.id.clearAllIconBtn:

                stringArrayList.clear();
                stringArrayAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayMainMenu() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    private void initiateData() {
        notes=this.findViewById(R.id.notes);
        listView = this.findViewById(R.id.listView);
        toolbar = this.findViewById(R.id.menuToolbar);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String current = sharedPref.getString(myTitle,"");
        Intent intent = getIntent();
        if (intent.getStringExtra("title")!=null)
        {title = intent.getStringExtra("title");}
        else title = current;
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        Set<String> sourceSet = sharedPref.getStringSet(storageKey+title, new HashSet<>());
        stringArrayList = new ArrayList<String>(sourceSet);
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArrayList);

        stringArrayAdapter.notifyDataSetChanged();
        listView.setAdapter(stringArrayAdapter);
        notes.requestFocus();
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
  
    public void populateList()
    {
        if (!notes.getText().toString().matches(""))
        {

            stringArrayList.add(0, notes.getText().toString());
            stringArrayAdapter.notifyDataSetChanged();

            notes.setText(null);

        }
    }
    public void openShopListActivity(String item)
    {
        Intent intent = new Intent(this, SecondPageActivity.class);
        intent.putStringArrayListExtra("stringArrayList", stringArrayList);
        intent.putExtra("title", item);
        startActivity(intent);
    }



}