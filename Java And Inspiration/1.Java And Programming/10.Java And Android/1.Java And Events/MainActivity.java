package com.android.project;

import android.util.Log;
import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity 
{
	String msg = "Android: ";
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		Log.d(msg, "The onCreate() event");
	}
	public void onStart()
	{
		super.onStart();
		Log.d(msg, "The onStart() event");
	}
	public void onResume() 
	{
		super.onResume();
		Log.d(msg, "The onResume() event");
	}
	protected void onPause() 
	{
		super.onPause();
		Log.d(msg, "The onPause() event");
	}
	public void onStop() 
	{
		super.onStop();
		Log.d(msg, "The onStop() event");
	}
	public void onDestroy() 
	{
		super.onDestroy();
		Log.d(msg, "The onDestroy() event");
	}
}