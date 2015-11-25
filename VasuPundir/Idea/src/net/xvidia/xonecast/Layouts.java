package net.xvidia.xonecast;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xvidia.xonecast.R;

public class Layouts extends ListActivity{
	
	private ActionBar actionBar;

	String[] city = {
			"Bangalore",
			"Chennai",
			"Mumbai",
			"Pune",
			"Delhi",
			"Jabalpur",
			"Indore",
			"Ranchi",
			"Hyderabad",
			"Ahmedabad",
			"Kolkata",
			"Bangalore",
			"Chennai",
			"Mumbai",
			"Pune",
			"Delhi",
			"Jabalpur",
			"Indore",
			"Ranchi",
			"Hyderabad",
			"Ahmedabad",
			"Kolkata"
			};
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layouts);
		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.ic_action_back);
		//Listview adapter
		setListAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1,city));
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
	            this.finish();
	            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void onListItemClick(ListView parent, View v,
			int position, long id) {
			Toast.makeText(this, "You have selected city : " + city[position],
			Toast.LENGTH_LONG).show();
			}
}
