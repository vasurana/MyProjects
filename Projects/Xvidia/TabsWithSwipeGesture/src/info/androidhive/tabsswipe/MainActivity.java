package info.androidhive.tabsswipe;


import info.androidhive.tabsswipe.adapter.TabsPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	private DrawerLayout mDrawerLayout;
	ImageView home;
	Fragment fragment = null;
	TextView appname;
	ExpandableListView expListView;
	HashMap<String, List<String>> listDataChild;
	ExpandableListAdapter listAdapter;
	List<String> listDataHeader;
	  private ActionBarDrawerToggle mDrawerToggle;

	int previousGroup;

	
	  private String[] tabs = { "Tab 1", "Tab 2", "Tab 3" };
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.pager);

		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		
		
		  for (String tab_name : tabs) {
		  actionBar.addTab(actionBar.newTab().setText(tab_name)
		  .setTabListener(this)); }
		 

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});

		setUpDrawer();
		

		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				if (parent.isGroupExpanded(groupPosition)) {
					parent.collapseGroup(groupPosition);
				} else {

					if (groupPosition != previousGroup) {
						parent.collapseGroup(previousGroup);
					}
					previousGroup = groupPosition;
					parent.expandGroup(groupPosition);
				}
				parent.smoothScrollToPosition(groupPosition);

				return true;
			}
		});

		expListView
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						// TODO Auto-generated method stub
						
						
						switch (groupPosition) {
						case 0:
							switch (childPosition) {
							case 0:
								
								Intent i = new Intent(MainActivity.this, Activity_One.class);
								startActivity(i);

								break;
							case 1:
								//fragment = new TwoFragment();
								break;
							case 2:
								//fragment = new ThreeFragment();
								break;
							default:
								break;
							}
							break;

						case 1:
							switch (childPosition) {
							case 0:
								//fragment = new OneFragment();
								break;
							case 1:
								//fragment = new TwoFragment();
								break;
							case 2:
								//fragment = new ThreeFragment();
								break;
							default:
								break;
							}
							break;

						case 2:
							switch (childPosition) {
							case 0:
								//fragment = new OneFragment();
								break;
							case 1:
								//fragment = new TwoFragment();
								break;
							case 2:
								//fragment = new ThreeFragment();
								break;
							default:
								break;
							}
							break;

						default:
							break;
						}
						/*getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
						*/
						mDrawerLayout.closeDrawer(expListView);
						return false;
					}
				});
		
		 mDrawerToggle = new ActionBarDrawerToggle(
	                this,
	                mDrawerLayout,
	                R.drawable.home, 
	                R.string.drawer_open,
	                R.string.drawer_close)
	        {
	            public void onDrawerClosed(View view)
	            {
	                
	                invalidateOptionsMenu();
	                
	            }

	            public void onDrawerOpened(View drawerView)
	            {
	              
	                invalidateOptionsMenu();
	            }
	        };


		 mDrawerLayout.setDrawerListener(mDrawerToggle);
		 getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);

	}
	 @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }

	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Pass the event to ActionBarDrawerToggle, if it returns
	        // true, then it has handled the app icon touch event
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	          return true;
	        }
	        // Handle your other action bar items...

	        return super.onOptionsItemSelected(item);
	    }


	private void setUpDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setScrimColor(getResources().getColor(
				android.R.color.transparent));
		// mDrawerLayout.setDrawerListener(mDrawerListener);
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		prepareListData();
		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		if (mDrawerLayout.isDrawerOpen(expListView)) {
            mDrawerLayout.closeDrawer(expListView);
        }
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public class ExpandableListAdapter extends BaseExpandableListAdapter {
		private Context _context;
		private List<String> _listDataHeader; // header titles
		// child data in format of header title, child title
		private HashMap<String, List<String>> _listDataChild;

		public ExpandableListAdapter(Context context,
				List<String> listDataHeader,
				HashMap<String, List<String>> listChildData) {
			this._context = context;
			this._listDataHeader = listDataHeader;
			this._listDataChild = listChildData;
		}

		@Override
		public Object getChild(int groupPosition, int childPosititon) {
			return this._listDataChild.get(
					this._listDataHeader.get(groupPosition))
					.get(childPosititon);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			final String childText = (String) getChild(groupPosition,
					childPosition);

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(
						R.layout.expandablelistsubcategories, null);
			}

			TextView txtListChild = (TextView) convertView
					.findViewById(R.id.subCat_desc_2);

			txtListChild.setText(childText);
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this._listDataChild.get(
					this._listDataHeader.get(groupPosition)).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this._listDataHeader.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return this._listDataHeader.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String headerTitle = (String) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.single_row, null);
			}

			TextView lblListHeader = (TextView) convertView
					.findViewById(R.id.cat_desc_1);
			lblListHeader.setTypeface(null, Typeface.BOLD);
			lblListHeader.setText(headerTitle);

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding group data
		listDataHeader.add("Item 1");
		listDataHeader.add("Item 2");
		listDataHeader.add("Item 3");

		// Adding child data
		List<String> Item1 = new ArrayList<String>();
		Item1.add("Sub-Item 1");
		Item1.add("Sub-Item 2");
		Item1.add("Sub-Item 3");

		List<String> Item2 = new ArrayList<String>();
		Item2.add("Sub-Item 1");
		Item2.add("Sub-Item 2");
		Item2.add("Sub-Item 3");

		List<String> Item3 = new ArrayList<String>();
		Item3.add("Sub-Item 1");
		Item3.add("Sub-Item 2");
		Item3.add("Sub-Item 3");

		listDataChild.put(listDataHeader.get(0), Item1);
		listDataChild.put(listDataHeader.get(1), Item2);
		listDataChild.put(listDataHeader.get(2), Item3);
	}

}
