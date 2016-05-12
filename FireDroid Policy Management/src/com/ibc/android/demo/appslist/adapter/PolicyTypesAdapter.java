package com.ibc.android.demo.appslist.adapter;

import java.util.ArrayList;


import com.ibc.android.demo.appslist.activity.PolicyTypesList;
import com.ibc.android.demo.appslist.activity.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PolicyTypesAdapter extends BaseAdapter{

	ArrayList<PolicyTypes> listItems = new ArrayList<PolicyTypes>();
	Context con;
	
	public PolicyTypesAdapter(ArrayList<PolicyTypes> lItems, Context context){
		listItems = lItems;
		con = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (v==null)
		{
			LayoutInflater vi = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row, null);
		}
		
		TextView header = (TextView) v.findViewById(R.id.header);
		TextView subheader1 = (TextView) v.findViewById(R.id.sub_header);
		
		PolicyTypes ptl= listItems.get(position);
		header.setText(ptl.getPolType());
		subheader1.setText(ptl.getDOutcome());
		return v;
	}
	
}
