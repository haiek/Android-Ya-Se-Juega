package com.yasejuega.yasejuega;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAdapter extends ArrayAdapter<Custom>{
   private ArrayList<Custom> entries;
   private Activity activity;
   private String fromFragment;

   public CustomAdapter(Activity a, int textViewResourceId, ArrayList<Custom> entries, String from) {
       super(a, textViewResourceId, entries);
       this.entries = entries;
       this.activity = a;
       this.fromFragment = from;
   }
   
   public void addItems(JSONObject jObject) throws JSONException {
	   	String num;
	   	//Add rumors to the array
	   	for (int i=1; i <= jObject.length(); i++) {
	   		num = Integer.toString(i);
	   		JSONObject jObject2 = jObject.getJSONObject(num);
	   		//Get the strings in the jObject2.
	   		int id = jObject2.getInt("id");
	   		String rumor = jObject2.getString("rumor");
	   		String date = jObject2.getString("date");
	   		String source = jObject2.getString("source");
	   		String numLikes = jObject2.getString("likes");
	   		Custom one = new Custom(id, rumor, date, source, numLikes);
	   		entries.add(one);
	   	}
	   	//Notify that the array has change
	   	notifyDataSetChanged();
   }
   
   //Inicialisate the variables for each row.
   public static class ViewHolder{
	   //Define the elements to work with
       public TextView rumor;
       public TextView date;
       public TextView source;
       public TextView numLikes;
       public ImageView share;
       public ImageView heart;
   }

   @Override
   //Get the views by id and set values to them for each row.
   public View getView(int position, View convertView, ViewGroup parent) {
       View v = convertView;
       //It's final because I set the text of numLikes to the new one after click on the hart
       final ViewHolder holder;
       if (v == null) {
           LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           v = vi.inflate(R.layout.grid_item, null);
           holder = new ViewHolder();
           //Get the elements by id
           holder.rumor = (TextView) v.findViewById(R.id.rumor);
           holder.date = (TextView) v.findViewById(R.id.date);
           holder.source = (TextView) v.findViewById(R.id.source);
           holder.numLikes = (TextView) v.findViewById(R.id.num);
           holder.share = (ImageView) v.findViewById(R.id.share);
           holder.heart = (ImageView) v.findViewById(R.id.heart);
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
           holder.numLikes.setText(custom.getcustomNumLikes());
           holder.share.setOnClickListener(new View.OnClickListener() {
        	   public void onClick(View v) {
        		   try {
					shareIt(v, custom.getcustomRumor(), custom.getcustomId());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	   }
           });
           holder.heart.setOnClickListener(new View.OnClickListener() {
        	   public void onClick(View v) {
        		   if(fromFragment.equals("fragment2")) {
        			   new ConnectToDb("likeRumor").execute("http://yasejuega.com/Android/ajaxphp.php?key=like&id=" + custom.getcustomId());
        			   Log.i("******hola", Integer.toString(custom.getcustomId()));
    		   		} else if(fromFragment.equals("fragment3")) {
    		   			new ConnectToDb("likeRumor").execute("http://yasejuega.com/Android/ajaxphp.php?key=twitterLike&id=" + custom.getcustomId());
    		   			Log.i("******hola", Integer.toString(custom.getcustomId()));
    		   		}
        		   //Get the total likes convert to int, add 1, convert to string
        		   //holder.numLikes.setText("");
        		   holder.numLikes.setText(Integer.toString(Integer.parseInt(custom.getcustomNumLikes())+1));
        		   //FIX THIS BECAUSE WHEN YOU DISABLE THEN YOU CAN ONLY LIKE IN THE FIRST 9 RUMORS
        		   //holder.heart.setEnabled(false);
        	   }
           });
       }
       return v;
   }
   
   public void shareIt(View v, String text, int id) throws UnsupportedEncodingException {
	 //64-ENCODE
	   String source = "";
	   if(this.fromFragment == "fragment2") {
		   source = "rumores";
	   } else if(this.fromFragment == "fragment3") {
		   source = "twitter";
	   }
	   String parametersString = id+"&"+source;
	   byte[] data = parametersString.getBytes("UTF-8");
	   String base64 = Base64.encodeToString(data, Base64.DEFAULT);
	 //sharing implementation here
	 Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
	 sharingIntent.setType("text/plain");
	 String shareBody = text;
	 
	 //Customizing share
	 /*ResolveInfo info = (ResolveInfo) mAdapter.getItem(position);
	 if (info.activityInfo.packageName.contains("facebook")) {
		 sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
     }*/
	 
	 sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, text);
	 sharingIntent.putExtra(Intent.EXTRA_TEXT, "Entra a YASEJUEGA http://www.yasejuega.com/?data="+base64);
	 v.getContext().startActivity(Intent.createChooser(sharingIntent, "Compartir en..."));
   }
   
   //Disable clicking in listView
   @Override
   public boolean isEnabled(int position) {
       return false;
   }
   
}