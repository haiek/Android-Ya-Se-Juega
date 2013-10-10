//add overflow button to rumors for shearing

package com.example.yasejuega;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;


public class Fragment2 extends Fragment{
	private ListView lv2;
    public static CustomAdapter adapter;
    private ArrayList<Custom> fetch = new ArrayList<Custom>();
    //Declare this var for the load more
    private int pos;
    public boolean setAdapterYes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	//It's link to the fragment2.xml file
        View root = inflater.inflate(R.layout.fragment2, container, false);

        //Assign the value 9, so will bring the the 10 to 19 rumors
        pos = 9;
        
        //to use findViewById you need to use root before to let the code know you are in this fragment.
        lv2 =(ListView) root.findViewById(R.id.listView2);
        //Set the scroll listener to the list view for scroll and get more.
        lv2.setOnScrollListener(new EndlessScrollListener());
        
        adapter = new CustomAdapter(getActivity(), R.id.listView2, fetch);
        lv2.setAdapter(adapter);
        
        //CALLING THE JSON METHOD
        setUpRumors("Inicio", 0);
        
        return root;

    }
    
    //GET RUMORS FORM DB IN THE BACKGORUND AND PRINT THEM IN THE FRAGMENT USING CUSTOMADAPTER AND CUSTOM CLASS
    public void setUpRumors(String categoria, int pos) {
    	//Create a GetRumor object and send the CustomAdapter created in onCreateView as a parameter.
    	GetRumors fromDB = new GetRumors(adapter);
    	//Send the URL where to fetch the rumors.
    	fromDB.execute("http://yasejuega.com/Android/loadRumors.php?categoria="+categoria+"&pos="+pos);
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
        public void onScroll(AbsListView view, int firstVisibleItem,
                int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    pos += 9;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next 9 rumors.
            	setUpRumors("Inicio", pos);
                loading = true;
            }
        }

    	@Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }
    
    
}