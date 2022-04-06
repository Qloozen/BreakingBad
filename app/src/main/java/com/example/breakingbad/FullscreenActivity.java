package com.example.breakingbad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullscreenActivity extends AppCompatActivity {

    private final String INTENT_EXTRA_CHARACTER = "character";
    private final String TAG = DetailActivity.class.getSimpleName();
    private BBCharacter character;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate aangeroepen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

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

        mImage = findViewById(R.id.breaking_bad_fullscreenImage);

        Picasso.get().load(character.getImgUrl()).into(mImage);

        mImage.setScaleType(ImageView.ScaleType.FIT_XY);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Plaatje aangeklikt");
                Intent intent = new Intent(FullscreenActivity.this, DetailActivity.class);
                intent.putExtra(INTENT_EXTRA_CHARACTER, character);
                startActivity(intent);
            }
        });
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