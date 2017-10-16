package com.android.project;

import java.util.Calendar;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;
import java.text.SimpleDateFormat;
import android.content.res.TypedArray;

public class TimeView extends TextView
{
	public TimeView(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		TypedArray typedArray = context.obtainStyledAttributes
		(
			attributeSet, R.styleable.TimeView
		);
		for(int i = 0; i < typedArray.getIndexCount(); ++i) 
		{
			int index = typedArray.getIndex(i);
			if(index == R.styleable.TimeView_title)
			{
				this.setTime(typedArray.getString(index));
			}
			else if(index == R.styleable.TimeView_useShadow)
			{
				this.setColor(typedArray.getBoolean(index, false));
			}
		}
	}
	void setTime(String title)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
		String time = dateFormat.format(Calendar.getInstance().getTime());
		this.setText(title+" "+time);
	}
	void setColor(boolean useShadow)
	{
		if(useShadow)
		{
			this.setShadowLayer(4, 2, 2, Color.GREEN);
			this.setBackgroundColor(Color.rgb(125, 125, 255));
		}
		else this.setBackgroundColor(Color.BLUE);
	}
}