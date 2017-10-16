package com.android.project;

import android.view.View;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.view.View.OnClickListener;
import android.graphics.Typeface;

public class MainActivity extends Activity implements OnClickListener
{
	TextView textView1;
	Button button1, button2;
	EditText editText1, editText2;
	ClipboardManager clipboardManager;
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		this.button1=(Button)findViewById(R.id.button1);
		this.button2=(Button)findViewById(R.id.button2);
		this.editText1=(EditText)findViewById(R.id.editText1);
		this.editText2=(EditText)findViewById(R.id.editText2);
		this.textView1=(TextView)findViewById(R.id.textView1);
		this.textView1.setTypeface(Typeface.createFromAsset(getAssets(), "font.ttf"));
		this.clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		this.button1.setOnClickListener(this); this.button2.setOnClickListener(this);
	}
	public void onClick(View view) 
	{
		if(view.getId()==R.id.button1)
		{
			String text = this.editText1.getText().toString();
			ClipData clipData=ClipData.newPlainText("text", text);
			this.clipboardManager.setPrimaryClip(clipData);
			this.makeText("Text Copied");
		}
		if(view.getId()==R.id.button2)
		{
			Item item = this.clipboardManager.getPrimaryClip().getItemAt(0);
			this.editText2.setText(item.getText().toString());
			this.makeText("Text Pasted");
		}
	}
	void makeText(String text)
	{
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
}

