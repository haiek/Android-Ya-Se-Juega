package com.yasejuega.yasejuega;

import java.util.Locale;

import com.google.analytics.tracking.android.EasyTracker;
import com.yasejuega.yasejuega.Fragment2.EndlessScrollListener;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	
	//Set to static so I can access mViewPager in Fragment1.
	static ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the two
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		//Cache the content of the fragment so when going back to that fragment you still be in the same part
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setCurrentItem(1);
	
	}
	
	//For Google Analytics
	  @Override
	  public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }
	
	  @Override
	  public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }
	//Finish Google Analytics
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
	    //locate the main_activity_actions.xml in res/menu for adding actions to the action bar
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    
	    // Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    
	    /* USE THIS FOR SEARCH IN ACTION BAR
	    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false);
	    */
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.refresh:
	        	//Refresh Fragment2
	            ((Fragment2) this.getActiveFragment(1)).loadCategory("Inicio");
	            //Remove text from search input.
	            ((Fragment2) this.getActiveFragment(1)).remuveTextFromSearch();
	        	//Refresh Fragment3
	            ((Fragment3) this.getActiveFragment(2)).refresh();
	            return true;
	        case R.id.order_by_likes:
	    		//Order Fragment2 by likes
	    		((Fragment2) this.getActiveFragment(1)).orderByLike();
	    		//Remove text from search input.
	    		((Fragment2) this.getActiveFragment(1)).remuveTextFromSearch();
	    		//Order Fragment3 by likes
	    		((Fragment3) this.getActiveFragment(2)).orderByLike();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	//get a reference to Fragment2 in your view page (SEBASTIAN).
	public Fragment getActiveFragment(int position) {
		String name = makeFragmentName(mViewPager.getId(), position);
		return getSupportFragmentManager().findFragmentByTag(name);
	}
	
	private static String makeFragmentName(int viewId, int index) {
			return "android:switcher:" + viewId + ":" + index;
	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
        @Override
        public Fragment getItem(int position) {
        	//Declare that fragment will be an object from class Fragment1 or Fragemnt2 that extends class Fragment
            Fragment fragment;
            //Check in witch position we are.
            switch (position) {
                case 0:
                	//Create an object from the class Fragment1 call fragment
                    fragment = new Fragment1();
                    break;
                case 1:
                	//Create an object from the class Fragment2 call fragment
                    fragment = new Fragment2();
                    break;
                case 2:
                    fragment = new Fragment3();
                    break;
                default:
                    fragment  = null;
                    break;
            }
            return fragment;

        }
		
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			//Set the title to the fragments
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
