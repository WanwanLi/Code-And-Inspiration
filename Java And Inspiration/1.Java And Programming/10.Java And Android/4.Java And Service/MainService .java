package com.android.project;

import android.os.IBinder;
import android.app.Service;
import android.widget.Toast;
import android.content.Intent;

class MainService extends Service 
{
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		return START_STICKY;
	}
	public void onDestroy() 
	{
		super.onDestroy();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}
}