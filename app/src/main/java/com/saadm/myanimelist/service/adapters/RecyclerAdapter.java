package com.saadm.myanimelist.service.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saadm.myanimelist.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<String> mListItems;
    Context mContext;

    public RecyclerAdapter(Context ct,ArrayList<String> listNames){
        mContext = ct;
        mListItems = listNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mainmenu_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItemText.setText(mListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mItemText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("onLoad", "itemView " + itemView.toString());
            mItemText = itemView.findViewById(R.id.txt_MainMenuListItem);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
