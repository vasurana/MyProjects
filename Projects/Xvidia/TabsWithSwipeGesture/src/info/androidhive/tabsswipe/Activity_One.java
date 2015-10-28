package info.androidhive.tabsswipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class Activity_One extends Activity {

	ArrayAdapter<String> myAdapter;
	ListView listView;
	String[] months = new String[] { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.back_button);

		setContentView(R.layout.activity_one);
		listView = (ListView) findViewById(R.id.listview);
		myAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, months);
		listView.setAdapter(myAdapter);
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println(position + " --postion");
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		case R.id.action_search:
			return true;
			
		 case R.id.sort:

			 sortAscending();
			 listView.setAdapter(myAdapter);
			// myAdapter.notifyDataSetChanged();
		        return true;
			 
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	private void sortAscending () {
	    List<String> sortedMonthsList = Arrays.asList(months);
	    Collections.sort(sortedMonthsList);

	    months = (String[]) sortedMonthsList.toArray();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		SearchView.OnQueryTextListener textChangeListener = new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				myAdapter.getFilter().filter(query);
				// System.out.println("on query submit: " + query);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				myAdapter.getFilter().filter(newText);
				// System.out.println("on text chnge text: " + newText);
				return true;
			}
		};
		searchView.setOnQueryTextListener(textChangeListener);
		return super.onCreateOptionsMenu(menu);
	}
}
