package com.android.project;

import android.graphics.Color;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;

public class EditImage
{
	Bitmap bitmap;
	int height, width;
	int[][] pixels, image;
	public EditImage(ImageView imageView)
	{
		Bitmap bitmap=getBitmap(imageView);
		this.height=bitmap.getHeight();
		this.width=bitmap.getWidth();
		this.pixels=new int[height][width];
		this.image=new int[height][width];
		for (int i=0; i<height; i++) 
		{
			for (int j=0; j<width; j++) 
			{
				this.image[i][j]=bitmap.getPixel(j, i);
				this.pixels[i][j]=this.image[i][j];
			}
		}
		this.bitmap=createBitmap(bitmap);
	}
	public void resetColor()
	{
		for (int i=0; i<height; i++) 
		{
			for (int j=0; j<width; j++) 
			{
				this.pixels[i][j]=this.image[i][j];
			}
		}
	}
	public void translateColor(double redTranslate, double greenTranslate, double blueTranslate, double alphaTranslate)
	{
		for (int i=0; i<height; i++) 
		{
			for (int j=0; j<width; j++) 
			{
				int red=(int)(clamp(Color.red(pixels[i][j])+redTranslate, 0, 255));
				int green=(int)(clamp(Color.green(pixels[i][j])+greenTranslate, 0, 255));
				int blue=(int)(clamp(Color.blue(pixels[i][j])+blueTranslate, 0, 255));
				int alpha=(int)(clamp(Color.alpha(pixels[i][j])+alphaTranslate, 0, 255));
				this.pixels[i][j]=Color.argb(alpha, red, green, blue);
			}
		}
	}
	public void scaleColor(double redScale, double greenScale, double blueScale, double alphaScale)
	{
		for (int i=0; i<height; i++) 
		{
			for (int j=0; j<width; j++) 
			{
				int red=(int)(clamp(Color.red(pixels[i][j])*redScale, 0, 255));
				int green=(int)(clamp(Color.green(pixels[i][j])*greenScale, 0, 255));
				int blue=(int)(clamp(Color.blue(pixels[i][j])*blueScale, 0, 255));
				int alpha=(int)(clamp(Color.alpha(pixels[i][j])*alphaScale, 0, 255));
				this.pixels[i][j]=Color.argb(alpha, red, green, blue);
			}
		}
	}
	public void edit(ImageView imageView)
	{
		for (int i=0; i<height; i++) 
		{
			for (int j=0; j<width; j++) 
			{
				this.bitmap.setPixel(j, i, pixels[i][j]);
			}
		}
		imageView.setImageBitmap(this.bitmap);
	}
	Bitmap getBitmap(ImageView imageView)
	{
		return ((BitmapDrawable)imageView.getDrawable()).getBitmap();
	}
	Bitmap createBitmap(Bitmap bitmap)
	{
		return Bitmap.createBitmap(width, height, bitmap.getConfig());
	}
	double clamp(double value, double min, double max)
	{
		return value<min?min:value>max?max:value;
	}
}
