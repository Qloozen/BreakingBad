package com.example.breakingbad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailActivity extends AppCompatActivity {
    private static final String INTENT_EXTRA_CHARACTER = "character";
    private static final String TAG = DetailActivity.class.getSimpleName();
    private BBCharacter character;
    private TextView mTitle;
    private ImageView mImage;
    private TextView mNickname;
    private TextView mStatus;
    private TextView mBirthday;
    private TextView mOccupation;
    private TextView mAppearance;
    private TextView mQuotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate methode aangeroepen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //Back button
        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Intent info koppelen
        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_EXTRA_CHARACTER)) {
            character = (BBCharacter) intent.getSerializableExtra(INTENT_EXTRA_CHARACTER);

        }
        mTitle = findViewById(R.id.breaking_bad_details_title);
        mImage = findViewById(R.id.breaking_bad_details_image);
        mNickname = findViewById(R.id.breaking_bad_details_nickname);
        mStatus = findViewById(R.id.breaking_bad_details_status);
        mBirthday = findViewById(R.id.breaking_bad_details_birthday);
        mOccupation = findViewById(R.id.breaking_bad_details_occupation);
        mAppearance = findViewById(R.id.breaking_bad_details_appearance);
        mQuotes = findViewById(R.id.breaking_bad_details_quotes);


        mTitle.setText(character.getName());
        Picasso.get().load(character.getImgUrl()).resize(800, 1000).into(mImage);

        mNickname.setText(character.getNickname());
        mStatus.append(character.getStatus());
        mBirthday.append(character.getBirthday());
        String[] occupationList = character.getOccupation();
        String occupation = String.join(", ", occupationList);
        mOccupation.append(occupation);
        int[] appearanceList = character.getAppearance();
        String appearance = Arrays.toString(appearanceList).replaceAll("\\[|\\]|\\s", "");
        mAppearance.append(appearance);
        mQuotes.append("\n \n");
        ArrayList<String> list = character.getQuotes();
        for (int i = 0; i < character.getQuotes().size(); i++) {
            mQuotes.append((i + 1) + ".  " + character.getQuotes().get(i) + "\n \n");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "Terugknop ingedrukt");
        int selectedItem = item.getItemId();
        if (selectedItem == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}