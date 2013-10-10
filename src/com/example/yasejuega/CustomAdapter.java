package com.example.yasejuega;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter<Custom>{
   private ArrayList<Custom> entries;
   private Activity activity;

   public CustomAdapter(Activity a, int textViewResourceId, ArrayList<Custom> entries) {
       super(a, textViewResourceId, entries);
       this.entries = entries;
       this.activity = a;
   }
   
   public void addItems(JSONObject jObject) throws JSONException {
	   	String num;
	   	//Add rumors to the array
	   	for (int i=1; i <= jObject.length(); i++) {
	   		num = Integer.toString(i);
	   		JSONObject jObject2 = jObject.getJSONObject(num);
	   		//Get the strings in the jObject2.
	   		String rumor = jObject2.getString("rumor");
	   		String date = jObject2.getString("date");
	   		String source = jObject2.getString("source");
	   		Custom one = new Custom(rumor, date, source);
	   		entries.add(one);
	   	}
	   	//Notify that the array has change
	   	notifyDataSetChanged();
   }

   public static class ViewHolder{
	   //Define the elements to work with
       public TextView rumor;
       public TextView date;
       public TextView source;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       View v = convertView;
       ViewHolder holder;
       if (v == null) {
           LayoutInflater vi =
               (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           v = vi.inflate(R.layout.grid_item, null);
           holder = new ViewHolder();
           //Get the elements by id
           holder.rumor = (TextView) v.findViewById(R.id.rumor);
           holder.date = (TextView) v.findViewById(R.id.date);
           holder.source = (TextView) v.findViewById(R.id.source);
           v.setTag(holder);
       }
       else
           holder=(ViewHolder)v.getTag();

       final Custom custom = entries.get(position);
       if (custom != null) {
    	   //Set the text to the elements
           holder.rumor.setText(custom.getcustomRumor());
           holder.date.setText("Subido "+custom.getcustomDate()+" desde ");
           holder.source.setText(custom.getcustomSource());
       }
       return v;
   }
}