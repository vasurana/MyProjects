package info.androidhive.tabsswipe;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
 
public class SearchResultsActivity extends Activity {
 
    private TextView txtQuery;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
 
        // get the action bar
        ActionBar actionBar = getActionBar();
 
        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.back_button);

 
        txtQuery = (TextView) findViewById(R.id.txtQuery);
 
        handleIntent(getIntent());
    }
 
  /*  @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }*/
 
    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
 
            
            /* * Use this query to display search results like 
             * 1. Getting the data from SQLite and showing in listview 
             * 2. Making webrequest and displaying the data 
             * For now we just display the query only
             **/
            txtQuery.setText("Display your search results here!!");
           
            txtQuery.setTextScaleX(1);
 
        }
 
    }
}
