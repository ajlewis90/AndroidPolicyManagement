package com.ibc.android.demo.appslist.permissions.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PoliciesDatabaseModule {

	public static final String KEY_NAME = "app_name";
	public static final String KEY_PERMISSION = "app_permissions";
	public static final String KEY_POLICY_NAME = "policy_name";
	public static final String KEY_DEFAULT_OUTCOME = "default_outcome";
	public static final String KEY_OUTCOME_PRIORITY = "outcome_priority";
	public static final String KEY_SUBJECT = "subject";
	public static final String KEY_TARGET = "target";
	public static final String KEY_OUTCOME = "assigned_outcome";
	/*public static final String KEY_OUTCOME4 = "outcome4";*/

	private static final String DATABASE_NAME = "PoliciesDB";
	private static final String PERMISSION_POLICY_TABLE = "permissionsTable";
	private static final String POLICY_TYPES_TABLE = "policyTypeTable";
	private static final String POLICY_INSTANCES_TABLE = "policyInstancesTable";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	public static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {

			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		//creating schema of permissions and policy instances table (schema attributes for policy instances to be defined)
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + POLICY_INSTANCES_TABLE + " (" +					
					KEY_POLICY_NAME + " TEXT NOT NULL, " +
					KEY_SUBJECT + " TEXT NOT NULL, " + 
					KEY_TARGET + " TEXT NOT NULL, " +
					KEY_OUTCOME + " TEXT NOT NULL, " + 
					"CONSTRAINT pkey PRIMARY KEY(" + KEY_POLICY_NAME + "," + KEY_SUBJECT + "," + KEY_TARGET + "));"
					);

			db.execSQL("CREATE TABLE " + PERMISSION_POLICY_TABLE + " (" +					
					KEY_NAME + " PRIMARY KEY, " +
					KEY_PERMISSION + " TEXT NOT NULL);"
					);

			db.execSQL("CREATE TABLE " + POLICY_TYPES_TABLE + " (" +					
					KEY_POLICY_NAME + " TEXT PRIMARY KEY, " +
					KEY_DEFAULT_OUTCOME + " TEXT NOT NULL, " +
					KEY_OUTCOME_PRIORITY + " TEXT NOT NULL);"
					); 

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + PERMISSION_POLICY_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + POLICY_TYPES_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + POLICY_INSTANCES_TABLE);
			onCreate(db);
		}

	}

	public PoliciesDatabaseModule(Context c){	
		ourContext =c;
	}

	//Opens the database for editing or retrieval of information
	public PoliciesDatabaseModule open() throws SQLException{

		ourHelper = new DbHelper(ourContext); //refer newBoston 114 for explanation
		//opens up the database through ourhelper
		ourDatabase = ourHelper.getWritableDatabase(); 
		return this;
	}

	//Closes the database after information is edited or retrieved
	public void close(){
		ourHelper.close();
	}

	//Creating database entries of apps and their corresponding permissions in the permission-policy table
	public long createEntry(String name, String perm) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_PERMISSION, perm);
		return ourDatabase.insert(PERMISSION_POLICY_TABLE, null, cv);
	}

	//Creating entries in PolicyType Table
	public long createPolicyTypeEntry(String policyName, String defaultOutcome, String outcomePriority) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_POLICY_NAME, policyName);
		cv.put(KEY_DEFAULT_OUTCOME, defaultOutcome);
		cv.put(KEY_OUTCOME_PRIORITY, outcomePriority);
		return ourDatabase.insert(POLICY_TYPES_TABLE, null, cv);
	}

	//Creating the entries in PolicyInstances Table
	public long createPolicyInstancesEntry(String policyName, String subject, String target, String outcome) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_POLICY_NAME, policyName);
		cv.put(KEY_SUBJECT, subject);
		cv.put(KEY_TARGET, target);
		cv.put(KEY_OUTCOME, outcome);
		return ourDatabase.insert(POLICY_INSTANCES_TABLE, null, cv);
	}

	//Retrieves the installed applications along with their corresponding permissions from the database
	public String getData(String app) throws SQLException{
		// TODO Auto-generated method stub

		String[] columns = new String[] {KEY_NAME, KEY_PERMISSION};
		String[] columns1= {app};
		Log.d("perms", " " + app);
		Cursor c = ourDatabase.query(PERMISSION_POLICY_TABLE, columns, KEY_NAME + "=?", columns1, null, null, null);
		Log.d("perms", " " + c.getCount());
		//Cursor c = ourDatabase.rawQuery("Select " + columns + " from " + DATABASE_TABLE + " where " + KEY_NAME  + " = " + app, null);
		String result = "";

		int iName = c.getColumnIndex(KEY_NAME);
		int iPermission = c.getColumnIndex(KEY_PERMISSION);

		//first row of table where cursor will start traversal i.e. moveToFirst
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

			result += c.getString(iName) + ":\n" + c.getString(iPermission) + "\n";
		}	
		return result;
	}

	//Retrieves all the installed apps from the database
	public String[] getApps() throws SQLException{

		Cursor c = ourDatabase.query(PERMISSION_POLICY_TABLE, new String[] {KEY_NAME},  null, null, null, null, null);

		if(c.getCount() == 0){

		}

		else{
			String[] str = new String[c.getCount()];
			int i = 0;

			while (c.moveToNext())
			{
				str[i] = c.getString(c.getColumnIndex(KEY_NAME));
				i++;
			}
			return str;
		}

		return null;	
	}

	//Retrieves all stored policy types from the policy-type table
	public String getPolicyTypeData() throws SQLException{
		// TODO Auto-generated method stub

		//Boolean exists = true;
		String[] columns = new String[] {KEY_POLICY_NAME, KEY_DEFAULT_OUTCOME, KEY_OUTCOME_PRIORITY};
		//String[] columns1= {app};
		//Log.d("perms", " " + app);
		Cursor c = ourDatabase.query(POLICY_TYPES_TABLE, columns, null, null, null, null, null);
		Log.d("perms", " " + c.getCount());
		//Cursor c = ourDatabase.rawQuery("Select " + columns + " from " + DATABASE_TABLE + " where " + KEY_NAME  + " = " + app, null);

		if(PolicyTypeExists()){
			String result = "";

			int polName = c.getColumnIndex(KEY_POLICY_NAME);
			int defOut = c.getColumnIndex(KEY_DEFAULT_OUTCOME);
			int priority = c.getColumnIndex(KEY_OUTCOME_PRIORITY);

			//first row of table where cursor will start traversal i.e. moveToFirst
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

				result += " Policy Type: \n " + c.getString(polName) + "\n Default Outcome: \n " + c.getString(defOut) + 
						"\n Priority Precedence: \n " + c.getString(priority) + "\n\n";
			}	
			return result;
		}

		else{
			String result = "There are currently no policy types defined in the database";
			return result;
		}
	}

	//Retrieves all the stored policy instances from the database
	public String getPolicyInstanceData() throws SQLException{
		// TODO Auto-generated method stub

		//Boolean exists = true;
		String[] columns = new String[] {KEY_POLICY_NAME, KEY_SUBJECT, KEY_TARGET, KEY_OUTCOME};
		//String[] columns1= {app};
		//Log.d("perms", " " + app);
		Cursor c = ourDatabase.query(POLICY_INSTANCES_TABLE, columns, null, null, null, null, null);
		Log.d("perms", " " + c.getCount());
		//Cursor c = ourDatabase.rawQuery("Select " + columns + " from " + DATABASE_TABLE + " where " + KEY_NAME  + " = " + app, null);

		if(c.getCount() != 0){
			String result = "";

			int polName = c.getColumnIndex(KEY_POLICY_NAME);
			int sub = c.getColumnIndex(KEY_SUBJECT);
			int tar = c.getColumnIndex(KEY_TARGET);
			int outcome = c.getColumnIndex(KEY_OUTCOME);

			//first row of table where cursor will start traversal i.e. moveToFirst
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

				result += " Policy Type: \n " + c.getString(polName) + "\n Subject: \n " + c.getString(sub) + 
						"\n Target: \n " + c.getString(tar) +"\n Outcome: \n " + c.getString(outcome) + "\n\n";
			}	
			return result;
		}

		else{
			String result = "There are currently no policy instances defined in the database";
			return result;
		}
	}


	//Retrieves all the stored policy instances for the requested app from the Policy-Instances table
	public String getAppPolicyInstanceData(String app) throws SQLException{
		// TODO Auto-generated method stub

		//Boolean exists = true;
		String[] columns = new String[] {KEY_POLICY_NAME, KEY_SUBJECT, KEY_TARGET, KEY_OUTCOME};
		String[] columns1= {app};
		//Log.d("perms", " " + app);
		Cursor c = ourDatabase.query(POLICY_INSTANCES_TABLE, columns, KEY_SUBJECT +"=?", columns1, null, null, null);
		Log.d("perms", " " + c.getCount());
		//Cursor c = ourDatabase.rawQuery("Select " + columns + " from " + DATABASE_TABLE + " where " + KEY_NAME  + " = " + app, null);

		if(AppPolicyInstanceExists(app)){
			String result = "";

			int polName = c.getColumnIndex(KEY_POLICY_NAME);
			int sub = c.getColumnIndex(KEY_SUBJECT);
			int tar = c.getColumnIndex(KEY_TARGET);
			int outcome = c.getColumnIndex(KEY_OUTCOME);

			//first row of table where cursor will start traversal i.e. moveToFirst
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

				result += " Policy Type: \n " + c.getString(polName) + "\n Subject: \n " + c.getString(sub) + 
						"\n Target: \n " + c.getString(tar) +"\n Outcome: \n " + c.getString(outcome) + "\n\n";
			}	
			return result;
		}

		else{
			String result = "There are currently no policy instances for the app defined in the database";
			return result;
		}
	}

	//Checks if the definition for the policy type exists in the database
	public Boolean PolicyTypeExists() throws SQLException{

		Boolean decision = true;
		String[] cols = new String[] {KEY_POLICY_NAME, KEY_DEFAULT_OUTCOME, KEY_OUTCOME_PRIORITY};
		//Cursor c = ourDatabase.rawQuery("Select * from " + POLICYTYPE_TABLE + " where " + KEY_POLICY_NAME + "=?" + "'" + col + "'",null);
		Cursor c = ourDatabase.query(POLICY_TYPES_TABLE, cols, null, null, null, null, null);
		//Log.d("Policy names ", "" + c.getCount());

		if(c.getCount() == 0){
			decision = false;
			return decision;
		}

		else{
			decision = true;
			return decision;
		}

		/*Log.i("Decision:",decision.toString());
		return decision;*/
	}

	//Checks if the definition for the policy instance for the requested app exists in the database
	public Boolean AppPolicyInstanceExists(String app) throws SQLException{

		Boolean decision = true;
		String[] cols = new String[] {KEY_POLICY_NAME, KEY_SUBJECT, KEY_TARGET, KEY_OUTCOME};
		String[] cols1 = {app};
		//Cursor c = ourDatabase.rawQuery("Select * from " + POLICYTYPE_TABLE + " where " + KEY_POLICY_NAME + "=?" + "'" + col + "'",null);
		Cursor c = ourDatabase.query(POLICY_INSTANCES_TABLE, cols, KEY_SUBJECT + "=?", cols1, null, null, null);
		//Log.d("Policy names ", "" + c.getCount());

		if(c.getCount() == 0){
			decision = false;
			return decision;
		}

		else{
			decision = true;
			return decision;
		}

		/*Log.i("Decision:",decision.toString());
			return decision;*/
	}
	//Deletes all the rows of the permission-policy table
	public void deletePermPolEntry() throws SQLException{
		ourDatabase.delete(PERMISSION_POLICY_TABLE, null, null);		
	}

	//Deletes all the rows of the policy-type table
	public void deletePolType() throws SQLException{
		ourDatabase.delete(POLICY_TYPES_TABLE, null, null);		
	}

	//Deletes the selected policy type definition from the policy-type table
	public void deletePolTypeEntry(String policy) throws SQLException{

		if(PolicyTypeCheck(policy)){
			ourDatabase.delete(POLICY_TYPES_TABLE, KEY_POLICY_NAME + "=" + "'" + policy + "'", null);
		}		

	}

	//Checks if the selected policy type has an entry in the policy-type table
	public Boolean PolicyTypeCheck(String polName) throws SQLException{

		Boolean decision = true;
		Log.i("Policy Name:",polName);

		String col[] = {polName};


		String[] cols = new String[] {KEY_POLICY_NAME, KEY_DEFAULT_OUTCOME, KEY_OUTCOME_PRIORITY};
		//Cursor c = ourDatabase.rawQuery("Select * from " + POLICYTYPE_TABLE + " where " + KEY_POLICY_NAME + "=?" + "'" + col + "'",null);
		Cursor c = ourDatabase.query(POLICY_TYPES_TABLE, cols, KEY_POLICY_NAME +"=?", col, null, null, null);
		//Log.d("Policy names ", "" + c.getCount());

		if(c.getCount() == 0){
			decision = false;
			return decision;
		}

		else{
			decision = true;
			return decision;
		}

		/*Log.i("Decision:",decision.toString());
		return decision;*/
	}

	//Checks if the selected
	//Returns a defined policy type's default outcome on to the custom list view 
	public String getDefaultOutcome(String polName) throws SQLException{


		Log.i("Policy Name:",polName);

		String col[] = {polName};


		String[] cols = new String[] {KEY_DEFAULT_OUTCOME};
		//Cursor c = ourDatabase.rawQuery("Select * from " + POLICYTYPE_TABLE + " where " + KEY_POLICY_NAME + "=?" + "'" + col + "'",null);
		Cursor c = ourDatabase.query(POLICY_TYPES_TABLE, cols, KEY_POLICY_NAME +"=?", col, null, null, null);


		int outcome = c.getColumnIndex(KEY_DEFAULT_OUTCOME);
		String result = "";

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

			result += c.getString(outcome);
		}	

		return result;

	}

	//Where condition to check if primary key is not violated in PolicyInstances Table
	public boolean policyInstancesIntegrity(String polname, String subject, String target){

		boolean decision = true;

		String[] cols = new String [] {KEY_POLICY_NAME, KEY_SUBJECT, KEY_TARGET, KEY_OUTCOME};
		String where = KEY_POLICY_NAME + "=? AND " + KEY_SUBJECT + "=? AND " + KEY_TARGET + "=?";
		String[] cols1 = {polname, subject, target};

		Cursor c = ourDatabase.query(POLICY_INSTANCES_TABLE, cols, where, cols1, null, null, null);
		Log.d("query", " " + c.getCount());

		//if such a record exists in the database then return true
		if(c.getCount() != 0){
			decision = true;
		}

		//if it doesn't exist in the database 
		else{
			decision = false;
		}

		return decision;
	}

	//Updates the specifics of a specified policy instance defined in the PolicyInstance Table
	public void updatePolInstanceEntry(String polName, String subject, String target, String outcome) throws SQLException{

		String where = KEY_POLICY_NAME + "=" + "'" + polName + "' AND " + KEY_SUBJECT + "=" + "'" + subject + "' AND " + KEY_TARGET + "=" + "'" + target + "'";
		
		//Update only possible if record exists in the PolicyInstances Table
		//if(policyInstancesIntegrity(polName, subject, outcome)){
			ContentValues cvUpdate = new ContentValues();
			cvUpdate.put(KEY_OUTCOME, outcome);
			ourDatabase.update(POLICY_INSTANCES_TABLE, cvUpdate, where, null);
		//}
	}
	
	//Deletes the specifics of a specified policy instance defined in the PolicyInstance Table
		public void deletePolInstanceEntry(String polName, String subject, String target) throws SQLException{
			
			String where = KEY_POLICY_NAME + "=" + "'" + polName + "' AND " + KEY_SUBJECT + "=" + "'" + subject + "' AND " + KEY_TARGET + "=" + "'" + target + "'";
			ourDatabase.delete(POLICY_INSTANCES_TABLE, where, null);
		}
		
	//Updates the specifics of a specified policy type defined in the policy-type table 
	public void updatePolTypeEntry(String polName, String defltOutcome, String priority) throws SQLException{

		if(PolicyTypeCheck(polName)){
			ContentValues cvUpdate = new ContentValues();
			cvUpdate.put(KEY_DEFAULT_OUTCOME, defltOutcome);
			cvUpdate.put(KEY_OUTCOME_PRIORITY, priority);
			ourDatabase.update(POLICY_TYPES_TABLE, cvUpdate, KEY_POLICY_NAME + "=" + "'" + polName + "'", null);
		}
	}
}
