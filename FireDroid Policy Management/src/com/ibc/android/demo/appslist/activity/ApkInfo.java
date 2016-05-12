package com.ibc.android.demo.appslist.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibc.android.demo.appslist.app.AppData;


public class ApkInfo extends Activity implements OnClickListener{

	TextView appLabel, packageName, version, features;
	TextView permissions, andVersion, installed, lastModify, path, appName, appPerm;
	PackageInfo packageInfo;
	Button editPol, viewPol, viewPerms;
	public String aplname, pkgname, reqperms;
	SQLiteDatabase db;
	int oldver, newver;

	public ApkInfo(){	   

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apkinfo);

		appLabel = (TextView) findViewById(R.id.applabel);
		packageName = (TextView) findViewById(R.id.package_name);
		//permissions = (TextView) findViewById(R.id.req_permission);

		editPol = (Button) findViewById(R.id.edit_policies);
		viewPol = (Button) findViewById(R.id.view_policies);
		viewPerms =(Button) findViewById(R.id.view_permissions);

		editPol.setOnClickListener(this);
		viewPol.setOnClickListener(this);
		viewPerms.setOnClickListener(this);

		AppData appData = (AppData) getApplicationContext();
		packageInfo = appData.getPackageInfo();

		setValues();         
	}

	public String getAplName(){
		aplname = appLabel.getText().toString();
		return aplname;
	}

	public String getPkgName(){
		pkgname = packageName.getText().toString();
		return pkgname;
	}

	public void enterPassword(){

		LayoutInflater li = LayoutInflater.from(ApkInfo.this);
		View promptsView = li.inflate(R.layout.search_prompt, null);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApkInfo.this);
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.user_input);


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
					Intent editPol = new Intent(ApkInfo.this, PolicyTypesList.class);
					Log.i("appname ", " "+ getAplName());
					editPol.putExtra("appname", getAplName());
					startActivity(editPol);
				}

				else{
					Log.d(user_text,"string is empty");
					String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
					AlertDialog.Builder builder = new AlertDialog.Builder(ApkInfo.this);
					builder.setTitle("Error");
					builder.setMessage(message);
					builder.setPositiveButton("Cancel", null);
					builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int id) {
							enterPassword();
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

	/* public boolean isUserApp(ApplicationInfo ai) {
        int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
        return (ai.flags & mask) == 0;
    }*/

	/* private void findViewsById() {
        appLabel = (TextView) findViewById(R.id.applabel);
        packageName = (TextView) findViewById(R.id.package_name);
        //version = (TextView) findViewById(R.id.version_name);
        //features = (TextView) findViewById(R.id.req_feature);
        //permissions = (TextView) findViewById(R.id.req_permission);
        editPol = (Button) findViewById(R.id.edit_policies);
        viewPol = (Button) findViewById(R.id.view_policies);
        viewPerms =(Button) findViewById(R.id.view_permissions);

        editPol.setOnClickListener(this);
        viewPol.setOnClickListener(this);
        viewPerms.setOnClickListener(this);
        //andVersion = (TextView) findViewById(R.id.andversion);
        //path = (TextView) findViewById(R.id.path);
        //installed = (TextView) findViewById(R.id.insdate);
        //lastModify = (TextView) findViewById(R.id.last_modify);

       /* String apkName = getPackageManager().getApplicationLabel(
                packageInfo.applicationInfo).toString();
    	Log.d(apkName, apkName);
    }*/

	private void setValues() {

		//application name
		appLabel.setText(getPackageManager().getApplicationLabel(
				packageInfo.applicationInfo));

		// package name
		packageName.setText(packageInfo.packageName);

		/*// version name
        //version.setText(packageInfo.versionName);

        // target version
        andVersion.setText(Integer
                .toString(packageInfo.applicationInfo.targetSdkVersion));

        // path
        //path.setText(packageInfo.applicationInfo.sourceDir);

        // first installation
       // installed.setText(setDateFormat(packageInfo.firstInstallTime));

        // last modified
        //lastModify.setText(setDateFormat(packageInfo.lastUpdateTime));

        // features
        if (packageInfo.reqFeatures != null)
            features.setText(getFeatures(packageInfo.reqFeatures));
        else
            features.setText("-");*/

		//uses-permission
		/*if (packageInfo.requestedPermissions != null){

        	permissions.setText(getPermissions(packageInfo.requestedPermissions));	


        }else{
        	permissions.setText("This app has no valid permissions");
        }

        reqperms = permissions.getText().toString();*/
		/*try {
			Permission entry = new Permission(ApkInfo.this);

			entry.open();
			entry.deleteEntry();
			entry.createEntry(getPackageManager().getApplicationLabel(
	                packageInfo.applicationInfo).toString(), getPermissions(packageInfo.requestedPermissions));
			entry.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}  

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.edit_policies:
			enterPassword();
			/*Intent editPol = new Intent(this, PolicyTypesList.class);
			Log.i("appname ", " "+ getAplName());
			editPol.putExtra("appname", getAplName());
			startActivity(editPol);*/
			break;

		case R.id.view_policies:
			Intent viewPol = new Intent(this, ViewAppPolicyInstances.class);
			Log.i("appname ", " " + getAplName());
			viewPol.putExtra("appname", getAplName());
			startActivity(viewPol);
			break;

		case R.id.view_permissions:			
			Intent viewPerm = new Intent(this, ViewPermissions.class);
			Log.i("appname ", " "+ getAplName());
			//String apkname = aplname;
			viewPerm.putExtra("appname", getAplName());
			startActivity(viewPerm);
			break;

		default:
			break;

		}
	}

}
