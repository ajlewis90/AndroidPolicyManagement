package com.ibc.android.demo.appslist.activity;

import java.util.ArrayList;


import com.ibc.android.demo.appslist.adapter.PolicyTypes;
import com.ibc.android.demo.appslist.adapter.PolicyTypesAdapter;
import com.ibc.android.demo.appslist.permissions.database.PoliciesDatabaseModule;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PolicyTypesList extends Activity implements OnItemClickListener {

	ArrayList<String> items = new ArrayList<String>();
	ArrayList<PolicyTypes> policyTypes = new ArrayList<PolicyTypes>();
	
	//PoliciesDatabaseModule pdm = new PoliciesDatabaseModule(this);
	
	public String[] policyTypeList = {"DNS","IPC","Network","File I/O","Shared Memory","Dial"};
	String app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policy_types_list);
		polTypesList();
	}

	private void polTypesList() {
		ListView myListView = (ListView)findViewById(R.id.policyTypesList);
		myListView.setAdapter(new PolicyTypesAdapter(policyTypes, this));
		myListView.setOnItemClickListener(this);
		
		try{
			for (int i = 0; i < policyTypeList.length; i++) {
				String pt = policyTypeList[i].toString();
				//String upperpt = pt.toUpperCase();
				
				PoliciesDatabaseModule pdmod = new PoliciesDatabaseModule(PolicyTypesList.this);
				
				String outcome = "";
				
				//To display the status of a given policy type i.e. whether it is defined in the database or not
				pdmod.open();
				if(!(pdmod.PolicyTypeCheck(pt))){
					outcome = "Not Defined";
				}
				
				else{
					outcome = pdmod.getDefaultOutcome(pt);
				}
				
				pdmod.close();
				
				items.add(pt + "\n\n" + outcome);
				policyTypes.add(new PolicyTypes(pt, outcome));
			}
			
		} catch(Exception e){
			
		}
		
	}
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.policy_types_list, menu);
		return true;
	}

	
	@Override
	public void onItemClick(AdapterView<?> l, View v, int position, long id) {
		// TODO Auto-generated method stub
		PoliciesDatabaseModule pdm = new PoliciesDatabaseModule(PolicyTypesList.this);
		try {
			Intent i = getIntent();
			app = i.getStringExtra("appname");
			Log.i("App name", app);
			String polType = policyTypeList[position];
			Log.i("Policy name: ", polType);
			pdm.open();
			Boolean check = pdm.PolicyTypeCheck(polType);
			
			//If the definition for the selected policy type doesn't exist
			if(check == false){
				
				Intent polDef = new Intent(this, PolicyTypeDefinition.class);
				Bundle b = new Bundle();
                b.putString("Policy", polType);
                polDef.putExtras(b);
				//Bundle extras = polDef.getExtras();
				//extras.putString("TextView",polType);
				//polDef.putExtra(polType, "Policy Type");
				Log.i("Policy name: ", polType);
				startActivity(polDef);
			}
			
			//If definition for the selected policy type exists
			else{
				Intent polInst = new Intent(this, PolicyInstances.class);
				Bundle b = new Bundle();
                b.putString("App and Policy:", app + ":" + polType);
                polInst.putExtras(b);
                Log.i("Policy name: ", polType);
				//polInst.putExtra(app, "Application Name");
				startActivity(polInst);
			}
			
			pdm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
