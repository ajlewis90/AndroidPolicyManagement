package com.ibc.android.demo.appslist.activity;

import java.util.List;

import com.ibc.android.demo.appslist.permissions.database.PoliciesDatabaseModule;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class FireDroidPolicyManagement extends Activity implements OnClickListener{

	Button storePerm, viewApps, editPolType, viewPolInst, viewPolTypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		storePerm = (Button) findViewById(R.id.storePermissions);
		viewApps = (Button) findViewById(R.id.viewApps);
		editPolType = (Button) findViewById(R.id.editPolicyType);
		viewPolInst = (Button) findViewById(R.id.viewPolicyInstances);
		viewPolTypes = (Button) findViewById(R.id.viewPolicyTypes);

		storePerm.setOnClickListener(this);
		viewApps.setOnClickListener(this);
		editPolType.setOnClickListener(this);
		viewPolInst.setOnClickListener(this);
		viewPolTypes.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void showDialog(){
		LayoutInflater li = LayoutInflater.from(FireDroidPolicyManagement.this);
		View promptsView = li.inflate(R.layout.search_prompt, null);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FireDroidPolicyManagement.this);
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView.findViewById(R.id.user_input);

		// set dialog message
		alertDialogBuilder
		.setCancelable(false)
		.setNegativeButton("Go",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				/** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
				String user_text = (userInput.getText()).toString();

				/** CHECK FOR USER'S INPUT **/
				if (user_text.equals("Fire"))
				{
					Log.d(user_text, "HELLO THIS IS THE MESSAGE CAUGHT :)");
					Intent it = new Intent(FireDroidPolicyManagement.this,EditPolicyTypes.class);
					startActivity(it);
				}

				else{

					Log.d(user_text,"string is empty");
					String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
					AlertDialog.Builder builder = new AlertDialog.Builder(FireDroidPolicyManagement.this);
					builder.setTitle("Error");
					builder.setMessage(message);
					builder.setPositiveButton("Cancel", null);
					builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int id) {
							showDialog();
						}
					});

					builder.create().show();

				}
			}
		})
		.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.dismiss();
			}

		}

				);

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.storePermissions:
			storePermissions(); 
			break;

		case R.id.editPolicyType:
			showDialog();
			break;
			/*Intent it = new Intent(Home.this,EditPolicyTypes.class);
			startActivity(it);*/

		case R.id.viewApps:
			Intent i = new Intent(FireDroidPolicyManagement.this,ApplicationList.class);
			startActivity(i);
			break;

		case R.id.viewPolicyInstances:
			Intent intent = new Intent(FireDroidPolicyManagement.this,ViewPolicyInstances.class);
			startActivity(intent);
			break;

		case R.id.viewPolicyTypes:
			Intent tent = new Intent(FireDroidPolicyManagement.this,ViewPolicyTypes.class);
			startActivity(tent);
			break;
		}

	}

	private void storePermissions() {
		PackageManager pm = getPackageManager();
		List<PackageInfo> packageList = pm
				.getInstalledPackages(PackageManager.GET_PERMISSIONS);

		//Storing the application along with its associated permissions into the database
		for (PackageInfo pkgInfo: packageList){
			try {
				PoliciesDatabaseModule entry = new PoliciesDatabaseModule(FireDroidPolicyManagement.this);

				entry.open();
				if (pkgInfo.requestedPermissions != null){
					//entry.deleteEntry();
					entry.createEntry(getPackageManager().getApplicationLabel(
							pkgInfo.applicationInfo).toString(), getPermissions(pkgInfo.requestedPermissions));
				}
				else {
					entry.createEntry(getPackageManager().getApplicationLabel(
							pkgInfo.applicationInfo).toString(), "This application has no defined permissions");
				}

				entry.close();

			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
	}

	private String getPermissions(String[] requestedPermissions) {

		String permission = "";

		for (int i = 0; i < requestedPermissions.length; i++) {
			permission = permission + requestedPermissions[i] + ",\n";
		}
		return permission;
	}
}
