package com.android.project;

import java.io.InputStream;
import java.util.LinkedList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParser
{
	Node node;
	Element element;
	NodeList nodeList;
	LinkedList linkedNodeList;
	LinkedList linkedElementList;
	public XMLParser(InputStream inputStream)
	{
		this.linkedNodeList=new LinkedList();
		this.linkedElementList=new LinkedList();
		try
		{
			Document document=DocumentBuilderFactory.newInstance().
			newDocumentBuilder().parse(inputStream);
			this.element=document.getDocumentElement();
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void pushElements()
	{
		this.linkedElementList.push(this.element);
		this.linkedNodeList.push(this.nodeList);
	}
	public void popElements()
	{
		this.element=(Element)this.linkedElementList.pop();
		this.nodeList=(NodeList)this.linkedNodeList.pop();
	}
	public void getElements(String tagName)
	{
		this.nodeList=this.element.getElementsByTagName(tagName);
	}
	public void getElements(int itemIndex)
	{
		Node node=this.nodeList.item(itemIndex);
		this.element=(Element)node;
		this.nodeList=node.getChildNodes();
	}
	public String getValue(int itemIndex, String tagName)
	{
		Node node=this.nodeList.item(itemIndex);
		if(node.getNodeType()==Node.ELEMENT_NODE)
		{
			NodeList nodeList=((Element)node).getElementsByTagName(tagName);
			return nodeList.item(0).getChildNodes().item(0).getNodeValue();
		}
		else return "error_value";
	}
	public int length()
	{
		return this.nodeList==null?0:this.nodeList.getLength();
	}
}
