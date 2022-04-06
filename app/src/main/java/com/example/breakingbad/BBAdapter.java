package com.example.breakingbad;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BBAdapter extends RecyclerView.Adapter<BBAdapter.BBViewHolder> implements Filterable, Parcelable {

    private static final String TAG = BBAdapter.class.getSimpleName();
    private final CharacterOnClickHandler mOnClickHandler;
    private final String FILTER_ALL = "All";
    private final String FILTER_ALIVE = "Alive";
    private final String FILTER_DECEASED = "Deceased";
    private final String FILTER_PRESUMED_DEAD = "Presumed dead";
    private String filterStatus;
    private ArrayList<BBCharacter> characterListFull;
    private ArrayList<BBCharacter> characterList;
    private Context context;

    public BBAdapter(ArrayList<BBCharacter> characterList, CharacterOnClickHandler onClickHandler, Context context) {
        Log.d(TAG, "Constructor aangeroepen");
        this.characterList = characterList;
        this.characterListFull = new ArrayList<>(characterList);
        this.mOnClickHandler = onClickHandler;
        this.context = context;
    }

    public void setFilterStatus(String filterStatus) {
        this.filterStatus = filterStatus;
        this.getFilter().filter(filterStatus);
    }

    //Adapter methodes
    @NonNull
    @Override
    public BBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder aangeroepen");

        Context context = parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(context);

        View characterListItem = inflator.inflate(R.layout.character_list_item, parent, false);
        BBAdapter.BBViewHolder viewHolder = new BBViewHolder(characterListItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BBViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder aangeroepen");
        BBCharacter character = characterList.get(position);

        holder.mTitle.setText(character.getName());
        Picasso.get().load(character.getImgUrl()).into(holder.mImage);
        holder.mStatus.setText(character.getStatus());
        holder.mNickname.setText(character.getNickname());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemcount aangeroepen: " + characterList.size());
        return characterList.size();
    }

    public class BBViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImage;
        public TextView mTitle;
        public TextView mNickname;
        public TextView mStatus;

        public BBViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder constructor aangeroepen");
            mImage = itemView.findViewById(R.id.breaking_bad_list_item_image);
            mTitle = itemView.findViewById(R.id.breaking_bad_list_item_title);
            mNickname = itemView.findViewById(R.id.breaking_bad_list_item_nickname);
            mStatus = itemView.findViewById(R.id.breaking_bad_list_item_status);
            mImage.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick aangeroepen");
            int clickedPosition = getAdapterPosition();
            mOnClickHandler.onCharacterClick(view, clickedPosition);
        }
    }

    //Filter methodes
    @Override
    public Filter getFilter() {
        Log.d(TAG, "getFilter aangeroepen");
        return SearchFilter;
    }

    private Filter SearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "Zoekopdracht begonnen met: " + constraint.toString());
            ArrayList<BBCharacter> filteredList = new ArrayList<>();

            //Backspace in searchView
            if (constraint == null || constraint.length() == 0) {
                //Laad alles indien de filter op alles stond
                if (filterStatus.equals(FILTER_ALL)) {
                    filteredList.addAll(characterListFull);
                }
                //laad alles van de gezette filter
                for (BBCharacter x : characterListFull) {
                    if (x.getStatus().equals(filterStatus)) {
                        filteredList.add(x);
                    }
                }
             //Wanneer de filter wordt aangepast
            } else if (constraint.toString().equals(FILTER_ALL)) {
                filteredList.addAll(characterListFull);
            } else if (constraint.toString().equals(FILTER_ALIVE)) {
                for (BBCharacter x : characterListFull) {
                    if (x.getStatus().equals(FILTER_ALIVE)) {
                        filteredList.add(x);
                    }
                }
            } else if (constraint.toString().equals(FILTER_DECEASED)) {
                for (BBCharacter x : characterListFull) {
                    if (x.getStatus().equals(FILTER_DECEASED)) {
                        filteredList.add(x);
                    }
                }
            } else if (constraint.toString().equals(FILTER_PRESUMED_DEAD)) {
                for (BBCharacter x : characterListFull) {
                    if (x.getStatus().equals(FILTER_PRESUMED_DEAD)) {
                        filteredList.add(x);
                    }
                }
             //Wanneer een naam ingetypt wordt
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BBCharacter x : characterList) {
                    if (x.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(x);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(TAG, "Zoekopdracht voltooid");
            characterList.clear();
            characterList.addAll((ArrayList<BBCharacter>)results.values);
            notifyDataSetChanged();
            Toast.makeText(BBAdapter.this.context, "Items loaded: " + characterList.size(), Toast.LENGTH_SHORT).show();
        }
    };

    //Parce methodes
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

}
