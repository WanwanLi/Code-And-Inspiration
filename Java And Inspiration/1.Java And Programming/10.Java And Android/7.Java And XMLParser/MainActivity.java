package com.android.project;

import java.io.InputStream;
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
		TextView textView=(TextView)findViewById(R.id.textView1);
		try 
		{
			String xmlFile="file.xml",result="XMLParser:"+xmlFile;
			InputStream xmlInput=getAssets().open(xmlFile);
			XMLParser xmlParser=new XMLParser(xmlInput);
			result+=this.runXMLParser(xmlParser, "google");
			result+=this.runXMLParser(xmlParser, "apple");
			textView.setText(result);
		}
		catch(Exception e) {e.printStackTrace();}
	}
	String runXMLParser(XMLParser xmlParser, String company)
	{
		String result="\n"; xmlParser.getElements(company);
		for(int i=0; i<xmlParser.length(); i++)
		{
			result+="\n"+company+".com["+i+"]={";
			xmlParser.pushElements();
			xmlParser.getElements(i);
			xmlParser.getElements("employee");
			for(int j=0; j<xmlParser.length(); j++)
			{
				result+=xmlParser.getValue(j,"name")+",  ";
			}
			xmlParser.popElements();
			result+="...  };\n";
		}
		return  result;
	}
}



