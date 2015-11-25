package net.xvidia.xonecast;

import java.util.ArrayList;
import java.util.Arrays;

import com.xvidia.xonecast.R;
import com.xvidia.xonecast.R.string;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlayersNameFragment extends ListFragment {

	private String players[];
	private ArrayAdapter<String> listAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
				"Jupiter", "Saturn", "Uranus", "Neptune", "Player 1",
				"Player 2", "Player 3", "Player 4", "Player 1", "Player 2",
				"Player 3", "Player 4", "Player 1", "Player 2", "Player 3",
				"Player 4" };
		
		ArrayList<String> planetList = new ArrayList<String>();
		planetList.addAll(Arrays.asList(planets));

		// Create ArrayAdapter using the planet list.
		listAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.simplerow, planetList);
		
		/* // Add more planets. If you passed a String[] instead of a List<String>   
	    // into the ArrayAdapter constructor, you must not add more items.   
	    // Otherwise an exception will occur.  
	    listAdapter.add( "Ceres" );  
	    listAdapter.add( "Pluto" );  
	    listAdapter.add( "Haumea" );  
	    listAdapter.add( "Makemake" );  
	    listAdapter.add( "Eris" );  */
		
		setListAdapter(listAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.playersname, container, false);
	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {

		Toast.makeText(getActivity(),
				getListView().getItemAtPosition(position).toString(),
				Toast.LENGTH_SHORT).show();
	}

}
