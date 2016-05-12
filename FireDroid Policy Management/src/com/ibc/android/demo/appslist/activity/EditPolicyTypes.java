package com.ibc.android.demo.appslist.activity;

import com.ibc.android.demo.appslist.permissions.database.PoliciesDatabaseModule;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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

public class EditPolicyTypes extends Activity implements OnClickListener{

	Button updatePol, delPol /*viewPol*/;
	Spinner policyName, defaultOutcome, priorityPrecedence;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_policy_types);

		policyName =(Spinner) findViewById(R.id.polName_spinner);

		defaultOutcome = (Spinner) findViewById(R.id.defaultOutcome_spinner);
		priorityPrecedence = (Spinner) findViewById(R.id.setpriority_spinner);

		updatePol = (Button) findViewById(R.id.update_policyType);
		delPol = (Button) findViewById(R.id.delete_policyType);
		//viewPol = (Button) findViewById(R.id.view_policyTypes);

		updatePol.setOnClickListener(this);
		delPol.setOnClickListener(this);
		//viewPol.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_policy_types, menu);
		return true;
	}

	@SuppressLint("DefaultLocale") @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){

		case R.id.update_policyType:

			try {
				PoliciesDatabaseModule updt = new PoliciesDatabaseModule(EditPolicyTypes.this);
				String policy = String.valueOf(policyName.getSelectedItem());

				updt.open();

				if(updt.PolicyTypeCheck(policy)){
					updt.updatePolTypeEntry(policy, String.valueOf(defaultOutcome.getSelectedItem()), String.valueOf(priorityPrecedence.getSelectedItem()));
					String success = "The policy type " + policy + " has been successfully updated with default outcome set to " +
							String.valueOf(defaultOutcome.getSelectedItem()) + " and priority order set to " + String.valueOf(priorityPrecedence.getSelectedItem()) + "!!!";
					Toast.makeText(EditPolicyTypes.this, success, Toast.LENGTH_LONG).show();
				}

				else{
					String error = "The policy type " + String.valueOf(policyName.getSelectedItem()) + " cannot be updated since it doesn't exist in the database!!!";
					Toast.makeText(EditPolicyTypes.this, error, Toast.LENGTH_LONG).show();
				}

				updt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case R.id.delete_policyType:

			try {
				PoliciesDatabaseModule del = new PoliciesDatabaseModule(EditPolicyTypes.this);
				String policy = String.valueOf(policyName.getSelectedItem());

				Log.i("Policy type:", policy);
				del.open();

				if(del.PolicyTypeCheck(policy)){
					del.deletePolTypeEntry(policy);
					Log.i("Policy type:", policy);
					String success = "The policy type " + policy + " has been successfully deleted from the database!!!";
					Toast.makeText(EditPolicyTypes.this, success, Toast.LENGTH_LONG).show();
				}

				else{
					String error = " The policy " + String.valueOf(policyName.getSelectedItem()) + " doesn't exist in the database. So unable to delete!!!";
					Toast.makeText(EditPolicyTypes.this, error, Toast.LENGTH_LONG).show();
				}

				del.close();

			} catch (Exception e) {
				e.printStackTrace();

			}

			break;

		/*case R.id.view_policyTypes:

			try {
				//Permission viewPT = new Permission(EditPolicyTypes.this);
				Intent viewPol = new Intent(EditPolicyTypes.this, ViewPolicyTypes.class);
				startActivity(viewPol);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;*/
		}
	}

}
