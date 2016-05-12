package com.ibc.android.demo.appslist.app;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnSpinnerItemSelectedListener implements OnItemSelectedListener{

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		
		Toast.makeText(parent.getContext(), 
				"You have selected: " + parent.getItemAtPosition(position).toString(),
				Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
