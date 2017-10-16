package com.android.project;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class MainActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		TextView TextView1 = (TextView) findViewById(R.id.TextView1);
		TextView1.setText("Java And TextView");
	}
}