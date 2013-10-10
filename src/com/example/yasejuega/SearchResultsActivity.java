package com.example.yasejuega;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class SearchResultsActivity extends ListActivity {
	
   private ListView searchListView;
   
   public void onCreate(Bundle savedInstanceState) { 
      super.onCreate(savedInstanceState); 
      handleIntent(getIntent());
      searchListView =(ListView)findViewById(R.id.searchListView);
      
   }
   
   public void onNewIntent(Intent intent) { 
      setIntent(intent); 
      handleIntent(intent); 
   } 
   public void onListItemClick(ListView l, View v, int position, long id) { 
      // call detail activity for clicked entry 
   }
   private void handleIntent(Intent intent) { 
      if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
         String query = intent.getStringExtra(SearchManager.QUERY); 
         doSearch(query); 
      } 
   }    
   private void doSearch(String queryStr) {
	   //String[] paises = { "Argentina", "Chile", "Paraguay", "Bolivia", "Peru", "Ecuador", "Brasil", "Colombia", "Venezuela", "Uruguay" };
	   //ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, paises);
	   //searchListView.setAdapter(adapter);
   // get a Cursor, prepare the ListAdapter
   // and set it
   } 
}
