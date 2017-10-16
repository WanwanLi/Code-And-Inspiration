package com.android.project;

import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

class MainReceiver extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent) 
	{
		Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
	}
}