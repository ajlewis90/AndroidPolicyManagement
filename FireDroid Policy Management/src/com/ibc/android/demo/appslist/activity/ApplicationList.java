package com.ibc.android.demo.appslist.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.ibc.android.demo.appslist.adapter.ApkAdapter;
import com.ibc.android.demo.appslist.app.AppData;
import com.ibc.android.demo.appslist.permissions.database.PoliciesDatabaseModule;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ApplicationList extends Activity implements OnItemClickListener {

	PackageManager packageManager;
	ApplicationInfo ai;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	

		//Get permissions associated with each installed package (app)
		packageManager = getPackageManager();
		List<PackageInfo> packageList = packageManager
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);

		List<PackageInfo> installedPackages = new ArrayList<PackageInfo>();
		
		//Getting List of only user installed apps
		for(PackageInfo p : packageList) //parse all packages to remove the ones with "System" flag
		{
			if(!((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM)) //package is not marked as "System"
			{
				installedPackages.add(p);
			}
		}

		//Sorting the list of packages in alphabetical order
		Collections.sort(packageList, new Comparator<PackageInfo>() {
			public int compare(PackageInfo o1, PackageInfo o2) {
				return o1.applicationInfo.loadLabel(getPackageManager()).toString().compareToIgnoreCase(o2.applicationInfo.loadLabel(getPackageManager()).toString());
			}
		});

		ListView mylistview= (ListView) findViewById(R.id.applist);
		//mylistview.setAdapter(new ApkAdapter(this, installedPackages, packageManager));
		mylistview.setAdapter(new ApkAdapter(this, packageList, packageManager));
		mylistview.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long row) {

		PackageInfo packageInfo = (PackageInfo) parent
				.getItemAtPosition(position);
		AppData appData = (AppData) getApplication();
		appData.setPackageInfo(packageInfo);

		Intent appInfo = new Intent(this, ApkInfo.class);
		startActivity(appInfo);
	}
}