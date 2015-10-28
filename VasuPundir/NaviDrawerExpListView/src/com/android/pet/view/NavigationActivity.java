package com.android.pet.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.doepiccoding.navigationdrawer.R;

public class NavigationActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	ImageView home;
	//Fragment fragment = null;
	TextView appname;
	ExpandableListView expListView;
	HashMap<String, List<String>> listDataChild;
	ExpandableListAdapter listAdapter;
	List<String> listDataHeader;
	int previousGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//String fontPath = "fonts/Shadow Boxing.ttf";
		setContentView(R.layout.activity_navigation);
		
		home = (ImageView)findViewById(R.id.home);
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mDrawerLayout.isDrawerOpen(expListView)){
					mDrawerLayout.closeDrawer(expListView);
				}else{
					mDrawerLayout.openDrawer(expListView);
				}
				
			}
		});
		
		appname = (TextView)findViewById(R.id.appname);
		/*Typeface tf = Typeface.createFromAsset(this.getAssets(), fontPath);
		appname.setTypeface(tf);*/
		
		setUpDrawer();
	}

	/**
	 * 
	 * Get the names and icons references to build the drawer menu...
	 */
	private void setUpDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
		//mDrawerLayout.setDrawerListener(mDrawerListener);
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		prepareListData();
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);
		/*fragment = new MercuryFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
		mDrawerLayout.closeDrawer(expListView);*/
		
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				if(parent.isGroupExpanded(groupPosition))
				{
					parent.collapseGroup(groupPosition);
				}else
				{
					
					if(groupPosition != previousGroup)
					{
						parent.collapseGroup(previousGroup);
					}
					previousGroup = groupPosition;
					parent.expandGroup(groupPosition);
				}
				parent.smoothScrollToPosition(groupPosition);
				
				return true;
			}
		});

		expListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				/*switch (groupPosition) {
				case 0:
					switch (childPosition) {
					case 0:
						fragment = new MercuryFragment();
						break;
					case 1:
						fragment = new VenusFragment();
						break;
					case 2:
						fragment = new EarthFragment();
						break;
					default:
						break;
					}
					break;

				case 1:
					switch (childPosition) {
					case 0:
						fragment = new MercuryFragment();
						break;
					case 1:
						fragment = new VenusFragment();
						break;
					case 2:
						fragment = new EarthFragment();
						break;
					default:
						break;
					}
					break;

				case 2:
					switch (childPosition) {
					case 0:
						fragment = new MercuryFragment();
						break;
					case 1:
						fragment = new VenusFragment();
						break;
					case 2:
						fragment = new EarthFragment();
						break;
					default:
						break;
					}
					break;

				default:
					break;
				}*/
				//getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
				mDrawerLayout.closeDrawer(expListView);
				return false;
			}
		});
	}


	/*private OnItemClickListener mDrawerItemClickedListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

			switch(position){
			case 0:
				fragment = new MercuryFragment();
				break;
			case 1:
				fragment = new VenusFragment();
				break;
			case 2:
				fragment = new EarthFragment();
				break;
			default:
				return;
			}

			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

			mDrawerLayout.closeDrawer(expListView);
		}
	};*/

	// Catch the events related to the drawer to arrange views according to this
	// action if necessary...
	/*private DrawerListener mDrawerListener = new DrawerListener() {

		@Override
		public void onDrawerStateChanged(int status) {

		}

		@Override
		public void onDrawerSlide(View view, float slideArg) {

		}

		@Override
		public void onDrawerOpened(View view) {
		}

		@Override
		public void onDrawerClosed(View view) {
		}
	};*/

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

	public class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Context _context;
		private List<String> _listDataHeader; // header titles
		// child data in format of header title, child title
		private HashMap<String, List<String>> _listDataChild;

		public ExpandableListAdapter(Context context, List<String> listDataHeader,
				HashMap<String, List<String>> listChildData) {
			this._context = context;
			this._listDataHeader = listDataHeader;
			this._listDataChild = listChildData;
		}

		@Override
		public Object getChild(int groupPosition, int childPosititon) {
			return this._listDataChild.get(this._listDataHeader.get(groupPosition))
					.get(childPosititon);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			final String childText = (String) getChild(groupPosition, childPosition);

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.list_item, null);
			}

			TextView txtListChild = (TextView) convertView
					.findViewById(R.id.lblListItem);

			txtListChild.setText(childText);
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this._listDataChild.get(this._listDataHeader.get(groupPosition))
					.size();
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
				convertView = infalInflater.inflate(R.layout.list_group, null);
			}

			TextView lblListHeader = (TextView) convertView
					.findViewById(R.id.lblListHeader);
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
}
