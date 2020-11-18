package com.saadm.myanimelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
                logout();
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
    Intent intent;
    switch(item){
        default:
        case "Plan To Watch":
            intent = new Intent(MainMenuActivity.this, PlanToWatchActivity.class);
            break;
        case "Watching":
            return;
        case "Completed":
            return;
    }
        startActivity(intent);
    }
}