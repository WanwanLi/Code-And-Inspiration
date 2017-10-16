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
	public void startService(View view) 
	{
		this.startService(new Intent(getBaseContext(), MainService.class));
	}
	public void stopService(View view) 
	{
		this.stopService(new Intent(getBaseContext(), MainService.class));
	}
}