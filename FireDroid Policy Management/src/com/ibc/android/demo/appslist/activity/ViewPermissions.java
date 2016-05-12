package com.ibc.android.demo.appslist.activity;

import com.ibc.android.demo.appslist.permissions.database.PoliciesDatabaseModule;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ViewPermissions extends Activity {

	String application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_permissions_view);

		TextView vp;
		String data;
		String app;
		
		try {

			Intent i = getIntent();
			app = i.getStringExtra("appname");
			vp = (TextView) findViewById(R.id.perm_info);
			PoliciesDatabaseModule info = new PoliciesDatabaseModule(this);
			info.open();
			data = info.getData(app);
			//data = info.getData(applname);
			//data = info.getAppPermission(application);
			info.close();
			vp.setText(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.permissions_view, menu);
		return true;
	}

}
