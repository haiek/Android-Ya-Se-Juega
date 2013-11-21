package com.yasejuega.yasejuega;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class GetRumors extends AsyncTask<String, Void, String> {
	
	//Create a CustomAdapter object.
	CustomAdapter adapter;
	
	public GetRumors(CustomAdapter fragmentAdapter) {
		//Assign the value from the parameter (now working with the fragment adapter).
		adapter = fragmentAdapter;
		
	    //Show loading textfield.
		Fragment2.loading.setVisibility(View.VISIBLE);
		//Fragment3 is done in fragment3.java because here triggers error
		//Fragment3.loadingTwitter.setVisibility(View.VISIBLE);
	}

	@Override
	protected String doInBackground(String... params) {
		
		String urlString = params[0];
		
		HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            //json.append(buff, 0, in.read(buff));

            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }

            
        } catch (IOException e) {
            Log.e("myTag", "Error connecting", e);
            //throw new IOException("Error connecting", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        //Log.i("JSONResponceRumors", json.toString());
        return json.toString();
	}

	@Override
	protected void onPostExecute(String json) {
		super.onPostExecute(json);
		try {
			
	        //Hide loading textfield.
	    	Fragment2.loading.setVisibility(View.GONE);
	    	Fragment3.loadingTwitter.setVisibility(View.GONE);
	    	
			putRumor(json);
			//adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    //Put the category in the ListView
    void putRumor(String json) throws JSONException {
        
    	//Since the PHP is returning a JSON object, I use JSONObject to get it.
    	JSONObject jObject = new JSONObject(json);
    	//See what hat i'm getting from the DB through JSON.
    	Log.i("JsonFetchRumors", jObject.toString());
    	//Add rumors to the adapter.
    	adapter.addItems(jObject);
    	adapter.notifyDataSetChanged();
    }
    void resetAdapter() {
    	adapter.notifyDataSetChanged();
    }
	
}