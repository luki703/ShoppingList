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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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
    static String storageTempKey = "com.lukasz.ShoppingList.tempArrayList";
    private String myTitle="current";

    private ListView listView;
    private ArrayList<String> stringArrayList;
    private ArrayAdapter<String> stringArrayAdapter;

    private ArrayList<String> tempArrayList;
    private ArrayList<Integer> indexTempArray;
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
        editor.putStringSet(storageKey+title,foo);//stringArray
        editor.putString(myTitle,title);//tittle
        Set<String> tmp = new HashSet<String>(tempArrayList);//tempArrayList
        editor.putStringSet(storageTempKey+title,tmp);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Set<String> sourceSet = sharedPref.getStringSet(storageTempKey+title, new HashSet<>());
        tempArrayList = new ArrayList<>(sourceSet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = MainActivity.this;
        initiateData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                highLineItemListview(position);
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
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    checkClickedItems();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                checkClickedItems();
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
        inflater.inflate(R.menu.activity_menu,menu);

        if (!isDeleteMode){

            menu.findItem(R.id.deleteBtn).setVisible(true);
            menu.findItem(R.id.clearAllIconBtn).setVisible(false);
            menu.findItem(R.id.backIconBtn).setVisible(true);
            menu.findItem(R.id.hiddenDoneBtn).setVisible(false);
            if (stringArrayList.size()!=tempArrayList.size())
            {

                menu.findItem(R.id.checkIconBtn).setVisible(true);
                menu.findItem(R.id.unCheckIconBtn).setVisible(false);
            }
            else {
                menu.findItem(R.id.checkIconBtn).setVisible(false);
                menu.findItem(R.id.unCheckIconBtn).setVisible(true);
            }


        }else if(isDeleteMode){
            menu.findItem(R.id.clearAllIconBtn).setVisible(true);
            menu.findItem(R.id.hiddenDoneBtn).setVisible(true);
            menu.findItem(R.id.deleteBtn).setVisible(false);
            menu.findItem(R.id.backIconBtn).setVisible(false);
            menu.findItem(R.id.unCheckIconBtn).setVisible(false);
            menu.findItem(R.id.checkIconBtn).setVisible(false);
        }

        checkClickedItems();
        invalidateOptionsMenu();
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
                selectAllItems();

                invalidateOptionsMenu();
                return true;
            case R.id.unCheckIconBtn:
                clearAllClicked();

                invalidateOptionsMenu();
                return true;
            case R.id.clearAllIconBtn:

                stringArrayList.clear();
                tempArrayList.clear();
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
        listView = (ListView) this.findViewById(R.id.listView);
        toolbar = this.findViewById(R.id.menuToolbar);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String current = sharedPref.getString(myTitle,"");
        Intent intent = getIntent();
        if (intent.getStringExtra("title")!=null)
        {title = intent.getStringExtra("title");}
        else title = current;
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        indexTempArray = new ArrayList<Integer>();
        Set<String> sourceSet = sharedPref.getStringSet(storageKey+title, new HashSet<>());
        stringArrayList = new ArrayList<String>(sourceSet);
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArrayList);
        Set<String> sourceTempSet = sharedPref.getStringSet(storageTempKey+title, new HashSet<>());
        tempArrayList = new ArrayList<>(sourceTempSet);
        stringArrayAdapter.notifyDataSetChanged();
        listView.setAdapter(stringArrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        populateIndexTempArray();
        notes.requestFocus();
    }
    public void saveList(View view)
    {
        populateList();
        checkClickedItems();
        listView.invalidate();

    }
    public void removeItemFromListView(String item)
    {
        stringArrayList.remove(item);
        checkClickedItems();
        stringArrayAdapter.notifyDataSetChanged();

    }
    public void populateList()
    {
        if (!notes.getText().toString().matches(""))
        {
            if (!stringArrayList.contains(notes.getText().toString()))
            {stringArrayList.add(0, notes.getText().toString());

            stringArrayAdapter.notifyDataSetChanged();
            populateIndexTempArray();}
            else Toast.makeText(getApplicationContext(),"duplikat",Toast.LENGTH_SHORT).show();
            notes.setText(null);

        }
        checkClickedItems();
    }
    public void checkClickedItems()
    {

        for (int i=0; i<listView.getCount();i++) {
            View v = listView.getChildAt(i);
            if (v!=null)
            {
                v.setBackgroundResource(R.color.white);
            }
        }
        indexTempArray.forEach((n) -> {
            if (n-listView.getFirstVisiblePosition()>=0)
            {
                View v = listView.getChildAt(n-listView.getFirstVisiblePosition());
                if (v!=null){
                    v.setBackgroundResource(R.color.pressed_color);}
            }
        });
        stringArrayAdapter.notifyDataSetChanged();
    }
    public void clearAllClicked()
    {
        tempArrayList.clear();
        indexTempArray.clear();
        checkClickedItems();
        stringArrayAdapter.notifyDataSetChanged();
    }
    public void selectAllItems()
    {
        tempArrayList.clear();
        tempArrayList.addAll(stringArrayList);
        stringArrayAdapter.notifyDataSetChanged();
        populateIndexTempArray();
        checkClickedItems();

    }
    public void populateIndexTempArray()
    {
        indexTempArray.clear();
        for (int i = 0; i<stringArrayAdapter.getCount(); i++)
        {
            String item = stringArrayAdapter.getItem(i);
            if (tempArrayList.contains(item))
            {
                indexTempArray.add(i);
            }
        }
        checkClickedItems();
    }
    public void highLineItemListview(int position)
    {
        String item = (String)stringArrayAdapter.getItem(position);
        boolean blnFound = tempArrayList.contains(item);
        if (blnFound==true)
        {
            tempArrayList.remove(item);
            indexTempArray.remove((Integer) position);
        }
        else
        {
            tempArrayList.add(item);
            indexTempArray.add(position);
        }
        checkClickedItems();

    }
}