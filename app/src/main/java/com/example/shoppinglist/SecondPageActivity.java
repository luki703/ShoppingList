package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleObserver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
    private boolean isDeleteMode = false;
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
        ViewGroup second = this.findViewById(R.id.mainLayout);
        second.invalidate();

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

                });
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            highlineItemListview(position);

        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.second_menu,menu);
        menu.findItem(R.id.clearAllIconBtn).setVisible(false);
        menu.findItem(R.id.backIconBtn).setVisible(false);
        menu.findItem(R.id.checkIconBtn).setVisible(false);
        menu.findItem(R.id.deleteBtn).setVisible(false);
        menu.findItem(R.id.hiddenDoneBtn).setVisible(false);
        checkClickedItems();

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
            case R.id.menuIconBtn:
                displayMainMenu();
                return true;
            case R.id.editIconBtn:
                editShopListActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateData() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        toolbar = this.findViewById(R.id.menuToolbar);
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
    public void editShopListActivity()
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