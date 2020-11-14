package com.saadm.myanimelist.service.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saadm.myanimelist.R;
import com.saadm.myanimelist.model.AnimeItemCard;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<String> mListItems;
    ArrayList<AnimeItemCard> mAnimeItems;
    Context mContext;

    public RecyclerAdapter(Context ct, ArrayList<String> listNames){
        mContext = ct;
        mListItems = listNames;
    }

    //The type string is just to differentiate the signature from the constructor above
    public RecyclerAdapter(Context ct,ArrayList<AnimeItemCard> animeItems, String type){
        mContext = ct;
        mAnimeItems = animeItems;
    }

    @Override
    public int getItemViewType(int position) {
        if(mAnimeItems.isEmpty() && !mListItems.isEmpty()){
            return 0;
        }
        else if(mListItems.isEmpty() && ! mAnimeItems.isEmpty()){
            return 1;
        }
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch(viewType){
            default:
            case 0:
                View view = inflater.inflate(R.layout.mainmenu_row, parent, false);
                return new ViewHolder(view, 0);
            case 1:
                view = inflater.inflate(R.layout.animeitem_card, parent, false);
                return new ViewHolder(view, 1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItemText.setText(mListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //For main menu
        TextView mItemText;

        //For anime item cards
        TextView mAnimeTitle;
        TextView mAnimeDescription;
        ImageView mImg;
        TextView mAnimeGenres;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == 0) {
                mItemText = itemView.findViewById(R.id.txt_MainMenuListItem);
            } else {
                mAnimeTitle = itemView.findViewById(R.id.txt_Title);
                mAnimeDescription = itemView.findViewById(R.id.txt_Description);
                mAnimeGenres = itemView.findViewById(R.id.txt_Genres);
                mImg = itemView.findViewById(R.id.img_AnimeImage);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
