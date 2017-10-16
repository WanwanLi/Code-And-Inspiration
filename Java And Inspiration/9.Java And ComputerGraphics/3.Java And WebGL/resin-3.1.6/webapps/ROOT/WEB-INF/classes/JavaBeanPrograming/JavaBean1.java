package JavaBeanPrograming;
import java.io.*;
import java.sql.*;
public class JavaBean1 
{
	private String String1="";
	public void setString(String string)
	{
		String1=string;
	}
	public String getString()
	{
		return String1;
	}
	public String getALink(String string)
	{
		return "<A Href="+string+" >"+string+"</A>";
	}
	public String getImage(String string)
	{
		return "<Image Src="+string+"  Width=200 Height=100 Border=1>";
	}
	public String readFile(String string) throws Exception
	{
		String FileString="";
		string="C:\\resin-3.1.6\\webapps\\ROOT\\JavaServerPagePrograming\\"+string;
		BufferedReader BufferedReader1=new BufferedReader(new FileReader(string));
		String s=BufferedReader1.readLine();
		while(s!=null){FileString+=s;s=BufferedReader1.readLine();}
		BufferedReader1.close();
		return FileString;
	}
	public String writeFile(String string1,String string2) throws Exception
	{
		String FileString="";
		string1="C:\\resin-3.1.6\\webapps\\ROOT\\JavaServerPagePrograming\\"+string1;
		PrintWriter PrintWriter1=new PrintWriter(string1);
		PrintWriter1.print(string2);
		PrintWriter1.close();	
		return "Completed Writing!";	
	}
	public ResultSet executeQuery(String string)throws Exception
	{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource3");
		ResultSet ResultSet1= Connection1.createStatement().executeQuery(string);
		ResultSet1.close();
		Connection1.close();
		return ResultSet1;
	}
	public void execute(String string)throws Exception
	{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource3");
		Connection1.createStatement().execute(string);
		Connection1.close();
	}
}