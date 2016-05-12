package com.ibc.android.demo.appslist.adapter;

import java.util.List;
import com.ibc.android.demo.appslist.activity.R;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ApkAdapter extends BaseAdapter {

	List<PackageInfo> packageList;
	//List<ApplicationInfo> packageList;
    Activity context;
    PackageManager packageManager;
    
    public ApkAdapter(Activity context, List<PackageInfo> packageList2,
            PackageManager packageManager) {
        super();
        this.context = context;
        this.packageList = packageList2;
        this.packageManager = packageManager;
    }
    
    private class ViewHolder {
        TextView apkName;
    }
 
    public int getCount() {
        return packageList.size();
    }
 
    public Object getItem(int position) {
        return packageList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();
 
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.apklist_item, null);
            holder = new ViewHolder();
 
            holder.apkName = (TextView) convertView.findViewById(R.id.appname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        //List<ApplicationInfo> list_user_app = getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        //ApplicationInfo applicationInfo = (ApplicationInfo) getItem(position);
        PackageInfo packageInfo = (PackageInfo) getItem(position);
        
        //gets the app's icon to be shown on the list
        Drawable appIcon = packageManager							
                .getApplicationIcon(packageInfo.applicationInfo);
        //gets the app's name to be displayed on the list
        String appName = packageManager.getApplicationLabel(packageInfo.    
                applicationInfo).toString();
        appIcon.setBounds(0, 0, 40, 40);
        holder.apkName.setCompoundDrawables(appIcon, null, null, null);
        holder.apkName.setCompoundDrawablePadding(15);
        holder.apkName.setText(appName);
 
        return convertView;
    }
	
}
