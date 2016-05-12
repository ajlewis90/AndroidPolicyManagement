package com.ibc.android.demo.appslist.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ibc.android.demo.appslist.permissions.database.PoliciesDatabaseModule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PolicyInstances extends Activity implements OnClickListener{

	PackageManager packageManager;
	TextView policy, subject;
	AutoCompleteTextView target;
	Spinner subj, trgt, outcome;
	Button add, update, delete;
	ArrayList<String> appList = new ArrayList<String>();
	PoliciesDatabaseModule pdm = new PoliciesDatabaseModule(this);
	Context context;
	public String selectedTrgt;
	public String[] targetsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_instances);
		String receivedata = getIntent().getStringExtra("App and Policy:");
		Log.i("Application Name ", receivedata);

		String applpol[] = receivedata.split(":");
		String app = applpol[0];
		String pol = applpol[1];

		Log.i("App: ", app);
		Log.i("Policy: ", pol);

		policy = (TextView) findViewById(R.id.policy_name_textView);
		policy.setText(pol);
		/*subject = (TextView) findViewById(R.id.subject_textView);
		subject.setText(app);*/

		subj = (Spinner) findViewById(R.id.subject_spinner);
		//trgt = (Spinner) findViewById(R.id.target_spinner);
		target = (AutoCompleteTextView) findViewById(R.id.target_autoTextView);
		outcome = (Spinner) findViewById(R.id.Outcome_spinner);

		List<String> subjlist = new ArrayList<String>();
		subjlist.add(app);
		subjlist.add("*");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,subjlist);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subj.setAdapter(dataAdapter);

		add = (Button) findViewById(R.id.add_policyInstance);
		update = (Button) findViewById(R.id.update_policyInstance);
		delete = (Button) findViewById(R.id.delete_policyInstance);

		add.setOnClickListener(this);
		update.setOnClickListener(this);
		delete.setOnClickListener(this);

		List<String> trgtlist = new ArrayList<String>();


		try {
			pdm.open();
			String[] apps = pdm.getApps();
			String[] any = {"*"};

			// Print out the values from the database to the log
			for(int i = 1; i < apps.length; i++)
			{
				Log.i(this.toString(), apps[i]);
				trgtlist.add(apps[i]);
			}

			trgtlist = new ArrayList<String>(Arrays.asList(any));
			trgtlist.addAll(Arrays.asList(apps));

			Object[] targetList =  trgtlist.toArray();
			targetsList = Arrays.copyOf(targetList, targetList.length, String[].class);
			//targetsList = (String []) targetList;

			//ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, R.layout.list_item, targetList);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, targetsList);
			target.setAdapter(adapter);

			pdm.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.policy_instances, menu);
		return true;
	}

	//To check if selected target exists in string array
	public static boolean existInList(String inputString, String[] items)
	{
		for(int i =0; i < items.length; i++)
		{
			if(inputString.contains(items[i]))
			{
				return true;
			}
		}
		return false;
	}

	//To perform IPV4 validation for Network Policy
	boolean isIPValid(String ip){

		String IPADDRESS_PATTERN = 
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
						"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	//Validation of fields
	public boolean validate(String polname, String selectedTrgt){

		boolean decision = true;
		//Target validation for Network Policy Type
		if(policy.getText().toString().equals("Network")){

			if(isIPValid(selectedTrgt)){
				String success = "You have selected " + selectedTrgt + " as the target value for the policy type: " + polname;
				Toast.makeText(this, success, Toast.LENGTH_LONG).show();
				decision = true;
			}

			else{
				String error = " The value " + selectedTrgt + " is an invalid target for the policy type: "+ polname;
				Toast.makeText(this, error, Toast.LENGTH_LONG).show();
				decision = false;
			}
		}

		//Target validation for DNS Policy Type
		else if(policy.getText().toString().equals("DNS")){

			if(URLUtil.isValidUrl(selectedTrgt)){
				String success = "You have selected " + selectedTrgt + " as the target value for the policy type: " + polname;
				Toast.makeText(this, success, Toast.LENGTH_LONG).show();
				decision = true;
			}

			else{
				String error = " The value " + selectedTrgt + " is an invalid target for the policy type: "+ polname;
				Toast.makeText(this, error, Toast.LENGTH_LONG).show();
				decision = false;
			}
		}

		//Target validation for other Policy Types
		else{

			Log.i("Selected target", selectedTrgt);
			for(int i = 0; i< targetsList.length; i++ )
			{
				Log.i("Selected target", targetsList[i].toString());
			}

			if(existInList(selectedTrgt, targetsList)){
				String success = "You have selected " + selectedTrgt + " as the target value for the policy type: " + polname;
				Toast.makeText(this, success, Toast.LENGTH_LONG).show();
				decision = true;
			}

			else{
				String error = " The value " + selectedTrgt + " is an invalid target for the policy type: "+ polname;
				Toast.makeText(this, error, Toast.LENGTH_LONG).show();
				decision = false;
			}
		}

		return decision;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		selectedTrgt = target.getText().toString();
		String polname = policy.getText().toString();

		switch(v.getId()){

		case R.id.add_policyInstance:

			if(validate(polname, selectedTrgt)){

				try {

					PoliciesDatabaseModule pdm = new PoliciesDatabaseModule(this);
					pdm.open();

					//If Policy Instance doesn't already then an entry for the instance is created in the database
					if(!pdm.policyInstancesIntegrity(polname, String.valueOf(subj.getSelectedItem()), selectedTrgt)){

						pdm.createPolicyInstancesEntry(polname, String.valueOf(subj.getSelectedItem()), selectedTrgt, String.valueOf(outcome.getSelectedItem()));
						String success = "The policy instance for the policy type " + polname + ", with subject " + String.valueOf(subj.getSelectedItem()) + 
								", with target " + selectedTrgt + " and outcome " + String.valueOf(outcome.getSelectedItem()) + " has been added to the database!!!";
						Toast.makeText(this, success, Toast.LENGTH_LONG).show();
					}

					//If Policy Instance already exists in the database then display error message
					else{

						String error = "Failure!!! The policy instance for the policy type " + polname + ", with subject " + String.valueOf(subj.getSelectedItem()) + 
								", with target " + selectedTrgt + " already exists!!!";
						Toast.makeText(this, error, Toast.LENGTH_LONG).show();
					}

					pdm.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			break;

		case R.id.update_policyInstance:

			if(validate(polname, selectedTrgt)){

				try {

					PoliciesDatabaseModule pdm = new PoliciesDatabaseModule(this);
					pdm.open();

					//If Policy Instance doesn't already then an entry for the instance is created in the database
					if(pdm.policyInstancesIntegrity(polname, String.valueOf(subj.getSelectedItem()), selectedTrgt)){

						pdm.updatePolInstanceEntry(polname, String.valueOf(subj.getSelectedItem()), selectedTrgt, String.valueOf(outcome.getSelectedItem()));
						String success = "The policy instance for the policy type " + polname + ", with subject " + String.valueOf(subj.getSelectedItem()) + 
								", with target " + selectedTrgt + " and outcome " + String.valueOf(outcome.getSelectedItem()) + " has been updated!!!";
						Toast.makeText(this, success, Toast.LENGTH_LONG).show();
					}

					//If Policy Instance already exists in the database then display error message
					else{

						String error = "Failure!!! The policy instance for the policy type " + polname + ", with subject " + String.valueOf(subj.getSelectedItem()) + 
								", with target " + selectedTrgt + " and outcome " + String.valueOf(outcome.getSelectedItem()) + " doesnt exist!!";
						Toast.makeText(this, error, Toast.LENGTH_LONG).show();
					}

					pdm.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			break;

		case R.id.delete_policyInstance:

			if(validate(polname, selectedTrgt)){
				
				PoliciesDatabaseModule pdm = new PoliciesDatabaseModule(this);
				pdm.open();

				//If Policy Instance doesn't already then an entry for the instance is created in the database
				if(pdm.policyInstancesIntegrity(polname, String.valueOf(subj.getSelectedItem()), selectedTrgt)){

					pdm.deletePolInstanceEntry(polname, String.valueOf(subj.getSelectedItem()), selectedTrgt);
					String success = "The policy instance for the policy type " + polname + ", with subject " + String.valueOf(subj.getSelectedItem()) + 
							", with target " + selectedTrgt + " and outcome " + String.valueOf(outcome.getSelectedItem()) + " has been deleted!!!";
					Toast.makeText(this, success, Toast.LENGTH_LONG).show();
				}

				//If Policy Instance already exists in the database then display error message
				else{

					String error = "Failure to Delete!!! The policy instance for the policy type " + polname + ", with subject " + String.valueOf(subj.getSelectedItem()) + 
							", with target " + selectedTrgt + " and outcome " + String.valueOf(outcome.getSelectedItem()) + " doesnt exist!!";
					Toast.makeText(this, error, Toast.LENGTH_LONG).show();
				}

				pdm.close();
			}

			break;

		}
	}

}
