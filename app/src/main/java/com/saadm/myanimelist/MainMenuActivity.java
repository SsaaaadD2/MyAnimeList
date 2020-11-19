package com.saadm.myanimelist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.saadm.myanimelist.service.adapters.RecyclerAdapter;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity implements RecyclerAdapter.onStringClickListener {

    RecyclerView mRecyView;
    ImageButton mImageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        mRecyView = findViewById(R.id.recycleMainListOptions);
        mImageButton = findViewById(R.id.ibtnLogout);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });


        String[] itemList = getResources().getStringArray(R.array.mainMenu_ListItems);
        ArrayList<String> listItems = new ArrayList<String>();
        for(String item : itemList){
            listItems.add(item);
        }



        RecyclerAdapter recyAdapter = new RecyclerAdapter(this, listItems, this);
        mRecyView.setAdapter(recyAdapter);
        mRecyView.setLayoutManager(new LinearLayoutManager(this));
        mRecyView.addItemDecoration(new DividerItemDecoration(mRecyView.getContext(), DividerItemDecoration.VERTICAL));
    }

    //Confirmation dialog to logout
    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("Cancel", null);
        AlertDialog confirm = builder.create();

        //Set colour of the buttons
        confirm.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                confirm.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#2E51A2"));
                confirm.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#2E51A2"));
            }
        });
        confirm.show();
    }

    private void logout() {
        SharedPreferences.Editor prefsEditor = getSharedPreferences(getString(R.string.PREFS_KEY), MODE_PRIVATE).edit();
        prefsEditor.remove("accessToken");
        prefsEditor.remove("expiredTime");
        prefsEditor.apply();
        Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onItemClick(String item) {
        if(item.equals("Search Anime")){
            return;
        } else{
            Intent intent= new Intent(MainMenuActivity.this, AnimeListActivity.class);
            intent.putExtra("listStatus", item.toLowerCase().replace(" ", "_"));
            startActivity(intent);
        }
    }
}