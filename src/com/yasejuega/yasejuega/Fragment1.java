package com.yasejuega.yasejuega;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Fragment1 extends Fragment {
	private ListView lv1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	//It's link to the fragment1.xml file
        View root = inflater.inflate(R.layout.fragment1, container, false);
        
        //to use findViewById you need to use root before to let the code know you are in this fragment.
        lv1 =(ListView) root.findViewById(R.id.listView1);
        
        //CALLING THE JSON METHOD
        setUpCategories();
        
        return root;

    }
    
    //GET CATEGORIES FORM DB IN THE BACKGORUND AND PRINT THEM IN THE FRAGMENT
    protected class GetCategories extends AsyncTask<String, Void, String> {

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
	            json.append(buff, 0, in.read(buff));
	            
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
	        Log.i("JSONResponseCategories", json.toString());
	        return json.toString();
		}

		@Override
		protected void onPostExecute(String json) {
			super.onPostExecute(json);
			try {
				putCategory(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
    
    private void setUpCategories() {
    	new GetCategories().execute("http://yasejuega.com/Android/ajaxphp.php?key=panel");
    }
    
    //Put the category in the ListView
    void putCategory(String json) throws JSONException {
    	//Since the php is returning a json array, I use JSONArray to get it.
    	JSONArray jArray = new JSONArray(json);
    	
        //Declare an array with the elements that will be shown in the screen.
        String[] items = new String[10];
        
    	//Loop the jArray
    	for (int i=0; i < jArray.length(); i++) {
    	    try {
    	    	//Put the category in the items array
    	    	items[i] = (String) jArray.get(i);
    	    } catch (JSONException e) {
    	        // Oops
    	    }
    	}
        //Insert the array in the listWiev.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list, items); 
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
            	//Save the category name in a string.
            	String category = lv1.getItemAtPosition(posicion).toString();
            	//Checking the category selected.
            	Log.i("category", category);
            	((Fragment2) ((MainActivity) getActivity()).getActiveFragment(1)).callRumorsFromCategory(category);
            	//mViewPager is created in MainActivity and it has to be static in order to access it in this file;
            	MainActivity.mViewPager.setCurrentItem(1);
            }
        });
    }
    
}