<%@ page  contentType="text/html;charset=GB2312"%>
<%@ page  import="java.util.*"%>
<%@ page  import="java.sql.*"%>

<Html>
<Head><Title></Title></Head>
<Body Text=White BackGround=14.jpg>
	<Br><Br>
	<Center><Font Face=΢���ź� Size=30 ><B>�������������������վ����ȷ������<Br></B></Font></Center>
	<Center><B>________________________________________________________________________________________________________</B></Center>
	<Br><Br><Br><Br><Marquee Behavior=Alternate ScrollAmount=20><B>��վ���ߣ�������</B></Marquee>
	<Center><B>����ѡ����ͼ��ֱ���:</B></Center>
	<Center>
	<Br><Br>
	<Form Action=WebSite2.jsp Method=post>
	<Table Width=1200 Border=1 BorderColorDark=Black BorderColorLight=White>
	<% 
	          String[] s=request.getParameterValues("CheckBoxes");	
	          try
	          {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection Connection1=DriverManager.getConnection("jdbc:odbc:DataSource3");
		ResultSet ResultSet1;
		int money=0;
		if(s!=null)
		        for(int i=0;i<s.length;i++)
		        {
			ResultSet1=Connection1.createStatement().executeQuery("Select * From Table1 Where ID="+s[i]);
			while(ResultSet1.next())
			{
	%>
			     <Tr>
				<Td><B>������</B></Td><Td><%=ResultSet1.getString("Name")%></Td>
				<Td><B>���ߣ�</B></Td><Td><%=ResultSet1.getString("Writer")%></Td>
				<Td><B>�����磺</B></Td><Td><%=ResultSet1.getString("Publisher")%></Td>
				<Td><B>�۸�</B></Td><Td><%=ResultSet1.getFloat("Price")%></Td>
				<Td><B>����</B><Input Type=CheckBox Name=CheckBoxes Value=<%=s[i]%>></Td>
				<Td><B>������</B><Input  Name= Text<%=s[i]%> Style="Width=20; Height=20"></Td>
			     </Tr>
	<%
		             }	
		             ResultSet1.close();	            
		        }		 
		Connection1.close();
	          }catch(Exception e){}
	%>
	</Table>
	<Br><Input Type=Submit Name=Submit1 Value=��������><Input Type=Reset Name=Reset1 Value=���¾���>

	</Form>
	</Center>
		
</Body>
</Html>






