import java.io.PrintWriter;
import java.io.InputStream;
import java.io.FileInputStream;

class XMLTable
{
	public String[][] items;
	public String[] attributes;
	public String tableName, itemName;
	public String[] tableAttributes, itemAttributes;
	public XMLTable(String tableName, String[] tableAttributes, String itemName, String[] itemAttributes)
	{
		this.itemName=itemName;
		this.tableName=tableName;
		this.tableAttributes=tableAttributes;
		this.itemAttributes=itemAttributes;
		try 
		{
			InputStream xmlInput=new FileInputStream(tableName+".xml");
			XMLStack xmlStack=new XMLStack(xmlInput);
			this.runXMLStack(xmlStack);
		}
		catch(Exception e) {e.printStackTrace();}
	}
	void runXMLStack(XMLStack xmlStack)
	{
		xmlStack.getElements(tableName);
		xmlStack.getElements(0);
		this.attributes=new String[tableAttributes.length];
		for(int i=0; i<attributes.length; i++)
		{
			this.attributes[i]=xmlStack.getAttribute(tableAttributes[i]);
		}
		xmlStack.getElements(itemName);
		this.items=new String[xmlStack.length()][itemAttributes.length];
		for(int i=0; i<items.length; i++)
		{
			xmlStack.pushElements();
			xmlStack.getElements(i);
			for(int j=0; j<itemAttributes.length; j++)
			{
				this.items[i][j]=xmlStack.getAttribute(itemAttributes[j]);
			}
			xmlStack.popElements();
		}
	}
	public void printAttributes()
	{
		System.out.print("Table["+tableName+"]={ ");
		for(int i=0; i<attributes.length; i++)
		{
			System.out.print(attributes[i]+", ");
		}
		System.out.println(", ... }");
	}
	public void printItems()
	{
		System.out.println("Item["+itemName+"]=");
		for(int i=0; i<items.length; i++)
		{
			System.out.print("{");
			for(int j=0; j<itemAttributes.length; j++)
			{
				System.out.print(items[i][j]+", ");
			}
			System.out.println(", ... }");
		}
	}
	public void writeJSONFile()
	{
		try
		{
			PrintWriter printWriter=new PrintWriter(tableName+".json");
			printWriter.println("{\n\""+tableName+"\":["); int i, j;
			for(i=0; i<attributes.length; i++)
			{
				printWriter.println(tableAttributes[i]+" : "+attributes[i]+", ");
			}
			printWriter.println("\""+itemName+"\":[");
			for(i=0; i<items.length-1; i++)
			{
				printWriter.println("{");
				for(j=0; j<itemAttributes.length-1; j++)
				{
					printWriter.println(itemAttributes[j]+" : "+items[i][j]+", ");
				}
				printWriter.println(itemAttributes[j]+" : "+items[i][j]+"},");
			}
			printWriter.println("{");
			for(j=0; j<itemAttributes.length-1; j++)
			{
				printWriter.println(itemAttributes[j]+" : "+items[i][j]+", ");
			}
			printWriter.println(itemAttributes[j]+" : "+items[i][j]+"}");
			printWriter.println("]\n}");
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}