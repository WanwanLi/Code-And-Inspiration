import java.io.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;

public class JavaJsonParser
{
	public static void main(String[] argv) 
	{
		JsonParser jsonParser=new JsonParser();
		jsonParser.srcDateFormat="M/d/yyyy h:m:s a";
		jsonParser.missedAttribute="GO_GO";
		jsonParser.destDateFormat="dd-MM-yyyy kk:mm:ss";
		jsonParser.dateTypes=new String[]{"TPS DATE", "START TIME", "FINISH TIME"};
		jsonParser.parse("cmp", argv[0], argv[1]);
	}
}
class JsonParser
{
	public String missedAttribute;
	public String[] dateTypes;
	public String srcDateFormat;
	public String destDateFormat;
	String[] dataItemAttribute;
	LinkedList<String>[] dataItemTable;
	LinkedList<String> attributeNameList;
	LinkedList<String> attributeValueList;
	boolean isDataItem, isDataItemTableCreated;
	public void parse(String format, String srcDir, String destDir)
	{
		File[] files=new File(srcDir).listFiles();
		for(File file : files)
		{
			String fileName=file.getName();
			String fileType=getFileType(fileName);
			if(!fileType.equals(format))continue;
			this.open(srcDir+"\\"+fileName);
			fileName=getFileName(fileName)+".";
			this.save(destDir+"\\"+fileName+".json");
			System.out.println(fileName+format+" parsed to json.");
		}
	}
	public void open(String fileName)
	{
		this.attributeNameList=new LinkedList<String>();
		this.attributeValueList=new LinkedList<String>();
		this.isDataItemTableCreated=false;
		this.isDataItem=false; 
		try
		{
			BufferedReader bufferedReader=new BufferedReader(new FileReader(fileName));
			for(String string; (string=bufferedReader.readLine())!=null; System.out.print(""))
			{
				if(string.equals(" "))this.isDataItem=true;
				if(!isDataItem)this.addAttribute(string);
				else
				{
					if(isDataItemTableCreated)this.addDataItem(string);
					else if(!string.equals(" "))this.createDataItemTable(string);	
				}
			}
			bufferedReader.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void save(String fileName)
	{
		try
		{
			String fileType=getFileType(fileName);
			PrintWriter printWriter=new PrintWriter(fileName);
			if(fileType.equals("json"))this.write(printWriter);
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	void write(PrintWriter printWriter)
	{
		String s="  ", l="\"";
		printWriter.println("{");
		for(int i=0; i<attributeNameList.size(); i++)
		{
			String attributeValue=getAttributeValuePair
			(
				attributeNameList.get(i),
				attributeValueList.get(i)
			);
			printWriter.println(s+attributeValue+",");
		}
		printWriter.println(s+l+"item"+l+" : ");
		printWriter.println(s+"[");
		for(int i=0; i<dataItemTable[0].size(); i++)
		{

			printWriter.println(s+s+"{");
			for(int j=0; j<dataItemAttribute.length; j++)
			{
				String c=j<dataItemAttribute.length-1?",":"";
				String attributeValue=getAttributeValuePair
				(
					dataItemAttribute[j],
					dataItemTable[j].get(i)
				);
				printWriter.println(s+s+s+attributeValue+c);
			}
			String c=i<dataItemTable[0].size()-1?",":"";
			printWriter.println(s+s+"}"+c);
		}
		printWriter.println(s+"]");
		printWriter.println("}");
	}
	void createDataItemTable(String string)
	{
		this.dataItemAttribute=string.split("\t");
		int length=this.dataItemAttribute.length;
		this.dataItemTable=new LinkedList[length];
		for(int i=0; i<length; i++)
		{
			this.dataItemTable[i]=new LinkedList<String>();
		}
		this.isDataItemTableCreated=true;
	}
	void alterDataItemTable()
	{
		int length=this.dataItemAttribute.length;
		String[] newDataItemAttribute=new String[length+1];
		for(int i=0; i<length; i++)
		{
			newDataItemAttribute[i]=dataItemAttribute[i];
		}
		newDataItemAttribute[length++]=missedAttribute;
		this.dataItemTable=new LinkedList[length];
		for(int i=0; i<length; i++)
		{
			this.dataItemTable[i]=new LinkedList<String>();
		}
		this.dataItemAttribute=newDataItemAttribute;
	}
	void addAttribute(String string)
	{
		String[] strings=string.split(":");
		if(strings.length>2)
		{
			for(int i=2; i<strings.length; i++)strings[1]+=":"+strings[i];
		}
		this.attributeNameList.add(tr(strings[0]));
		this.attributeValueList.add(tr(strings[1]));
	}
	void addDataItem(String string)
	{
		String[] dataItem=string.split("\t");
		if(dataItemTable.length<dataItem.length)this.alterDataItemTable();
		for(int i=0; i<dataItemTable.length; i++)this.dataItemTable[i].add(dataItem[i]);
	}
	String getAttributeValuePair(String attribute, String value)
	{
		String l="\""; return l+attribute+l+" : " +l+(isDateType(attribute)?d(parseDate(value)):value)+l;
	}
	String d(String string)
	{
		String[] s=string.split(" ");
		return s[0]+"T"+s[1]+".000Z";
	}
	String tr(String string)
	{
		int start=0, end=string.length()-1;
		while(start<=end&&string.charAt(start++)==' '); start--;
		while(start<=end&&string.charAt(end--)==' '); end++;
		return start<=end?string.substring(start, end+1):" ";
	}
	String getFileName(String fileName)
	{
		return fileName.split(Pattern.quote("."))[0];
	}
	String getFileType(String fileName)
	{
		String[] strings=fileName.split(Pattern.quote("."));
		return strings[strings.length-1].toLowerCase();
	}
	boolean isDateType(String name)
	{
		if(dateTypes==null)return false;
		for(String s : dateTypes)if(s.equals(name))return true;
		return false;
	}
	String parseDate(String text)
	{
		try
		{
			Date date=new SimpleDateFormat(srcDateFormat).parse(text);
			return new SimpleDateFormat(destDateFormat).format(date);
		}
		catch(Exception e){e.printStackTrace(); return text; }
	}
}
