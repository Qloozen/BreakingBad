package com.example.breakingbad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BBApiTask.BBListener, CharacterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener{

    private final String TAG = MainActivity.class.getSimpleName();
    private final String INTENT_EXTRA_CHARACTER = "character";
    private final String SAVED_LIST = "BBList";
    private final String FILTER_ALL = "All";
    private final String FILTER_ALIVE = "Alive";
    private final String FILTER_DECEASED = "Deceased";
    private final String FILTER_PRESUMED_DEAD = "Presumed dead";
    private RecyclerView breakingBadRecyclerView;
    private ArrayList<BBCharacter> characterList;
    private BBAdapter breakingBadAdapter;
    private TextView statusIndication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate aangeroepen");

        statusIndication = findViewById(R.id.breaking_bad_status_indication);


        //Recyclerview implementeren met layout en adapter
        breakingBadRecyclerView = findViewById(R.id.breaking_bad_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            breakingBadRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            breakingBadRecyclerView.setLayoutManager(gridLayoutManager);
        }

        //Controle of er een adapter state beschikbaar is
        if (savedInstanceState != null) {
            Log.d(TAG, "Opgeslagen adapter teruggezet vanuit savedInstanceState");
            breakingBadAdapter = savedInstanceState.getParcelable(SAVED_LIST);
            //Horizontaal slaat hij soms een lege savedInstance op, defaultsetup wordt pas aangeroepen als de adapter != null
            if (breakingBadAdapter != null) {
                breakingBadRecyclerView.setAdapter(breakingBadAdapter);
                defaultSetup();
            } else {
                Log.d(TAG, "Lege adapter gevonden in de savedInstanceState");
                new BBApiTask(this).execute();
            }
        } else {
            Log.d(TAG, "Geen data gevonden in de savedInstanceState");
            new BBApiTask(this).execute();
        }

    }

    //Link tussen asynctask (onPostExecute) en adapter (data), verantwoordelijk voor het ophalen van gegevens en en nieuwe adapter aan te maken
    @Override
    public void setCharacters(List<BBCharacter> characterList) {
        Log.d(TAG, "setCharacters Lijstwaarde (van API): " + characterList.size() + " Items");

        this.characterList = new ArrayList<>(characterList);
        breakingBadAdapter = new BBAdapter(this.characterList, this, this);
        breakingBadRecyclerView.setAdapter(breakingBadAdapter);

        //defaultSetup haalt eerder geselecteerde filter settings op en past deze toe
        defaultSetup();
    }

    //Bij het afsluiten van de app wordt de listener uitgeschreven
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy aangeroepen");
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    //Bij het opstarten van de app laadt deze methode de sharedpreferences voor de status-filter
    private void defaultSetup() {
        Log.d(TAG, "defaultSetup aangeroepen (sharedPreferences)");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        //Filterselectie opvragen
        String option = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_def));
        statusIndication.setText("Filter:  " + option);
        this.checkStatus(option);
    }

    //Controlleert welke filter aanstaat en roept de getFilter method van de adapter aan met de meegegeven constraint
    private void checkStatus(String option) {
        Log.d(TAG, "checkStatus aangeroepen (filter)");
        if (option.equals(FILTER_ALL)) {
            breakingBadAdapter.setFilterStatus(FILTER_ALL);
            breakingBadAdapter.getFilter().filter(FILTER_ALL);
        } else if (option.equals(FILTER_ALIVE)) {
            breakingBadAdapter.setFilterStatus(FILTER_ALIVE);
            breakingBadAdapter.getFilter().filter(FILTER_ALIVE);
        } else if (option.equals(FILTER_DECEASED)) {
            breakingBadAdapter.setFilterStatus(FILTER_DECEASED);
            breakingBadAdapter.getFilter().filter(FILTER_DECEASED);
        } else if (option.equals(FILTER_PRESUMED_DEAD)) {
            breakingBadAdapter.setFilterStatus(FILTER_PRESUMED_DEAD);
            breakingBadAdapter.getFilter().filter(FILTER_PRESUMED_DEAD);
        }
    }

    //click implementatie recyclerview
    @Override
    public void onCharacterClick(View view, int index) {
        BBCharacter bbCharacter = characterList.get(index);

        int viewId = view.getId();
        if (viewId == R.id.breaking_bad_list_item_image) {
            Log.d(TAG, "plaatje aangeklikt");
            Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
            intent.putExtra(INTENT_EXTRA_CHARACTER, bbCharacter);
            startActivity(intent);
        } else {
            Log.d(TAG, "character aangeklikt");
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(INTENT_EXTRA_CHARACTER, bbCharacter);
            startActivity(intent);
        }

    }

    // Verantwoordelijk voor de toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu aangeroepen");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bb_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                breakingBadAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    //Verantwoordelijk voor de status-filter knop
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected aangeroepen");
        int selectedItem = item.getItemId();

        if (selectedItem == R.id.action_filter) {
            Log.d(TAG, "FilterButton ingedrukt");
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d(TAG, "onSaveInstanceState aangeroepen(adapter saved)");
        outState.putParcelable(SAVED_LIST, breakingBadAdapter);
        super.onSaveInstanceState(outState);

    }

    // Verantwoordelijk voor de sharedPreferences op te slaan tijdens het gebruikt van de app
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "onSharedPreferenceChanged aangeroepen");
        String option = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_def));
        Log.d(TAG, "onSharedPreferenceChanged changed filter to: " + option);
        statusIndication.setText("Filter:  " + option);
        this.checkStatus(option);
    }

}