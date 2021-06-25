package com.example.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainMenu extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String storageKey = "com.lukasz.ShoppingList.titleArrayList";
    private ArrayList titleArrayList;
    private ArrayList buttonsList;
    private EditText newListEditTextTitle;
    private Button addListBtn;
    private LinearLayout ll;
    private LinearLayout.LayoutParams lp;
    private boolean isDeleteMode = false;
    private Toolbar toolbar;

    @Override
    protected void onPause() {
        super.onPause();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Set<String> foo = new HashSet<String>(titleArrayList);
        editor.putStringSet(storageKey,foo);
        editor.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu_activity);
        Context context = MainMenu.this;
        initiateData();


        for (Object item:titleArrayList) {
            Button button = new Button(context);
            button.setText(item.toString());
            buttonsList.add(button);
            ll.addView(button, lp);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isDeleteMode)
                    openEditListActivity(button.getText().toString());
                    else
                    {
                        titleArrayList.remove(button.getText());
                        ll.removeView(v);
                    }
                }
            });

        }
        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newListEditTextTitle.getText().toString().equals("")) {
                    titleArrayList.add(newListEditTextTitle.getText());
                    Button button = new Button(context);
                    button.setText(newListEditTextTitle.getText());
                    buttonsList.add(button);
                    ll.addView(button, lp);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openEditListActivity(button.getText().toString());
                        }
                    });
                    newListEditTextTitle.setText("");
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        menu.findItem(R.id.backIconBtn).setVisible(false);
        menu.findItem(R.id.menuIconBtn).setVisible(false);
        menu.findItem(R.id.checkIconBtn).setVisible(false);
        menu.findItem(R.id.clearAllIconBtn).setVisible(false);
        menu.findItem(R.id.editIconBtn).setVisible(false);
        if (!isDeleteMode){
            menu.findItem(R.id.hiddenDoneBtn).setVisible(false);
            menu.findItem(R.id.deleteBtn).setVisible(true);


        }else if(isDeleteMode){
            menu.findItem(R.id.hiddenDoneBtn).setVisible(true);
            menu.findItem(R.id.deleteBtn).setVisible(false);
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
                toolbar.setTitle(R.string.mainMenuTitle);
                invalidateOptionsMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initiateData()
    {
        titleArrayList = new ArrayList();
        buttonsList = new ArrayList();
        newListEditTextTitle = this.findViewById(R.id.newListEditTextTitle);
        addListBtn = this.findViewById(R.id.addListBtn);
        toolbar = this.findViewById(R.id.menuToolbar);
        setSupportActionBar(toolbar);
        ll = (LinearLayout)findViewById(R.id.mainMenuLinearLayout);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        Set<String> sourceSet = sharedPref.getStringSet(storageKey, new HashSet<>());
        titleArrayList = new ArrayList<String>(sourceSet);


    }

    public void openEditListActivity(String item)
    {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("title", item);
        startActivity(intent);
    }

}
