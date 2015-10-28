package info.androidhive.tabsswipe.adapter;

import info.androidhive.tabsswipe.Tab_One;
import info.androidhive.tabsswipe.Tab_Two;
import info.androidhive.tabsswipe.Tab_Three;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			
			return new Tab_One();
		case 1:
			
			return new Tab_Two();
		case 2:
			
			return new Tab_Three();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
