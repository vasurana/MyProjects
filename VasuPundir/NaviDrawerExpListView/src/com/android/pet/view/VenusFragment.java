package com.android.pet.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doepiccoding.navigationdrawer.R;

public class VenusFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.secont, null);
		//TextView.class.cast(rootView.findViewById(R.id.labelText)).setText("Venus");
		return rootView;
	}
	
}
