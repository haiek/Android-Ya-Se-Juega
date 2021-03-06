
package com.yasejuega.yasejuega;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;


public class Fragment2 extends Fragment{
	private ListView lv2;
    public static CustomAdapter adapter;
    public AutoCompleteTextView textViewSearch;
    private ArrayList<Custom> fetch = new ArrayList<Custom>();
    //Declare this var for the load more
    private int pos;
    public boolean setAdapterYes;
    
    //For loading while fetching rumors
    public static TextView loading;
    
    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp;
    
    //Declaring the var for using it when loading more rumors when choosing a category.
    public String selection;
    
    //Declare an array with the elements that will be shown in textViewSearch.
    String[] items;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	//It's link to the fragment2.xml file
        View root = inflater.inflate(R.layout.fragment2, container, false);
        
        //Google Analytics
    	Tracker v3EasyTracker = EasyTracker.getInstance(getActivity());

    	// Set the screen name on the tracker so that it is used in all hits sent from this screen.
    	v3EasyTracker.set(Fields.SCREEN_NAME, "Web");

    	// Send a screenview.
    	v3EasyTracker.send(MapBuilder
    	  .createAppView()
    	  .build()
    	);
    	//Finish Google Analytics
    	
    	//Show loading text while fetching data
    	loading = (TextView) root.findViewById(R.id.loading);
    	
        //Assign the value 9, so will bring the the 10 to 19 rumors
        pos = 9;
        selection = "Inicio";
        //to use findViewById you need to use root before to let the code know you are in this fragment.
        lv2 =(ListView) root.findViewById(R.id.listView2);
        //Set the scroll listener to the list view for scroll and get more.
        lv2.setOnScrollListener(new EndlessScrollListener());
        
        adapter = new CustomAdapter(getActivity(), R.id.listView2, fetch, "fragment2");

        lv2.setAdapter(adapter);
        
        //CALLING THE JSON METHOD
        setUpRumors("Inicio", 0);
        
        //First scroll is gonna be down
        mIsScrollingUp = true;
        
        //SEARCH
        // Get a reference to the AutoCompleteTextView in the layout
        textViewSearch = (AutoCompleteTextView) root.findViewById(R.id.autocomplete_country);
        // Get the string array and set the adapter to the suggestions
        new GetCategories("getCategories").execute("http://yasejuega.com/Android/ajaxphp.php?key=allCategorias");
        
