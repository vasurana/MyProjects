package net.xvidia.xonecast;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.xvidia.xonecast.R;

public class NaviActivity extends ActionBarActivity {

	private ListView mDrawerList;
	private DrawerLayout mDrawerLayout;
	private ArrayAdapter<String> mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private String mActivityTitle;
	 GridView grid;
	    String[] web = {
	            "Players",
	            "Layouts",
	            "Schedule",
	            "Locate Player",
	            "Reports"
	    } ;
	    int[] imageId = {
	            R.drawable.players,
	            R.drawable.layout,
	            R.drawable.schedule,
	            R.drawable.locate,
	            R.drawable.reports
	    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 CustomGrid adapter = new CustomGrid(NaviActivity.this, web, imageId);
	        grid=(GridView)findViewById(R.id.grid);
	                grid.setAdapter(adapter);
	                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	 
	                    @Override
	                    public void onItemClick(AdapterView<?> parent, View view,
	                                            int position, long id) {
	                    	switch (position) {
							case 0:
								 Intent intentPlayers = new Intent(NaviActivity.this, Players.class);
								 startActivity(intentPlayers);
								break;
							case 1:
								Intent intentLayouts = new Intent(NaviActivity.this, Layouts.class);
								 startActivity(intentLayouts);
								break;
							case 2:

								break;
							case 3:
								 Intent intentMap = new Intent(NaviActivity.this, Map.class);
								 startActivity(intentMap);

								break;
							case 4:

								break;
							default:
								return;
							}
	 
	                    }
	                });

		mDrawerList = (ListView) findViewById(R.id.navList);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mActivityTitle = getTitle().toString();
		addDrawerItems();
		setupDrawer();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}

	private void addDrawerItems() {
		String[] osArray = { "Home", "Setting", "About", "Logout" };
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, osArray);
		mDrawerList.setAdapter(mAdapter);

		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						switch (position) {
						case 0:
							mDrawerLayout.closeDrawers();
							break;
						case 1:
							
							break;
						case 2:

							break;
						case 3:

							break;
						default:
							return;
						}

						// getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
						// fragment).commit();

						mDrawerLayout.closeDrawers();

					}
				});
	}

	private void setupDrawer() {
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				// getSupportActionBar().setTitle("Navigation!");
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mActivityTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		mDrawerToggle.setDrawerIndicatorEnabled(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		// Activate the navigation drawer toggle
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
