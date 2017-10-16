package com.android.project;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
		relativeLayout.setBackgroundColor(Color.BLACK);
		TextView textView=(TextView)findViewById(R.id.textView5);
		textView.setText(getResources().getString(R.string.chinese));
	}
}