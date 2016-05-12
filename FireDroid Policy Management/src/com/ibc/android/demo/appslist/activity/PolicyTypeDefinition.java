package com.ibc.android.demo.appslist.activity;


import com.ibc.android.demo.appslist.permissions.database.PoliciesDatabaseModule;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PolicyTypeDefinition extends Activity implements OnClickListener {

	private Spinner defOutcome, priority;
	Button addPolicyType, back;
	TextView policyName;
	protected String polName, policy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.policy_type_definition);

		policyName = (TextView) findViewById(R.id.policyName_textView);
		defOutcome = (Spinner) findViewById(R.id.defOutcome_spinner);
		priority = (Spinner) findViewById(R.id.priority_spinner);
		addPolicyType =(Button) findViewById(R.id.add_policyType);
		back = (Button) findViewById(R.id.backToPolicyTypeList);
		//Bundle b = getIntent();
		String receivingdata = getIntent().getStringExtra("Policy");
		//polName = i.getExtras("Policy Name");
		//Log.i("Policy name: ", polName);
		policyName.setText(receivingdata);

		addPolicyType.setOnClickListener(this);
		back.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.policy_type_definition, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){

		case R.id.add_policyType:
			Log.d("test","blah");
			
			try {
				PoliciesDatabaseModule perm = new PoliciesDatabaseModule(PolicyTypeDefinition.this);
				perm.open();
				perm.createPolicyTypeEntry(policyName.getText().toString(), String.valueOf(defOutcome.getSelectedItem()), String.valueOf(priority.getSelectedItem()));
				String pol = " " + policyName.getText().toString() + " " + String.valueOf(defOutcome.getSelectedItem()) + " " + String.valueOf(priority.getSelectedItem());
				Log.d("entry",pol);
				Toast.makeText(PolicyTypeDefinition.this, "The policy type " + policyName.getText().toString() + 
				" is created with default outcome " + String.valueOf(defOutcome.getSelectedItem()) + " and with the priority precedence set as " + 
				String.valueOf(priority.getSelectedItem()), Toast.LENGTH_LONG).show();
				perm.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;

		case R.id.backToPolicyTypeList:
			/*Intent back = new Intent(PolicyTypeDefinition.this, PolicyTypesList.class);
			startActivity(back);*/
			break;
		}

	}
}