        return root;

    }

    
    //GET RUMORS FORM DB IN THE BACKGORUND AND PRINT THEM IN THE FRAGMENT USING CUSTOMADAPTER AND CUSTOM CLASS
    public void setUpRumors(String categoria, int pos) {
    	//Create a GetRumor object and send the CustomAdapter created in onCreateView as a parameter.
    	GetRumors fromDB = new GetRumors(adapter);
    	
    	//Send the URL where to fetch the rumors.
    	fromDB.execute("http://yasejuega.com/Android/loadRumors.php?categoria="+categoria+"&pos="+pos);
    	Log.i("***URL", "http://yasejuega.com/Android/loadRumors.php?categoria="+categoria+"&pos="+pos);
    }
    
    //Clearing the rumors list and loading for a new category (SEBASTIAN)
    public void loadCategory(String category) {
    	fetch  = new ArrayList<Custom>();
    	adapter = new CustomAdapter(getActivity(), R.id.listView2, fetch, "fragment2");
    	lv2.setAdapter(adapter);
    	lv2.setOnScrollListener(new EndlessScrollListener());
    	setUpRumors(category, 0);
    }
    
    //Scroll to get more rumors.
    public class EndlessScrollListener implements OnScrollListener {
    	private int pos;
    	
        private int visibleThreshold = 9;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }
        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    pos += 9;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next 9 rumors.
            	setUpRumors(selection, pos);
            	Log.i("***pos", Integer.toString(pos));
                loading = true;
            }
        }

    	@Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
    		AnimationSet set = new AnimationSet(true);
    		Animation animation = new AlphaAnimation(0.0f, 1.0f);
    		if (view.getId() == lv2.getId()) {
    	        final int currentFirstVisibleItem = lv2.getFirstVisiblePosition();

    	        if (currentFirstVisibleItem > mLastFirstVisibleItem) {
    	            if(mIsScrollingUp){
	    	            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
	    	            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
	    	            Animation.RELATIVE_TO_SELF, 1.0f,
	    	            Animation.RELATIVE_TO_SELF, 0.0f);
	    	            animation.setDuration(150);
	        	        set.addAnimation(animation);
	        	        textViewSearch.startAnimation(set);
    	           }
    	            
    	           mIsScrollingUp = false;
    	           Handler handler = new Handler();
    	           handler.postDelayed(new Runnable() {
    	               @Override
    	               public void run() {
    	            	   textViewSearch.setVisibility(View.GONE);
    	               }
    	           }, 100);
    	            
    	        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
    	            
    	            if(!mIsScrollingUp) {
	    	            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
	    	            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
	    	            Animation.RELATIVE_TO_SELF, -1.0f,
	    	            Animation.RELATIVE_TO_SELF, 0.0f);
	        	        animation.setDuration(600);
	        	        set.addAnimation(animation);
	        	        textViewSearch.startAnimation(set);
    	            }
        	        
        	        textViewSearch.setVisibility(View.VISIBLE);
        	        mIsScrollingUp = true;
    	        }
    	        
    	        mLastFirstVisibleItem = currentFirstVisibleItem;
    	    }
        }
    }
    
    protected class GetCategories extends AsyncTask<String, Void, String> {
    	
    	private String action;
    	
    	public GetCategories (String todo) {
    		action = todo;
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
	        return json.toString();
		}

		@Override
		protected void onPostExecute(String json) {
			super.onPostExecute(json);
			try {
				if(action.equals("getCategories")) putCategoriesInArray(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	
    }
    
    void putCategoriesInArray(String json) throws JSONException {
    	//Since the php is returning a json array, I use JSONArray to get it.
    	JSONArray jArray = new JSONArray(json);
    	items = new String[jArray.length()];
    	//Loop the jArray
    	for (int i=0; i < jArray.length(); i++) {
    	    try {
    	    	//Put the category in the items array
    	    	items[i] = jArray.get(i).toString();
    	    } catch (JSONException e) {
    	        // Oops
    	    }
    	}
    	
    	if(textViewSearch.requestFocus()) {
    		textViewSearch.setText("");
    	} else {
    		textViewSearch.setText("Buscar...");
    	}
    	
    	// Create the adapter and set it to the AutoCompleteTextView 
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        textViewSearch.setAdapter(adapter);
        //Declare OnItemClickListener for suggestions.
        textViewSearch.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
            	//Save the category name in a string.
            	String category = (String)parent.getItemAtPosition(posicion);
            	callRumorsFromCategory(category);
            }
        });
    }
    
    //Add %20 to the category pressed
    public void callRumorsFromCategory(String pressed) {
    	selection = "";
    	String[] result = pressed.split(" ");
        for(String s : result){
        	String space = s+"%20";
        	Log.i("***sapce", s);
            selection += space;
        }
    	//Checking the category selected.
    	Log.i("category", selection);
    	loadCategory(selection);
    	//Add category name to search input.
    	textViewSearch.setText(pressed+" ");
    }
    
    //Order rumors by likes
    public void orderByLike() {
    	fetch  = new ArrayList<Custom>();
    	adapter = new CustomAdapter(getActivity(), R.id.listView2, fetch, "fragment2");
    	lv2.setAdapter(adapter);
    	lv2.setOnScrollListener(new EndlessScrollListener());
    	setUpRumors("orderByLikeWeb", 0);
    }
    
    //Remove text from search input.
    public void remuveTextFromSearch() {
    	textViewSearch.setText("");
    }
    
}