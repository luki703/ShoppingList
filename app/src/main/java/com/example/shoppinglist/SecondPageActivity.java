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
import android.widget.AbsListView;
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
    private ArrayList<Integer> indexTempArray;
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
        initiateData();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        checkClickedItems();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listView.setOnItemClickListener((parent, lvView, position, id) -> {
                    highlineItemListview(position);
                    Toast.makeText(getApplicationContext(),"position"+position,Toast.LENGTH_SHORT).show();

                });
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            highlineItemListview(position);

        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayMainMenu();

            }
        });


    }

    private void initiateData() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        toolbar = this.findViewById(R.id.menuToolbar);
        menuBtn = this.findViewById(R.id.menuBtn);
        listView = this.findViewById(R.id.listView);
        String current = sharedPref.getString(myTitle,"");
        indexTempArray = new ArrayList<Integer>();
        Intent intent = getIntent();
        if (intent.getStringExtra("title")!=null)
        {title = intent.getStringExtra("title");}
        else title = current;

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        stringArrayList = intent.getStringArrayListExtra("stringArrayList");
        tempArrayList = new ArrayList<String>();
        stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArrayList);

        stringArrayAdapter.notifyDataSetChanged();
        listView.setAdapter(stringArrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Set<String> sourceSet = sharedPref.getStringSet(storageKey+title, new HashSet<>());
        tempArrayList = new ArrayList<>(sourceSet);
        populateIndexTempArray();
        checkClickedItems();

    }
    public void populateIndexTempArray()
    {
        for (int i = 0; i<stringArrayAdapter.getCount(); i++)
        {
            String item = stringArrayAdapter.getItem(i);
            if (tempArrayList.contains(item))
            {
                indexTempArray.add(i);
            }
        }
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
            Toast.makeText(getApplicationContext(),"first:"+listView.getFirstVisiblePosition(),Toast.LENGTH_SHORT).show();
            if (v!=null)

                    v.setBackgroundResource(R.color.pressed_color);


            stringArrayAdapter.notifyDataSetChanged();

                    }});
    }
    public void highlineItemListview(int position)
    {
        checkClickedItems();
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

        stringArrayAdapter.notifyDataSetChanged();
    }
    public void clearAllClicked(View view)
    {
        tempArrayList.clear();
        indexTempArray.clear();
        checkClickedItems();
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