package com.android.project;

import android.net.Uri;
import android.view.View;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends Activity
{
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		Button Button1=(Button)findViewById(R.id.button1);
		Button1.setOnClickListener(new View.OnClickListener() 
		{	
			public void onClick(View view)
			{
				String uri="http://www.google.com";
				Intent intent = new Intent
				(
					Intent.ACTION_VIEW, 
					Uri.parse(uri)
				);
				startActivity(intent);
			}
		});
		Button Button2=(Button)findViewById(R.id.button2);
		Button2.setOnClickListener(new View.OnClickListener() 
		{	
			public void onClick(View view) 
			{
				String uri="tel:3217329405";
				Intent intent = new Intent
				(
					Intent.ACTION_VIEW, 
					Uri.parse(uri)
				);
				startActivity(intent);
			}
		});
	}
}
