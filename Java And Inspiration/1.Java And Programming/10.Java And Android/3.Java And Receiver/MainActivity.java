package com.android.project;

import android.view.View;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity 
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
	}
	public void broadcastIntent(View view)
	{
		Intent intent = new Intent();
		intent.setAction("com.android.project.CUSTOM_INTENT"); 
		this.sendBroadcast(intent);
	}
}