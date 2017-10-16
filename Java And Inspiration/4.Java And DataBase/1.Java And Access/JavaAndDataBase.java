import java.sql.*;
import java.util.*;
public class JavaAndDataBase
{
	public static void main(String[] args)
	{		
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource1");
			
			String StructuredQueryLanguage="Select Key,Data1,Data2 From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);	
			while(ResultSet1.next())System.out.println("Row:"+ResultSet1.getRow()+" Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
						
			StructuredQueryLanguage="Select * From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			ResultSetMetaData ResultSetMetaData1=ResultSet1.getMetaData();
			for(int i=1;i<=ResultSetMetaData1.getColumnCount();i++)System.out.println("CollumnClassName:"+ResultSetMetaData1.getColumnClassName(i)+"  ColumnName:"+ResultSetMetaData1.getColumnName(i)+" getColumnTypeName:"+ResultSetMetaData1.getColumnTypeName(i));						
			
			StructuredQueryLanguage="Select Key,Data2,Data2*0.1 As Data4 From Table1";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2")+"  Data4:"+ResultSet1.getString("Data4"));								
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2>=60";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));

			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Like '5%'";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
						
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Like '[1-3,5-7]%'";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));						
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Not Like '5%'";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Between 90 And 60";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2 Not In (50,70,90)";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Key>2 And Data2>=60";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Where Data2<60 Or Key<2";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data2 From Table1 Order By Data2";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Insert Into Table1 (Key,Data1,Data2) Values (11,'String11',0)";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);										
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);										
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Delete * From Table1 Where Data1='String11'";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);										
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);										
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Update Table1  Set Data2=0 Where Data1='String10'";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);										
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);										
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Create Table Table2(K int,Data1 text,Data2 int,Data3 char(10),Data4 money,Data5 datetime,Data8 float,Data9 image,Primary key(K));";System.out.println(StructuredQueryLanguage);						
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
			StructuredQueryLanguage="Select * From Table2";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			ResultSetMetaData1=ResultSet1.getMetaData();
			for(int i=1;i<=ResultSetMetaData1.getColumnCount();i++)System.out.println("CollumnClassName:"+ResultSetMetaData1.getColumnClassName(i)+"  ColumnName:"+ResultSetMetaData1.getColumnName(i)+" getColumnTypeName:"+ResultSetMetaData1.getColumnTypeName(i));						
			
			StructuredQueryLanguage="Insert Into Table2 (K,Data1,Data2) Values (1,'String11',30)";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);										
			StructuredQueryLanguage="Insert Into Table2 (K,Data1,Data2) Values (2,'String10',40)";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);										
			StructuredQueryLanguage="Insert Into Table2 (K,Data1,Data2) Values (3,'String9',50)";System.out.println(StructuredQueryLanguage);
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);										
			
			StructuredQueryLanguage="Select K,Data1,Data2 From Table2";System.out.println(StructuredQueryLanguage);			
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);										
			while(ResultSet1.next())System.out.println("K:"+ResultSet1.getString("K")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));

			StructuredQueryLanguage="Select Table1.Key,Table1.Data1,Table1.Data2 From Table1,Table2 Where Table1.Data2=Table2.Data2";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1 Where Data1 In (Select Data1 From Table2 Where Data2=40)";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));

			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1 Where Data2 In (Select Avg(Data2) From Table2 )";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
						
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1 Where Data2 In (Select Max(Data2) From Table2 )";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
						
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1 Where Data2 In (Select Min(Data2) From Table2 )";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
						
			StructuredQueryLanguage="Select Key,Data1,Data2 From Table1 Where Data2 In (Select Count(*)*10 From Table2 )";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Key:"+ResultSet1.getString("Key")+"  Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));

			StructuredQueryLanguage="(Select Data1,Data2 From Table1 ) Union (Select Data1,Data2 From Table2 )";System.out.println(StructuredQueryLanguage);						
			ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
			while(ResultSet1.next())System.out.println("Data1:"+ResultSet1.getString("Data1")+"  Data2:"+ResultSet1.getString("Data2"));
			
			ResultSet1.close();
			Connection1.close();
			Connection1=DriverManager.getConnection("jdbc:odbc:DataSource1");

			StructuredQueryLanguage="Drop Table Table2";System.out.println(StructuredQueryLanguage);						
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);									
			Connection1.close();

			System.out.print("Input any integer and continue ...");									
			Scanner Scanner1=new Scanner(System.in);
			Scanner1.nextInt();

		}
		catch(Exception e){e.printStackTrace();}	
	}
}