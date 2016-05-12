package com.ibc.android.demo.appslist.activity;

import com.ibc.android.demo.appslist.permissions.database.PoliciesDatabaseModule;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class ViewPolicyInstances extends Activity {

	TextView polInst;
	String data; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_policy_instances);
		
		String data;

		try {

			//Intent i = getIntent();
			polInst = (TextView) findViewById(R.id.policyInstance_Records);
			PoliciesDatabaseModule info = new PoliciesDatabaseModule(this);
			info.open();
			data = info.getPolicyInstanceData();
			info.close();
			polInst.setText(data);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_policy_instances, menu);
		return true;
	}

}
