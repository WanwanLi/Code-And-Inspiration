import java.io.*;
import java.util.*;
import java.text.*;
import org.w3c.dom.*;
import java.util.regex.*;
import javax.xml.parsers.*;

public class JavaAndXMLParser
{
	public static void main(String[] argv) 
	{
		XMLParser xmlParser=new XMLParser();
		xmlParser.srcDateFormat="M/d/yyyy h:m:s a";
		xmlParser.destDateFormat="dd-MM-yyyy kk:mm:ss";
		xmlParser.dateTypes=new String[]{"lastUpdate", "timestamp"};
		xmlParser.parse("json", argv[0], argv[1]);
	}
}
class XMLParser
{
	Document document;
	public String[] dateTypes;
	public String srcDateFormat;
	public String destDateFormat;
	public void parse(String format, String srcDir, String destDir)
	{
		File[] files=new File(srcDir).listFiles();
		for(File file : files)
		{
			String fileName=file.getName();
			String fileType=getFileType(fileName);
			if(!fileType.equals("xml"))continue;
			this.open(srcDir+"\\"+fileName);
			fileName=getFileName(fileName)+".";
			this.save(destDir+"\\"+fileName+format);
			System.out.println(fileName+"xml parsed to "+format);
		}
	}
	public void open(String fileName)
	{
		try
		{
			this.document=DocumentBuilderFactory.newInstance().
			newDocumentBuilder().parse(new FileInputStream(fileName));
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void println()
	{
		if(this.document!=null)this.print(document.getDocumentElement());
	}
	void print(Node node)
	{
		System.out.print(node.getNodeName());
		NodeList childNodes=node.getChildNodes();
		if(childNodes.getLength()>1)System.out.println();
		else System.out.println(":" +getTextContent(node));
		NamedNodeMap attributes=node.getAttributes();
		for(int i=0; i<attributes.getLength(); i++) 
		{
			Node attribute=attributes.item(i);
			System.out.print(attribute.getNodeName()+":");
			System.out.println(getNodeValue(attribute));
		}
		if(childNodes.getLength()>1)
		{
			for(int i=0; i<childNodes.getLength(); i++) 
			{
				Node childNode=childNodes.item(i);
				if(childNode.getNodeType()==Node.ELEMENT_NODE)this.print(childNode);
			}
		}
	}
	public void save(String fileName)
	{
		try
		{
			String fileType=getFileType(fileName);
			PrintWriter printWriter=new PrintWriter(fileName);
			if(fileType.equals("json"))
			{
				printWriter.println("{");
				this.writeJsonFile(printWriter, document.getDocumentElement(), "  ", false);
				printWriter.println("}");
			}
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	void writeJsonFile(PrintWriter printWriter, Node node, String space, boolean hasComma)
	{
		String s=space, l="\"", c=hasComma?",":"";
		NodeList childNodes=node.getChildNodes();
		if(childNodes.getLength()==1)
		{
			printWriter.print(s+l+node.getNodeName()+l+" : ");
			printWriter.println(l+getTextContent(node)+l+c); return;
		}
		printWriter.println(s+l+node.getNodeName()+l+" : ");
		printWriter.println(s+"{");
		NamedNodeMap attributes=node.getAttributes();
		int lastElementNode=getLastElementNode(childNodes);
		int length=attributes.getLength();
		for(int i=0; i<length; i++) 
		{
			Node attribute=attributes.item(i);
			printWriter.print(s+s+s+l+attribute.getNodeName()+l+" : ");
			printWriter.print(l+getNodeValue(attribute)+l);
			if(lastElementNode<0)printWriter.println(i<length-1?",":"");
			else printWriter.println(",");
		}
		for(int i=0; i<childNodes.getLength(); i++) 
		{
			Node childNode=childNodes.item(i);
			if(childNode.getNodeType()==Node.ELEMENT_NODE)
			{
				this.writeJsonFile(printWriter, childNode, s+s, i!=lastElementNode);
			}
		}
		printWriter.println(s+"}"+c);
	}
	int getFirstElementNode(NodeList nodeList)
	{
		for(int i=0; i<nodeList.getLength(); i++) 
		{
			Node node=nodeList.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE)return i;
		}
		return 0;
	}
	int getLastElementNode(NodeList nodeList)
	{
		int lastElementNode=-1;
		for(int i=0; i<nodeList.getLength(); i++) 
		{
			Node node=nodeList.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE)lastElementNode=i;
		}
		return lastElementNode;
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
	String getTextContent(Node node)
	{
		return isDateType(node.getNodeName())?parseDate(node.getTextContent()):node.getTextContent();
	}
	String getNodeValue(Node node)
	{
		return isDateType(node.getNodeName())?parseDate(node.getNodeValue()):node.getNodeValue();
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


