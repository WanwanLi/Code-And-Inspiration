import java.sql.*;
import java.util.*;
public class JavaAndDataBase
{
	public static void main(String[] args)
	{		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection Connection1=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Library","root","11235813");
			ResultSet ResultSet1;
			String StructuredQueryLanguage;
/*
			StructuredQueryLanguage="Create Table Book(BookTitle Text,PublisherName Text,Year int,Pages int,Price double)";
			System.out.println("mysql> "+StructuredQueryLanguage+";");			
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
			StructuredQueryLanguage="Create Table Author(AuthorName Text,Affiliation Text)";
			System.out.println("mysql> "+StructuredQueryLanguage+";");			
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
			StructuredQueryLanguage="Create Table Publisher(PublisherName Text,Country Text)";
			System.out.println("mysql> "+StructuredQueryLanguage+";");			
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
			StructuredQueryLanguage="Create Table Book_Author(BookTitle Text,AuthorName Text)";
			System.out.println("mysql> "+StructuredQueryLanguage+";");			
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
			StructuredQueryLanguage="Alter Table Book_Author add AuthorOrder int";
			System.out.println("mysql> "+StructuredQueryLanguage+";");			
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
			StructuredQueryLanguage="Load Data Local Infile \"C:\\\\Users\\\\ASUS\\\\java\\\\Java And Inspiration\\\\3.Java And DataBase\\\\2.Java And MySQL\\\\MySQL\\\\Library\\\\Book.txt\"  Into Table Book";
			System.out.println("mysql> "+StructuredQueryLanguage+";");			
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
			StructuredQueryLanguage="Load Data Local Infile \"C:\\\\Users\\\\ASUS\\\\java\\\\Java And Inspiration\\\\3.Java And DataBase\\\\2.Java And MySQL\\\\MySQL\\\\Library\\\\Book_Author.txt\"  Into Table Book_Author";
			System.out.println("mysql> "+StructuredQueryLanguage+";");			
			Connection1.createStatement().executeUpdate(StructuredQueryLanguage);
*/
			System.out.print("\nWelcome to the MySQL monitor.");
			Scanner Scanner1=new Scanner(System.in);
			String String1="";
			while(true)
			{
				System.out.print("\nmysql> Please Input A BookTitle:\nmysql> ");
				String1=Scanner1.nextLine();
				if(String1.equals("exit"))break;
				StructuredQueryLanguage="Select PublisherName,Year From Book Where BookTitle='"+String1+"'";
				ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
				while(ResultSet1.next())System.out.print("mysql> PublisherName:"+ResultSet1.getString("PublisherName")+"  Year:"+ResultSet1.getInt("Year")+" AuthorName:");
				StructuredQueryLanguage="Select AuthorName From Book_Author  Where BookTitle='"+String1+"' Order By AuthorOrder";
				ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
				while(ResultSet1.next())System.out.print(ResultSet1.getString("AuthorName")+"\t");
				System.out.print("\nmysql> Please Input An AuthorName:\nmysql> ");
				String1=Scanner1.nextLine();
				StructuredQueryLanguage="Select BookTitle,PublisherName,Year From Book Where BookTitle In (Select BookTitle From Book_Author Where AuthorName='"+String1+"') Order By Year DESC";
				ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
				while(ResultSet1.next())System.out.print("\nmysql> BookTitle:"+ResultSet1.getString("BookTitle")+"\tPublisherName:"+ResultSet1.getString("PublisherName")+"\tYear:"+ResultSet1.getInt("Year"));
				System.out.print("\nmysql> Please Input An PublisherName:\nmysql> ");
				String1=Scanner1.nextLine();
				StructuredQueryLanguage="Select Book.BookTitle,Book.Year,Book_Author.AuthorName From Book,Book_Author Where Book.PublisherName='"+String1+"' And Book.BookTitle=Book_Author.BookTitle";
				ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
				while(ResultSet1.next())System.out.print("\nmysql> BookTitle:"+ResultSet1.getString("Book.BookTitle")+"\tYear:"+ResultSet1.getInt("Book.Year")+"\tAuthorName:"+ResultSet1.getString("Book_Author.AuthorName"));
				System.out.print("\nmysql> Please Input An AuthorName:\nmysql> ");
				String1=Scanner1.nextLine();
				StructuredQueryLanguage="Select AuthorName,Count(*) As BookNumber From Book_Author Where BookTitle In(Select BookTitle From Book_Author Where AuthorName='"+String1+"') Group By AuthorName";
				ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
				while(ResultSet1.next())System.out.print("\nmysql> AuthorName:"+ResultSet1.getString("AuthorName")+"\tBookNumber:"+ResultSet1.getInt("BookNumber"));
				System.out.print("\nmysql> Please Input An Integer:\nmysql> ");
				int int1=Integer.parseInt(Scanner1.nextLine());
				StructuredQueryLanguage="Select AuthorName,Count(*) As BookNumber From Book_Author Group By AuthorName";
				ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
				while(ResultSet1.next())if(ResultSet1.getInt("BookNumber")>int1)System.out.print("\nmysql> AuthorName:"+ResultSet1.getString("AuthorName"));
				System.out.print("\nmysql> Please Input An PublisherName:\nmysql> ");
				String1=Scanner1.nextLine();
				StructuredQueryLanguage="Select AuthorName,Count(*) As BookNumber From Book_Author Where AuthorName Not In(Select Book_Author.AuthorName From Book_Author,Book Where PublisherName='"+String1+"' And Book_Author.BookTitle=Book.BookTitle) Group By AuthorName";
				ResultSet1=Connection1.createStatement().executeQuery(StructuredQueryLanguage);
				while(ResultSet1.next())System.out.print("\nmysql> AuthorName:"+ResultSet1.getString("AuthorName")+"\tBookNumber:"+ResultSet1.getInt("BookNumber"));
			}
			Connection1.close();
			System.out.print("Bye\n");
		}
		catch(Exception e){e.printStackTrace();}	
	}
}