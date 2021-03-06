package com.saadm.myanimelist.service.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saadm.myanimelist.R;
import com.saadm.myanimelist.model.AnimeItemCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<String> mListItems;
    ArrayList<AnimeItemCard> mAnimeItems;

    //Used for filtering and searching
    ArrayList<AnimeItemCard> mAnimeItemsCopy;

    Context mContext;
    onStringClickListener mStringListener;
    onCardClickListener mCardListener;

    public interface onStringClickListener{
        void onItemClick(String item);
    }

    public interface onCardClickListener{
        void onItemClick(AnimeItemCard card);
    }

    public RecyclerAdapter(Context ct, ArrayList<String> listNames, onStringClickListener itemListener){
        mContext = ct;
        mListItems = listNames;
        mStringListener = itemListener;
    }

    //The type string is just to differentiate the signature from the constructor above
    public RecyclerAdapter(Context ct,ArrayList<AnimeItemCard> animeItems, onCardClickListener itemListener){
        mContext = ct;
        mAnimeItems = animeItems;
        mAnimeItemsCopy = new ArrayList<AnimeItemCard>();
        mAnimeItemsCopy.addAll(mAnimeItems);
        mCardListener = itemListener;
    }

    @Override
    public int getItemViewType(int position) {
        if(mAnimeItems == null && mListItems!= null){
            return 0;
        }
        else{
            return 1;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch(viewType){
            default:
            case 0:
                View view = inflater.inflate(R.layout.mainmenu_row, parent, false);
                return new ViewHolder(view, 0, mStringListener);
            case 1:
                view = inflater.inflate(R.layout.animeitem_card, parent, false);
                return new ViewHolder(view, 1, mCardListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int state = getItemViewType(position);
        if(state == 0){
            holder.mItemText.setText(mListItems.get(position));
        } else{
            holder.mItem = mAnimeItems.get(position);
            holder.mAnimeTitle.setText(mAnimeItems.get(position).getTitle());
            holder.mAnimeGenres.setText(mAnimeItems.get(position).getGenres());
            Picasso.get().load(mAnimeItems.get(position).getImageUrl()).into(holder.mImg);
        }

    }

    @Override
    public int getItemCount() {
        return (mListItems != null ? mListItems.size() : mAnimeItems.size());
    }

    public void filter(String chars){
        mAnimeItems.clear();
        if(chars.isEmpty()){
            mAnimeItems.addAll(mAnimeItemsCopy);
        } else{
            for(AnimeItemCard card: mAnimeItemsCopy){
                if(card.getTitle().toLowerCase().contains(chars.toLowerCase())){
                    mAnimeItems.add(card);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        //For main menu
        TextView mItemText;

        //For anime item cards
        TextView mAnimeTitle;
        TextView mAnimeDescription;
        ImageView mImg;
        TextView mAnimeGenres;

        //To pass in to the clicklistener
        AnimeItemCard mItem;

        onStringClickListener mStringListener;
        onCardClickListener mCardListener;

        public ViewHolder(@NonNull View itemView, int viewType, onStringClickListener myClickListener) {
            super(itemView);
            mItemText = itemView.findViewById(R.id.txt_MainMenuListItem);
            mStringListener = myClickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mStringListener.onItemClick(mItemText.getText().toString());
                }
            });
        }

        public ViewHolder(@NonNull View itemView, int viewType, onCardClickListener myClickListener) {
            super(itemView);
            mAnimeTitle = itemView.findViewById(R.id.txt_Title);
            mAnimeDescription = itemView.findViewById(R.id.txt_Description);
            mAnimeGenres = itemView.findViewById(R.id.txt_Genres);
            mImg = itemView.findViewById(R.id.img_AnimeImage);
            mCardListener = myClickListener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCardListener.onItemClick(mItem);
                }
            });
            //itemView.setOnClickListener(this);
        }


//        @Override
//        public void onClick(View v) {
//            if(mStringListener != null){
//
//            } else{
//                AnimeItemCard itemCard = new AnimeItemCard(
//                        mAnimeTitle.getText().toString(),
//                        mAnimeDescription.getText().toString(),
//                        mAnimeGenres.getText().toString(),
//                        mImg.getDrawable());
//                mCardListener.onItemClick(itemCard);
//            }
//        }
    }
}
