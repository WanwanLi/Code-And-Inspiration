package com.android.project;

import android.view.View;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageSwitcher;
import android.view.View.OnClickListener;
import android.app.ActionBar.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity implements OnClickListener, ViewFactory
{
	TextView textView1;
	EditImage editImage1;
	ImageView imageView1;
	ImageSwitcher imageSwitcher1;
	int[] images; int time=0, imageId=1; 
	EditText editText1, editText2, editText3, editText4;
	Button button1, button2, button3, button4, button5;
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		this.button1=(Button)findViewById(R.id.button1);
		this.button2=(Button)findViewById(R.id.button2);
		this.button3=(Button)findViewById(R.id.button3);
		this.button4=(Button)findViewById(R.id.button4);
		this.button5=(Button)findViewById(R.id.button5);
		this.editText1=(EditText)findViewById(R.id.editText1);
		this.editText2=(EditText)findViewById(R.id.editText2);
		this.editText3=(EditText)findViewById(R.id.editText3);
		this.editText4=(EditText)findViewById(R.id.editText4);
		this.textView1=(TextView)findViewById(R.id.textView1);
		this.imageSwitcher1=(ImageSwitcher)findViewById(R.id.imageSwitcher1);
		this.button1.setOnClickListener(this); 
		this.button2.setOnClickListener(this);
		this.button3.setOnClickListener(this);
		this.button4.setOnClickListener(this);
		this.button5.setOnClickListener(this);
		this.imageSwitcher1.setFactory(this);
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
		this.images=new int[]
		{
			R.drawable.image_prev,
			R.drawable.image_main,
			R.drawable.image_next
		};
		this.imageView1.setImageResource(getImage());
		this.editImage1=new EditImage(imageView1);
           		return this.imageView1;
	}
	public void onClick(View view) 
	{
		double red=getDouble(editText1);
		double green=getDouble(editText2);
		double blue=getDouble(editText3);
		double alpha=getDouble(editText4);
		if(view.getId()==R.id.button1)
		{
			if(isValidInput4d(red, green, blue, alpha))
			{
				this.editImage1.scaleColor
				(
					red,  green, blue, alpha
				);
				this.editImage1.edit(imageView1);
			}
			else this.makeText("Error input");
		}
		if(view.getId()==R.id.button2)
		{
			if(isValidInput4d(red, green, blue, alpha))
			{
				this.editImage1.translateColor
				(
					red,  green, blue, alpha
				);
				this.editImage1.edit(imageView1);
			}
			else this.makeText("Error input");
		}
		if(view.getId()==R.id.button3)
		{
			this.makeText("Image reseted");
			this.editImage1.resetColor();
			this.editImage1.edit(imageView1);
		}
		if(view.getId()==R.id.button4)
		{
			int newImage=this.getPreviousImage();
			this.imageSwitcher1.setImageResource(newImage);
			if(time++!=0)this.imageSwitcher1.setImageResource(newImage);
			this.editImage1=new EditImage(imageView1);
		}
		if(view.getId()==R.id.button5)
		{
			int newImage=this.getNextImage();
			this.imageSwitcher1.setImageResource(newImage);
			if(time++!=0)this.imageSwitcher1.setImageResource(newImage);
			this.editImage1=new EditImage(imageView1);
		}
	}
	double getDouble(EditText editText)
	{
		String value=editText.getText().toString();
		try
		{
			return Double.parseDouble(value);
		}
		catch(Exception e)
		{
			editText.setText("0"); 
			return ERROR_VALUE;
		}
	}
	int getImage()
	{
		return this.images[this.imageId];
	}
	int getNextImage()
	{
		this.imageId=(imageId+1)%images.length;
		return this.images[this.imageId];
	}
	int getPreviousImage()
	{
		this.imageId=(images.length+imageId-1)%images.length;
		return this.images[this.imageId];
	}
	boolean isValidInput4d(double input1, double input2, double input3, double input4)
	{
		return input1!=ERROR_VALUE&&input2!=ERROR_VALUE&&input3!=ERROR_VALUE&&input4!=ERROR_VALUE;
	}
	void makeText(String text)
	{
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	final double ERROR_VALUE = Double.MAX_VALUE;
}

