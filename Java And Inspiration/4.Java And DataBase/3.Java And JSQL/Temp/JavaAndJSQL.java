import java.io.*;
import java.util.*;
public class JavaAndJSQL
{
	public static void main(String[] args)
	{
		//JSQL JSQL1=new JSQL();
		//JSQL1.run();
		test();
	}
	private static void test()
	{
		JSQL JSQL1=new JSQL();
		JSQL1.execute("Create Database JDatabase1");
//		JSQL1.execute("Show Databases");
		JSQL1.execute("Use JDatabase1");
		JSQL1.execute("Drop Table JTable1");
		JSQL1.execute("Drop Table JTable2");
		JSQL1.execute("Drop Table JTable3");
		JSQL1.execute("Create Table JTable1(JField1 int,JField2 text)");
		JSQL1.execute("Create Table JTable2(JField1 int,JField2 text)");
		JSQL1.execute("Create Table JTable3(JField1 int,JField2 text)");
//		JSQL1.execute("Describe JTable1");
//		JSQL1.execute("Alter Table JTable1 Add JField5 text");
//		JSQL1.execute("Alter Table JTable1 Drop JField4");
//		JSQL1.execute("Describe JTable1");
		JSQL1.executeUpdate("Delete From JTable1");
		JSQL1.executeUpdate("Delete From JTable2");
		JSQL1.executeUpdate("Insert Into JTable1 Values(1,A)");
		JSQL1.executeUpdate("Insert Into JTable1 Values(2,B)");
		JSQL1.executeUpdate("Insert Into JTable2 Values(3,C)");
		JSQL1.executeUpdate("Insert Into JTable2 Values(4,D)");
		JSQL1.executeUpdate("Insert Into JTable2 Values(2,G)");
		JSQL1.executeUpdate("Insert Into JTable3 Values(5,E)");
		JSQL1.executeUpdate("Insert Into JTable3 Values(6,F)");
//		JSQL1.executeUpdate("Delete From JTable1 Where JField1=4 And JField2=5 And JField4=6");
//		JSQL1.executeUpdate("Update JTable1 Set JField1=10 Where JField1=A");
//		JSQL1.execute("Select * From JTable1 Where JField1>0");
		JSQL1.execute("Select * From JTable1");
		JSQL1.execute("Select * From JTable2");
//		JSQL1.execute("Select * From JTable1,JTable2,JTable3 Where JTable1.JField1>1");
//		JSQL1.execute("Select * From JTable1,JTable2,JTable3 Where JTable1.JField1=JTable2.JField");
		JSQL1.execute("Select * From JTable1 Where JField1 In(Select JField1 From JTable2 Where JField2 In (Select JField2 From JTable2 )   )");
	}
}
class JSQL
{
	private JDBMS DBMS;
	private JDatabase currentDatabase;
	private int beginIndex;
	public JSQL()
	{
		this.DBMS=new JDBMS();
		this.currentDatabase=null;
		this.beginIndex=0;
	}
	public void run()
	{
		System.out.println("Welcome to the JSQL monitor !");
		System.out.println("Server version: Java Runtime Environment 1.0 Version");
		System.out.println("Java Code Producer : Li Wanwan");
		System.out.println("Exit Query By Input 'exit' ");
		System.out.print("Enjoy Your Time!\nJSQL>");
		Scanner Scanner1=new Scanner(System.in);
		String cmd=Scanner1.nextLine();
		while(!cmd.toLowerCase().equals("exit"))
		{
			this.execute(cmd);
			System.out.print("JSQL>");
			cmd=Scanner1.nextLine();
		}
	}
	public void execute(String SQL)
	{
		this.beginIndex=0;
		String word=this.getWord(SQL,' ');
		if(word.equals("select"))
		{
			JResultSet JResultSet1=this.executeQuery(SQL);
			if(JResultSet1!=null)JResultSet1.print();
		}
		else if(word.equals("update")||word.equals("insert")||word.equals("delete"))this.executeUpdate(SQL);
		else
		{
			if(word.equals("use"))
			{
				word=this.getWord(SQL,';');
				this.currentDatabase=DBMS.use(word);
				if(currentDatabase==null)System.out.println("ERROR:Unknown database '"+word+"'");
				else System.out.println("Database changed");
			}
			else if(word.equals("create"))
			{
				word=this.getWord(SQL,' ');
				if(word.equals("database"))
				{
					word=this.getWord(SQL,' ');
					this.DBMS.createDatabase(word);
				}
				else if(word.equals("table"))
				{
					String tableName=this.getWord(SQL,'(');
					int l=this.getCount(SQL,',');
					String[] fieldNames=new String[l+1];
					String[] fieldTypes=new String[l+1];
					for(int i=0;i<l;i++)
					{
						fieldNames[i]=this.getWord(SQL,' ');
						fieldTypes[i]=this.getWord(SQL,',');
					}
					fieldNames[l]=this.getWord(SQL,' ');
					fieldTypes[l]=this.getWord(SQL,')');
					if(currentDatabase!=null)this.currentDatabase.createTable(tableName,fieldNames,fieldTypes);
					else System.out.println("ERROR:No database selected");
				}
				else System.out.println("ERROR:You have an error in your SQL syntax;");
			}
			else if(word.equals("drop"))
			{
				word=this.getWord(SQL,' ');
				if(word.equals("database"))
				{
					word=this.getWord(SQL,' ');
					this.DBMS.dropDatabase(word);
				}
				else if(word.equals("table"))
				{
					if(currentDatabase!=null)
					{
						word=this.getWord(SQL,' ');
						this.currentDatabase.dropTable(word);
					}
					else System.out.println("ERROR:No database selected");
				}
				else System.out.println("ERROR:You have an error in your SQL syntax;");
			}
			else if(word.equals("show"))
			{
				word=this.getWord(SQL,' ');
				if(word.equals("databases"))this.DBMS.showDatabases();
				else if(word.equals("tables"))
				{
					if(currentDatabase!=null)this.currentDatabase.showTables();
					else System.out.println("ERROR:No database selected");
				}
				else System.out.println("ERROR:You have an error in your SQL syntax;");
			}
			else if(word.equals("describe"))
			{
				word=this.getWord(SQL,' ');
				if(currentDatabase!=null)
				{
					JTable table=this.currentDatabase.getTable(word);
					if(table!=null)table.describe();
					else System.out.println("ERROR:Unknown table '"+word+"'");
				}
				else System.out.println("ERROR:No database selected");
			}
			else if(word.equals("alter"))
			{
				word=this.getWord(SQL,' ');
				if(word.equals("table"))
				{
					word=this.getWord(SQL,' ');
					if(currentDatabase!=null)
					{
						JTable table=this.currentDatabase.getTable(word);
						if(table!=null)
						{
							word=this.getWord(SQL,' ');
							if(word.equals("add"))
							{
								String fieldName=this.getWord(SQL,' ');
								String fieldType=this.getWord(SQL,' ');
								table.alterTableAdd(fieldName,fieldType);
							}
							else if(word.equals("drop"))
							{
								word=this.getWord(SQL,' ');
								table.alterTableDrop(word);
							}
							else System.out.println("ERROR:You have an error in your SQL syntax;");
						}
						else System.out.println("ERROR:Unknown table '"+word+"'");
					}
					else System.out.println("ERROR:No database selected");
				}
				else System.out.println("ERROR:You have an error in your SQL syntax;");
			}
			else System.out.println("ERROR:You have an error in your SQL syntax;");
		}
	}
	public JResultSet executeQuery(String SQL)
	{
		this.beginIndex=0;
		JResultSet JResultSet1=null;
		String word=this.getWord(SQL,' ');
		if(!word.equals("select"))
		{
			System.out.println("ERROR:You have an error in your SQL syntax;");
			return JResultSet1;
		}
		String[] strings=this.getDividedStrings(SQL,"from");
		int l=this.getCount(strings[0],',');
		String[] fieldNames=new String[l+1];
		for(int i=0;i<l;i++)fieldNames[i]=this.getWord(SQL,',');
		fieldNames[l]=this.getWord(SQL,' ');
		word=this.getWord(SQL,' ');
		if(word.equals("from"))
		{
			if(currentDatabase!=null)
			{
				l=this.getCount(SQL,',');
				if(l!=0)
				{
					String[] tableNames=new String[l+1];
					for(int i=0;i<l;i++)tableNames[i]=this.getWord(SQL,',');
					tableNames[l]=this.getWord(SQL,' ');
					word=this.getWord(SQL,' ');
					if(word.equals(""))JResultSet1=this.currentDatabase.selectFrom(fieldNames,tableNames,null,null,null);
					else if(word.equals("where"))
					{
						String[] dividedStrings=this.getDividedStrings(SQL,"and");
						l=dividedStrings.length;
						String[] fields=new String[l];
						int[] compare=new int[l];
						String[] datas=new String[l];
						for(int i=0;i<l;i++)
						{
							String[] compareStrings=this.getCompareStrings(dividedStrings[i]);
							fields[i]=compareStrings[0];
							compare[i]=Integer.parseInt(compareStrings[1]);
							datas[i]=compareStrings[2];
						}
						JResultSet1=this.currentDatabase.selectFrom(fieldNames,tableNames,fields,compare,datas);
					}
					else System.out.println("ERROR:You have an error in your SQL syntax;");
				}
				else
				{
					word=this.getWord(SQL,' ');
					JTable table=this.currentDatabase.getTable(word);
					if(table!=null)
					{
						word=this.getWord(SQL,' ');
						if(word.equals(""))JResultSet1=table.selectFrom(fieldNames,null,null,null);
						else if(word.equals("where"))
						{
							l=this.getWordCount(SQL,"in");
							if(l!=0)
							{
								String field=this.getWord(SQL,' ');
								this.getWord(SQL,'(');
								System.out.println("field="+field);
								String sql=this.getHind(SQL,')');
								System.out.println(sql);
								JResultSet resultSet=this.executeQuery(sql);
								JResultSet1=table.selectFrom(fieldNames,field,resultSet);
							}
							else
							{
								String[] dividedStrings=this.getDividedStrings(SQL,"and");
								l=dividedStrings.length;
								String[] fields=new String[l];
								int[] compare=new int[l];
								String[] datas=new String[l];
								for(int i=0;i<l;i++)
								{
									String[] compareStrings=this.getCompareStrings(dividedStrings[i]);
									fields[i]=compareStrings[0];
									compare[i]=Integer.parseInt(compareStrings[1]);
									datas[i]=compareStrings[2];
								}
								JResultSet1=table.selectFrom(fieldNames,fields,compare,datas);
							}
						}
						else System.out.println("ERROR:You have an error in your SQL syntax;");
					}
					else System.out.println("ERROR:Unknown table '"+word+"'");
				}
			}
			else System.out.println("ERROR:No database selected");
		}
		else System.out.println("ERROR:You have an error in your SQL syntax;");
		return JResultSet1;
	}
	public void executeUpdate(String SQL)
	{
		this.beginIndex=0;
		String word=this.getWord(SQL,' ');
		if(word.equals("update"))
		{
			word=this.getWord(SQL,' ');
			if(currentDatabase!=null)
			{
				JTable table=this.currentDatabase.getTable(word);
				if(table!=null)
				{
					word=this.getWord(SQL,' ');
					if(word.equals("set"))
					{
						int l=this.getCount(SQL,',');
						String[] expressionStrings=new String[l+1];
						for(int i=0;i<l;i++)expressionStrings[i]=this.getData(SQL,',');
						expressionStrings[l]=this.getData(SQL,' ');
						String[] fieldNames=new String[l+1];
						String[] newDatas=new String[l+1];
						for(int i=0;i<l+1;i++)
						{
							String[] compareStrings=this.getCompareStrings(expressionStrings[i]);
							fieldNames[i]=compareStrings[0];
							newDatas[i]=compareStrings[2];
						}
						word=this.getWord(SQL,' ');
						if(word.equals(""))table.updateSet(fieldNames,newDatas,null,null,null);
						else if(word.equals("where"))
						{
							String[] dividedStrings=this.getDividedStrings(SQL,"and");
							l=dividedStrings.length;
							String[] fields=new String[l];
							int[] compare=new int[l];
							String[] datas=new String[l];
							for(int i=0;i<l;i++)
							{
								String[] compareStrings=this.getCompareStrings(dividedStrings[i]);
								fields[i]=compareStrings[0];
								compare[i]=Integer.parseInt(compareStrings[1]);
								datas[i]=compareStrings[2];
							}
							table.updateSet(fieldNames,newDatas,fields,compare,datas);
						}
						else System.out.println("ERROR:You have an error in your SQL syntax;");
					}
					else System.out.println("ERROR:You have an error in your SQL syntax;");
				}
				else System.out.println("ERROR:Unknown table '"+word+"'");
			}
			else System.out.println("ERROR:No database selected");
		}
		else if(word.equals("insert"))
		{
			word=this.getWord(SQL,' ');
			if(word.equals("into"))
			{
				if(currentDatabase!=null)
				{
					word=this.getWord(SQL,' ');
					JTable table=this.currentDatabase.getTable(word);
					if(table!=null)
					{
						word=this.getWord(SQL,'(');
						if(word.equals("values"))
						{
							int l=this.getCount(SQL,',');
							String[] datas=new String[l+1];
							for(int i=0;i<l;i++)datas[i]=this.getData(SQL,',');
							datas[l]=this.getData(SQL,')');
							table.insertInto(datas);
						}
						else System.out.println("ERROR:You have an error in your SQL syntax;");
					}
					else System.out.println("ERROR:Unknown table '"+word+"'");
				}
				else System.out.println("ERROR:No database selected");
			}
			else System.out.println("ERROR:You have an error in your SQL syntax;");
		}
		else if(word.equals("delete"))
		{
			word=this.getWord(SQL,' ');
			if(word.equals("from"))
			{
				if(currentDatabase!=null)
				{
					word=this.getWord(SQL,' ');
					JTable table=this.currentDatabase.getTable(word);
					if(table!=null)
					{
						word=this.getWord(SQL,' ');
						if(word.equals(""))table.deleteFrom(null,null,null);
						else if(word.equals("where"))
						{
							String[] dividedStrings=this.getDividedStrings(SQL,"and");
							int l=dividedStrings.length;
							String[] fields=new String[l];
							int[] compare=new int[l];
							String[] datas=new String[l];
							for(int i=0;i<l;i++)
							{
								String[] compareStrings=this.getCompareStrings(dividedStrings[i]);
								fields[i]=compareStrings[0];
								compare[i]=Integer.parseInt(compareStrings[1]);
								datas[i]=compareStrings[2];
							}
							table.deleteFrom(fields,compare,datas);
						}
						else System.out.println("ERROR:You have an error in your SQL syntax;");
					}
					else System.out.println("ERROR:Unknown table '"+word+"'");
				}
				else System.out.println("ERROR:No database selected");
			}
			else System.out.println("ERROR:You have an error in your SQL syntax;");
		}
		else System.out.println("ERROR:You have an error in your SQL syntax;");
	}
	private String getWord(String string,char endChar)
	{
		return this.getData(string,endChar).toLowerCase();
	}
	private String getData(String string,char endChar)
	{
		int i=beginIndex;
		int l=string.length();
		if(i>=l)return "";
		String data="";
		char Char=string.charAt(i++);
		while(Char==' '&&i<l)Char=string.charAt(i++);
		while(Char!=endChar)
		{
			data+=Char;
			if(i>=l)break;
			Char=string.charAt(i++);
		}
		this.beginIndex=i;
		return data;
	}
	private String getHind(String string,char endChar)
	{
		int l=string.length()-1;
		int i=beginIndex;
		if(l<i)return "";
		char Char=string.charAt(l--);
		while(Char==' '&&l>=i)Char=string.charAt(l--);
		while(Char!=endChar)
		{

			Char=string.charAt(l--);
			if(l<i)break;
		}
		return string.substring(i,l+1);
	}
	private String getFilteredWord(String word,char filterChar)
	{
		int l=word.length();
		String filteredWord="";
		for(int i=0;i<l;i++)
		{
			char Char=word.charAt(i);
			if(Char!=filterChar)filteredWord+=Char;
		}
		return filteredWord;
	}
	private int getCount(String string,char countChar)
	{
		int count=0;
		int l=string.length();
		for(int i=beginIndex;i<l;i++)
		{
			if(string.charAt(i)==countChar)count++;
		}
		return count;
	}
	private int getWordCount(String string,String word)
	{
		int count=0;
		int BeginIndex=this.beginIndex;
		String data=this.getData(string,' ');
		while(!data.equals(""))
		{
			if(data.toLowerCase().equals(word))count++;
			data=this.getData(string,' ');
		}
		this.beginIndex=BeginIndex;
		return count;
	}
	private String[] getDividedStrings(String string,String divideWord)
	{
		int count=0;
		int BeginIndex=this.beginIndex;
		String data=this.getData(string,' ');
		while(!data.equals(""))
		{
			if(data.toLowerCase().equals(divideWord))count++;
			data=this.getData(string,' ');
		}
		this.beginIndex=BeginIndex;
		String[] dividedStrings=new String[count+1];
		count=0;
		data=this.getData(string,' ');
		String dividedString="";
		while(!data.equals(""))
		{
			if(data.toLowerCase().equals(divideWord))
			{
				dividedStrings[count++]=dividedString;
				dividedString="";
			}
			else dividedString+=data;
			data=this.getData(string,' ');
		}
		dividedStrings[count]=dividedString;
		this.beginIndex=BeginIndex;
		return dividedStrings;
	}
	private String[] getCompareStrings(String string)
	{
		int i=0;
		int l=string.length();
		String[] compareStrings=new String[3];
		compareStrings[0]="";
		compareStrings[1]="0";
		compareStrings[2]="";
		if(l==0)return compareStrings;
		char c=string.charAt(i++);
		while(c!='<'&&c!='='&&c!='>'&&i<l)
		{
			if(c!=' ')compareStrings[0]+=c;
			c=string.charAt(i++);
		}
		compareStrings[0]=compareStrings[0].toLowerCase();
		switch(c)
		{
			case '<':compareStrings[1]="-1";break;
			case '=':compareStrings[1]="0";break;
			case '>':compareStrings[1]="1";break;
			default:compareStrings[1]="0";
		}
		while(i<l)
		{
			c=string.charAt(i++);
			if(c!=' ')compareStrings[2]+=c;
		}
		return compareStrings;
	}
}
class JDBMS
{
	public JDatabase FirstDatabase;
	public JDatabase LastDatabase;
	public File JDBMSFile;
	public JDBMS()
	{
		this.FirstDatabase=null;
		this.LastDatabase=null;
		this.JDBMSFile=new File("JDBMS");
		try
		{
			this.init();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private void init()
	{
		if(JDBMSFile.exists())
		{
			String[] DatabaseNames=JDBMSFile.list();
			int l=DatabaseNames.length;
			for(int i=0;i<l;i++)
			{
				JDatabase JDatabasei=new JDatabase(DatabaseNames[i]);
				this.insertDatabase(JDatabasei);
			}
		}
		else JDBMSFile.mkdir();
	}
	private void insertDatabase(JDatabase dataBase)
	{
		if(FirstDatabase==null)this.FirstDatabase=this.LastDatabase=dataBase;
		else
		{
			this.LastDatabase.NextDatabase=dataBase;
			dataBase.PreviousDatabase=LastDatabase;
			this.LastDatabase=dataBase;
		}
	}
	private void deleteDatabase(JDatabase database)
	{
		if(database==FirstDatabase)
		{
			this.FirstDatabase=FirstDatabase.NextDatabase;
			if(FirstDatabase!=null)this.FirstDatabase.PreviousDatabase=null;
		}
		else if(database==LastDatabase)
		{
			this.LastDatabase=LastDatabase.PreviousDatabase;
			if(LastDatabase!=null)this.LastDatabase.NextDatabase=null;
		}
		else
		{
			database.PreviousDatabase.NextDatabase=database.NextDatabase;
			database.NextDatabase.PreviousDatabase=database.PreviousDatabase;
		}
	}
	public void createDatabase(String databaseName)
	{
		try
		{
			File file=new File("JDBMS\\"+databaseName);
			if(!file.exists())
			{
				JDatabase JDatabase1=new JDatabase(databaseName);
				this.insertDatabase(JDatabase1);
				System.out.println("Query OK, 1 row affected");
			}
			else System.out.println("ERROR:Can't create database '"+databaseName+"'; database exists");
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void dropDatabase(String databaseName)
	{
		try
		{
			File file=new File("JDBMS\\"+databaseName);
			if(file.exists())
			{
				File[] files=file.listFiles();
				int l=files.length;
				for(int i=0;i<l;i++)files[i].delete();
				file.delete();
				JDatabase JDatabase1=this.use(databaseName);
				this.deleteDatabase(JDatabase1);
				System.out.println("Query OK, 1 row affected");
			}
			else System.out.println("ERROR:Can't drop database '"+databaseName+"'; database doesn't exist");
		}
		catch(Exception e){e.printStackTrace();}
	}
	public JDatabase use(String databaseName)
	{
		JDatabase JDatabase1=null;
		for(JDatabase1=FirstDatabase;JDatabase1!=null;JDatabase1=JDatabase1.NextDatabase)
		{
			if(JDatabase1.DatabaseName.equals(databaseName))return JDatabase1;
		}
		return JDatabase1;
	}
	public void showDatabases()
	{
		System.out.println("_______________________\n");
		System.out.println("Database");
		System.out.println("_______________________\n");
		for(JDatabase d=FirstDatabase;d!=null;d=d.NextDatabase)System.out.println(d.DatabaseName);
		System.out.println("_______________________\n");
		
	}
}
class JDatabase
{
	public String DatabaseName;
	public JTable FirstTable;
	public JTable LastTable;
	public JDatabase NextDatabase;
	public JDatabase PreviousDatabase;
	public File JDatabaseFile;
	public JDatabase(String dataBaseName)
	{
		this.DatabaseName=dataBaseName;
		this.FirstTable=null;
		this.LastTable=null;
		this.NextDatabase=null;
		this.PreviousDatabase=null;
		this.JDatabaseFile=new File("JDBMS\\"+DatabaseName);
		try
		{
			this.init();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private void init()
	{
		if(JDatabaseFile.exists())
		{
			String[] TableNames=JDatabaseFile.list();
			int l=TableNames.length;
			for(int i=0;i<l;i++)
			{
				String TableNamei=TableNames[i].substring(0,TableNames[i].length()-5);
				JTable JTablei=new JTable(DatabaseName,TableNamei);
				this.insertTable(JTablei);
			}
		}
		else JDatabaseFile.mkdir();
	}
	public void insertTable(JTable table)
	{
		if(FirstTable==null)this.FirstTable=this.LastTable=table;
		else
		{
			this.LastTable.NextTable=table;
			table.PreviousTable=LastTable;
			this.LastTable=table;
		}
	}
	public void deleteTable(JTable table)
	{
		if(table==FirstTable)
		{
			this.FirstTable=FirstTable.NextTable;
			if(FirstTable!=null)this.FirstTable.PreviousTable=null;
		}
		else if(table==LastTable)
		{
			this.LastTable=LastTable.PreviousTable;
			if(LastTable!=null)this.LastTable.NextTable=null;
		}
		else
		{
			table.PreviousTable.NextTable=table.NextTable;
			table.NextTable.PreviousTable=table.PreviousTable;
		}
	}
	public void showTables()
	{
		System.out.println("_______________________\n");
		System.out.println("Tables In "+DatabaseName);
		System.out.println("_______________________\n");
		for(JTable t=FirstTable;t!=null;t=t.NextTable)System.out.println(t.TableName);
		System.out.println("_______________________\n");
		
	}
	public void createTable(String tableName,String[] fieldNames,String[] fieldTypes)
	{
		int l=fieldNames.length;
		try
		{
			File file=new File("JDBMS\\"+DatabaseName+"\\"+tableName+".jsql");
			if(!file.exists())
			{
				JTable JTable1=new JTable(DatabaseName,tableName);
				String JTableFieldInfo="";
				for(int i=0;i<l;i++)
				{
					JField JField1=new JField(fieldNames[i],fieldTypes[i]);
					JTable1.insertField(JField1);
					JTableFieldInfo+=fieldNames[i]+" "+fieldTypes[i]+",";
				}
				this.insertTable(JTable1);
				file.createNewFile();
				PrintWriter PrintWriter1=new PrintWriter(file);
				PrintWriter1.println(JTableFieldInfo);
				PrintWriter1.close();
				System.out.println("Query OK, 1 row affected");
			}
			else System.out.println("ERROR:Table '"+tableName+"' already exists");
		}
		catch(Exception e){e.printStackTrace();}
	}
	public void dropTable(String tableName)
	{
		try
		{
			File file=new File("JDBMS\\"+DatabaseName+"\\"+tableName+".jsql");
			if(file.exists())
			{
				file.delete();
				JTable JTable1=this.getTable(tableName);
				this.deleteTable(JTable1);
				System.out.println("Query OK, 1 row affected");
			}
			else System.out.println("ERROR:Unknown table '"+tableName+"'");
		}
		catch(Exception e){e.printStackTrace();}
	}
	public JTable getTable(String tableName)
	{
		JTable JTable1=null;
		for(JTable1=FirstTable;JTable1!=null;JTable1=JTable1.NextTable)
		{
			if(JTable1.TableName.equals(tableName))return JTable1;
		}
		return JTable1;
	}
	JTable getDescartesProductTable(JTable table1,JTable table2)
	{
		int fieldCount1=table1.FieldCount;
		int fieldCount2=table2.FieldCount;
		JTable productTable=new JTable(DatabaseName,"");
		for(JField f=table1.FirstField;f!=null;f=f.NextField)
		{
			if(table1.TableName.equals(""))productTable.insertField(new JField(f.FieldName,f.FieldType));
			else productTable.insertField(new JField(table1.TableName+"."+f.FieldName,f.FieldType));
		}
		for(JField f=table2.FirstField;f!=null;f=f.NextField)
		{
			if(table2.TableName.equals(""))productTable.insertField(new JField(f.FieldName,f.FieldType));
			else productTable.insertField(new JField(table2.TableName+"."+f.FieldName,f.FieldType));
		}
		JItem[] items2=table2.getFirstItems();
		for(;items2[0]!=null;items2[0]=items2[0].NextItem)
		{
			JItem[] items1=table1.getFirstItems();
			for(;items1[0]!=null;items1[0]=items1[0].NextItem)
			{
				int j=0;
				JField f=productTable.FirstField;
				for(j=0;j<fieldCount1;j++,f=f.NextField)f.insertItem(new JItem(items1[j].Data));
				for(j=0;j<fieldCount2;j++,f=f.NextField)f.insertItem(new JItem(items2[j].Data));
				for(int i=1;i<table1.FieldCount;i++)items1[i]=items1[i].NextItem;
			}
			for(int i=1;i<table2.FieldCount;i++)items2[i]=items2[i].NextItem;
		}
		return productTable;
	}
	public JResultSet selectFrom(String[] fieldNames,String[] tableNames,String[] fields,int[] compare,String[] datas)
	{
		int l=tableNames.length;
		JTable[] tables=new JTable[l];
		for(int i=0;i<l;i++)
		{
			tables[i]=this.getTable(tableNames[i]);
			if(tables[i]==null)
			{
				System.out.println("ERROR: Table '"+DatabaseName+"."+tableNames[i]+"' doesn't exist");
				return null;
			}
		}
		JTable productTable=tables[0];
		for(int i=1;i<l;i++)productTable=this.getDescartesProductTable(productTable,tables[i]);
		return productTable.selectFrom(fieldNames,fields,compare,datas);
	}
}
class JTable
{
	public String DatabaseName;
	public String TableName;
	public JField FirstField;
	public JField LastField;
	public JTable NextTable;
	public JTable PreviousTable;
	public File JTableFile;
	public int FieldCount;
	public JTable(String databaseName,String tableName)
	{
		this.DatabaseName=databaseName;
		this.TableName=tableName;
		this.FirstField=null;
		this.LastField=null;
		this.NextTable=null;
		this.PreviousTable=null;
		this.JTableFile=new File("JDBMS\\"+DatabaseName+"\\"+TableName+".jsql");
		try
		{
			this.init();
		}
		catch(Exception e){e.printStackTrace();}
	}
	private void init()throws Exception
	{
		if(!JTableFile.exists())return;
		BufferedReader BufferedReader1=new BufferedReader(new FileReader(JTableFile));
		String[] JTableFieldInfo=this.getTableFieldInfo(BufferedReader1.readLine());
		int n=JTableFieldInfo.length;
		int j=0;
		while(j<n)this.insertField(new JField(JTableFieldInfo[j++],JTableFieldInfo[j++]));
		JField field=FirstField;
		String Datas=BufferedReader1.readLine();
		while(Datas!=null)
		{
			int l=Datas.length();
			String data="";
			for(int i=0;i<l;i++)
			{
				char c=Datas.charAt(i);
				if(c=='\t')
				{
					field.insertItem(new JItem(data));
					data="";
				}
				else data+=c;
			}
			if(field!=null)field=field.NextField;
			Datas=BufferedReader1.readLine();
		}
		BufferedReader1.close();
	}
	private String[] getTableFieldInfo(String JTableFieldInfo)
	{
		int n=0;
		int l=JTableFieldInfo.length();
		for(int i=0;i<l;i++)
		{
			char c=JTableFieldInfo.charAt(i);
			if(c==',')n++;
		}
		String[] tableFieldInfo=new String[2*n];
		String s="";
		n=0;
		for(int i=0;i<l;i++)
		{
			char c=JTableFieldInfo.charAt(i);
			if(c==' '||c==',')
			{
				tableFieldInfo[n++]=s;
				s="";
			}
			else s+=c;
		}
		return tableFieldInfo;
	}
	public void insertField(JField field)
	{
		this.FieldCount++;
		if(FirstField==null)this.FirstField=this.LastField=field;
		else
		{
			this.LastField.NextField=field;
			field.PreviousField=LastField;
			this.LastField=field;
		}
	}
	public void deleteField(JField field)
	{
		this.FieldCount--;
		if(field==FirstField)
		{
			this.FirstField=FirstField.NextField;
			if(FirstField!=null)this.FirstField.PreviousField=null;
		}
		else if(field==LastField)
		{
			this.LastField=LastField.PreviousField;
			if(LastField!=null)this.LastField.NextField=null;
		}
		else
		{
			field.PreviousField.NextField=field.NextField;
			field.NextField.PreviousField=field.PreviousField;
		}
	}
	public void alterTableAdd(String fieldName,String fieldType)
	{
		JField JField1=this.getField(fieldName);
		if(JField1!=null)System.out.println("ERROR:Duplicate field name '"+fieldName+"'");
		else
		{
			JField1=new JField(fieldName,fieldType);
			for(JItem i=FirstField.FirstItem;i!=null;i=i.NextItem)JField1.insertItem(new JItem("null"));
			this.insertField(JField1);
			this.writeToDisk();
			System.out.println("Query OK, 1 row affected");
		}
	}
	public void alterTableDrop(String fieldName)
	{
		JField JField1=this.getField(fieldName);
		if(JField1==null)System.out.println("ERROR:Can't DROP '"+fieldName+"'; check that field exists");
		else
		{
			this.deleteField(JField1);
			this.writeToDisk();
			System.out.println("Query OK, 1 row affected");
		}
	}
	public void describe()
	{
		System.out.println("_______________________\n");
		System.out.println("Type\tField");
		System.out.println("_______________________\n");
		for(JField f=FirstField;f!=null;f=f.NextField)System.out.println(f.FieldType+"\t"+f.FieldName);
		System.out.println("_______________________\n");
	}
	public void insertInto(String[] datas)
	{
		int i=0;
		int l=datas.length;
		for(JField f=FirstField;f!=null;f=f.NextField)i++;
		if(i!=l)System.out.println("ERROR:Field count doesn't match value count at row 1");
		else
		{
			i=0;
			for(JField f=FirstField;f!=null;f=f.NextField)f.insertItem(new JItem(datas[(i++)]));
			this.writeToDisk();
			System.out.println("Query OK, 1 row affected");
		}
	}
	public void deleteFrom(String[] fields,int[] compare,String[] datas)
	{
		if(fields==null)for(JField f=FirstField;f!=null;f=f.NextField)f.FirstItem=f.LastItem=null;
		else
		{
			String notFieldName=this.getNotFieldName(fields);
			if(notFieldName!=null)
			{
				System.out.println("ERROR:Unknown field '"+notFieldName+"' in 'where clause'");
				return;
			}
			JItem[] items=this.getFirstItems();
			for(;items[0]!=null;items[0]=items[0].NextItem)
			{
				if(this.isCorrespondTo(items,fields,compare,datas))
				{
					int l=0;
					for(JField f=FirstField;f!=null;f=f.NextField)f.deleteItem(items[l++]);
				}
				for(int i=1;i<FieldCount;i++)items[i]=items[i].NextItem;
			}
		}
		this.writeToDisk();
		System.out.println("Query OK, 1 row affected");
	}
	public void updateSet(String[] fieldNames,String[] newDatas,String[] fields,int[] compare,String[] datas)
	{
		String notFieldName=this.getNotFieldName(fieldNames);
		if(notFieldName!=null)
		{
			System.out.println("ERROR:Unknown field '"+notFieldName+"' in 'where clause'");
			return;
		}
		if(fields!=null)
		{
			notFieldName=this.getNotFieldName(fields);
			if(notFieldName!=null)
			{
				System.out.println("ERROR:Unknown field '"+notFieldName+"' in 'where clause'");
				return;
			}
		}
		JItem[] items=this.getFirstItems();
		int l=fieldNames.length;
		for(;items[0]!=null;items[0]=items[0].NextItem)
		{
			if(this.isCorrespondTo(items,fields,compare,datas))
			{
				for(int i=0;i<l;i++)items[this.getFieldIndex(fieldNames[i])].Data=newDatas[i];
			}
			for(int i=1;i<FieldCount;i++)items[i]=items[i].NextItem;
		}
		this.writeToDisk();
		System.out.println("Query OK, 1 row affected");
	}
	public JResultSet selectFrom(String[] fieldNames,String[] fields,int[] compare,String[] datas)
	{
		String notFieldName=null;
		if(fields!=null)notFieldName=this.getNotFieldName(fields);
		if(notFieldName!=null)
		{
			System.out.println("ERROR:Unknown field '"+notFieldName+"' in 'where clause'");
			return null;
		}
		JItem[] items=this.getFirstItems();
		if(fieldNames[0].equals("*"))
		{	
			JField[] resultFields=new JField[FieldCount];
			int l=0;
			for(JField f=FirstField;f!=null;f=f.NextField)resultFields[l++]=new JField(f.FieldName,f.FieldType);
			for(;items[0]!=null;items[0]=items[0].NextItem)
			{
				if(this.isCorrespondTo(items,fields,compare,datas))
				{
					l=0;
					for(JField f=FirstField;f!=null;f=f.NextField)resultFields[l].insertItem(new JItem(items[l++].Data));
				}
				for(int i=1;i<FieldCount;i++)items[i]=items[i].NextItem;
			}
			return new JResultSet(resultFields);
		}
		else
		{
			notFieldName=this.getNotFieldName(fieldNames);
			if(notFieldName!=null)
			{
				System.out.println("ERROR:Unknown field '"+notFieldName+"' in 'where clause'");
				return null;
			}
			int l=fieldNames.length;
			JField[] resultFields=new JField[l];
			for(int i=0;i<l;i++)
			{
				JField f=this.getField(fieldNames[i]);
				resultFields[i]=new JField(f.FieldName,f.FieldType);
			}
			for(;items[0]!=null;items[0]=items[0].NextItem)
			{
				if(this.isCorrespondTo(items,fields,compare,datas))
				{
					for(int i=0;i<l;i++)resultFields[i].insertItem(new JItem(items[this.getFieldIndex(fieldNames[i])].Data));
				}
				for(int i=1;i<FieldCount;i++)items[i]=items[i].NextItem;
			}
			return new JResultSet(resultFields);
		}
	}
	public JResultSet selectFrom(String[] fieldNames,String field,JResultSet resultSet)
	{
		if(resultSet==null)return null;
		String notFieldName=this.getNotFieldName(new String[]{field});
		if(notFieldName!=null)
		{
			System.out.println("ERROR:Unknown field '"+notFieldName+"' in 'where clause'");
			return null;
		}
		if(resultSet.getFieldCount()!=1)System.out.println("Operand should contain 1 field(s)");
		JItem[] items=this.getFirstItems();
		if(fieldNames[0].equals("*"))
		{	
			JField[] resultFields=new JField[FieldCount];
			int l=0;
			for(JField f=FirstField;f!=null;f=f.NextField)resultFields[l++]=new JField(f.FieldName,f.FieldType);
			for(;items[0]!=null;items[0]=items[0].NextItem)
			{
				if(this.isCorrespondTo(items,field,resultSet))
				{
					l=0;
					for(JField f=FirstField;f!=null;f=f.NextField)resultFields[l].insertItem(new JItem(items[l++].Data));
				}
				for(int i=1;i<FieldCount;i++)items[i]=items[i].NextItem;
			}
			return new JResultSet(resultFields);
		}
		else
		{
			notFieldName=this.getNotFieldName(fieldNames);
			if(notFieldName!=null)
			{
				System.out.println("ERROR:Unknown field '"+notFieldName+"' in 'where clause'");
				return null;
			}
			int l=fieldNames.length;
			JField[] resultFields=new JField[l];
			for(int i=0;i<l;i++)
			{
				JField f=this.getField(fieldNames[i]);
				resultFields[i]=new JField(f.FieldName,f.FieldType);
			}
			for(;items[0]!=null;items[0]=items[0].NextItem)
			{
				if(this.isCorrespondTo(items,field,resultSet))
				{
					for(int i=0;i<l;i++)resultFields[i].insertItem(new JItem(items[this.getFieldIndex(fieldNames[i])].Data));
				}
				for(int i=1;i<FieldCount;i++)items[i]=items[i].NextItem;
			}
			return new JResultSet(resultFields);
		}
	}
	private int sgn(int x)
	{
		if(x<0)return -1;
		else if(x>0)return 1;
		else return 0;
	}
	private int sub(String x,String y)
	{
		if(isInteger(x)&&isInteger(y))return Integer.parseInt(x)-Integer.parseInt(y);
		else if(isFloat(x)&&isFloat(y))return (int)(Double.parseDouble(x)-Double.parseDouble(y));
		else return x.compareTo(y);
	}
	private boolean isInteger(String s)
	{
		int length=s.length();
		if(length==0)return false;
		for(int i=0;i<length;i++)
		{
			char c=s.charAt(i);
			if(c<'0'||c>'9')return false;
		}
		return true;
	}
	private boolean isFloat(String s)
	{
		int length=s.length();
		if(length==0)return false;
		String s0="",s1="";
		int i=0;
		for(;i<length;i++)
		{
			char c=s.charAt(i);
			if(c=='.')break;
			s0+=c;
		}
		if(i==length)return isInteger(s0);
		i++;
		for(;i<length;i++)
		{
			char c=s.charAt(i);
			s1+=c;
		}
		return isInteger(s0)&&isInteger(s1);
	}
	private String getData(String data)
	{
		int l=data.length();
		if(l==0)return "";
		if(data.charAt(0)=='\''&&data.charAt(l-1)=='\'')return data.substring(1,l-1);
		else if(isInteger(data)||isFloat(data))return data;
		else return "";
	}
	private boolean isCorrespondTo(JItem[] items,String[] fields,int[] compare,String[] datas)
	{
		if(fields==null)return true;
		int l=fields.length;
		for(int i=0;i<l;i++)
		{
			String data=this.getData(datas[i]);
			if(data.equals(""))
			{
				int index=this.getFieldIndex(datas[i]);
				if(index!=-1)data=items[index].Data;
				else data=datas[i];
			}
			if(this.sgn(this.sub(items[this.getFieldIndex(fields[i])].Data,data))!=compare[i])return false;
		}
		return true;
	}
	private boolean isCorrespondTo(JItem[] items,String field,JResultSet resultSet)
	{
		if(field==null||resultSet==null)return false;
		String fieldName=(resultSet.getFieldNames())[0];
		if(!resultSet.first())return false;
		do
		{
			if(items[this.getFieldIndex(field)].Data.equals(resultSet.getString(fieldName)))return true;
		}
		while(resultSet.next());
		return false;
	}
	private JField getField(String fieldName)
	{
		for(JField f=FirstField;f!=null;f=f.NextField)
		{
			if(f.FieldName.equals(fieldName))return f;
		}
		return null;
	}
	private String getNotFieldName(String[] fieldNames)
	{
		String notFieldName=null;
		int l=fieldNames.length;
		for(int i=0;i<l;i++)
		{
			if(this.getField(fieldNames[i])==null)
			{
				notFieldName=fieldNames[i];
				break;
			}
		}
		return notFieldName;
	}
	private int getFieldIndex(String fieldName)
	{
		int index=0;
		for(JField f=FirstField;f!=null;f=f.NextField)
		{
			if(f.FieldName.equals(fieldName))break;
			else index++;
		}
		if(index>=FieldCount)index=-1;
		return index;
	}
	public JItem[] getFirstItems()
	{
		JItem[] items=new JItem[FieldCount];
		int l=0;
		for(JField f=FirstField;f!=null;f=f.NextField)items[l++]=f.FirstItem;
		return items;
	}
	private void writeToDisk()
	{
		try
		{
			String JTableFieldInfo="";
			for(JField f=FirstField;f!=null;f=f.NextField)JTableFieldInfo+=f.FieldName+" "+f.FieldType+",";
			PrintWriter PrintWriter1=new PrintWriter(JTableFile);
			PrintWriter1.println(JTableFieldInfo);
			for(JField f=FirstField;f!=null;f=f.NextField)
			{
				String JDatas="";
				for(JItem i=f.FirstItem;i!=null;i=i.NextItem)JDatas+=i.Data+"\t";
				PrintWriter1.println(JDatas);
			}
			PrintWriter1.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}
class JField
{
	public String FieldName;
	public String FieldType;
	public JItem FirstItem;
	public JItem LastItem;
	public JField NextField;
	public JField PreviousField;
	public JField(String fieldName,String fieldType)
	{
		this.FieldName=fieldName;
		this.FieldType=fieldType;
		this.FirstItem=null;
		this.LastItem=null;
		this.NextField=null;
		this.PreviousField=null;
	}
	public void insertItem(JItem item)
	{
		if(FirstItem==null)this.FirstItem=this.LastItem=item;
		else
		{
			this.LastItem.NextItem=item;
			item.PreviousItem=LastItem;
			this.LastItem=item;
		}
	}
	public void deleteItem(JItem item)
	{
		if(item==FirstItem)
		{
			this.FirstItem=FirstItem.NextItem;
			if(FirstItem!=null)this.FirstItem.PreviousItem=null;
		}
		else if(item==LastItem)
		{
			this.LastItem=LastItem.PreviousItem;
			if(LastItem!=null)this.LastItem.NextItem=null;
		}
		else
		{
			item.PreviousItem.NextItem=item.NextItem;
			item.NextItem.PreviousItem=item.PreviousItem;
		}
	}
}
class JItem
{
	public String Data;
	public JItem NextItem;
	public JItem PreviousItem;
	public JItem(String data)
	{
		this.Data=data;
		this.NextItem=null;
		this.PreviousItem=null;
	}
}
class JResultSet
{
	private JField[] fields;
	private int fieldCount;
	private JItem[] items;
	public JResultSet(JField[] fields)
	{
		this.fieldCount=fields.length;
		this.fields=new JField[fieldCount];
		this.items=new JItem[fieldCount];
		for(int i=0;i<fieldCount;i++)
		{
			this.fields[i]=new JField(fields[i].FieldName,fields[i].FieldType);
			this.fields[i].FirstItem=fields[i].FirstItem;
			this.items[i]=null;
		}
	}
	public boolean first()
	{
		for(int i=0;i<fieldCount;i++)this.items[i]=fields[i].FirstItem;
		if(items[0]==null)return false;
		else return true;
	}
	public boolean next()
	{
		if(items[0]==null)for(int i=0;i<fieldCount;i++)this.items[i]=fields[i].FirstItem;
		else for(int i=0;i<fieldCount;i++)this.items[i]=items[i].NextItem;
		if(items[0]==null)return false;
		else return true;
	}
	public int getFieldCount()
	{
		return this.fieldCount;
	}
	public String[] getFieldNames()
	{
		String[] fieldNames=new String[fieldCount];
		for(int i=0;i<fieldCount;i++)fieldNames[i]=fields[i].FieldName;
		return fieldNames;
	}
	public String getString(String fieldName)
	{
		return items[this.getFieldIndex(fieldName)].Data;
	}
	public int getInt(String fieldName)
	{
		int Int=0;
		try
		{
			Int=Integer.parseInt(items[this.getFieldIndex(fieldName)].Data);
		}
		catch(Exception e){e.printStackTrace();}
		return Int;
	}
	public double getDouble(String fieldName)
	{
		double d=0.0;
		try
		{
			d=Double.parseDouble(items[this.getFieldIndex(fieldName)].Data);
		}
		catch(Exception e){e.printStackTrace();}
		return d;
	}
	public boolean getBoolean(String fieldName)
	{
		boolean b=false;
		try
		{
			b=Boolean.parseBoolean(items[this.getFieldIndex(fieldName)].Data);
		}
		catch(Exception e){e.printStackTrace();}
		return b;
	}
	private int getFieldIndex(String fieldName)
	{
		int index=0;
		for(;index<fieldCount;index++)
		{
			if(fields[index].FieldName.equals(fieldName))break;
		}
		if(index>=fieldCount)index=fieldCount-1;
		return index;
	}
	public void print()
	{
		this.printUnderLine();
		for(int i=0;i<fieldCount;i++)System.out.print(fields[i].FieldName+"\t\t");
		System.out.println();
		this.printUnderLine();
		while(this.next())
		{
			for(int i=0;i<fieldCount;i++)System.out.print(items[i].Data+"\t\t|");
			System.out.println();
		}
		this.printUnderLine();
	}
	private void printUnderLine()
	{
		for(int i=0;i<fieldCount;i++)System.out.print("____________________");
		System.out.println("\n");
	}
}