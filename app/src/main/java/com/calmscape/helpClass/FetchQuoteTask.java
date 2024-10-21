package com.calmscape.helpClass;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.calmscape.activities.FinalActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 *  Class FetchQuoteTask helps us fetch and use quotes from the Quotes API.
 */
public class FetchQuoteTask extends AsyncTask<Void, Void, String> {

    private Context context;  //provides context about where is the method used
    private String urlString; //an URL for Quotes API
    private String apiKey;  //an API key, without it we can't access the API

    /**
     * Just a casual constructor.
     *
     * @param context provides context about where is the method used
     * @param urlString an URL for Quotes API
     * @param apiKey an API key, without it we can't access the API
     */
    public FetchQuoteTask(Context context, String urlString, String apiKey) {
        this.context = context;
        this.urlString = urlString;
        this.apiKey = apiKey;
    }

    /**
     * {@inheritDoc}
     * This method also connects us to the remote API and pulls an inspirational quote from there.
     *
     * @param voids The parameters of the task.
     * @return return a quote that is parsed into a string
     */
    @Override
    protected String doInBackground(Void... voids) {

        try {

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  //connecting to the API
            urlConnection.setRequestProperty("X-Api-Key", apiKey);  //putting the key into the request header
            urlConnection.setRequestProperty("Accept", "application/json");

            try {
                InputStream in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                if (scanner.hasNext()) {  //scanner selecting the quote
                    String jsonResponse = scanner.next();  //since we're fetching quotes from the json file we have to parse them
                    return parseQuote(jsonResponse);  //returning a parsed quote

                } else {

                    return null;  //return null if there are no quotes

                }

            } finally {
                urlConnection.disconnect();
            }

        } catch (IOException e) {
            Log.e("NetworkTask", "Error while fetching data: " + e.getMessage());
            return null;
        }
    }

    /**
     * This method is used for parsing quotes we fetch from the json file.
     *
     * @param jsonResponse this is the quote and it's author in their natural format
     * @return returns a parsed quote
     */
    private String parseQuote(String jsonResponse) {

        try {

            JSONArray jsonArray = new JSONArray(jsonResponse);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String quote = jsonObject.getString("quote");  //getting the "quote" part of the json object
            String author = jsonObject.getString("author"); //getting the "author" part of the json object
            return quote + " - " + author;  //formatting the string in the way we want to

        } catch (Exception e) {

            Log.e("NetworkTask", "Error while parsing JSON: " + e.getMessage());
            return null;

        }
    }

    /**
     * {@inheritDoc}
     * This method also starts the EmojiActivity since it's the last this this class does after getting
     * the quote and parsing it.
     *
     * @param result The result of the operation computed by {@link #doInBackground}.
     */
    @Override
    protected void onPostExecute(String result) {

        if (result != null) {

            Intent intent = new Intent(context, FinalActivity.class);
            intent.putExtra("citat", result);
            context.startActivity(intent);

        } else {
            // Obrada greške ako podaci nisu mogli biti dohvaćeni
        }
    }
}
