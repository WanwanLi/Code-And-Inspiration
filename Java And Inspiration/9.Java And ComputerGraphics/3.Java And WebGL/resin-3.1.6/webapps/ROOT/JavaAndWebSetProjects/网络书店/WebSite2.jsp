<%@ page  contentType="text/html;charset=GB2312"%>
<%@ page  import="java.util.*"%>
<%@ page  import="java.sql.*"%>

<Html>
<Head><Title></Title></Head>
<Body Text=White BackGround=13.jpg>
	<Br><Br>
	<Center><Font Face=΢���ź� Size=30 ><B>�������������������վ����ȷ������<Br></B></Font></Center>
	<Center><B>________________________________________________________________________________________________________</B></Center>
	<Br><Br><Br><Br><Marquee Behavior=Alternate ScrollAmount=20><B>��վ���ߣ�������</B></Marquee>
	<Center><B>����ѡ����ͼ��ֱ���:</B></Center>
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
				out.print("<Td><B>������</B></Td><Td>"+ResultSet1.getString("Name")+"</Td>");
				out.print("<Td><B>���ߣ�</B></Td><Td>"+ResultSet1.getString("Writer")+"</Td>");
				out.print("<Td><B>�����磺</B></Td><Td>"+ResultSet1.getString("Publisher")+"</Td>");
				out.print("<Td><B>�۸�</B></Td><Td>"+price+"</Td>");
				out.print("<Td><B>������</B></Td><Td>"+number+"</Td>");
			        out.print("</Tr>");
			       money+=number*price;	
		             }	
		             ResultSet1.close();	            
		        }		 
		Connection1.close();
		out.print("<Tr><Td ColSpan=10 Align=Right>���繺��۸��ܼƣ�"+money+"��</Td></Tr>");
	          }catch(Exception e){}
	%>
	
	</Table>
	</Form>
	</Center>
		
</Body>
</Html>






