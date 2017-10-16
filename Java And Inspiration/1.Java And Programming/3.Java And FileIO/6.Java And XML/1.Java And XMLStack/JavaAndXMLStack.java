import java.io.InputStream;
import java.io.FileInputStream;

public class JavaAndXMLStack
{
	public static void main(String[] argv) 
	{
		try 
		{
			String xmlFile=argv[0], result="XMLStack:"+xmlFile;
			InputStream xmlInput=new FileInputStream(xmlFile);
			XMLStack xmlStack=new XMLStack(xmlInput);
			result+=runXMLStack(xmlStack, "google");
			result+=runXMLStack(xmlStack, "apple");
			System.out.println(result);
		}
		catch(Exception e) {e.printStackTrace();}
	}
	static String runXMLStack(XMLStack xmlStack, String company)
	{
		String result="\n"; xmlStack.getElements(company);
		for(int i=0; i<xmlStack.length(); i++)
		{
			result+="\n"+company+".com["+i+"]={";
			xmlStack.pushElements();
			xmlStack.getElements(i);
			xmlStack.getElements("employee");
			for(int j=0; j<xmlStack.length(); j++)
			{
				result+=xmlStack.getValue(j,"name")+",  ";
			}
			xmlStack.popElements();
			result+="...  };\n";
		}
		return  result;
	}
}



