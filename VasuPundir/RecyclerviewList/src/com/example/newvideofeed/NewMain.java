package com.example.newvideofeed;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;

public class NewMain extends ActionBarActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    //GridLayoutManager lm;
    String[] dataArray = new String[]{"Item 1","Item 2","Item 3","Item 4","Item 5","Item 6","Item 7","Item 8","Item 9","Item 10","Item 11","Item 12","Item 13","Item 14","Item 15","Item 16","Item 17","Item 18","Item 19","Item 20","Item 21","Item 22","Item 23","Item 24"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
       /* lm = GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			
			@Override
			public int getSpanSize(int position) {
				// TODO Auto-generated method stub
				return (position % 3 == 0 ? 2: 1);
			}
		});*/
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(dataArray);
        recyclerView.setAdapter(adapter);
 
    }
	
}
