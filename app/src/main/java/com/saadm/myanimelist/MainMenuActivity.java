package com.saadm.myanimelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.saadm.myanimelist.service.adapters.RecyclerAdapter;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {

    RecyclerView mRecyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        mRecyView = findViewById(R.id.recycleMainListOptions);


        String[] itemList = getResources().getStringArray(R.array.mainMenu_ListItems);
        ArrayList<String> listItems = new ArrayList<String>();
        for(String item : itemList){
            listItems.add(item);
        }

        RecyclerAdapter recyAdapter = new RecyclerAdapter(this, listItems);
        mRecyView.setAdapter(recyAdapter);
        mRecyView.setLayoutManager(new LinearLayoutManager(this));
        mRecyView.addItemDecoration(new DividerItemDecoration(mRecyView.getContext(), DividerItemDecoration.VERTICAL));
    }
}