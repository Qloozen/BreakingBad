package com.example.breakingbad;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.breakingbad.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BBApiTask extends AsyncTask<String, Void, List<BBCharacter>> {

    private final String TAG = BBApiTask.class.getSimpleName();
    private final String CHARACTERS_URL = "https://breakingbadapi.com/api/characters";
    private final String QUOTES_URL = "https://breakingbadapi.com/api/quotes";
    private final String CHARACTER_NAME = "name";
    private final String CHARACTER_NICKNAME = "nickname";
    private final String CHARACTER_IMG = "img";
    private final String CHARACTER_STATUS = "status";
    private final String CHARACTER_BIRTHDAY = "birthday";
    private final String CHARACTER_OCCUPATION = "occupation";
    private final String CHARACTER_APPEARANCE = "appearance";
    private final String CHARACTER_AUTHOR = "author";
    private final String CHARACTER_QUOTE = "quote";

    private BBListener listener;

    public BBApiTask(BBListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<BBCharacter> doInBackground(String... params) {
        Log.d(TAG, "doInbackground aangeroepen");

        String stringCharactersUrl = CHARACTERS_URL;
        String stringQuotesUrl = QUOTES_URL;

        URL charactersUrl = null;
        URL quotesUrl = null;
        ArrayList<BBCharacter> resultList = new ArrayList<>();
        try {
            charactersUrl = NetworkUtils.buildUrl(stringCharactersUrl);
            quotesUrl = NetworkUtils.buildUrl(stringQuotesUrl);

            String charactersJson = NetworkUtils.getResponseFromHttpUrl(charactersUrl);
            String quotesJson = NetworkUtils.getResponseFromHttpUrl(quotesUrl);

            resultList = this.convertJsonToArrayList(charactersJson, quotesJson);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return resultList;
    }

    @Override
    protected void onPostExecute(List<BBCharacter> bbCharacters) {
        super.onPostExecute(bbCharacters);
        Log.d(TAG, "onPostExecute Lijstwaarde: " + bbCharacters.size() + " Items");

        listener.setCharacters(bbCharacters);
    }

    private ArrayList<BBCharacter> convertJsonToArrayList(String charactersjson, String quotesJson) {
        Log.d(TAG, "Omzetten Json naar Character");

        ArrayList<BBCharacter> characterList = new ArrayList<>();

        try {
            JSONArray JsonCharacterList = new JSONArray(charactersjson);
            JSONArray jsonQuotesList = new JSONArray(quotesJson);

            //Loop door alle character objecten
            for (int i = 0; i < JsonCharacterList.length(); i++) {
                JSONObject JsonCharacter = JsonCharacterList.getJSONObject(i);

                String name = JsonCharacter.getString(CHARACTER_NAME);
                String nickname = JsonCharacter.getString(CHARACTER_NICKNAME);
                String imgUrl = JsonCharacter.getString(CHARACTER_IMG);
                String status = JsonCharacter.getString(CHARACTER_STATUS);
                String birthday = JsonCharacter.getString(CHARACTER_BIRTHDAY);
                JSONArray jsonOccupationList = JsonCharacter.getJSONArray(CHARACTER_OCCUPATION);
                String[] occupationList = new String[jsonOccupationList.length()];
                for (int j = 0; j < jsonOccupationList.length(); j++) {
                    occupationList[j] = jsonOccupationList.getString(j);
                }
                JSONArray jsonAppearanceList = JsonCharacter.getJSONArray(CHARACTER_APPEARANCE);
                int[] appearanceList = new int[jsonAppearanceList.length()];
                for (int k = 0; k < jsonAppearanceList.length(); k++) {
                    appearanceList[k] = jsonAppearanceList.getInt(k);
                }

                //loop door alle quotes
                ArrayList<String> quotes = new ArrayList<>();
                for (int t = 0; t < jsonQuotesList.length(); t++) {
                    JSONObject jsonQuote = jsonQuotesList.getJSONObject(t);
                    String author = jsonQuote.getString(CHARACTER_AUTHOR);
                    String quote = jsonQuote.getString(CHARACTER_QUOTE);
                    if (author.equals(name)) {
                        quotes.add(quote);
                    }
                }
                characterList.add(new BBCharacter(name, nickname, imgUrl, status, birthday, occupationList, appearanceList, quotes));
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        return characterList;
    }

    public interface BBListener {
        public void setCharacters(List<BBCharacter> characterList);
    }

}
