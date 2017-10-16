import java.io.*;
public class JavaAndStringIO
{
	public static void main(String[] args)
	{
		int length=26;
		String[] strings=new String[length];
		for(int i=0;i<length;i++)
		{
			String s="";
			for(int j=0;j<=i;j++)s+=(char)('a'+j);
			strings[i]=s;
		}
		writeString("StringIO.txt",strings);
		strings=readString("StringIO.txt");
		for(int i=0;i<strings.length;i++)System.out.println(i+":"+strings[i]);
	}
	static void writeString(String fileName,String[] strings)
	{
		try
		{
			PrintWriter PrintWriter1=new PrintWriter(fileName);
			for(int i=0;i<strings.length;i++)PrintWriter1.println(strings[i]);
			PrintWriter1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
	static String[] readString(String fileName)
	{
		String string="",s="";
		try
		{
			BufferedReader BufferedReader1=new BufferedReader(new FileReader(fileName));
			while((s=BufferedReader1.readLine())!=null)string+=s+"\n";
			BufferedReader1.close();
		}
		catch(Exception e){e.printStackTrace();}
		return string.split("\n");
	}
}