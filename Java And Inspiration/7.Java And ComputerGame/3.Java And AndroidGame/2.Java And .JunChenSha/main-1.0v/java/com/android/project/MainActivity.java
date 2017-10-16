package com.android.project;

import android.view.View;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageSwitcher;
import android.content.pm.ActivityInfo;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.app.ActionBar.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity implements OnClickListener, ViewFactory
{
	EditText editText1;
	TextView textView1;
	TianShen tianShen1;
	ImageView imageView1;
	Button button1, button2;
	JunChenSha junChenSha1;
	ImageSwitcher imageSwitcher1;
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.button1=(Button)findViewById(R.id.button1);
		this.button2=(Button)findViewById(R.id.button2);
		this.editText1=(EditText)findViewById(R.id.editText1);
		this.textView1=(TextView)findViewById(R.id.textView1);
		this.imageSwitcher1=(ImageSwitcher)findViewById(R.id.imageSwitcher1);
		this.tianShen1=new TianShen(true, editText1, textView1, getResources(), imageSwitcher1, this);
		this.junChenSha1=new JunChenSha(tianShen1);
		this.button1.setOnClickListener(this); 
		this.button2.setOnClickListener(this);
		this.imageSwitcher1.setFactory(this);
		this.imageSwitcher1.setOnClickListener(this);
		junChenSha1.show();
	}
	public void onBackPressed() 
	{
		new AlertDialog.Builder(this).
		setIcon(android.R.drawable.ic_dialog_alert).
		setTitle(R.string.on_back_pressed).
		setMessage(R.string.close_activity).
		setPositiveButton
		(
			R.string.yes, 
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which) 
				{
					finish();    
				}
			}
		).
		setNegativeButton(R.string.no, null).show();
	}
	public View makeView() 
	{
		this.imageView1=new ImageView(getApplicationContext());
		this.imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
		this.imageView1.setLayoutParams
		(
			new ImageSwitcher.LayoutParams
			(
				LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT
			)
		);
		return this.imageView1;
	}
	public void onClick(View view) 
	{
		if(view.getId()==R.id.button1)
		{
			this.junChenSha1.prev();
		}
		if(view.getId()==R.id.button2)
		{
			this.junChenSha1.next();
		}
		if(view.getId()==R.id.imageSwitcher1)
		{
			this.junChenSha1.showAllPlayers();
		}
	}
	public void makeText(String text)
	{
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
}

