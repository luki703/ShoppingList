package com.example.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

        titleArrayList = new ArrayList();
        buttonsList = new ArrayList();
        newListEditTextTitle = this.findViewById(R.id.newListEditTextTitle);
        addListBtn = this.findViewById(R.id.addListBtn);
        ll = (LinearLayout)findViewById(R.id.mainMenuLinearLayout);
        lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        Set<String> sourceSet = sharedPref.getStringSet(storageKey, new HashSet<>());
        titleArrayList = new ArrayList<String>(sourceSet);
        for (Object item:titleArrayList) {
            Button button = new Button(context);
            button.setText(item.toString());
            buttonsList.add(button);
            ll.addView(button, lp);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openEditListActivity(button.getText().toString());
                }
            });

        }
        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

    }
    public void openEditListActivity(String item)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("title", item);
        startActivity(intent);
    }

}
