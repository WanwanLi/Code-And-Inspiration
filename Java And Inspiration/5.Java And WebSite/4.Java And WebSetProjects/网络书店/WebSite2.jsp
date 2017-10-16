<%@ page  contentType="text/html;charset=GB2312"%>
<%@ page  import="java.util.*"%>
<%@ page  import="java.sql.*"%>

<Html>
<Head><Title></Title></Head>
<Body Text=White BackGround=13.jpg>
	<Br><Br>
	<Center><Font Face=微软雅黑 Size=30 ><B>计算机类丛书网络书店网站购买确认中心<Br></B></Font></Center>
	<Center><B>________________________________________________________________________________________________________</B></Center>
	<Br><Br><Br><Br><Marquee Behavior=Alternate ScrollAmount=20><B>网站作者：李万万</B></Marquee>
	<Center><B>您所选购的图书分别是:</B></Center>
	<Center>
	<Br><Br>
	<Form Action=WebSite1.jsp Method=post>
	<Table Width=1200 Border=1 BorderColorDark=Black BorderColorLight=White>
	<% 
	          String[] s=request.getParameterValues("CheckBoxes");
	          int number=0;
	          double money=0;
	          try
	          {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource3");
		ResultSet ResultSet1;
		float price=0.0f;		
		if(s!=null)
		        for(int i=0;i<s.length;i++)
		        {
			number=Integer.parseInt(request.getParameter("Text"+s[i]));
			ResultSet1=Connection1.createStatement().executeQuery("Select * From Table1 Where ID="+s[i]);
			while(ResultSet1.next())
			{
			        price=ResultSet1.getFloat("Price");
			        out.print("<Tr>");
				out.print("<Td><B>书名：</B></Td><Td>"+ResultSet1.getString("Name")+"</Td>");
				out.print("<Td><B>作者：</B></Td><Td>"+ResultSet1.getString("Writer")+"</Td>");
				out.print("<Td><B>出版社：</B></Td><Td>"+ResultSet1.getString("Publisher")+"</Td>");
				out.print("<Td><B>价格：</B></Td><Td>"+price+"</Td>");
				out.print("<Td><B>数量：</B></Td><Td>"+number+"</Td>");
			        out.print("</Tr>");
			       money+=number*price;	
		             }	
		             ResultSet1.close();	            
		        }		 
		Connection1.close();
		out.print("<Tr><Td ColSpan=10 Align=Right>网络购书价格总计："+money+"￥</Td></Tr>");
	          }catch(Exception e){}
	%>
	
	</Table>
	</Form>
	</Center>
		
</Body>
</Html>






